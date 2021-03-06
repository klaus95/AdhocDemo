package AdhocAPI;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.Random;

public class WindowsAdHocConfig extends AdHocConfig {

    public static void setDynamicIP(String interfaceName) {
        // reset the user's interface to dyanmic ip assignment after disconnecting from a network
    }

    public static void setStaticIP (String interfaceName) throws ScriptFailureException {
        // IP address have four sets of values between 1 and 254, 255 is broadcast, and 0 is not usable
        // in our case the first two sets are known
        //StringBuilder new_ip = new StringBuilder("169.254.1");
        //Random rand =  new Random();
       // int fourth = rand.nextInt(255);
       // new_ip.append("." + fourth);
       // String ip = new_ip.toString();
        // now run script to set
        String ip = "169.254.1.1";
        String subnet_mask = "255.255.255.0";
        String default_gateway = "192.168.1.1";

        try {
            final String scriptName = "WINDOWS_SET_STATIC_IP.cmd";
            String[] script_name = {scriptName, interfaceName, ip, subnet_mask, default_gateway};
            ScriptMeta metadata = runScript(script_name);
            metadata.setName(scriptName);
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }

    }

    public boolean fileExists(String fileName) {
        String path =Paths.get(".").toAbsolutePath().normalize().toString() + "\\src\\AdhocAPI\\scripts\\Windows\\" + fileName;
        File temp = new File(path);
        return temp.exists();
    }

    public String getMissingArgs() {
        StringBuilder missingArgs = new StringBuilder();

        if (this.getNetworkInterface() == null) { missingArgs.append("[Network Interface]");}
        if (this.getSSID() == null) { missingArgs.append("[SSID]");}
        if (this.getPassword() == null){ missingArgs.append("[Password]");}
        if (this.getChannel() == 0){ missingArgs.append("[Channel]");}
        return missingArgs.toString();
    }

   @Override
   public String[] getInterfaces() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException {
       final String scriptName = "WINDOWS_INTERFACE_SHOW_INTERFACE.cmd";
       if (!fileExists(scriptName)) {
           throw new ScriptMissingException(scriptName);
       }
       // make sure to finish the denied permission exception in each method
       if (1 < 0) {
           throw new DeniedPermissionException("");
       }
        try {
            //**************Check that all the necessary arguments are defined***********
            //String missingArgs = getMissingArgs();
            //if (!missingArgs.equals("")) throw new MissingArgumentsException(missingArgs);
            //****************************************************************************
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
                        StringBuilder interfaceName = new StringBuilder();
                        for (int i = 3; i < words.length; i++) {
                            if (interfaceName.length() > 0) {
                                interfaceName.append(" ");
                            }
                            interfaceName.append(words[i]);
                        }
                        interfaces.add(interfaceName.toString()); // return the Interface Name
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

       } catch (IOException e) {
           throw catchSFE(e);

       } catch (InterruptedException e) {
            throw catchSFE(e);
        }
   }

    @Override
    public String[] getConnectedInterfaces() throws ScriptFailureException {
        try {
            //**************Check that all the necessary arguments are defined***********
            String missingArgs = getMissingArgs();
            if (!missingArgs.equals("")) throw new MissingArgumentsException(missingArgs);
            //****************************************************************************
            final String scriptName = "WINDOWS_INTERFACE_SHOW_INTERFACE.cmd";
            String[] script_with_args = {scriptName};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName(scriptName);
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
    public int connectToNetwork() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException {
        connectToNetwork(this.getSSID(), this.getPassword(), this.getNetworkInterface(), this.getChannel());
        return 0;
    }

    @Override
    public int connectToNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException, DeniedPermissionException {
        createProfile();
        try {
            //*************Update the SystemConfiguration here**************************
            this.setSSID(networkName);
            this.setPassword(password);
            this.setNetworkInterface(interfaceName);
            this.setChannel(channel);
            //***************************************************************************
            //**************Check that all the necessary arguments are defined***********
            String missingArgs = getMissingArgs();
            if (!missingArgs.equals("")) throw new MissingArgumentsException(missingArgs);
            //***************************************************************************
            final String scriptName = "WINDOWS_CONNECT_TO_NETWORK.cmd";
            String[] script_with_args = {scriptName, networkName, interfaceName};
            ScriptMeta metadata = runScript(script_with_args);
            metadata.setName(scriptName);
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
            // now set a static ip address 169.254.XXX.XXX
            setStaticIP(interfaceName);
            //**********************************************
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
    }

    public int createProfile() throws ScriptFailureException {
        try {
            //**************Check that all the necessary arguments are defined***********
            String missingArgs = getMissingArgs();
            if (!missingArgs.equals("")) throw new MissingArgumentsException(missingArgs);
            //****************************************************************************
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
        String dir =Paths.get(".").toAbsolutePath().normalize().toString() + "\\src\\AdhocAPI\\scripts\\Windows\\";
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
    public int createNetwork() throws ScriptFailureException, MissingArgumentsException {
            createNetwork(this.getSSID(), this.getPassword(), this.getNetworkInterface(), this.getChannel());
        return 0;
    }

    @Override
    public int createNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, MissingArgumentsException {
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
        //*************Update the SystemConfiguration here**************************
        this.setSSID(networkName);
        this.setPassword(password);
        this.setNetworkInterface(interfaceName);
        this.setChannel(channel);
        //***************************************************************************
        //**************Check that all the necessary arguments are defined***********
        String missingArgs = getMissingArgs();
        if (!missingArgs.equals("")) throw new MissingArgumentsException(missingArgs);
        //***************************************************************************
        createProfile();
        // new networks must have a profile
        // this method call is all that makes createNetwork different from connectToNetwork
        // if you create a profile for a network that already exists, then
        // instead of connecting, then it will over-write the old profile info
        // and you will end up with your own local ad-hoc network with an identical SSID
        // instead of connecting to the network you intended to.
        // ONLY USE CREATE_PROFILE for NEW AD-HOC Networks, hence why it's ONLY in
        // the createNetwork function
        try {
            connectToNetwork(networkName, password, interfaceName, channel);
        } catch (ScriptMissingException e) {
            e.printStackTrace();
        } catch (DeniedPermissionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int disconnectFromNetwork() throws ScriptFailureException {
        try { // Set the interface back to dynamic ip
            String new_script = "WINDOWS_SET_DYNAMIC_IP";
            String[] new_script_name = {new_script, this.getNetworkInterface()};
            ScriptMeta metadata2 = runScript(new_script_name);
            metadata2.setName(new_script);
        } catch(Exception e) {
            throw catchSFE(e);
        }
        try {
            // old way was to host networks, but thats in infrastructure mode
            //String[] script_name = {"WINDOWS_STOP_HOST_ADHOC.cmd"};
            final String scriptName = "WINDOWS_DELETE_PROFILE.cmd";
            String[] script_name = {scriptName, this.getNetworkInterface(), this.getSSID()};
            ScriptMeta metadata = runScript(script_name);
            //metadata.setName("WINDOWS_STOP_HOST_ADHOC.cmd");
            metadata.setName(scriptName);
            /*//*********Update the SystemConfiguration here***********************************
            this.setSSID("");
            this.setPassword("");
            this.setNetworkInterface("");
            //*******************************************************************************/
            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
            try { // Set the interface back to dynamic ip
                String new_script = "WINDOWS_SET_DYNAMIC_IP";
                String[] new_script_name = {new_script, this.getNetworkInterface()};
                ScriptMeta metadata2 = runScript(new_script_name);
                metadata2.setName(new_script);
            } catch(Exception e) {
                throw catchSFE(e);
            }
        } catch (Exception e) {
            throw catchSFE(e);
        }
        return 0;
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
        pb.directory(new File("src/AdhocAPI/scripts/Windows"));
        Process p = pb.start();
        return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
    }
}
