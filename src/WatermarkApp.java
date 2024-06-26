import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.*;

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
            config.load(new FileInputStream("marca.prm"));
        } catch (IOException e) {
            System.err.println("Unable to load config file.");
            e.printStackTrace();
        }
    }

    private void setUpTrayIcon() {
        SystemTray tray = SystemTray.getSystemTray();
        ImageIcon icon = new ImageIcon("ico.png"); // Path to icon here
        PopupMenu popup = new PopupMenu();
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
        frame.setType(Window.Type.UTILITY);

        boolean includeIP = "1".equals(config.getProperty("address"));
        boolean showTime = "1".equals(config.getProperty("time"));
        boolean showDate = "1".equals(config.getProperty("date"));

        // Get all system info in one call
        SystemInfo.InfoBundle userInfo = SystemInfo.getAllInfo(includeIP, showTime, showDate);

        frame.add(new WatermarkPanel(userInfo.toString(), config));
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
        String userInfo;
        Font font;
        Color color;
        Properties config;

        public WatermarkPanel(String userInfo, Properties config) {
            this.userInfo = userInfo;
            this.config = config;
            setOpaque(false);
            setLayout(new BorderLayout());
            initConfig();
        }

        private void initConfig() {
            String fontName = config.getProperty("font.name", "Arial");
            int fontStyle = config.getProperty("font.style", "Bold").equalsIgnoreCase("Bold") ? Font.BOLD : Font.PLAIN;
            int fontSize = Integer.parseInt(config.getProperty("font.size", "40"));

            int red = Integer.parseInt(config.getProperty("color.red", "255"));
            int green = Integer.parseInt(config.getProperty("color.green", "255"));
            int blue = Integer.parseInt(config.getProperty("color.blue", "255"));
            int alpha = Integer.parseInt(config.getProperty("color.alpha", "100"));  // Default is 100

            int alphaScaled = (int) (alpha / 100.0 * 255);

            font = new Font(fontName, fontStyle, fontSize);
            color = new Color(red, green, blue, alphaScaled); // Apply transparency correctly
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(font);
            g2d.setColor(color);

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(userInfo);
            int textHeight = fm.getHeight();

            boolean isHorizontal = "1".equals(config.getProperty("horizontal", "0"));

            if (isHorizontal) {
                // Diminuir o espaço horizontal entre as repetições de texto
                for (int y = textHeight; y < getHeight(); y += textHeight + 30) {  // Espaço vertical ajustado
                    for (int x = 0; x < getWidth(); x += textWidth + 20) {  // Espaço horizontal ajustado
                        g2d.drawString(userInfo, x, y);
                    }
                }
            } else {
                AffineTransform transform = new AffineTransform();
                transform.rotate(-Math.PI / 4); // Diagonal
                g2d.setTransform(transform);

                int diagonalLength = (int) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
                int startX = -diagonalLength / 2;

                // Diminuir o espaço diagonal entre as repetições de texto
                for (int x = startX; x < diagonalLength; x += textWidth + 20) {  // Espaço horizontal ajustado
                    for (int y = -getHeight(); y < getHeight() * 2; y += textHeight + 30) {  // Espaço vertical ajustado
                        g2d.drawString(userInfo, x, y);
                    }
                }
            }
            g2d.dispose();
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(WatermarkApp::new);
    }
}
