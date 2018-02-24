package zap.tool.utils;

@SuppressWarnings("serial")
public class ZAProxyException extends RuntimeException {

    public ZAProxyException() {
        super();
    }

    public ZAProxyException(String message) {
        super(message);
    }

    public ZAProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZAProxyException(Throwable cause) {
        super(cause);
    }
	
}
