import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPThread extends Thread {
    private Socket s;

    public TCPThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
