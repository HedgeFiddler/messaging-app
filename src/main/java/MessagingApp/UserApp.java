package MessagingApp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserApp {
    private List<User> userList;

    public UserApp() {
        this.userList = new ArrayList<>();
    }

    public User saveUser(String email, String name, int age, String password) {
        Path destination = Paths.get("C:\Users\Sander\IdeaProjects\messaging-app");
        Files.createFile(destination,email + ".txt")

        User user = new User(email, name, age, password);
        return user;
    }
}
