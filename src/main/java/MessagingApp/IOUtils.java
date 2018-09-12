package MessagingApp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class IOUtils {

    private static final String USER_FOLDER_PATH = "C://Users//Sander//IdeaProjects//messaging-app//src//main//Users/";
    private static final String CHAT_FOLDER_PATH = "C://Users//Sander//IdeaProjects//messaging-app//src//main//Chats/";
    private static final String GROUP_CHATS_FOLDER = "C://Users//Sander//IdeaProjects//messaging-app//src//main//GroupChats/";
    private static final String GROUP_CHATS_TIMESTAMPS = "C://Users//Sander//IdeaProjects//messaging-app//src//main//GroupChatTimestamps/";

    private volatile List<String> lines;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");

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
        User savedUser = new User(email, name, age, password);
        savedUser.setLastUpdate(LocalDateTime.now());
        writer.write(savedUser.toString());
        writer.close();
        System.out.println("User created successfully \n");
    }

    public boolean userExists(String userEmail) {
        File user = new File(USER_FOLDER_PATH + userEmail + ".txt");

        return fileExists(user);
    }

    public String getChatFilename(String firstUser, String secondUser) {
        if (firstUser.charAt(0) <= secondUser.charAt(0)) {
            return firstUser + "_" + secondUser;
        } else {
            return secondUser + "_" + firstUser;
        }
    }

    public void appendMessageToChatFile(String firstUser, String secondUser, String message, String sender) throws IOException {
        File a = new File(CHAT_FOLDER_PATH + firstUser + "_" + secondUser + ".txt");
        LocalDateTime timestamp = LocalDateTime.now();

        if (fileExists(a)) {
            BufferedWriter out = new BufferedWriter(new FileWriter(a, true));
            out.write(timestamp + "<>" + getUserFromEmail(sender).getName() + ":<>" + message + "\n");
            out.close();
        } else {
            Writer writer = new BufferedWriter(new FileWriter(a));
            writer.write(timestamp + "<>" + getUserFromEmail(sender).getName() + ":<>" + message + "\n");
            writer.close();
        }

    }

//    public void printHistoryOfCurrentChat(String firstUser, String secondUser) throws IOException {
//        File a = new File(CHAT_FOLDER_PATH + generateChatFilename(firstUser, secondUser) + ".txt");
//        if (fileExists(a)) {
//            List<String> history = Files.readAllLines(a.toPath());
//            for (String line : history) {
//                writeMessage(line);
//            }
//        }
//    }

    public String readNext() {
        return scanner.nextLine();
    }

    public int readNextInt() {
        int result = scanner.nextInt();
        scanner.nextLine();
        return result;
    }

    public static String getUserFolderPath() {
        return USER_FOLDER_PATH;
    }

    public static String getChatFolderPath() {
        return CHAT_FOLDER_PATH;
    }

    public static String getGroupChatsFolder() {
        return GROUP_CHATS_FOLDER;
    }

    public synchronized String[] readUserFile(User user) throws IOException {
        File file = new File(USER_FOLDER_PATH + user.getEmail() + ".txt");
        List<String> readLines = Files.readAllLines(file.toPath());
        return readLines.get(0).split(" ");
    }

    public synchronized void updateUserLastUpdate(User user) throws IOException {
        File file = new File(USER_FOLDER_PATH + user.getEmail() + ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        User tempUser = user;
        tempUser.setLastUpdate(LocalDateTime.now());
        writer.write(tempUser.toString());
        writer.close();
    }

    private synchronized LocalDateTime getUserLastUpdate(User user) throws IOException {

        return stringToLdt(readUserFile(user)[4]);

    }

    public LocalDateTime stringToLdt(String localDateTime) {

        return LocalDateTime.parse(localDateTime);

    }

    public User getUserFromEmail(String userEmail) throws IOException {

        File file = new File(USER_FOLDER_PATH + userEmail + ".txt");
        List<String> readLines = Files.readAllLines(file.toPath());
        String[] lines = readLines.get(0).split(" ");
        User result = new User(lines[0], lines[1], Integer.parseInt(lines[2]), lines[3]);
        result.setLastUpdate(stringToLdt(lines[4]));
        return result;


    }

    public void checkChatFileForNewMessages(Path path, User user) throws IOException {
        lines = null;
        if (path.toFile().getName().contains(user.getEmail())) {

            try {
                lines = Files.readAllLines(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String line : lines) {
                String[] lineSplit = line.split("<>");
                if (stringToLdt(lineSplit[0]).isAfter(getUserLastUpdate(user)) && !((user.getName() + ":").equals(lineSplit[1]))) {
                    String formattedTimestamp = stringToLdt(lineSplit[0]).format(formatter);
                    writeMessage(formattedTimestamp + " " + lineSplit[1] + " " + lineSplit[2] + "\n");
                }


            }
            updateUserLastUpdate(user);
        }
    }

    public synchronized void writeToGroupChat(String chatName, User user, String message) throws IOException {
        File f = new File(GROUP_CHATS_FOLDER + chatName + ".txt");
        LocalDateTime timestamp = LocalDateTime.now();
        if (fileExists(f)) {
            BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
            out.write(timestamp + "<>" + "in " + chatName + "<>" + user.getName() + ":<>" + message + "\n");
            out.close();
        } else {
            Writer writer = new BufferedWriter(new FileWriter(f));
            writer.write(timestamp + "<>" + "in " + chatName + "<>" + user.getName() + ":<>" + message + "\n");
            writer.close();
        }

    }

    public void checkGroupChatFileForNewMessages(User user, Path path) throws IOException {

        lines = null;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            writeMessage(e.getMessage());
        }
        for (String line : lines) {
            String[] lineSplit = line.split("<>");
            if (stringToLdt(lineSplit[0]).isAfter(getUserGroupChatsLastUpdate(user))) {
                String formattedTimestamp = stringToLdt(lineSplit[0]).format(formatter);
                writeMessage(formattedTimestamp + " " + lineSplit[1] + " " + lineSplit[2] + " " + lineSplit[3] + "\n");
            }

        }
        updateGroupChatTimestamp(user);
    }

    public LocalDateTime getUserGroupChatsLastUpdate(User user) throws IOException {
        File f = new File(GROUP_CHATS_TIMESTAMPS + user.getEmail() + ".txt");
        return stringToLdt(Files.readAllLines(f.toPath()).get(0).split("<>")[0]);
    }

//    public LocalDateTime getTimeStampForChat(String chatName, User user) throws IOException {
//        File f = new File(GROUP_CHATS_TIMESTAMPS + user.getEmail() + ".txt");
//        List<String> lines = Files.readAllLines(f.toPath());
//        for (String line : lines) {
//            if (line.split(" ")[0].equals(chatName)) {
//                return stringToLdt(line.split(" ")[1]);
//            }
//        }
//        return LocalDateTime.now();
//    }

    public synchronized void updateGroupChatTimestamp(User user) throws IOException {
        File f = new File(GROUP_CHATS_TIMESTAMPS + user.getEmail() + ".txt");

        Writer writer = new BufferedWriter(new FileWriter(f));
        writer.write(LocalDateTime.now().toString());
        writer.close();

//        if (fileExists(f)) {
//            List<String> lines = Files.readAllLines(f.toPath());
//            for (String line : lines) {
//                Writer writer = new BufferedWriter(new FileWriter(f));
//                if (line.split(" ")[0].equals(chatName)) {
//                    writer.write(line.split(" ")[0] + LocalDateTime.now().toString());
//                } else {
//                    writer.write(line);
//                }
//            }
//
//        }
    }

//    public synchronized void createGroupChatTimestamp(String chatName, User user) throws IOException {
//        File f = new File(GROUP_CHATS_TIMESTAMPS + user.getEmail() + "txt");
//        if (fileExists(f) && !(isChatTimeStampPresent(chatName, user))) {
//            Writer writer = new BufferedWriter(new FileWriter(f, true));
//            writer.append(chatName + " " + LocalDateTime.now().toString());
//            writer.close();
//
//        }
//
//    }

//    public boolean isChatTimeStampPresent(String chatName, User user) throws IOException {
//        File f = new File(GROUP_CHATS_TIMESTAMPS + user.getEmail() + ".txt");
//        if (fileExists(f)) {
//            List<String> lines = Files.readAllLines(f.toPath());
//            for (String line : lines) {
//                if (line.split(" ")[0].equals(chatName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


}
