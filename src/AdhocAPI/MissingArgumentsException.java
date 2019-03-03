package AdhocAPI;

public class MissingArgumentsException extends Exception {
    String args;

    MissingArgumentsException (String args) {
        this.args = args;
    }

    public String toString() {
        return "Arguments missing: " + args;
    }
}
