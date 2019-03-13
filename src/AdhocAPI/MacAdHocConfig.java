package AdhocAPI;

import javax.swing.plaf.basic.BasicTreeUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

public class MacAdHocConfig extends AdHocConfig {
    @Override
    public String[] getInterfaces() throws ScriptFailureException, ScriptMissingException {
        String output;

        try {

          String[] temp = {"./all_interfaces.swift"};
          ScriptMeta metadata = runScript(temp);
          metadata.setName(temp[0].substring(2));

          output = metadata.getOutput();

          if (metadata.getErrorCode() != 0) {
              throw sendLOG(metadata);
          }
      } catch (InterruptedException e) {
          throw catchSFE(e);
      }
      return output.split(":");
    }

    @Override
    public String[] getConnectedInterfaces() throws ScriptFailureException, ScriptMissingException {
        String output;

        try {

            String[] temp = {"./current_interface.swift"};
            ScriptMeta metadata = runScript(temp);
            metadata.setName(temp[0].substring(2));

            output = metadata.getOutput();

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }

        setNetworkInterface(output);
        return null;
    }

    @Override
    public int[] getSupportedChannels(String wirelessInterface) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException {
        String output;

        setNetworkInterface(wirelessInterface);

        if (getNetworkInterface() == null) {
            throw new MissingArgumentsException("[Network Interface]");
        }

        try {

            String[] temp = {"./all_channels.swift", wirelessInterface};
            ScriptMeta metadata = runScript(temp);
            metadata.setName(temp[0].substring(2));

            output = metadata.getOutput();

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }

        String[] channels = output.split(":");
        int[] intChannels = new int[channels.length];
        int index = 0;
        for (String str : channels) {
            intChannels[index] = Integer.parseInt(str);
            index++;
        }

        return intChannels;
    }

    @Override
    public int connectToNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException {
        checkMissingArgs(networkName, password, interfaceName, channel);

        try {
            String[] temp = {"./connect_to_network.swift", interfaceName, networkName, password};
            ScriptMeta metadata = runScript(temp);
            metadata.setName(temp[0].substring(2));

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (InterruptedException e) {
            throw catchSFE(e);
        }
        return 0;
    }

    @Override
    public int connectToNetwork() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException {
        connectToNetwork(getSSID(), getPassword(), getNetworkInterface(), getChannel());
        return 0;
    }

    @Override
    public int disconnectFromNetwork() throws ScriptFailureException, ScriptMissingException {
        try {
            String[] temp = {"./disconnect_from_network.swift"};
            ScriptMeta metadata = runScript(temp);
            metadata.setName(temp[0].substring(2));

            if (metadata.getErrorCode() != 0) {
                throw sendLOG(metadata);
            }
        } catch (InterruptedException e) {
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
    public int createNetwork(String networkName, String password, String interfaceName, int channel) throws ScriptFailureException, MissingArgumentsException, ScriptMissingException {
        checkMissingArgs(networkName, password, interfaceName, channel);

        try {
            String[] temp = {"./create_network.swift",interfaceName , networkName, password, "" + channel};
            ScriptMeta metadata = runScript(temp);
            metadata.setName(temp[0].substring(2));

            if (metadata.getErrorCode() != 0 || metadata.getOutput().contains("Error")) {
                throw sendLOG(metadata);
            }
        }catch (InterruptedException e) {
            throw catchSFE(e);
        }
        return 0;
    }

    @Override
    public int createNetwork() throws ScriptFailureException, MissingArgumentsException, ScriptMissingException {
        createNetwork(getSSID(), getPassword(), getNetworkInterface(), getChannel());
        return 0;
    }

    @Override
    public boolean isAdHocCapable() {
        return true;
    }

    private static ScriptMeta runScript(String[] command) throws InterruptedException, ScriptMissingException {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            String absPath = new File("src/AdhocAPI/scripts").getAbsolutePath();
            pb.directory(new File(absPath));
            Process p = pb.start();
            return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
        } catch (IOException ex) {
            ScriptMissingException sme = new ScriptMissingException(command[0].substring(2));
            ErrorLogging.LOGS.log(Level.SEVERE, sme.toString(), sme);
            throw sme;
        }
    }

    private void checkMissingArgs(String networkName, String password, String interfaceName, int channel) throws MissingArgumentsException {
        setNetworkInterface(interfaceName);
        setChannel(channel);
        setPassword(password);
        setSSID(networkName);

        String missingArgs = "";

        if (getNetworkInterface() == null) missingArgs += "[Network Interface]";
        if (getSSID() == null) missingArgs += "[SSID]";
        if (getPassword() == null) missingArgs += "[Password]";
        if (getChannel() == 0) missingArgs += "[Channel]";

        if (!missingArgs.equals("")) throw new MissingArgumentsException(missingArgs);
    }

    private static ScriptFailureException sendLOG (ScriptMeta metadata) {
        ScriptFailureException temporary = new ScriptFailureException(metadata);
        ErrorLogging.LOGS.log(Level.SEVERE, temporary.toString(), temporary);
        return temporary;
    }

}
