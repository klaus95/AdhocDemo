import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Server {
    static JFrame oldFrame;

    private JPanel panel1;

    public Server(String name, String password, int channel,String interfaceName, JFrame oFrame) {
        oldFrame = oFrame;

        JFrame frame = new JFrame("Server");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        CreateNetwork cn = new CreateNetwork(name, password, channel, interfaceName);
        cn.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                oFrame.setVisible(true);

                DiscconectNetwork dn = new DiscconectNetwork();
                dn.start();
            }
        });
    }

}
