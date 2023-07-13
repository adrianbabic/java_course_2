package messenger.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import messenger.exceptions.MessageException;

public class OutMessage extends Message {
	
	private String message;
	private long packetNumber;
	private long uid;
	
	public OutMessage(long packetNumber, long uid, String message) {
		this.message = message;
		this.uid = uid;
		this.packetNumber = packetNumber;
	}
	
	public OutMessage(byte[] inBytes) throws MessageException  {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			this.packetNumber = dis.readLong();
			this.uid = dis.readLong();
			this.message = dis.readUTF();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to read OUT message.");
		}
	}

	@Override
	public byte[] messageToSend() throws MessageException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeByte(4);
			dos.writeLong(packetNumber);			
			dos.writeLong(uid);
			dos.writeUTF(message);
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

	public String getMessage() {
		return message;
	}

	public long getUid() {
		return uid;
	}


	
}
