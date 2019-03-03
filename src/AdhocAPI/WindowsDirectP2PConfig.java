package AdhocAPI;

public class WindowsDirectP2PConfig extends DirectP2PConfig {
    @Override
    public String[] getP2PNetworks() {
        return null;
    }

    @Override
    public int createNetwork(String name, String PIN) {
        return 0;
    }

    @Override
    public int joinNetwork(String networkName, String PIN) {
        return 0;
    }

    @Override
    public boolean isP2PCapable() {
        //do stuff
        return true;
    }
}
