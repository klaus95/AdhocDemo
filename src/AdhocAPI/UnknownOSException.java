package AdhocAPI;

public class UnknownOSException extends Exception {
    private String os;

    public UnknownOSException(String osName) {
        os = osName;
    }

    public String toString() {
        String retValue = "os name found is:" + os;
        retValue += "\r\n";
        retValue += "Expected to contain \"windows\", \"linux\", or \"mac os\"";
        return retValue;
    }
}