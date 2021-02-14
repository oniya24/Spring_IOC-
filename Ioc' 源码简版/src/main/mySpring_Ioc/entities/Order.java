package entities;

import annotations.Autowired;
import annotations.Component;
import annotations.Qualifier;
import annotations.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Component
@Data
public class Order {
    @Value("2")
    private Integer Id;
    @Value("马桶刷")
    private String bathroomCleaner;
    @Value("12.6")
    private Float price;
    @Autowired
//    @Qualifier()
    private User user;

    @Override
    public String toString() {
        return "Order{" +
                "Id=" + Id +
                ", bathroomCleaner='" + bathroomCleaner + '\'' +
                ", price=" + price +
                ", user=" + user +
                '}';
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getBathroomCleaner() {
        return bathroomCleaner;
    }

    public void setBathroomCleaner(String bathroomCleaner) {
        this.bathroomCleaner = bathroomCleaner;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
