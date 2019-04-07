import AdhocAPI.*;

import javax.swing.*;

public class ConnectNetwork extends Thread {
    static String WIFIname;
    static String WIFIpassword;
    static int WIFIchannel;
    static String WIFIinterface;
    static JLabel LabelStatus;
    static JFrame frame;

    private AdHocConfig network;

    public ConnectNetwork(String name, String password, int channel, String interfaceName, JLabel status, JFrame frame) {
        WIFIname = name;
        WIFIpassword = password;
        WIFIchannel = channel;
        WIFIinterface = interfaceName;
        LabelStatus = status;
        ConnectNetwork.frame = frame;
    }

    @Override
    public void run() {
        super.run();

        try {
            network = FactoryAdHocConfig.init();
            network.setChannel(WIFIchannel);
            network.setNetworkInterface(WIFIinterface);
            network.setPassword(WIFIpassword);
            network.setSSID(WIFIname);

            try {
                network.createNetwork();
                LabelStatus.setText(" Connected to  \"" + WIFIname + "\"!");
                frame.pack();
            } catch (Exception e) {
                e.printStackTrace();
                LabelStatus.setText(" Failed to connect to \"" + WIFIname + "\"!");
                frame.pack();
            }
        } catch (UnknownOSException e) {
            System.out.println("OS not supported");
        }
    }
}