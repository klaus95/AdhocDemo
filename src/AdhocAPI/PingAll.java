package AdhocAPI;

import java.util.concurrent.Semaphore;

public class PingAll extends Thread {
    //----------------------------------- DO NOT USE ------------------------------------
    private String[] command;
    private Semaphore s;

    public PingAll(String[] command, Semaphore s) {
        this.command = command;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            String output = AdHocConfig.output(p.getInputStream());
            if (output.contains("time=")) {
                try {
                    s.acquire();
                } catch (Exception e) {
                    System.out.println("Failed to catch the semaphore in PINGALL");
                }
                //do smth
                s.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PINGALL THREAD ERROR");
        }

    }
}