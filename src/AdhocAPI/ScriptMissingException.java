package AdhocAPI;

public class ScriptMissingException extends Exception {
    private String name;

    public ScriptMissingException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return " Filename: " + name + " Error: File is not found!";
    }

}
