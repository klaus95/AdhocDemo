package AdhocAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

public abstract class AdHocConfig {
    private int channel = 0;
    private String SSID;
    private String password;
    private String networkInterface;

    public int getChannel() {
        return channel;
    }

    public String getPassword() { return password; }

    public String getSSID() { return SSID; }

    public String getNetworkInterface() { return networkInterface; }

    public abstract String[] getInterfaces() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

    public abstract String[] getConnectedInterfaces() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

    public abstract int[] getSupportedChannels(String wirelessInterface) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

    public abstract int connectToNetwork() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

    public abstract int connectToNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

    public abstract int createNetwork() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

    public abstract int createNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;
	
	public abstract int disconnectFromNetwork() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException;

	public abstract Socket clientSocket(String ip, int port) throws IOException;

	public abstract ServerSocket serverSocket(int port) throws IOException;

    public abstract boolean isAdHocCapable();

    public static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString().trim();
    }

    public static ScriptFailureException catchSFE(Exception e) {
        int errorCode = Integer.parseInt(e.toString().substring(e.toString().indexOf("error=") + "error=".length(), e.toString().lastIndexOf(",")));
        ScriptMeta temp = new ScriptMeta(errorCode, e.toString());
        ScriptFailureException hold = new ScriptFailureException(temp);
        ErrorLogging.LOGS.log(Level.SEVERE, hold.toString(), hold);
        return hold;
    }

    public static List<String> ping(String ipDomain) {

        int numberOfIPs = 255;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfIPs);
        List<Future<PingResult>> list = new ArrayList<>();
        Callable<PingResult> callable;
        List<String> ips = new ArrayList<>();

        for(int i = 1; i < numberOfIPs; i++){
            callable = new PingTask(ipDomain + i);
            Future<PingResult> future = executor.submit(callable);
            list.add(future);
        }

        for(Future<PingResult> result : list){
            try {
                if (result.get().getResultCode() == 0) {
                    ips.add(result.get().getIpAddress());
                }
            } catch (Exception ex) {
                System.out.println("IP hunt failed");
            }
        }
        executor.shutdown();

        return ips;
    }

	public void setChannel(int c) { channel = c; }

    public void setSSID (String ssid) {
	    SSID = ssid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }
}