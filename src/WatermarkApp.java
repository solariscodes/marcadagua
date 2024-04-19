import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WatermarkApp {

    private TrayIcon trayIcon;
    private Properties config;

    public WatermarkApp() {
        loadConfig();
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported!");
            return;
        }
        setUpTrayIcon();
        createAndShowGUI();
    }

    private void loadConfig() {
        config = new Properties();
        try {
            config.load(new FileInputStream("marca.cfg"));
        } catch (IOException e) {
            System.err.println("Unable to load config file.");
            e.printStackTrace();
        }
    }

    private void setUpTrayIcon() {
        SystemTray tray = SystemTray.getSystemTray();
        ImageIcon icon = new ImageIcon("ico.png"); // Coloque o caminho para o ícone aqui
        PopupMenu popup = new PopupMenu();
/*        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });*/
        //popup.add(exitItem);
        trayIcon = new TrayIcon(icon.getImage(), "Watermark App", popup);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        frame.setSize(screenSize.width, screenSize.height);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 1));
        frame.setAlwaysOnTop(true);
        frame.setType(Window.Type.UTILITY); // Impede que apareça na barra de tarefas

        String username = System.getProperty("user.name");
        frame.add(new WatermarkPanel(username, config));

        frame.setVisible(true);
        makeWindowTransparent(frame);
    }

    private void makeWindowTransparent(JFrame frame) {
        WinDef.HWND hwnd = new WinDef.HWND(Native.getWindowPointer(frame));
        int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
        wl |= WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
    }

    private static class WatermarkPanel extends JPanel {
        String username;
        Font font;
        Color color;

        public WatermarkPanel(String username, Properties config) {
            this.username = username;
            setOpaque(false);
            setLayout(new BorderLayout());
            initConfig(config);
        }

        private void initConfig(Properties config) {
            String fontName = config.getProperty("font.name", "Arial");
            int fontStyle = config.getProperty("font.style", "Bold").equalsIgnoreCase("Bold") ? Font.BOLD : Font.PLAIN;
            int fontSize = Integer.parseInt(config.getProperty("font.size", "40"));
            int red = Integer.parseInt(config.getProperty("color.red", "192"));
            int green = Integer.parseInt(config.getProperty("color.green", "192"));
            int blue = Integer.parseInt(config.getProperty("color.blue", "192"));
            int alpha = Integer.parseInt(config.getProperty("color.alpha", "100"));

            font = new Font(fontName, fontStyle, fontSize);
            color = new Color(red, green, blue, alpha);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(font);
            g2d.setColor(color);

            FontMetrics fm = g2d.getFontMetrics();
            int x = 50;
            while (x < getWidth()) {
                int y = 50;
                while (y < getHeight()) {
                    g2d.drawString(username, x, y);
                    y += fm.getHeight() + 50;
                }
                x += fm.stringWidth(username) + 50;
            }
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WatermarkApp();
            }
        });
    }
}