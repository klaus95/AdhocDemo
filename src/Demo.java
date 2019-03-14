import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import AdhocAPI.*;

public class Demo {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton hostButton;
    private JButton connectButton;
    private JLabel warning;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    static private JFrame frame;

    public Demo() {

        warning.setVisible(false);

        textField1.setText("Klaus");
        textField2.setText("1234");

        try {
            AdHocConfig network = FactoryAdHocConfig.init();
            String[] interfaces = network.getInterfaces();
            for (String inter : interfaces) {
                comboBox2.addItem(inter);
            }
            int[] channels = network.getSupportedChannels((String) comboBox2.getSelectedItem());
            for (int channel : channels) {
                comboBox1.addItem(channel);
            }
            comboBox1.setSelectedItem(11);
        } catch (Exception e) {
            e.printStackTrace();
        }

        hostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (areArgumentsValid()) {
                    new Server(textField1.getText(), textField2.getText(), (int) comboBox1.getSelectedItem(), (String) comboBox2.getSelectedItem(), frame);
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
                    new Client(textField1.getText(), textField2.getText(), (int) comboBox1.getSelectedItem(), (String) comboBox2.getSelectedItem(), frame);
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
        return !textField1.getText().isEmpty();
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
