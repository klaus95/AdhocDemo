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

                if (areArgumentsValid()) {
                    new Server(textField1.getText(), textField2.getText(), Integer.parseInt(textField3.getText()), textField4.getText(), frame);
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

                if (areArgumentsValid()) {
                    new Client(textField1.getText(), textField2.getText(), Integer.parseInt(textField3.getText()), textField4.getText(), frame);
                    warning.setVisible(false);
                    frame.setVisible(false);
                    frame.pack();
                } else {
                    warning.setVisible(true);
                    frame.pack();
                }
            }
        });
    }

    private boolean areArgumentsValid() {
        if (textField1.getText().isEmpty()) return false;
        try {
            Integer.parseInt(textField3.getText());
        } catch (Exception ex) {
            return false;
        }
        return !textField4.getText().isEmpty();
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
