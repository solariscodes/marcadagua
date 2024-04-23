import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemInfo {

    public static class InfoBundle {
        public String ipAddress = "";
        public String dateTime = "";
        public String username = System.getProperty("user.name");
        public String fullName = "";

        @Override
        public String toString() {
            return (fullName.isEmpty() ? username : fullName) + (ipAddress.isEmpty() ? "" : " " + ipAddress) + (dateTime.isEmpty() ? "" : " " + dateTime);
        }
    }

    public static InfoBundle getAllInfo(boolean includeIP, boolean showTime, boolean showDate) {
        InfoBundle info = new InfoBundle();

        // Retrieve the full name of the user from the domain
        info.fullName = getFullName(info.username);

        // Retrieve the IP address if included
        if (includeIP) {
            try {
                InetAddress ip = InetAddress.getLocalHost();
                info.ipAddress = ip.getHostAddress();
            } catch (UnknownHostException e) {
                System.err.println("Error getting IP address.");
                info.ipAddress = "IP unknown";
            }
        }

        // Retrieve the current date and time if needed
        if (showTime || showDate) {
            SimpleDateFormat sdf = new SimpleDateFormat((showDate ? "dd/MM/yyyy" : "") + (showTime ? (showDate ? " HH:mm" : "HH:mm") : ""));
            info.dateTime = sdf.format(new Date());
        }

        return info;
    }

    private static String getFullName(String username) {
        try {
            // Execute the shell command to get the user information from the domain
            Process process = Runtime.getRuntime().exec("net user /domain " + username);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("Full Name")) {
                    // Extract and return the full name
                    return line.substring(line.indexOf("Full Name") + "Full Name".length()).trim();
                }
            }
            process.waitFor(); // Wait for the process to complete
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to execute command or read output.");
            e.printStackTrace();
        }
        return username; // Return the username if the full name cannot be retrieved
    }
}
