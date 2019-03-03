package AdhocAPI;

public class ScriptFailureException extends Exception {
    private ScriptMeta meta;

    public ScriptFailureException(ScriptMeta metadata) {
        meta = metadata;
    }

    public ScriptMeta getMetaData() {
        return meta;
    }

    public String toString() {
        return " Filename: " + meta.name + " " + meta.output;
    }

}