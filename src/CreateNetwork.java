import AdhocAPI.*;

import javax.swing.*;

public class CreateNetwork extends Thread {
    static String WIFIname;
    static String WIFIpassword;
    static int WIFIchannel;
    static String WIFIinterface;
    static JLabel performance;
    static JFrame frame;

    private AdHocConfig network;

    public CreateNetwork(String name, String password, int channel, String interfaceName, JLabel perf, JFrame frame) {
        WIFIname = name;
        WIFIpassword = password;
        WIFIchannel = channel;
        WIFIinterface = interfaceName;
        performance = perf;
        CreateNetwork.frame = frame;
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
                //network.createNetwork();
                performance.setText(" WIFI \"" + WIFIname + "\" created!");
                frame.pack();
            } catch (Exception e) {
                e.printStackTrace();
                performance.setText(" WIFI \"" + WIFIname + "\" failed!");
                frame.pack();
            }
        } catch (UnknownOSException e) {
            System.out.println("OS not supported");
        }
    }
}
