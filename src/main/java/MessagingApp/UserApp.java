package MessagingApp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class UserApp {
    IOUtils ioUtils;

    public UserApp(IOUtils ioUtils) {
        this.ioUtils = ioUtils;
    }

    public User saveUser(String email, String name, int age, String password) throws IOException, EmailAlreadyRegisteredException {
        File f = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Users/" + email + ".txt");
        if(ioUtils.fileExists(f)) {
            throw new EmailAlreadyRegisteredException("This email is already in use, try again\n");
        }
        ioUtils.writeUserToFile(email, name, age, password, f);
        return new User(email, name, age, password);

    }

    public boolean validateUser(String email, String password) throws WrongEmailOrPasswordException, IOException {
        File f = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Users/" + email + ".txt");
        if(ioUtils.fileExists(f)){
           List<String> readLines = Files.readAllLines(f.toPath());
           String[] userDetails = readLines.get(0).split(" ");
            if (userDetails[0].equals(email) && userDetails[3].equals(password)){

                ioUtils.writeMessage("You're in!");
                return true;
            }

        }
        throw new WrongEmailOrPasswordException("Your email or password is incorrect!");
    }

    public void messageUser(String targetUser, String sendingUser,  String message) throws NoSuchUserException {
        File f = new File("C://Users//Sander//IdeaProjects//messaging-app//src//main//Users/" + targetUser + ".txt");
          if(ioUtils.fileExists(f)){
            if(targetUser.toLowerCase().charAt(0) <= sendingUser.toLowerCase().charAt(0)){
                try {
                    ioUtils.appendMessageToChatFile(targetUser, sendingUser, message, sendingUser);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            } else {
                try {
                    ioUtils.appendMessageToChatFile(sendingUser, targetUser, message, sendingUser);
                    return;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        }

        throw new NoSuchUserException("No user with such name logged in");

    }

}