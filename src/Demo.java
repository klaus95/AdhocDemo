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

    public Demo() {
        hostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Create the server side
                //Send info from textField1-4
                //Create the new window and close the old one
            }
        });

        connectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //Create the client side
                //Send info from textField1-4
                //Create the new window and close the old one
                //Change done
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo");
        frame.setContentPane(new Demo().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
