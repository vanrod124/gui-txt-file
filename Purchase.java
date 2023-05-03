import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Purchase {

    private LocalDateTime dateTime;
    private Customer customer;
    private List<Product> products;
    private String customerAddress;

public String getCustomerAddress() {
    return customerAddress;
}

public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
}


    public Purchase(Customer customer) {
        this.dateTime = LocalDateTime.now();
        this.customer = customer;
        this.products = new ArrayList<>();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Product product : products) {
            totalPrice += product.getTotalPrice();
        }
        return totalPrice;
    }

    public int calculateLoyaltyPoints() {
        return (int) (getTotalPrice() / 10);
    }

}
