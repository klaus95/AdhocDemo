package AdhocAPI;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class PingAll extends Thread {
    private int i;

    @Override
    public void run() {
        try {
            runScript(i);
            System.out.printf("Thread %d is going \n", i);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PINGALL THREAD ERROR");
        }

    }

    public PingAll(int i) {
        this.i = i;
    }

    private static void runScript(int i) throws IOException, InterruptedException {
        String ip = "169.254.1." + i;
        String[] command = {"cmd", "/c", "ping", ip};
        ProcessBuilder pb = new ProcessBuilder(command);
        Process p = pb.start();
       // p.waitFor();
    }
}