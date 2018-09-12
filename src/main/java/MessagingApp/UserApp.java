package MessagingApp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UserApp {
    IOUtils ioUtils;

    public UserApp(IOUtils ioUtils) {
        this.ioUtils = ioUtils;
    }

    public void saveUser() throws IOException, EmailAlreadyRegisteredException {
        ioUtils.writeMessage("Please enter an email for your account");
        String email = ioUtils.scanner.next();
        File f = new File(IOUtils.getUserFolderPath() + email + ".txt");
        if (ioUtils.fileExists(f)) {
            throw new EmailAlreadyRegisteredException("This email is already in use, try again\n");
        }
        ioUtils.writeMessage("Please enter a name without spaces");
        String name = ioUtils.scanner.next();
        ioUtils.writeMessage("Please enter your age");
        int age = ioUtils.scanner.nextInt();
        ioUtils.writeMessage("Please enter a password for your account");
        String password = ioUtils.scanner.next();
        ioUtils.writeUserToFile(email, name, age, password, f);
    }

    public boolean validateUser(String email, String password) throws WrongEmailOrPasswordException, IOException {
        File f = new File(IOUtils.getUserFolderPath() + email + ".txt");
        if (ioUtils.fileExists(f)) {
            List<String> readLines = Files.readAllLines(f.toPath());
            String[] userDetails = readLines.get(0).split(" ");
            if (userDetails[0].equals(email) && userDetails[3].equals(password)) {

                ioUtils.writeMessage("You're in!");
                return true;
            }

        }
        throw new WrongEmailOrPasswordException("Your email or password is incorrect!");
    }

    public void logInToMessenger() {
        ioUtils.writeMessage("Please enter your email address");
        String email2 = ioUtils.readNext();
        ioUtils.writeMessage("Please enter your password");
        String password2 = ioUtils.readNext();

        boolean validatio = false;
        try {
            if (validateUser(email2, password2)) {
                validatio = true;
            }
            ioUtils.writeMessage("all good");
        } catch (WrongEmailOrPasswordException | IOException e) {
            ioUtils.writeMessage(e.getMessage());
        }
        User user = null;
        try {
            user = ioUtils.getUserFromEmail(email2);
        } catch (IOException e) {
            ioUtils.writeMessage(e.getMessage());
        }

        while (validatio) {
            ioUtils.writeMessage("What would you like to do now?");
            ioUtils.writeMessage("1. Send a message to another user");
            ioUtils.writeMessage("2. Send message to several users");
            ioUtils.writeMessage("3. Start or join group chat");
            ioUtils.writeMessage("0. Quit");

            User finalUser = user;
            Thread messageChecker = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }
                    try {
                        checkForNewMessages(finalUser);
                    } catch (IOException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }
                }
            });
                messageChecker.start();
            switch (ioUtils.readNextInt()) {
                case 1:
                    ioUtils.writeMessage("Type in the email of the person to chat with");
                    String userToMessage = ioUtils.readNext();

                    if (ioUtils.userExists(userToMessage)) {
                        ioUtils.writeMessage("Message: ");
                        String messageToSend = ioUtils.readNext();

                        try {
                            messageUser(userToMessage, email2, messageToSend);
                            ioUtils.writeMessage("Message sent successfully");
                        } catch (NoSuchUserException e) {
                            ioUtils.writeMessage(e.getMessage());
                        }
                    }
                    break;
                case 2:
                    ioUtils.writeMessage("Enter the emails of the users your would like to message, comma separated");
                    String emails = ioUtils.readNext();
                    ioUtils.writeMessage("Message:");
                    String message = ioUtils.readNext();
                    try {
                        messageMultiUsers(emails, user.getEmail(), message );
                    } catch (NoSuchUserException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }
                    break;
                case 3:
                    ioUtils.writeMessage("Enter the name of the group chat to create or join");
                    String groupChatName = ioUtils.readNext();
                    try {
                        enterGroupChat(finalUser, groupChatName);
                    } catch (IOException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }

                case 0:
                    validatio = false;

                default:
                    ioUtils.writeMessage("Incorrect choice");
                    break;

            }

        }
    }

    public void messageUser(String targetUser, String sendingUser, String message) throws NoSuchUserException {
        File f = new File(IOUtils.getUserFolderPath() + targetUser + ".txt");
        if (ioUtils.fileExists(f)) {
            if (targetUser.toLowerCase().charAt(0) <= sendingUser.toLowerCase().charAt(0)) {
                try {
                    ioUtils.appendMessageToChatFile(targetUser, sendingUser, message, sendingUser);
                } catch (IOException e) {
                    System.out.println(e.getMessage());

                }
            } else {
                try {
                    ioUtils.appendMessageToChatFile(sendingUser, targetUser, message, sendingUser);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        } else {

            throw new NoSuchUserException("No such email in the chat program");

        }


    }



    public void messageMultiUsers(String emailsString, String sender, String message) throws NoSuchUserException {
        String[] splitEmails = emailsString.split(",");
        for (String email:splitEmails){
            if(ioUtils.userExists(email)){
                messageUser(email, sender, message);
            }
        }
        ioUtils.writeMessage("Messages sent successfully");
    }

    public void checkForNewMessages(User user) throws IOException {
        Files.walk(Paths.get(IOUtils.getChatFolderPath()))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        ioUtils.checkChatFileForNewMessages(path, user);
                    } catch (IOException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }
                });

    }

    public void checkGroupChatsForNewMessages(User user) throws IOException {
        Files.walk(Paths.get(IOUtils.getGroupChatsFolder()))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        ioUtils.checkGroupChatFileForNewMessages(user, path);
                    } catch (IOException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }
                });
    }

    public void enterGroupChat(User user, String chatName) throws IOException {
        ioUtils.updateGroupChatTimestamp(user);
        boolean groupLogin = true;
        ioUtils.writeMessage("Welcome to the " + chatName + " chatroom");
        ioUtils.writeMessage("Type in your messages and they'll get to everyone in this chatroom");
        ioUtils.writeMessage("To exit the chatroom type \"exit\"");

        Thread groupChecker = new Thread(()-> {

         while(true){
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 ioUtils.writeMessage(e.getMessage());
             }

             try {
                 checkGroupChatsForNewMessages(user);
             } catch (IOException e) {
                 ioUtils.writeMessage(e.getMessage());
             }

         }


        });


        groupChecker.start();
        while(groupLogin){
            String input = ioUtils.readNext();
            if (input.equals("exit")){
                groupLogin = false;
            } else {

                ioUtils.writeToGroupChat(chatName, user, input);
            }
        }
        ioUtils.writeMessage("Thanks for beta testing the group chat feature");


    }


}