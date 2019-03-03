package AdhocAPI;

public class ScriptMeta {
	String name;
	int errorCode;
	String output;

	public ScriptMeta (int errorCode, String output) {
		this.errorCode = errorCode;
		this.output = output;
		this.name = "";
	}
	public void setName(String name) { this.name = name;}

	public int getErrorCode() { return errorCode; }
	public String getName() { return name;}
	public String getOutput() { return output; }
}
