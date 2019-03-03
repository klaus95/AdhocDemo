package AdhocAPI;

public class LinuxDirectP2PConfig extends DirectP2PConfig {


    @Override
    public String[] getP2PNetworks() {
        return new String[0];
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
        return false;
    }
}
