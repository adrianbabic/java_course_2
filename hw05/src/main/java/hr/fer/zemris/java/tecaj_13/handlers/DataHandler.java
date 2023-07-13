package hr.fer.zemris.java.tecaj_13.handlers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataHandler {
	
	public static String generateSHA1Hash(String password) throws NoSuchAlgorithmException {	        	
	    
		MessageDigest md = MessageDigest.getInstance("SHA-1");
	    byte[] hashedBytes = md.digest(password.getBytes());
	
	    StringBuilder sb = new StringBuilder();
	    for (byte b : hashedBytes) {
	        sb.append(String.format("%02x", b));
	    }
	
	    return sb.toString();
	}
}
