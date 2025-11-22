package models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courier {

    private String login;
    private String password;
    private String firstName;

    // Дополнительный конструктор на два поля
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
