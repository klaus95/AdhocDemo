import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;

public class Server {
    private JPanel panel1;
    private JList list1;
    private JButton refreshButton;
    private JButton disconnectButton;
    private JLabel Information;
    private JLabel Performance;

    public Server(String name, String password, int channel,String interfaceName, JFrame oldFrame) {
        Information.setText("<html> Server Information: <br/><br/>   SSID:       " + name + "<br/>    Password:   " + password
                                        + "<br/>    Interface:  " + interfaceName + "<br/>    Channel:    " + channel + "</html>");

        Performance.setText("Creating adhoc network...");

        JFrame frame = new JFrame("Server");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new CreateNetwork(name, password, channel, interfaceName, Performance, frame).start();

        Semaphore lock = new Semaphore(1);
        new TCPServer(23462, list1, lock).start();

        disconnectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultListModel model = new DefaultListModel();
                list1.setModel(model);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                oldFrame.setVisible(true);

                new DiscconectNetwork().start();
            }
        });
    }

    private void createUIComponents() {
        DefaultListModel model = new DefaultListModel();
        list1 = new JList<>();
        list1.setModel(model);
    }
}
