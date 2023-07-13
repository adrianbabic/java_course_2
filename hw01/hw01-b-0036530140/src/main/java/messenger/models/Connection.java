package messenger.models;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import messenger.messages.Message;


public class Connection {
    private boolean invalid;
    private long inCounter;
    private long outCounter;
    private long helloKey;
    private long uid;
    private String name;
    private InetAddress address;
    private int port;
    private BlockingQueue<Message> receivedQueue;
    private BlockingQueue<Message> outgoingQueue;
	private Thread worker;
    
    public Connection() {
        this.invalid = false;
        this.receivedQueue = new LinkedBlockingQueue<Message>();
        this.outgoingQueue = new LinkedBlockingQueue<Message>();
        this.inCounter = 0L;
        this.outCounter = 0L;
        this.worker = null;
    }

	public boolean isInvalid() {
		return invalid;
	}

	public long getInCounter() {
		return inCounter;
	}

	public long getOutCounter() {
		return outCounter;
	}

	public Thread getWorker() {
		return worker;
	}

	public long getHelloKey() {
		return helloKey;
	}

	public long getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public BlockingQueue<Message> getReceivedQueue() {
		return receivedQueue;
	}

	public BlockingQueue<Message> getOutgoingQueue() {
		return outgoingQueue;
	}

	public void setHelloKey(long helloKey) {
		this.helloKey = helloKey;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}	
	
	
	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	
	public void setWorker(Thread worker) {
		this.worker = worker;
	}

	public void incrementInCounter() {
		this.inCounter++;
	}
	
	public void incrementOutCounter() {
		this.outCounter++;
	}
}
