import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import AdhocAPI.*;

public class Demo {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton hostButton;
    private JButton connectButton;
    private JLabel warning;
    static JFrame frame;

    public Demo() {

        warning.setVisible(false);

        hostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Create the server side

                boolean host = true;

                String name = textField1.getText();
                if (name.isEmpty()) host = false;
                String pass = textField2.getText();
                if (pass.isEmpty()) host = false;
                int channel = -1;
                try {
                    channel = Integer.parseInt(textField3.getText());
                } catch (Exception ex) {
                    host = false;
                }
                String inter = textField4.getText();
                if (inter.isEmpty()) host = false;

                if (host) {
                    new Server(name, pass, channel, inter, frame);
                    warning.setVisible(false);
                    frame.setVisible(false);
                } else {
                    warning.setVisible(true);
                }
            }
        });

        connectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Create the client side
                //Send info from textField1-4
                //Create the new window and close the old one

                //Micheal work here!!
            }
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("Demo");
        frame.setContentPane(new Demo().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
