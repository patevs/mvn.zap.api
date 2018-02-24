package zap.main;

import zap.tool.ZAProxyTool;

public class ZAProxyMain {

	// Start ZAP client with UI enabled
	private static final boolean UI_ENABLED = false;
	
	/**
	 * MAIN METHOD
	 */
	public static void main(String[] args) {

		ZAProxyTool zap = new ZAProxyTool();
		// start zap
		boolean startSuccess = 
				(UI_ENABLED ? zap.startClientUI() : zap.startClient());
		System.out.println("ZAP Client started: " + startSuccess);
		// wait for ZAP client to start
		wait5s();
		// run spider
		zap.spider();
		// run active scan
		//zap.ascan();
		// shutdown zap
		System.out.println("Stopping ZAP Client...");
		System.out.println("ZAP Client stopped: " + zap.stopClient());

	}
	
	/**
	 * Wait 5 seconds
	 */
	public static void wait5s() { 
		wait(5000); 
	}
	
	/**
	 * Wait some amout of time in milliseconds
	 * @param milliseconds
	 */
	private static void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}




