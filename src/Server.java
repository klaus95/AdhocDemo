import AdhocAPI.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Server {
    static String WIFIname;
    static String WIFIpassword;
    static int WIFIchannel;
    static String WIFIinterface;
    static JFrame oldFrame;

    private JPanel panel1;

    public Server(String name, String password, int channel,String interfaceName, JFrame oFrame) {
        oldFrame = oFrame;
        WIFIname = name;
        WIFIpassword = password;
        WIFIchannel = channel;
        WIFIinterface = interfaceName;

        JFrame frame = new JFrame("Server");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                oFrame.setVisible(true);
            }
        });
    }

}
