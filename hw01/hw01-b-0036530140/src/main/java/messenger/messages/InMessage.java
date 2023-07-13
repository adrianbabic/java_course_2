package messenger.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import messenger.exceptions.MessageException;

public class InMessage extends Message {
	
	private String message;
	private long packetNumber;
	private String name;
	
	public InMessage(long packetNumber, String name, String message) {
		this.message = message;
		this.name = name;
		this.packetNumber = packetNumber;
	}
	
	public InMessage(byte[] inBytes) throws MessageException  {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			this.packetNumber = dis.readLong();
			this.name = dis.readUTF();
			this.message = dis.readUTF();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to read IN message.");
		}
	}

	@Override
	public byte[] messageToSend() throws MessageException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeByte(5);
			dos.writeLong(packetNumber);			
			dos.writeUTF(name);
			dos.writeUTF(message);
			dos.close();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to prepare IN msg for sending.");
		}

		byte[] buf = bos.toByteArray();
		
		return buf;
	}

	@Override
	public long getPacketNumber() {
		return this.packetNumber;
	}

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name;
	}
}
