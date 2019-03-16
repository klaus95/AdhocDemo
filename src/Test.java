import AdhocAPI.*;

public class Test {

    public static void main(String[] args) {
        try {

            AdHocConfig network = FactoryAdHocConfig.init();
            network.createNetwork("Connect", "", "Wi-Fi", 11);
            Thread.sleep(40*1000);
            network.disconnectFromNetwork();
            network.connectToNetwork("PanthAIR", "", "Wi-Fi", 11);
            System.out.print("Success");
        } catch(Exception e) {
            System.out.println("Shit happened");
            //e.printStackTrace();
        }
    }
}
