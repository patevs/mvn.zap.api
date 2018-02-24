package zap.tool;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import zap.tool.utils.ZAProxyToolUtilities;

public abstract class ZAProxyToolBase extends ZAProxyToolUtilities {

	/************* FIELDS *************/
	
	// ZAP client properties
    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 8080;
    private static final String ZAP_API_KEY = null; // Change this if you have set the apikey in ZAP via Options / API

    // target URL to attack
    private static final String TARGET = "http://localhost:8181/";

    // ZAP tool client api
    public static ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
    
    /************* METHODS *************/
    
	/**
	 * Validates the ZAP client's properties
	 */
	protected void validate() {
		validateHost(ZAP_ADDRESS);
		validatePort(ZAP_PORT);
	}
	
    /**
     * Attempt to start the ZAP Client
     * @return true if client successfuly starts, else false.
     */
	protected static boolean start(boolean UIenabled) {
		return ((UIenabled) ? startUI() : startDaemon());
	}
	
    /**
     * Attempt to start the ZAP Client
     * @return true if client successfuly starts, else false.
     */
	protected static boolean stop() {
		return stopZAP(api, ZAP_ADDRESS, ZAP_PORT);
	}
    
    /**
     * Runs a scan scan of the target URL
     */
    protected void runSpider() {
        try {
            // Start spidering the target
            System.out.println("Spider : " + TARGET);
            // It's not necessary to pass the ZAP API key again, already set when creating the ClientApi.
            ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
            // The scan now returns a scan id to support concurrent scanning
            String scanid = ((ApiResponseElement) resp).getValue();
            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                int progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanid)).getValue());
                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Spider complete");
            // Give the passive scanner a chance to complete
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Runs an active scan of the target URL
     */
    protected void runActiveScan() {
        try {
            System.out.println("Active scan : " + TARGET);
            ApiResponse resp = api.ascan.scan(TARGET, "True", "False", null, null, null);
            // The scan now returns a scan id to support concurrent scanning
            String scanid = ((ApiResponseElement) resp).getValue();
            // Poll the status until it completes
            while (true) {
                Thread.sleep(5000);
                int progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
                System.out.println("Active Scan progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Active Scan complete");
            //System.out.println("Alerts:");
            //System.out.println(new String(api.core.xmlreport()));
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
