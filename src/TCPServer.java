import AdhocAPI.*;
import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer extends Thread {
    int port;

    public TCPServer(int port) {
        this.port = port;
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
                new TCPThread(ss.accept()).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
