import AdhocAPI.*;

public class Test {

    public static void main(String[] args) {
        try {

            AdHocConfig network = FactoryAdHocConfig.init();
            network.createNetwork("Connect", "", "en0", 11);
            Thread.sleep(40 * 1000);
            network.disconnectFromNetwork();
            network.connectToNetwork("PanthAIR", "", "en0", 11);

        } catch(Exception e) {
            System.out.println("Shit happened");
            //e.printStackTrace();
        }
    }
}
