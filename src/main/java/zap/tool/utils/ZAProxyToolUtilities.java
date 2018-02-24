package zap.tool.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.zaproxy.clientapi.core.ClientApiMain;

public abstract class ZAProxyToolUtilities {

	/************* FIELDS *************/
	
	// ZAP Client install directory
	private static final String ZAP_LOCATION = 
			"C:\\bin\\ZAP\\";
	
	/************* METHODS *************/
	
	/**
	 * Validates the host address of the ZAP client
	 * @param host
	 */
    protected static void validateHost(String host) {
        if (host == null) {
            throw new IllegalArgumentException("Parameter host must not be null.");
        }
        if (host.isEmpty()) {
            throw new IllegalArgumentException("Parameter host must not be empty.");
        }
    }

    /**
     * Validates the port of the ZAP client
     * @param port
     */
    protected static void validatePort(int port) {
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Parameter port must be between 1 and 65535.");
        }
    }
    
    /**
     * Attempt to start the ZAP Client with UI enabled
     */
    protected static boolean startUI() {
    	return startZAP("ZAP.exe", true);
    }
    /**
     * Attempt to start the ZAP Client with UI disabled (daemon mode)
     */
    protected static boolean startDaemon() {
    	return startZAP("ZAP.exe -daemon", false);
    }
    /**
     * Attempt to start the ZAP Client
     * @return true if client successfuly starts, else false.
     */
    private static boolean startZAP(String cmd, boolean uiEnabled) {
		try {
			String[] command = { "CMD", "/C", ZAP_LOCATION + cmd };
			ProcessBuilder proc = new ProcessBuilder(command);
			proc.directory(new File(ZAP_LOCATION));
			Process p = proc.start();
			p.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			OutputStreamWriter oStream = new OutputStreamWriter(
					p.getOutputStream());
			oStream.write("process where name='" + cmd + "'");
			oStream.flush();
			oStream.close();
			String line;
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
				//kludge to tell when ZAP is started and ready
				if(uiEnabled) {
					if (line.contains("INFO") 
							&& line.contains("org.parosproxy.paros.control.Control") 
							&& line.contains("New Session")) {
						input.close();
						break;
					}
				} else {
					if (line.contains("INFO") 
							&& line.contains("org.zaproxy.zap.DaemonBootstrap ") 
							&& line.contains("ZAP is now listening")) {
						input.close();
						break;
					}
				}

			}
			System.out.println("ZAP has started successfully.");
			return true;
		} catch (Exception ex) {
			System.out.println("ZAP was unable to start.");
			ex.printStackTrace();
			return false;
		}
    }
    
    /**
     * Attempt to stop the ZAP Client
     */
    protected static boolean stopZAP(ClientApi _api, String zapaddr, int zapport) { 
    	String resp = "";
    	try {
			resp = _api.core.shutdown().toString();
		} catch (ClientApiException e) {
			e.printStackTrace();
		}
    	stop(zapaddr, zapport); 
    	return resp.equals("OK");
    }
	private static void stop(String zapaddr, int zapport) {
		ClientApiMain.main(new String[] { "stop", "zapaddr=" + zapaddr,	"zapport=" + zapport });
	}
	
}






