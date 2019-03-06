package AdhocAPI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinuxAdHocConfig extends AdHocConfig {
    private int[] channelsforareason;
    private int[] channelsinHz;
    @Override
    public String[] getInterfaces() throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        try {
            String[] temp = {"/bin/bash", "SCRIPT_LINUX_DETECT_WIRELESS_INTERFACES.sh"};
            ScriptMeta metadata = runScript(temp);
            metadata.setName("SCRIPT_LINUX_DETECT_WIRELESS_INTERFACES.sh");
            if (metadata.getErrorCode() != 0) {
                if (metadata.getErrorCode() == 2) {
                    ScriptMissingException temporary = new ScriptMissingException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else if (metadata.getErrorCode() == 20/*placeholder for deniedpermission*/) {
                    DeniedPermissionException temporary = new DeniedPermissionException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else {
                    ScriptFailureException temporary = new ScriptFailureException(metadata);
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                }

            }
            return metadata.getOutput().split("\n");

        } catch (IOException e) {
            throw catchSFE(e);
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }
    }

    @Override
    public String[] getConnectedInterfaces() throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        try {
            String[] temp = {"/bin/bash", "SCRIPT_LINUX_FIND_CURRENT_INTERFACE.sh"};
            ScriptMeta metadata = runScript(temp);
            metadata.setName("SCRIPT_LINUX_FIND_CURRENT_INTERFACE.sh");
            if (metadata.getErrorCode() != 0) {
                if (metadata.getErrorCode() == 2) {
                    ScriptMissingException temporary = new ScriptMissingException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else if (metadata.getErrorCode() == 20/*placeholder for deniedpermission*/) {
                    DeniedPermissionException temporary = new DeniedPermissionException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else {
                    ScriptFailureException temporary = new ScriptFailureException(metadata);
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                }
            }
            String[] stuff = metadata.output.split(":\n");
            stuff[stuff.length-1] = stuff[stuff.length-1].substring(0, stuff[stuff.length-1].length()-1);
            return stuff;
        } catch (IOException e) {
            throw catchSFE(e);
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }
    }


    @Override
    public int createNetwork() throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        try {

            String temp = "";
            String openOrRestricted = "";
            if (this.getPassword().equals("off") || this.getPassword().equals("")) {
                temp = "off";
                openOrRestricted = "open";
            } else {
                temp = "s:"+this.getPassword();
                openOrRestricted = "restricted";
            }
            if (openOrRestricted.equals("restricted")) {

                ErrorLogging.LOGS.log(Level.WARNING, "Passphrase supplied, attempting to use wpa_supplicant to configure interface");
                //attempt to use a script for wpa_supplicant confguration
                try {
                    writeWPASupplicantConf();
                    //use wpasupplicant script
                    String[] additionalArguments = {"/bin/bash", "SCRIPT_LINUX_WPA_SUPPLICANT.sh", this.getNetworkInterface()};
                    ScriptMeta metadata = runScript(additionalArguments);
                    metadata.setName("SCRIPT_LINUX_WPA_SUPPLICANT.sh");
                    if (metadata.getErrorCode() != 0) {
                        if (metadata.getErrorCode() == 2) {
                            ScriptMissingException temporary = new ScriptMissingException(metadata.getName());
                            ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                            throw temporary;
                        } else if (metadata.getErrorCode() == 20/*placeholder for deniedpermission*/) {
                            DeniedPermissionException temporary = new DeniedPermissionException(metadata.getName());
                            ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                            throw temporary;
                        } else {
                            ScriptFailureException temporary = new ScriptFailureException(metadata);
                            ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                            throw temporary;
                        }
                    }
                    //System.out.println(metadata.getOutput());
                    return 0;
                } catch (IOException e) {
                    ErrorLogging.LOGS.log(Level.WARNING, "Failed to use wpa_supplicant to configure device. Attempting to use iwconfig commands");
                    throw catchSFE(e);
                } catch (InterruptedException e) {
                    ErrorLogging.LOGS.log(Level.WARNING, "Failed to use wpa_supplicant to configure device. Attempting to use iwconfig commands");
                    throw catchSFE(e);
                }
            }
            //if failure, level.warning and attempt to use iwconfig script
            //if success, level.warning "In WEP security, network interface configured"
            //if failure again, level.severe and inform of failure to configure interface

            ErrorLogging.LOGS.log(Level.WARNING, "attempting to configure device without wpa_supplicant");
            String[] additionalArguments =  {"/bin/bash", "SCRIPT_LINUX_CONFIGURE_WIRELESS_INTERFACE.sh", this.getNetworkInterface(), ""+this.getChannel(), this.getSSID(), temp, openOrRestricted, ""};
            ScriptMeta metadata = runScript(additionalArguments);
            metadata.setName("SCRIPT_LINUX_CONFIGURE_WIRELESS_INTERFACE.sh");
            if (metadata.getErrorCode() != 0) {
                if (metadata.getErrorCode() == 2) {
                    ScriptMissingException temporary = new ScriptMissingException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else if (metadata.getErrorCode() == 20/*placeholder for deniedpermission*/) {
                    DeniedPermissionException temporary = new DeniedPermissionException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else {
                    ScriptFailureException temporary = new ScriptFailureException(metadata);
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                }
            }
            System.out.println(metadata.getOutput());
        } catch (IOException e) {
            throw catchSFE(e);
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }
        return 0;


    }


    private int indexOfinhz() {
        for (int i = 0; i < channelsforareason.length; i++) {
            if (channelsforareason[i] == this.getChannel()) {
                return i;
            }
        }
        return 0;
    }
    private void writeWPASupplicantConf() throws IOException {
        String res = "ap_scan=2\n";
        res += "network={";
        res += "\n";
        res += "ssid=\"" +this.getSSID() +"\"";
        res += "\n";
        res += "frequency=" +channelsinHz[indexOfinhz()];
        res += "\n";
        if (!getPassword().equals("off")) {
            res += "proto=WPA";
            res += "\n";
            res += "key_mgmt=WPA-NONE";
            res += "\n";
            res += "psk=\"" + this.getPassword() + "\"";
            res += "\n";
        }
        res += "mode=1";
        res += "\n";
        res += "pairwise=NONE";
        res += "\n";
        res += "group=TKIP";
        res += "\n";
        res += "}";
        Files.write(Paths.get("wpa.conf"), res.getBytes());
    }

    @Override
    public int[] getSupportedChannels(String wirelessInterface) throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        //it esd put here
        Pattern pattern = Pattern.compile("[ ]*Channel .* : .* GHz$");
        try {
            String[] additionalArguments = {"/bin/bash", "SCRIPT_LINUX_GET_SUPPORTED_CHANNELS.sh", wirelessInterface};
            ScriptMeta metadata = runScript(additionalArguments);
            metadata.setName("SCRIPT_LINUX_GET_SUPPORTED_CHANNELS.sh");
            if (metadata.getErrorCode() != 0) {
                if (metadata.getErrorCode() == 2) {
                    ScriptMissingException temporary = new ScriptMissingException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else if (metadata.getErrorCode() == 20/*placeholder for deniedpermission*/) {
                    DeniedPermissionException temporary = new DeniedPermissionException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else {
                    ScriptFailureException temporary = new ScriptFailureException(metadata);
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                }
            }
            //System.out.println(metadata.getOutput());
            String[] res = metadata.getOutput().split("\n");
            int count = 0;

            for (String s : res) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    count++;
                }
            }
            int[] channels = new int[count];
            channelsinHz = new int[count];
            count = 0;
            for (String s : res) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String[] temp = s.split(" ");
                    //System.out.printf("DEBUG: %s\n", Arrays.toString(temp));
                    String ezGigs = temp[temp.length - 2];
                    ezGigs = ezGigs.replace(".", "");
                    //System.out.printf("DEBUG: %d", Integer.parseInt(ezGigs));
                    // temp[temp.length - 1] = "MHz"
                    channels[count] = Integer.parseInt(temp[11]);
                    channelsinHz[count] = Integer.parseInt(ezGigs);
                    count++;
                }
            }
            Arrays.sort(channels);
            Arrays.sort(channelsinHz);
            channelsforareason = Arrays.copyOf(channels, channels.length);
            return channels;
        } catch (IOException e) {
            throw catchSFE(e);
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }
    }

    public int connectToNetwork() throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        return createNetwork();

    }

    public int createNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        return connectToNetwork(networkName, password, interfaceName, channel);
    }

    @Override
    public int connectToNetwork(String networkName, String pass, String interfaceName, int channel) throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        this.setChannel(channel);
        this.setSSID(networkName);
        this.setPassword(pass);
        this.setNetworkInterface(interfaceName);
        return createNetwork();
    }

    @Override
    public int disconnectFromNetwork() throws ScriptFailureException, ScriptMissingException, DeniedPermissionException {
        try {
            String[] temp = {"/bin/bash", "SCRIPT_LINUX_DISCONNECT.sh"};
            ScriptMeta metadata = runScript(temp);
            metadata.setName("SCRIPT_LINUX_DISCONNECT.sh");
            if (metadata.getErrorCode() != 0) {
                if (metadata.getErrorCode() == 2) {
                    ScriptMissingException temporary = new ScriptMissingException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else if (metadata.getErrorCode() == 20/*placeholder for deniedpermission*/) {
                    DeniedPermissionException temporary = new DeniedPermissionException(metadata.getName());
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                } else {
                    ScriptFailureException temporary = new ScriptFailureException(metadata);
                    ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
                    throw temporary;
                }
            }
            return 0;

        } catch (IOException e) {
            throw catchSFE(e);
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }

    }

    @Override
    public Socket clientSocket(String ip, int port) throws IOException {
        return new Socket(ip, port);
    }

    @Override
    public ServerSocket serverSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    @Override
    public boolean isAdHocCapable() {
        return true;
    }

    private ScriptMeta runScript(String[] additionalarguments) throws IOException, InterruptedException {
        ProcessBuilder pb;
        pb = new ProcessBuilder( additionalarguments);

        pb.directory(new File("../code/scripts"));
        Process p = pb.start();
        return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
    }

}
