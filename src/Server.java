import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class Server {
    private JPanel panel1;
    private JList list1;
    private JButton refreshButton;
    private JButton disconnectButton;
    private JLabel Information;
    private JLabel Performance;

    public Server(String name, String password, int channel,String interfaceName, JFrame oldFrame) {
        Information.setText("<html> Server Information: <br/>    Network SSID: " + name + "<br/>    Network Password: " + password
                                        + "<br/>    Network Interface: " + interfaceName + "<br/>    Network Channel: " + channel + "</html>");

        Performance.setText("Creating " + name + " WIFI...");

        JFrame frame = new JFrame("Server");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        CreateNetwork cn = new CreateNetwork(name, password, channel, interfaceName, Performance, frame);
        cn.start();

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
                Random rnd = new Random();
                for (int i = 0; i < 2 + rnd.nextInt(20); i++) {
                    model.addElement("Element " + i);
                }
                list1.setModel(model);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                oldFrame.setVisible(true);

                DiscconectNetwork dn = new DiscconectNetwork();
                //dn.start();
            }
        });
    }

    private void createUIComponents() {
        DefaultListModel model = new DefaultListModel();
        list1 = new JList<>();
        for (int i = 0; i < 4; i++) {
            model.addElement("Element " + i);
        }
        list1.setModel(model);
    }
}
