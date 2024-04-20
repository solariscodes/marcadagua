import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigPanel extends JFrame {
    private Properties config;
    private JComboBox<String> fontNameBox, fontSizeBox, fontStyleBox;
    private JComboBox<Color> colorBox;
    private JCheckBox showIPCheckBox;
    private JCheckBox showTimeCheckBox;
    private JButton saveButton, closeButton;
    private JPanel fontPanel, controlPanel;
    private Color[] commonColors = {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW, new Color(0x800080), new Color(0x808000), new Color(0x008080)
    };

    public ConfigPanel() {
        config = new Properties();
        initializeUI();
        loadConfig();
    }

    private void initializeUI() {
        setTitle("Configurações da Marca d'Água");
        setLayout(new BorderLayout());
        setSize(450, 250); // Setting height to 250 pixels
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Definindo o ícone da janela
        ImageIcon icon = new ImageIcon("ico.png");
        setIconImage(icon.getImage());

        fontPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // Adjusted grid layout
        fontPanel.setBorder(BorderFactory.createTitledBorder("Font Settings"));
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontNameBox = new JComboBox<>(fontNames);
        fontSizeBox = new JComboBox<>(new String[]{"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "36", "40", "44", "48"});
        fontSizeBox.setEditable(true);
        fontStyleBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic"});
        fontPanel.add(new JLabel("Font Name:"));
        fontPanel.add(fontNameBox);
        fontPanel.add(new JLabel("Font Size:"));
        fontPanel.add(fontSizeBox);
        fontPanel.add(new JLabel("Font Style:"));
        fontPanel.add(fontStyleBox);

        colorBox = new JComboBox<>(commonColors);
        colorBox.setRenderer(new ColorRenderer());
        colorBox.setEditable(true);
        colorBox.setEditor(new ColorRenderer());
        fontPanel.add(new JLabel("Color:"));
        fontPanel.add(colorBox);

        showIPCheckBox = new JCheckBox("Mostrar IP");
        fontPanel.add(showIPCheckBox);

        showTimeCheckBox = new JCheckBox("Mostrar Hora");
        fontPanel.add(showTimeCheckBox);

        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center aligning control panel
        saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveConfig);
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose());
        controlPanel.add(saveButton);
        controlPanel.add(closeButton);

        add(fontPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER); // Adding control panel to center

        setVisible(true);
    }

    private void loadConfig() {
        try (FileInputStream in = new FileInputStream("marca.cfg")) {
            config.load(in);
            updateUIFromConfig();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUIFromConfig() {
        fontNameBox.setSelectedItem(config.getProperty("font.name", "Arial"));
        fontSizeBox.setSelectedItem(config.getProperty("font.size", "12"));
        fontStyleBox.setSelectedItem(config.getProperty("font.style", "Bold"));
        Color selectedColor = new Color(
                Integer.parseInt(config.getProperty("color.red", "0")),
                Integer.parseInt(config.getProperty("color.green", "0")),
                Integer.parseInt(config.getProperty("color.blue", "0"))
        );
        colorBox.setSelectedItem(selectedColor);

        String showIP = config.getProperty("address", "1");
        showIPCheckBox.setSelected(showIP.equals("1"));

        String showTime = config.getProperty("data", "0");
        showTimeCheckBox.setSelected(showTime.equals("1"));
    }

    private void saveConfig(ActionEvent event) {
        config.setProperty("font.name", (String) fontNameBox.getSelectedItem());
        config.setProperty("font.size", fontSizeBox.getEditor().getItem().toString());
        config.setProperty("font.style", (String) fontStyleBox.getSelectedItem());
        Color selectedColor = (Color) colorBox.getSelectedItem();
        config.setProperty("color.red", String.valueOf(selectedColor.getRed()));
        config.setProperty("color.green", String.valueOf(selectedColor.getGreen()));
        config.setProperty("color.blue", String.valueOf(selectedColor.getBlue()));

        String showIP = showIPCheckBox.isSelected() ? "1" : "0";
        config.setProperty("address", showIP);

        String showTime = showTimeCheckBox.isSelected() ? "1" : "0";
        config.setProperty("data", showTime);

        try (FileOutputStream out = new FileOutputStream("marca.cfg")) {
            config.store(out, "Watermark Configuration");
            JOptionPane.showMessageDialog(this, "Configurações salvas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configurações: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ColorRenderer extends JPanel implements ListCellRenderer<Color>, ComboBoxEditor {
        private Color color;
        private JLabel label;

        public ColorRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel("       ");  // Espaço reservado para garantir visibilidade
            label.setOpaque(true);
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
            color = value;
            label.setBackground(color);
            label.setText(" ");
            if (isSelected) {
                setBorder(BorderFactory.createLineBorder(Color.white, 2));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.black));
            }
            return this;
        }

        @Override
        public void setItem(Object anItem) {
            if (anItem instanceof Color) {
                color = (Color) anItem;
                label.setBackground(color);
            }
        }

        @Override
        public Object getItem() {
            return color;
        }

        @Override
        public Component getEditorComponent() {
            return this;
        }

        @Override
        public void addActionListener(java.awt.event.ActionListener l) {
            // Não é necessário para esta demonstração
        }

        @Override
        public void removeActionListener(java.awt.event.ActionListener l) {
            // Não é necessário para esta demonstração
        }

        @Override
        public void selectAll() {
            // Não é necessário para esta demonstração
        }
    }

    public static void main(String[] args) {
        new ConfigPanel();
    }
}
