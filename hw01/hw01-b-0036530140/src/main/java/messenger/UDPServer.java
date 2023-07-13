package messenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import messenger.exceptions.MessageException;
import messenger.exceptions.ServerException;
import messenger.messages.AckMessage;
import messenger.messages.ByeMessage;
import messenger.messages.HelloMessage;
import messenger.messages.InMessage;
import messenger.messages.Message;
import messenger.messages.OutMessage;
import messenger.models.Connection;

public class UDPServer {

	private DatagramSocket socket;
	private List<Connection> connections;
	private AtomicLong currentUid;

	public UDPServer(int port) throws ServerException {
		try {
			this.socket = new DatagramSocket(null);
			this.socket.bind(new InetSocketAddress((InetAddress) null, port));
		} catch (SocketException e) {
			throw new ServerException("Error occured while initializing server socket!");
		}
		this.currentUid = new AtomicLong(new Random().nextLong());
		this.connections = new ArrayList<>();
		System.out.println("Server started on port: " + port);
	}

	public void runServer() throws MessageException, IOException {

		byte[] data;
		while (true) {
			data = new byte[512];
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			System.out.print("Received packet from: " + datagramPacket.getSocketAddress());

			int type = Message.readMessageType(datagramPacket.getData());

			switch (type) {
			case 1:
				System.out.println(". It's a HelloMessage.");
				handleHello(datagramPacket);
				break;
			case 2:
				System.out.println(". It's a AckMessage.");
				handleAck(datagramPacket);
				break;
			case 3:
				System.out.println(". It's a ByeMessage.");
				handleBye(datagramPacket);
				break;
			case 4:
				System.out.println(". It's a OutMessage.");
				handleOutMsg(datagramPacket);
				break;
			case 5:
				System.out.println(". It's a InMessage.");
				handleInMsg();
				break;
			default:
				System.out.println("Error! Received message with unknown message type.");
			}

		}
	}

	private void handleHello(DatagramPacket datagramPacket) throws MessageException, IOException {

		HelloMessage msg = new HelloMessage(datagramPacket.getData());

		Connection connectionByKey = null;
		synchronized (this.connections) {
			connectionByKey = this.findConnectionByKey(datagramPacket.getAddress(), datagramPacket.getPort(),
					msg.getRandomKey());
			if (connectionByKey == null) {
				connectionByKey = new Connection();
				connectionByKey.setAddress(datagramPacket.getAddress());
				connectionByKey.setPort(datagramPacket.getPort());
				connectionByKey.setHelloKey(msg.getRandomKey());
				connectionByKey.setUid(this.currentUid.incrementAndGet());
				connectionByKey.setName(msg.getUsername());
				connectionByKey.incrementOutCounter();
				this.connections.add(connectionByKey);
				final Connection conn = connectionByKey;
				connectionByKey.setWorker(new Thread(() -> this.worker(conn)));
				connectionByKey.getWorker().start();
			} else if (connectionByKey.getInCounter() > 0L || connectionByKey.getOutCounter() > 0L) {
				System.out.println("Hello received for connection which already has sent messages; ignoring...");
				return;
			}
		}

		AckMessage ackMsg = new AckMessage(0L, connectionByKey.getUid());
		byte[] msgBytes = ackMsg.messageToSend();

		DatagramPacket toSend = new DatagramPacket(msgBytes, msgBytes.length);
		toSend.setAddress(connectionByKey.getAddress());
		toSend.setPort(connectionByKey.getPort());
		this.socket.send(toSend);
	}

	private void handleAck(DatagramPacket datagramPacket) throws MessageException {

		AckMessage msg = new AckMessage(datagramPacket.getData());
		Connection connectionByUID = null;
		synchronized (this.connections) {
			connectionByUID = this.findConnectionByUID(datagramPacket.getAddress(), datagramPacket.getPort(),
					msg.getUid());
			if (connectionByUID == null) {
				return;
			}
		}
		connectionByUID.getReceivedQueue().add(msg);
//		connectionByUID.incrementInCounter();
	}

	private void handleBye(DatagramPacket datagramPacket) throws MessageException, IOException {

		ByeMessage msg = new ByeMessage(datagramPacket.getData());
		Connection connectionByUID = null;
		synchronized (this.connections) {
			connectionByUID = this.findConnectionByUID(datagramPacket.getAddress(), datagramPacket.getPort(),
					msg.getUid());
			if (connectionByUID == null) {
				return;
			}
		}
		if (msg.getPacketNumber() != connectionByUID.getOutCounter() ) {
			System.out.println("Packet ByeMessage has unexpected packet number : " + msg.getPacketNumber()
					+ ", expected " + (connectionByUID.getOutCounter() ) + "; sending ack but ignoring packet.");
		} else {
			connectionByUID.incrementOutCounter();
			connectionByUID.setInvalid(true);
		}

		AckMessage ackMsg = new AckMessage(msg.getPacketNumber(), connectionByUID.getUid());
		byte[] msgBytes = ackMsg.messageToSend();

		DatagramPacket toSend = new DatagramPacket(msgBytes, msgBytes.length);
		toSend.setAddress(connectionByUID.getAddress());
		toSend.setPort(connectionByUID.getPort());
		this.socket.send(toSend);
	}

	private void handleOutMsg(DatagramPacket datagramPacket) throws MessageException, IOException {

		OutMessage outMsg = new OutMessage(datagramPacket.getData());
		Connection connection = null;
		synchronized (this.connections) {
			for (Connection connection2 : this.connections) {
				if (outMsg.getUid() == connection2.getUid()) {
					connection = connection2;
					break;
				}
			}
		}

		if (connection == null) {
			return;
		}
		if (connection.isInvalid())
			return;

		boolean allGood = true;
		if (outMsg.getPacketNumber() != connection.getOutCounter() ) {
			System.out.println("Packet OutMessage has unexpected packet number: " + outMsg.getPacketNumber()
					+ ", expected: " + (connection.getOutCounter() ) + "; sending AckMessage but ignoring packet.");
			allGood = false;
		} else {
			connection.incrementOutCounter();
		}
		if (allGood) {
			this.processOutgoingMessage(connection.getName(), outMsg);
		}

		AckMessage ackMsg = new AckMessage(outMsg.getPacketNumber(), connection.getUid());
		byte[] msgBytes = ackMsg.messageToSend();

		DatagramPacket toSend = new DatagramPacket(msgBytes, msgBytes.length);
		toSend.setAddress(connection.getAddress());
		toSend.setPort(connection.getPort());
		this.socket.send(toSend);

	}

	private void handleInMsg() {
		System.out.println("Packet InMessage shouldn't be send by a client!");
	}

	private Connection findConnectionByKey(InetAddress address, int port, long key) {
		for (final Connection connection : this.connections) {
			if (connection.getAddress().equals(address) && connection.getPort() == port
					&& connection.getHelloKey() == key) {
				return connection;
			}
		}
		return null;
	}

	private Connection findConnectionByUID(InetAddress address, int port, long uid) {
		for (final Connection connection : this.connections) {
			if (connection.getAddress().equals(address) && connection.getPort() == port && connection.getUid() == uid) {
				return connection;
			}
		}
		return null;
	}

	private void worker(Connection connection) {
		try {
			try {
				do {
					this.sendToClient(connection.getOutgoingQueue().take(), connection);
				} while (!connection.isInvalid());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex2) {
			ex2.printStackTrace();
		}
		synchronized (this.connections) {
			this.connections.remove(connection);
		}
	}

	private void sendToClient(Message message, Connection connection) throws MessageException {
		
		byte[] msgBytes = message.messageToSend();

		DatagramPacket toSend = new DatagramPacket(msgBytes, msgBytes.length);
		toSend.setAddress(connection.getAddress());
		toSend.setPort(connection.getPort());

		int n = 0;
		while (true) {
			++n;
			try {
				this.socket.send(toSend);
			} catch (IOException ex) {
				ex.printStackTrace();
				if (n > 10) {
					connection.setInvalid(true); 
					System.out.println("Connection has been deemed invalid.");
					return;
				}
				continue;
			}
			Message returnedMsg;
			try {
				 returnedMsg = connection.getReceivedQueue().poll(5L, TimeUnit.SECONDS);
			} catch (InterruptedException ex2) {
				continue;
			}
			if (returnedMsg == null) {
				continue;
			}
			
			int type = Message.readMessageType(returnedMsg.messageToSend());
			
			if (type == 2) {
				if (returnedMsg.getPacketNumber() == message.getPacketNumber()) {
					break;
				}
				if(n > 10) break;
				System.out.println("Packet AckMessage number doesn't match! Expected: " + message.getPacketNumber() + 
						", but received: " + returnedMsg.getPacketNumber());
			} else {
				System.out.println("Unexpected message in queue of received messages: " + returnedMsg.toString());
			}
		}
	}

	private void processOutgoingMessage(String username, OutMessage outMessage) {
		synchronized (this.connections) {
			for (final Connection connection : this.connections) {
				try {
					if(outMessage.getUid() == connection.getUid()) continue;
					final BlockingQueue<Message> outgoingQueue = connection.getOutgoingQueue();
					connection.incrementInCounter();
					outgoingQueue.put(new InMessage(connection.getInCounter(), username, outMessage.getMessage()));
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected port as the only argument.");
			return;
		}

		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Check your port argument!");
			return;
		}

		try {
			UDPServer server = new UDPServer(port);
			try {
				server.runServer();
			} catch (MessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ServerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
}