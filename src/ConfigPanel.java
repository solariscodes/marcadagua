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
    private JCheckBox showIPCheckBox, showTimeCheckBox, showDateCheckBox, agentNameCheckBox, domainCheckBox, companyCheckBox;
    private JButton saveButton, closeButton;
    private JPanel fontPanel, controlPanel;

    public ConfigPanel() {
        config = new Properties();
        initializeUI();
        loadConfig();
        pack();
        setVisible(true);
    }

    private void initializeUI() {
        setTitle("Configurações da Marca d'Água");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("ico.png").getImage());

        fontPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        fontPanel.setBorder(BorderFactory.createTitledBorder("Configurações de Fonte"));

        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontNameBox = new JComboBox<>(fontNames);
        fontSizeBox = new JComboBox<>(new String[]{"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "36", "40", "44", "48"});
        fontSizeBox.setEditable(true);
        fontStyleBox = new JComboBox<>(new String[]{"Plain", "Bold", "Italic"});

        fontPanel.add(new JLabel("Nome da Fonte:"));
        fontPanel.add(fontNameBox);
        fontPanel.add(new JLabel("Tamanho da Fonte:"));
        fontPanel.add(fontSizeBox);
        fontPanel.add(new JLabel("Estilo da Fonte:"));
        fontPanel.add(fontStyleBox);

        colorBox = new JComboBox<>(new Color[]{Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW, new Color(0x800080), new Color(0x808000), new Color(0x008080)});
        colorBox.setRenderer(new ColorRenderer());
        fontPanel.add(new JLabel("Cor:"));
        fontPanel.add(colorBox);

        showIPCheckBox = new JCheckBox("Mostrar IP");
        showTimeCheckBox = new JCheckBox("Mostrar Hora");
        showDateCheckBox = new JCheckBox("Mostrar Data");
        agentNameCheckBox = new JCheckBox("Nome do Agente");
        domainCheckBox = new JCheckBox("Mostrar Domínio");
        companyCheckBox = new JCheckBox("Empresa (Interno/Externo)");

        fontPanel.add(showIPCheckBox);
        fontPanel.add(showTimeCheckBox);
        fontPanel.add(showDateCheckBox);
        fontPanel.add(agentNameCheckBox);
        fontPanel.add(domainCheckBox);
        fontPanel.add(companyCheckBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Aplicar");
        saveButton.addActionListener(this::saveConfig);
        closeButton = new JButton("Sair");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(fontPanel, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(controlPanel);
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
        showIPCheckBox.setSelected(config.getProperty("address", "1").equals("1"));
        showTimeCheckBox.setSelected(config.getProperty("time", "0").equals("1"));
        showDateCheckBox.setSelected(config.getProperty("date", "0").equals("1"));
        agentNameCheckBox.setSelected(config.getProperty("agent.name", "0").equals("1"));
        domainCheckBox.setSelected(config.getProperty("domain", "0").equals("1"));
        companyCheckBox.setSelected(config.getProperty("company", "0").equals("1"));
    }

    private void saveConfig(ActionEvent event) {
        config.setProperty("font.name", (String) fontNameBox.getSelectedItem());
        config.setProperty("font.size", fontSizeBox.getEditor().getItem().toString());
        config.setProperty("font.style", (String) fontStyleBox.getSelectedItem());
        Color selectedColor = (Color) colorBox.getSelectedItem();
        config.setProperty("color.red", String.valueOf(selectedColor.getRed()));
        config.setProperty("color.green", String.valueOf(selectedColor.getGreen()));
        config.setProperty("color.blue", String.valueOf(selectedColor.getBlue()));
        config.setProperty("address", showIPCheckBox.isSelected() ? "1" : "0");
        config.setProperty("time", showTimeCheckBox.isSelected() ? "1" : "0");
        config.setProperty("date", showDateCheckBox.isSelected() ? "1" : "0");
        config.setProperty("agent.name", agentNameCheckBox.isSelected() ? "1" : "0");
        config.setProperty("domain", domainCheckBox.isSelected() ? "1" : "0");
        config.setProperty("company", companyCheckBox.isSelected() ? "1" : "0");

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

    class ColorRenderer extends JPanel implements ListCellRenderer<Color>, ComboBoxEditor {
        private Color color;
        private JLabel label;

        public ColorRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel("       ");
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
            // Not necessary for this demonstration
        }

        @Override
        public void removeActionListener(java.awt.event.ActionListener l) {
            // Not necessary for this demonstration
        }

        @Override
        public void selectAll() {
            // Not necessary for this demonstration
        }
    }
}
