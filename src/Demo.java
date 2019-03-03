import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Demo {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton hostButton;
    private JButton connectButton;
    private JLabel warning;

    static private JFrame frame;

    public Demo() {

        warning.setVisible(false);
        textField1.setText("Klaus");
        textField2.setText("1234");
        textField3.setText("11");
        textField4.setText("en0");

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
                int channel = 11;
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
                    frame.pack();
                } else {
                    warning.setVisible(true);
                    frame.pack();
                }
            }
        });

        connectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Create the client side
                //Create a new GUI form like the Server class
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
