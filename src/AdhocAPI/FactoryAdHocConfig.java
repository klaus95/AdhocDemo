package AdhocAPI;

public class FactoryAdHocConfig {

    public static AdHocConfig init() throws UnknownOSException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) {
            return new LinuxAdHocConfig();
        } else if (os.contains("mac")) {
            return new MacAdHocConfig();
        } else if (os.contains("windows")) {
            return new WindowsAdHocConfig();
        } else {
            throw new UnknownOSException(os);
        }
    }
}