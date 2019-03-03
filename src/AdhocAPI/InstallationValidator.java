package AdhocAPI;

import java.lang.ProcessBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class InstallationValidator{

	private final static Logger LOGS = Logger.getLogger("InstallationValidatorLogger");
    private static ConsoleHandler console;
	private static FileHandler logFile;

	public static int validateScripts() throws IOException, UnknownOSException, ScriptFailureException, InterruptedException {


		try {
			console = new ConsoleHandler();
			logFile = new FileHandler("AdhocAPI/AdhocAPI.log", false);
			LOGS.addHandler(console);
			LOGS.addHandler(logFile);

		} catch (IOException e) {
			LOGS.log(Level.SEVERE, "Cannot construct log file", e);
		}

		String os = System.getProperty("os.name").toLowerCase();

		String[][] winScripts = { {"SCRIPT_WIN_WIRELESS_DETECT_SSID"},
        															{"SCRIPT_WINDOWS_ADMIN_RECONNECT_WIRELESS_DHCP"},
        															{"SCRIPT_WINDOWS_ADMIN_SET_WIRELESS_DD_IP"}
																	};

        		String[][] macScripts = { {"./mac_start.swift"} };

        		String[][] linuxScripts = { {"./SCRIPT_LINUX_DETECT_WIRELESS_INTERFACES.sh"},
        																{"./SCRIPT_LINUX_DETECT_WIRELESS_IP.sh"},
        																{"./SCRIPT_LINUX_DETECT_WIRELESS_SSID.sh"}};


		if (os.contains("windows")) {
			for (String[] scripts : winScripts) {
                try {
                    ScriptMeta metadata = runScript(scripts, os);
                    metadata.setName(scripts[0]);

                    try {
                        if (metadata.getErrorCode() != 0) {
                            throw new ScriptFailureException(metadata);
                        }
                    } catch (ScriptFailureException e) {
                        LOGS.log(Level.SEVERE, e.toString(), e);
                        throw e;
                    }


                } catch (IOException e) {
                    int errorCode = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("error=") + "error=".length(), e.getMessage().lastIndexOf(",")));
                    ScriptMeta temp = new ScriptMeta(errorCode, e.getMessage());
                    ScriptFailureException hold = new ScriptFailureException(temp);
                    LOGS.log(Level.SEVERE, hold.toString(), hold);
                    throw hold;

                }
            }
		} else if (os.contains("linux")) {
			for (String[] scripts : linuxScripts) {
			    try {
                    ScriptMeta metadata = runScript(scripts, os);
                    metadata.setName(scripts[0]);

                    try {
                        if (metadata.getErrorCode() != 0) {
                            throw new ScriptFailureException(metadata);
                        }
                    } catch (ScriptFailureException e) {
                        LOGS.log(Level.SEVERE, e.toString(), e);
                        throw e;
                    }
                } catch (IOException e) {
                    int errorCode = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("error=") + "error=".length(), e.getMessage().lastIndexOf(",")));
                    ScriptMeta temp = new ScriptMeta(errorCode, e.getMessage());
                    ScriptFailureException hold = new ScriptFailureException(temp);
                    LOGS.log(Level.SEVERE, hold.toString(), hold);
                    throw hold;

                }

			}
		} else if (os.contains("mac os")) {
			for (String[] scripts : macScripts) {
                try {
                    ScriptMeta metadata = runScript(scripts, os);
                    metadata.setName(scripts[0]);

                    try {
                        if (metadata.getErrorCode() != 0) {
                            throw new ScriptFailureException(metadata);
                        }
                    } catch (ScriptFailureException e) {
                        LOGS.log(Level.SEVERE, e.toString(), e);
                        throw e;
                    }

                } catch (IOException e) {
                    int errorCode = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("error=") + "error=".length(), e.getMessage().lastIndexOf(",")));
                    ScriptMeta temp = new ScriptMeta(errorCode, e.getMessage());
                    ScriptFailureException hold = new ScriptFailureException(temp);
                    LOGS.log(Level.SEVERE, hold.toString(), hold);
                    throw hold;

                }
            }
		} else {
		    LOGS.log(Level.SEVERE, "Unknown OS: " + os);
			throw new UnknownOSException(os);
		}

    return 0;
	}

	private static ScriptMeta runScript(String[] command, String os) throws IOException, InterruptedException {
		if (os.contains("windows")) {
			ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", command[0]);
			pb.directory(new File("../code/scripts"));
			Process p = pb.start();
			return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
		} else if (os.contains("linux")) {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", command[0]);
			pb.directory(new File("../code/scripts"));
			Process p = pb.start();
			return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
		} else { //mac
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(new File("../code/scripts"));
			Process p = pb.start();
			return new ScriptMeta(p.waitFor(), output(p.getInputStream()));
		}
	}

	private static String output(InputStream inputStream) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
		} finally {
			br.close();
		}
		return sb.toString().trim();
	}

}
