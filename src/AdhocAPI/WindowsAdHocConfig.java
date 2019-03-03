package AdhocAPI;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class WindowsAdHocConfig extends AdHocConfig {

    public boolean fileExists(String fileName) {
        String path =Paths.get(".").toAbsolutePath().normalize().toString() + "\\code\\scripts\\Windows\\ + fileName";
        File temp = new File(path);
        return temp.exists();
    }

   @Override
   public String[] getInterfaces() throws ScriptMissingException, ScriptFailureException, DeniedPermissionException {
       try {
           String scriptName = "WINDOWS_INTERFACE_SHOW_INTERFACE.cmd";
           if (!fileExists(scriptName)) {
               throw new ScriptMissingException(scriptName);
               // for testing
               //System.out.println("Missing the neccessary files in getInterfaces()");
           }
           String[] script_with_args = {scriptName};
           ScriptMeta metadata = runScript(script_with_args);
           metadata.setName(scriptName);
           Scanner output = new Scanner(metadata.getOutput());
            /* The pattern is:
               Admin State --- State --- Type --- Interface Name
             */
            LinkedList<String> interfaces = new LinkedList<String>();
            for (int i = 0; i < 2; i++) {
                // burn the header
                final String line = output.nextLine();
            }
            while (output.hasNext()) {
                final String line = output.nextLine();
                    try {
                        String[] words = line.split("\\s+");
                        interfaces.add(words[3]); // return the Interface Name
                    } catch (PatternSyntaxException ex) {
                        System.out.println("Please fix the getInterfaces method in SystemConfigurationWindows.java. I'm right here in the try/catch of the while loop.");

                    }
            }

           try {
               if (metadata.getErrorCode() != 0) {
                   throw new ScriptFailureException(metadata);
               }
           } catch (ScriptFailureException e) {
               ErrorLogging.LOGS.log(Level.SEVERE, e.toString(), e);
               throw e;
           }
           output.close();
           return interfaces.toArray(new String[interfaces.size()]);

       } catch (Exception e) {
           throw catchSFE(e);

       }
   }

    @Override
    public String[] getConnectedInterfaces() throws ScriptFailureException {
        try {
            String[] script_with_args = {"WINDOWS_INTERFACE_SHOW_INTERFACE.cmd"};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_INTERFACE_SHOW_INTERFACE.cmd");
            Scanner output = new Scanner(metadata.getOutput());
            /* The pattern is:
               Admin State --- State --- Type --- Interface Name
             */
            Pattern pattern = Pattern.compile("Enabled[ ]*Connected[ ]*.*[ ]*.*$");
            LinkedList<String> interfaces = new LinkedList<String>();
            while (output.hasNext()) {
                final String line = output.nextLine();
                Matcher isMatch = pattern.matcher(line);
                if (isMatch.find()) {
                    try {
                        String[] words = line.split("\\s+");
                        StringBuilder elem = new StringBuilder(words[3]);
                        // this accounts for spaces in the interface name
                        for (int i = 4; i < words.length; i++) {
                            elem.append(" ");
                            elem.append(words[i]);
                        }
                        interfaces.add(elem.toString()); // record the Interface Name
                    } catch (PatternSyntaxException ex) {
                        System.out.println("Please fix the getConnectedInterfaces method in SystemConfigurationWindows.java. I'm right here in the try/catch of the while loop.");

                    }
                }
            }

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
            output.close();
            return interfaces.toArray(new String[interfaces.size()]);
        } catch (Exception e) {
            throw catchSFE(e);
        }
    }

    @Override
    public int[] getSupportedChannels(String wirelessInterface) {
        int[] channels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        return channels;
    }

    @Override
    public int connectToNetwork() throws ScriptFailureException {
        try {
            createProfile();
            String[] script_with_args = {"WINDOWS_CONNECT_TO_NETWORK.cmd", this.getSSID(), this.getNetworkInterface()};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_CONNECT_TO_NETWORK.cmd");
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }

    @Override
    public int connectToNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException {
        try {
            //*************Update the SystemConfiguration here**************************
            this.setSSID(networkName);
            this.setPassword(password);
            this.setNetworkInterface(interfaceName);
            this.setChannel(channel);
            //***************************************************************************
            createProfile();
            String[] script_with_args = {"WINDOWS_CONNECT_TO_NETWORK.cmd", networkName, interfaceName};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_CONNECT_TO_NETWORK.cmd");
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }

    public int createProfile() throws ScriptFailureException {
        try {
            writeXML(this.getSSID());
            String[] script_with_args = {"WINDOWS_CREATE_PROFILE.cmd", this.getSSID() + ".xml", this.getNetworkInterface()};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_CREATE_PROFILE.cmd");

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }

    public static void writeXML(String SSID) throws IOException {
        StringBuilder xml_file = new StringBuilder();
        xml_file.append("<?xml version=\"1.0\"?>\n");
        xml_file.append("<WLANProfile xmlns=\"http://www.microsoft.com/networking/WLAN/profile/v1\">\n");
        xml_file.append("<name>" + SSID + "</name>\n");
        xml_file.append("<SSIDConfig>\n");
        xml_file.append("<SSID>\n");
        xml_file.append("<hex>"+toHex(SSID)+"</hex>\n");
        xml_file.append("<name>" + SSID + "</name>\n");
        xml_file.append("</SSID>\n");
        xml_file.append("<nonBroadcast>false</nonBroadcast>\n");
        xml_file.append("</SSIDConfig>\n");
        xml_file.append("<connectionType>IBSS</connectionType>\n");
        xml_file.append("<connectionMode>manual</connectionMode>\n");
        xml_file.append("<MSM>\n");
        xml_file.append("<security>\n");
        xml_file.append("<authEncryption>\n");
        xml_file.append("<authentication>open</authentication>\n");
        xml_file.append("<encryption>none</encryption>\n");
        xml_file.append("<useOneX>false</useOneX>\n");
        xml_file.append("</authEncryption>\n");
        xml_file.append("</security>\n");
        xml_file.append("</MSM>\n");
        xml_file.append("</WLANProfile>\n");
        String filename = SSID + ".xml";
        String dir =Paths.get(".").toAbsolutePath().normalize().toString() + "\\code\\scripts\\Windows\\";
        Files.write(Paths.get(dir + filename), xml_file.toString().getBytes());
        // creates new xml file each time a new profile is added
    }

    public static String toHex(String networkName) {
        return String.format("%X", new BigInteger(1, networkName.getBytes()));
    }

    /*
    @Override
    public int connectToNetwork(String profile, String ssid, String interfaceName) throws ScriptFailureException {
        try {
            String[] script_with_args = {"WINDOWS_CONNECT_TO_NETWORK_THREE_ARGS.cmd", profile, ssid, interfaceName};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_CONNECT_TO_NETWORK_THREE_ARGS.cmd");
            //*************Update the SystemConfiguration here**************************
            this.setSSID(ssid);
            this.setNetworkInterface(interfaceName);
            this.setPassword("");
            //***************************************************************************

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }
    */

	/*@Override
    public int createNetwork(String ssid, String password_key) throws ScriptFailureException {
        try {
            /* Possible password restrictions here
            if (password_key.length() < 8) {
                System.out.println("Password key must be between _ and _ characters");
                return 1;
            }
            String[] script_with_args = {"WINDOWS_START_HOST_ADHOC.cmd", ssid, password_key};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_START_HOST_ADHOC.cmd");
            //*********Update the SystemConfiguration here***********************************
            this.setSSID(ssid);
            this.setPassword(password_key);
            this.setNetworkInterface(getCurrentInterfaces());
            //*******************************************************************************//*
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }
    */

    @Override
    public int createNetwork() throws ScriptFailureException {
        /*try {
             Possible password restrictions here
            if (password_key.length() < 8) {
                System.out.println("Password key must be between _ and _ characters");
                return 1;
            }
            */
            /*String[] script_with_args = {"WINDOWS_START_HOST_ADHOC.cmd", this.getSSID(), this.getPassword()};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_START_HOST_ADHOC.cmd");
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }*/

            connectToNetwork();
        return 0;
    }

    @Override
    public int createNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException {
       /* try {
             Possible password restrictions here
            if (password_key.length() < 8) {
                System.out.println("Password key must be between _ and _ characters");
                return 1;
            }
            */
            /*
            String[] script_with_args = {"WINDOWS_START_HOST_ADHOC.cmd", networkName, password};
            /****************Update Object attributes***********************************/
            /*
            this.setSSID(networkName);
            this.setPassword(password);
            this.setNetworkInterface(interfaceName);
            this.setChannel(channel);/*
            /***************************************************************************/
            /*ScriptMeta metadata = runScript(script_with_args);
            metadata.setName("WINDOWS_START_HOST_ADHOC.cmd");
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        */
        connectToNetwork(networkName, password, interfaceName, channel);
        return 0;
    }

    @Override
    public int disconnectFromNetwork() throws ScriptFailureException {
        try {
            // old way was to host networks, but thats in infrastructure mode
            //String[] script_name = {"WINDOWS_STOP_HOST_ADHOC.cmd"};
            String[] script_name = {"WINDOWS_DELETE_PROFILE.cmd", this.getNetworkInterface(), this.getSSID()};
            ScriptMeta metadata = runScript(script_name);
            //metadata.setName("WINDOWS_STOP_HOST_ADHOC.cmd");
            metadata.setName("WINDOWS_DELETE_PROFILE.cmd");
            /*//*********Update the SystemConfiguration here***********************************
            this.setSSID("");
            this.setPassword("");
            this.setNetworkInterface("");
            //*******************************************************************************/
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }

    @Override
    public boolean isAdHocCapable() {
        // do stuff here
        return true;
    }

    private static ScriptFailureException sendLOG (ScriptMeta metadata) {
        ScriptFailureException temporary = new ScriptFailureException(metadata);
        ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
        return temporary;
    }

    private static ScriptMeta runScript(String[] command) throws IOException, InterruptedException {
        LinkedList<String> commands_with_arguments = new LinkedList<String>(Arrays.asList("cmd", "/c"));
        for (String s : command) {
            commands_with_arguments.add(s);
        }
        ProcessBuilder pb = new ProcessBuilder(commands_with_arguments);
        //ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command[0]);
        pb.directory(new File("code/scripts/Windows"));
        Process p = pb.start();
        return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
    }
}
