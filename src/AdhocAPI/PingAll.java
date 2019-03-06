package AdhocAPI;

public class PingAll extends Thread {
    //command variable for Windows = {"cmd", "/c", "ping", ip}
    //ip variable for Windows = "169.254.1." + i
    private String[] command;

    public PingAll(String[] command) {
        this.command = command;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            String output = AdHocConfig.output(p.getInputStream());
            if (output.contains("time=")) {
                //successful ping
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PINGALL THREAD ERROR");
        }

    }
}