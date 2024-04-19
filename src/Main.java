import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Comando do Windows
        processBuilder.command("cmd.exe", "/c", "hostname");
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        List<String> output = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            output.add(line);
        }
        int exitCode = process.waitFor();
        System.out.println("\nSaída do comando:");
        for(String s : output){
            System.out.println(s);
        }
        System.out.println("\nCódigo de saída: " + exitCode);
    }
}
