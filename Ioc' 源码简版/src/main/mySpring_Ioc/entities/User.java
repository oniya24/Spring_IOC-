package entities;

import annotations.Component;
import annotations.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Component
@Data
public class User {
    @Value("1")
    private Integer id;
    @Value("小刘")
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
