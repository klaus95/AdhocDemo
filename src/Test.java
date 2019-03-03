import AdhocAPI.*;

public class Test {

    public static void main(String[] args) {
        try {

            AdHocConfig network = FactoryAdHocConfig.init();
            //network.disconnectFromNetwork();
            network.connectToNetwork("PanthAIR", "", "en0", 11);
            //network.createNetwork("Connect", "hello123", "en0", 11);

        } catch(Exception e) {
            System.out.println("Shit happened");
            //e.printStackTrace();
        }
    }
}
