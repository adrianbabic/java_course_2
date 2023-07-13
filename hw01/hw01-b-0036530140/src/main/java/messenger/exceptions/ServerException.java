package messenger.exceptions;

public class ServerException extends Exception { 
    private static final long serialVersionUID = 1L;

	public ServerException(String errorMessage) {
        super(errorMessage);
    }
}