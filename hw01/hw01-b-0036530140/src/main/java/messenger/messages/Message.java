package messenger.messages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import messenger.exceptions.MessageException;

public abstract class Message {
	
	abstract public byte[] messageToSend() throws MessageException;
	abstract public long getPacketNumber();
	
	public static int readMessageType(byte[] messageBytes) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(messageBytes);
		DataInputStream dis = new DataInputStream(bis);
		int result;
		try {
			result = dis.readByte();
			return result;
		} catch (IOException e) {
			System.out.println("Error while trying to read received message's type.");
			e.printStackTrace();
			return 0;
		}
		
	}
}
