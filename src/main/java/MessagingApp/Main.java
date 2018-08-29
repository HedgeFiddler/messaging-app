package MessagingApp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        while(true){
            UserApp userApp = new UserApp();
            System.out.println("Welcome to Blah-Blah messaging app");
            System.out.println("Please log in or create a new user to shart chatting. Please input the number corresponding to your needs.");
            System.out.println("1. Create a new user ");
            System.out.println("2. Log in with an already created user");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Please enter an email for your account");
                    String email = scanner.next();
                    System.out.println("Please enter a name without spaces");
                    String name = scanner.next();
                    System.out.println("Please enter your age");
                    int age = scanner.nextInt();
                    System.out.println("Please enter a password for your account");
                    String password = scanner.next();
                    userApp.saveUser(email, name, age, password);

            }
        }

    }
}
