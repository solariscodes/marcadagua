import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWriterApp extends JFrame {
    private JTextField fontNameField;
    private JTextField fontStyleField;
    private JTextField fontSizeField;
    private JTextField colorRedField;
    private JTextField colorGreenField;
    private JTextField colorBlueField;
    private JTextField colorAlphaField;
    private JButton saveButton;

    public ConfigWriterApp() {
        createUI();
    }

    private void createUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel("Font Name:"));
        fontNameField = new JTextField("Arial");
        add(fontNameField);

        add(new JLabel("Font Style:"));
        fontStyleField = new JTextField("Bold");
        add(fontStyleField);

        add(new JLabel("Font Size:"));
        fontSizeField = new JTextField("40");
        add(fontSizeField);

        add(new JLabel("Color Red:"));
        colorRedField = new JTextField("100");
        add(colorRedField);

        add(new JLabel("Color Green:"));
        colorGreenField = new JTextField("100");
        add(colorGreenField);

        add(new JLabel("Color Blue:"));
        colorBlueField = new JTextField("100");
        add(colorBlueField);

        add(new JLabel("Color Alpha:"));
        colorAlphaField = new JTextField("30");
        add(colorAlphaField);

        saveButton = new JButton("Save Configurations");
        saveButton.addActionListener(this::saveConfig);
        add(saveButton);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveConfig(ActionEvent event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("marca.cfg"))) {
            writer.write("# Configurações para a fonte da marca d'água\n");
            writer.write("font.name=" + fontNameField.getText() + "\n");
            writer.write("font.style=" + fontStyleField.getText() + "\n");
            writer.write("font.size=" + fontSizeField.getText() + "\n");
            writer.write("\n# Configurações para a cor da marca d'água\n");
            writer.write("color.red=" + colorRedField.getText() + "\n");
            writer.write("color.green=" + colorGreenField.getText() + "\n");
            writer.write("color.blue=" + colorBlueField.getText() + "\n");
            writer.write("color.alpha=" + colorAlphaField.getText() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save configurations: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConfigWriterApp::new);
    }
}
