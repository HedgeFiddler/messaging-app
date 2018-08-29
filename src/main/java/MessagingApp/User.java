package MessagingApp;

import java.time.LocalDateTime;

public class User {
    private String email;
    private String name;
    private int age;
    private String password;
    private LocalDateTime lastLogin;

    public User(String email, String name, int age, String password) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }


    public String getName() {
        return name;
    }


    public int getAge() {
        return age;
    }


    public String getPassword() {
        return password;
    }


    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
