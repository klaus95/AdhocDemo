import AdhocAPI.*;

public class DiscconectNetwork extends Thread {

    private AdHocConfig network;

    @Override
    public void run() {
        super.run();

        try {
            network = FactoryAdHocConfig.init();

            try {
                network.disconnectFromNetwork();
                network.connectToNetwork("PanthAIR", "", "en0", 11);
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
