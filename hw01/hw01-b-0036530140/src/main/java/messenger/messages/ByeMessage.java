package messenger.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import messenger.exceptions.MessageException;

public class ByeMessage extends Message {
	
	private long packetNumber;
	private long uid;
	
	public ByeMessage(long packetNumber, long uid) {
		this.packetNumber = packetNumber;
		this.uid = uid;
	}
	
	public ByeMessage(byte[] inBytes) throws MessageException {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
		DataInputStream dis = new DataInputStream(bis);
		try {
			dis.readByte();
			this.packetNumber = dis.readLong();
			this.uid = dis.readLong();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to read BYE message.");
		}
	}

	@Override
	public byte[] messageToSend() throws MessageException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeByte(3);
			dos.writeLong(packetNumber);			
			dos.writeLong(uid);
			dos.close();
		} catch (IOException e) {
			throw new MessageException("Error occured while trying to prepare BYE msg for sending.");
		}

		byte[] buf = bos.toByteArray();
		
		return buf;
	}

	@Override
	public long getPacketNumber() {
		return this.packetNumber;
	}

	public long getUid() {
		return uid;
	}

	
}
