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
            ioUtils.writeMessage("2. Log in with an already created user");
            ioUtils.writeMessage("3. Quit!");

            switch (ioUtils.readNextInt()) {
                case 1:

                    try {
                        userApp.saveUser();
                    } catch (IOException | EmailAlreadyRegisteredException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                case 2:

                    userApp.logInToMessenger();

                    break;
                case 3:
                    ioUtils.writeMessage("Thanks for using Blah-Blah, come back soon");
                    System.exit(1);

            }
        }

    }
}
