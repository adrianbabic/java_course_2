package messenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import messenger.exceptions.MessageException;
import messenger.messages.AckMessage;
import messenger.messages.ByeMessage;
import messenger.messages.HelloMessage;
import messenger.messages.InMessage;
import messenger.messages.Message;
import messenger.messages.OutMessage;


public class UDPClient {
	
	private InetSocketAddress serverAddress;
	private String username;
	private DatagramSocket datagramSocket;
	private long randomKey;
//	private long packetNumber;
	private long outPacketNum;
	private long inPacketNum;
	private long uid;
	private BlockingQueue<Long> receivedMsgs;
	//sinkronizacija kriva
	private Scanner scanner;

	
	public UDPClient(InetSocketAddress serverAddress, String username) throws SocketException, MessageException   {
		this.serverAddress = serverAddress;
		this.username = username;
		this.datagramSocket = new DatagramSocket();
		this.randomKey = new Random().nextLong();
		this.uid = 0L;
//		this.packetNumber = 0L;
		this.inPacketNum = 0L;
		this.outPacketNum = 0L;
		this.uid = sendHelloMsg(this.username, this.randomKey);
		this.receivedMsgs = new PriorityBlockingQueue<>();
		this.scanner = new Scanner(System.in);
	}
	
	public void listening() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					listenForMessages();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void sendingMessages() throws MessageException, InterruptedException {
		
		while(true) {
			String msgToSend = scanner.nextLine();
			OutMessage outMsg = new OutMessage(this.outPacketNum++, this.uid, this.username + ": " + msgToSend);
			byte[] msgBytes = outMsg.messageToSend();
			
			int i = 0;
			while(i < 10){
				i++;
				DatagramPacket respondPacket = new DatagramPacket(msgBytes, msgBytes.length);
			    respondPacket.setSocketAddress(serverAddress);
			    
			    try {
					this.datagramSocket.send(respondPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
			    //zaustavit dretvu, koristit blocking queue
			    if(this.receivedMsgs.take() == this.outPacketNum - 1) {
//			    	System.out.println("Received ACK message for sent OUT msg.");
			    	break;
			    }
//			    System.out.println("Did not receive ACK msg for sent OUT msg: " + i);
			}
			
			
		}
	}
	
	public void shutdown() throws MessageException, SocketException {
		
		if(this.uid == 0L) {
			this.datagramSocket.close();
			this.scanner.close();
			return;
		}
		
		ByeMessage msg = new ByeMessage(this.outPacketNum, uid);
		byte[] sendMsg = msg.messageToSend();
		int retransmission = 0;
		byte[] receiveMsg;
		
		while (retransmission < 10) {
			retransmission++;
			System.out.println("Sending BYE message number: " + retransmission);
			DatagramPacket sendingPacket = new DatagramPacket(sendMsg, sendMsg.length);
			sendingPacket.setSocketAddress(serverAddress);
			try {
				datagramSocket.send(sendingPacket);
			} catch (IOException e1) {
				System.out.println("Error occured while trying to send BYE msg. Will try to resend "
						+ (10 - retransmission) + " more times.");
				continue;
			}
			
			receiveMsg = new byte[512];
			DatagramPacket receivingPacket = new DatagramPacket(receiveMsg, receiveMsg.length);
			this.datagramSocket.setSoTimeout(5000);
			try {
				this.datagramSocket.receive(receivingPacket);
			} catch (IOException e) {
				System.out.println("Timeout on BYE message. Will try to resend " 
						+ (10 - retransmission) + " more times.");
				continue;
			}
			
			if(Message.readMessageType(receivingPacket.getData()) != 2) {
				System.out.println("Expected ACK msg answer to my BYE msg, but received something else. Trying again...");
				continue;
			}
			
			System.out.println("Successfully disconnected.");
			this.datagramSocket.close();
			this.scanner.close();
			return;
		}
		
		this.datagramSocket.close();
		this.scanner.close();
		throw new MessageException("Failed to receive ACK message about disconnecting from server. Shuting down anyway.");
	}
	
	private void listenForMessages() throws InterruptedException {
		
		byte[] receivedMsg;
		while(true) {
			receivedMsg = new byte[512];
			DatagramPacket receivedPacket = new DatagramPacket(receivedMsg, receivedMsg.length);
			try {
				this.datagramSocket.setSoTimeout(0);
			} catch (SocketException e) {
				System.out.println("Error with socket timeout.");
				e.printStackTrace();
				continue;
			}
			try {
				this.datagramSocket.receive(receivedPacket);
			} catch (IOException e) {
				System.out.println("Error while receiving packet.");
				e.printStackTrace();
				continue;
			}
			int msgType = Message.readMessageType(receivedPacket.getData());
			
			switch (msgType) {
			  case 2:
				AckMessage ack;
				try {
					ack = new AckMessage(receivedPacket.getData());
					this.receivedMsgs.put(ack.getPacketNumber());
				} catch (MessageException e) {
					System.out.println("Error receiving ACK message.");
					e.printStackTrace();
				}
			    break;
			  case 5:
			    InMessage in;
				try {
					in = new InMessage(receivedPacket.getData());
					System.out.println(in.getMessage());
					this.inPacketNum++;
				} catch (MessageException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					break;
				}
			    AckMessage respond = new AckMessage(this.inPacketNum, this.uid);
			    byte[] respondData;
				try {
					respondData = respond.messageToSend();
					DatagramPacket respondPacket = new DatagramPacket(respondData, respondData.length);
				    respondPacket.setSocketAddress(serverAddress);
				    this.datagramSocket.send(respondPacket);
				} catch (MessageException | IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			    break;
			  default:
			    System.out.println("Received unexpected message: " + msgType);
			}
		}
	}
	
	private long sendHelloMsg(String username, long randomKey) throws MessageException, SocketException {
		
		HelloMessage msg = new HelloMessage(username, randomKey);
		byte[] sendMsg = msg.messageToSend();
		int retransmission = 0;
		byte[] receiveMsg;
		
		while (retransmission < 10) {
			retransmission++;
//			System.out.println("Sending HELLO message number: " + retransmission);
			DatagramPacket sendingPacket = new DatagramPacket(sendMsg, sendMsg.length);
			sendingPacket.setSocketAddress(serverAddress);
			try {
				datagramSocket.send(sendingPacket);
			} catch (IOException e1) {
				System.out.println("Error occured while trying to send HELLO msg. Will try to resend "
						+ (10 - retransmission) + " more times.");
				continue;
			}
			
			receiveMsg = new byte[512];
			DatagramPacket receivingPacket = new DatagramPacket(receiveMsg, receiveMsg.length);
			this.datagramSocket.setSoTimeout(5000);
			try {
				this.datagramSocket.receive(receivingPacket);
			} catch (IOException e) {
				System.out.println("Timeout on HELLO message. Will try to resend " 
						+ (10 - retransmission) + " more times.");
				continue;
			}
			
			if(Message.readMessageType(receivingPacket.getData()) != 2) {
				System.out.println("Expected ACK msg answer to my HELLO msg, but received something else. Trying again...");
				continue;
			}
			
			AckMessage ackMsg = new AckMessage(receivingPacket.getData());
			if(ackMsg.getUid() == 0L) {
				System.out.println("Received uid is 0L. Trying again...");
				continue;
			}
			
//			System.out.println("Successfully connected!");
			this.outPacketNum++;
			return ackMsg.getUid();
		}
		
		shutdown();
		throw new MessageException("Failed to connect to the server.");
	}
	
	public static void main(String[] args) throws SocketException, MessageException, InterruptedException {
		
		if(args.length != 3) {
			System.out.println("Expected three arguments: host, port and username.");
			return;
		}
		
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host error. Please check your host argument.");
			return;
		}
		
		int port;
		InetSocketAddress serverAddress;
		try {
			port = Integer.parseInt(args[1]);
			serverAddress = new InetSocketAddress(inetAddress, port);
		} catch (NumberFormatException e) {
			System.out.println("Error occured. Please check your port argument.");
			return;
		}
		
		
		UDPClient client = new UDPClient(serverAddress, args[2]);
		client.listening();
		client.sendingMessages();
	}
	
}



