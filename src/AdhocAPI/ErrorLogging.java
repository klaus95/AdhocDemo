package AdhocAPI;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ErrorLogging {

    public final static Logger LOGS = Logger.getLogger("InstallationValidatorLogger");
    private static ConsoleHandler console;
    private static FileHandler logFile;

}
