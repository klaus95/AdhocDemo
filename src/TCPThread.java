import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class TCPThread extends Thread {
    private Socket s;
    private JList list;
    private Semaphore lock;

    public TCPThread(Socket s, JList list, Semaphore lock) {
        this.s = s;
        this.list = list;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String inputLine = in.readLine();
            DefaultListModel model = new DefaultListModel();

            lock.acquire();

            ListModel lm = list.getModel();
            for (int i = 0; i < lm.getSize(); i++) {
                model.addElement(lm.getElementAt(i));
            }
            model.addElement(inputLine);
            list.setModel(model);

            lock.release();

            out.println("Entry added successfully!");

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
