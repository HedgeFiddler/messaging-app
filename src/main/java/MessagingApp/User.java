package MessagingApp;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private String email;
    private String name;
    private int age;
    private String password;
    private LocalDateTime lastUpdate;

    public User(String email, String name, int age, String password) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.password = password;
        this.lastUpdate = null;
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


    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(lastUpdate, user.lastUpdate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, name, age, password, lastUpdate);
    }

    @Override
    public String toString() {
        return
                email + " " + name + " " + age + " " + password + " " + lastUpdate ;
    }
}
