package AdhocAPI;

public abstract class DirectP2PConfig {

    public abstract String[] getP2PNetworks();

    public abstract int createNetwork(String name, String PIN);

    public abstract int joinNetwork(String networkName, String PIN);

    public abstract boolean isP2PCapable();
}
