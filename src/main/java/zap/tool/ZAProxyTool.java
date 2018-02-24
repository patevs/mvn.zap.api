package zap.tool;

public class ZAProxyTool extends ZAProxyToolBase {

	/**
	 * CONSTRUCTOR
	 */
	public ZAProxyTool() {
		validate();
	}
	
	/**
	 * Attempt to start the ZAP Client with UI disbaled (daemon mode)
	 * @return true if client starts successful, else false.
	 */
	public boolean startClient() {
		return start(false);
	}
	
	/**
	 * Attempt to start the ZAP Client with UI enabled
	 * @return true if client starts successful, else false.
	 */
	public boolean startClientUI() {
		return start(true);
	}
	
	/**
	 * Attempt to stop the ZAP Client
	 */
	public boolean stopClient() {
		return stop();
	}

	/**
	 * Run a spider scan
	 */
	public void spider() {
		runSpider();
	}
	
	/**
	 * Run an active scan
	 */
	public void ascan() {
		runActiveScan();
	}
	
}
