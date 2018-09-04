package MessagingApp;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        while (true) {
            IOUtils ioUtils = new IOUtils();
            UserApp userApp = new UserApp(ioUtils);

            ioUtils.writeMessage("Welcome to Blah-Blah messaging app");
            ioUtils.writeMessage("Please log in or create a new user to start chatting. Please input the number corresponding to your needs.");
            ioUtils.writeMessage("1. Create a new user ");
            ioUtils.writeMessage("2. Log in with an already created user and start chatting");
            ioUtils.writeMessage("3. Quit!");

            int choice = ioUtils.scanner.nextInt();
            switch (choice) {
                case 1:
                    ioUtils.writeMessage("Please enter an email for your account");
                    String email = ioUtils.scanner.next();
                    ioUtils.writeMessage("Please enter a name without spaces");
                    String name = ioUtils.scanner.next();
                    ioUtils.writeMessage("Please enter your age");
                    int age = ioUtils.scanner.nextInt();
                    ioUtils.writeMessage("Please enter a password for your account");
                    String password = ioUtils.scanner.next();
                    try {
                        userApp.saveUser(email, name, age, password);
                    } catch (IOException | EmailAlreadyRegisteredException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                case 2:
                    ioUtils.writeMessage("Please enter your email address");
                    String email2 = ioUtils.scanner.next();
                    ioUtils.writeMessage("Please enter your password");
                    String password2 = ioUtils.scanner.next();

                    boolean validatio = false;
                    try {
                        if (userApp.validateUser(email2, password2)) {
                            validatio = true;
                        }
                        ioUtils.writeMessage("all good");
                    } catch (WrongEmailOrPasswordException | IOException e) {
                        ioUtils.writeMessage(e.getMessage());
                    }


                    while (validatio) {
                        ioUtils.writeMessage("What would you like to do now?");
                        ioUtils.writeMessage("1. Send a message to antother user");
                        ioUtils.writeMessage("Quit");
                        int messagerChoice = ioUtils.scanner.nextInt();
                        switch (messagerChoice) {
                            case 1:
                                ioUtils.writeMessage("Type in the email of the person to chat with");
                                String userToMessage = ioUtils.readNext();

                                if (ioUtils.isUserThere(userToMessage)) {
                                    ioUtils.writeMessage("Message: ");
                                    String messageToSend = ioUtils.readNext();

                                    try {
                                        userApp.messageUser(userToMessage, email2, messageToSend);
                                        ioUtils.writeMessage("Message sent successfully");
                                    } catch (NoSuchUserException e) {
                                        ioUtils.writeMessage(e.getMessage());
                                    }
                                }
                                break;
                            case 2:
                                return;

                            default:
                                ioUtils.writeMessage("Incorrect choice");
                                break;

                        }

                    }

                    break;
                case 3:
                    ioUtils.writeMessage("Thanks for using Blah-Blah, come back soon");
                    System.exit(1);


            }
        }

    }
}
