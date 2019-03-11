import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Client {

    private JPanel pane1;
    private JTextField textField1;
    private JButton markAttendenceButton;
    private JLabel waitLabel;
    private JLabel warning;

    public Client (String name, String password, int channel, String interfaceName, JFrame oldFrame) {

        waitLabel.setText("Connecting to " + name + "...");
        warning.setVisible(false);

        JFrame frame = new JFrame("Client");
        frame.setContentPane(this.pane1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new ConnectNetwork(name, password, channel, interfaceName, waitLabel, frame).start();

        markAttendenceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String name = textField1.getText();

                if (name.isEmpty()) {
                    warning.setVisible(true);
                    frame.pack();
                } else {
                    warning.setVisible(false);

                    //Display loading icon
                    //Ping all ips
                    //Find the server
                    //Send username to server
                    //Close connection

                }

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
}
