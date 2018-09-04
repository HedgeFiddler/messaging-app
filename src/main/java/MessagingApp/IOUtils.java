package MessagingApp;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class IOUtils {

    Scanner scanner;

    public IOUtils() {
        this.scanner = new Scanner(System.in);
    }

    public IOUtils(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean fileExists(File file) {
        return (file.exists() && !file.isDirectory());
    }

    public void writeMessage(String message) {
        System.out.println(message);
    }

    public void writeUserToFile(String email, String name, int age, String password, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(email + " " + name + " " + Integer.toString(age) + " " + password);
        writer.close();
        System.out.println("User saved successfully");
    }

    public boolean isUserThere(String userEmail) {
        File user = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Users/" + userEmail + ".txt");

        return fileExists(user);
    }

    public void appendMessageToChatFile(String firstUser, String secondUser, String message, String sender) throws IOException {
        File a = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Chats/" + firstUser + "_" + secondUser + ".txt");
        LocalDateTime timestamp = LocalDateTime.now();

        if (fileExists(a)) {
            BufferedWriter out = new BufferedWriter(new FileWriter(a, true));
            out.write(timestamp + "  " + sender + ": " + message + "\n");
            out.close();
        } else {
            Writer writer = new BufferedWriter(new FileWriter(a));
            writer.write(timestamp + "  " + sender + ": " + message + "\n");
            writer.close();
        }

    }

    public void printHistoryOfCurrentChat(String firstUser, String secondUser) throws IOException {
        File a = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Chats/" + firstUser + "_" + secondUser + ".txt");
        if (fileExists(a)) {
            List<String> history = Files.readAllLines(a.toPath());
            for (String line : history) {
                writeMessage(line);
            }
        }
    }

    public String readNext() {
        return scanner.next();
    }

    public String[] readUserFile(File file) throws IOException {
        List<String> readLines = Files.readAllLines(file.toPath());
        String[] userDetails = readLines.get(0).split(" ");
        return userDetails;
    }
}
