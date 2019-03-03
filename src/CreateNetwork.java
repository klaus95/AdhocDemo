import AdhocAPI.*;

public class CreateNetwork extends Thread {
    static String WIFIname;
    static String WIFIpassword;
    static int WIFIchannel;
    static String WIFIinterface;

    private AdHocConfig network;

    public CreateNetwork(String name, String password, int channel,String interfaceName) {
        WIFIname = name;
        WIFIpassword = password;
        WIFIchannel = channel;
        WIFIinterface = interfaceName;
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
            } catch (ScriptFailureException e) {
                e.printStackTrace();
            } catch (MissingArgumentsException e) {
                e.printStackTrace();
            } catch (ScriptMissingException e) {
                e.printStackTrace();
            } catch (DeniedPermissionException e) {
                e.printStackTrace();
            }
        } catch (UnknownOSException e) {
            System.out.println("OS not supported");
        }
    }
}
