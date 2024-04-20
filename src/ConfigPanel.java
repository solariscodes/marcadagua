import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigPanel extends JFrame {
    private Properties config;
    private JComboBox<String> fontNameBox, fontSizeBox;
    private JComboBox<String> fontStyleBox;
    private JSlider redSlider, greenSlider, blueSlider, alphaSlider;
    private JButton saveButton;
    private JPanel fontPanel, colorPanel, controlPanel;

    public ConfigPanel() {
        config = new Properties();
        initializeUI();
        loadConfig();
    }

    private void initializeUI() {
        setTitle("Configurações da Marca d'Água");
        setLayout(new BorderLayout());
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Painel para configurações de fonte
        fontPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fontPanel.setBorder(BorderFactory.createTitledBorder("Font Settings"));

        // Lista de fontes disponíveis no sistema
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        fontNameBox = new JComboBox<>(fontNames);

        // ComboBox editável para o tamanho da fonte
        fontSizeBox = new JComboBox<>(new String[]{"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "36", "40", "44", "48"});
        fontSizeBox.setEditable(true);

        fontStyleBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic"});

        fontPanel.add(new JLabel("Font Name:"));
        fontPanel.add(fontNameBox);
        fontPanel.add(new JLabel("Font Size:"));
        fontPanel.add(fontSizeBox);
        fontPanel.add(new JLabel("Font Style:"));
        fontPanel.add(fontStyleBox);

        // Painel para configurações de cor
        colorPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        colorPanel.setBorder(BorderFactory.createTitledBorder("Color Settings"));

        redSlider = createColorSlider(255);
        greenSlider = createColorSlider(255);
        blueSlider = createColorSlider(255);
        alphaSlider = createColorSlider(100);

        colorPanel.add(new JLabel("Red:"));
        colorPanel.add(redSlider);
        colorPanel.add(new JLabel("Green:"));
        colorPanel.add(greenSlider);
        colorPanel.add(new JLabel("Blue:"));
        colorPanel.add(blueSlider);
        colorPanel.add(new JLabel("Alpha:"));
        colorPanel.add(alphaSlider);

        // Painel de controle com botão de salvar
        controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveConfig);
        controlPanel.add(saveButton);

        add(fontPanel, BorderLayout.NORTH);
        add(colorPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JSlider createColorSlider(int max) {
        JSlider slider = new JSlider(0, max);
        slider.setMajorTickSpacing(max / 4);
        slider.setMinorTickSpacing(max / 8);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void loadConfig() {
        try (FileInputStream in = new FileInputStream("marca.cfg")) {
            config.load(in);
            fontNameBox.setSelectedItem(config.getProperty("font.name", "Arial"));
            fontSizeBox.setSelectedItem(config.getProperty("font.size", "12"));
            fontStyleBox.setSelectedItem(config.getProperty("font.style", "Bold"));
            redSlider.setValue(Integer.parseInt(config.getProperty("color.red", "255")));
            greenSlider.setValue(Integer.parseInt(config.getProperty("color.green", "255")));
            blueSlider.setValue(Integer.parseInt(config.getProperty("color.blue", "255")));
            alphaSlider.setValue(Integer.parseInt(config.getProperty("color.alpha", "100")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveConfig(ActionEvent event) {
        config.setProperty("font.name", (String) fontNameBox.getSelectedItem());
        config.setProperty("font.size", fontSizeBox.getEditor().getItem().toString());
        config.setProperty("font.style", (String) fontStyleBox.getSelectedItem());
        config.setProperty("color.red", String.valueOf(redSlider.getValue()));
        config.setProperty("color.green", String.valueOf(greenSlider.getValue()));
        config.setProperty("color.blue", String.valueOf(blueSlider.getValue()));
        config.setProperty("color.alpha", String.valueOf(alphaSlider.getValue()));

        try (FileOutputStream out = new FileOutputStream("marca.cfg")) {
            config.store(out, "Watermark Configuration");
            JOptionPane.showMessageDialog(this, "Configurações salvas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configurações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ConfigPanel();
    }
}
