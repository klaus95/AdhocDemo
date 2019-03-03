package AdhocAPI;

public class DeniedPermissionException extends Exception {
    private String fileName;

    public DeniedPermissionException(String file) {
        this.fileName = file;
    }
    public String toString() {
        return "File: " + fileName + " was denied Permission"; // the only relevant one would be the execute permission
    }
}
