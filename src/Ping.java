import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ping extends Thread {

    private JLabel text;
    private String name;

    Ping(JLabel text, String name) {
        this.text = text;
        this.name = name;
    }

    @Override
    public void run() {
        super.run();

        int numberOfIPs = 255;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfIPs);
        List<Future<PingResult>> list = new ArrayList<>();
        Callable<PingResult> callable;
        List<String> ips = new ArrayList<>();

        for(int i = 2; i < numberOfIPs; i++){
            callable = new PingTask("169.254.1." + i);
            Future<PingResult> future = executor.submit(callable);
            list.add(future);
        }

        for(Future<PingResult> result : list){
            try {
                if (result.get().getResultCode() == 0) {
                    ips.add(result.get().getIpAddress());
                }
            } catch (Exception ex) {
                System.out.println("IP hunt failed!");
            }
        }
        executor.shutdown();

        if (!ips.isEmpty()) {
            text.setText("Please Wait! Connecting to server...");
            for (String ip : ips) {
                Socket client;
                try {
                    client = new Socket(ip, 23462);

                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    out.println(name);
                    String inputLine = in.readLine();

                    if (inputLine.equals("Entry added successfully!")) {
                        text.setText("Attendance marked successfully!");
                        text.setIcon(new ImageIcon(new File("").getAbsolutePath() + "/src/tick.png"));
                        client.close();
                    } else {
                        text.setText("No devices found!");
                        text.setIcon(new ImageIcon(new File("").getAbsolutePath() + "/src/x.png"));
                        client.close();
                    }

                } catch (IOException e) {
                    System.out.println("No server in " + ip);
                }
            }

        } else {
            text.setText("No devices found!");
            text.setIcon(new ImageIcon(new File("").getAbsolutePath() + "/src/x.png"));
        }
    }
}
