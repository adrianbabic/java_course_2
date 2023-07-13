package messenger.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import messenger.exceptions.MessageException;

public class HelloMessage extends Message {
	
	private String username;
	private long packetNumber;
	private long randomKey;
	
	public HelloMessage(String username, long randomKey) {
		this.username = username;
		this.randomKey = randomKey;
		this.packetNumber = 0L;
	}
	
	public HelloMessage(byte[] inBytes) throws MessageException {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			this.packetNumber = dis.readLong();
			this.randomKey = dis.readLong();
			this.username = dis.readUTF();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to read HELLO message.");
		}
	}

	@Override
	public byte[] messageToSend() throws MessageException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeByte(1);
			dos.writeLong(this.packetNumber);			
			dos.writeLong(randomKey);
			dos.writeUTF(username);
			dos.close();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to prepare HELLO msg for sending.");
		}

		byte[] buf = bos.toByteArray();
		
		return buf;
	}

	@Override
	public long getPacketNumber() {
		return this.packetNumber;
	}

	public String getUsername() {
		return username;
	}

	public long getRandomKey() {
		return randomKey;
	}
	
}
