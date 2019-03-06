import AdhocAPI.*;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Semaphore;

public class TCPServer extends Thread {
    int port;
    JList list;
    Semaphore lock;

    public TCPServer(int port, JList list, Semaphore lock) {
        this.port = port;
        this.list = list;
        this.lock = lock;
    }

    @Override
    public void run() {
        AdHocConfig network = null;
        try {
            network = FactoryAdHocConfig.init();
        } catch (UnknownOSException e) {
            e.printStackTrace();
        }
        if (network == null) { System.out.println("AdHocConfig object failed!"); return; }

        ServerSocket ss = null;
        try {
            ss = network.serverSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ss == null) { System.out.println("ServerSocket failed!"); return; }

        try {
            while (true) {
                new TCPThread(ss.accept(), list, lock).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
