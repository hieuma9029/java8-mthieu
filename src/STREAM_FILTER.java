import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Product {

    private final int id;
    private final String name;
    private final int price;

    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%-8s Price: %4d", name, price);
    }
}

public class STREAM_FILTER {

    public static void main(String[] args) {

        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Mouse", 300));
        products.add(new Product(2, "Keyboard", 500));
        products.add(new Product(3, "Laptop", 2000));
        products.add(new Product(4, "Monitor", 1500));
        products.add(new Product(5, "Phone", 900));

        // 1. Giá > 1000
        List<Product> result1 = products.stream()
                .filter(p -> p.getPrice() > 1000)
                .collect(Collectors.toList());

        System.out.println("\nGiá > 1000");
        result1.forEach(System.out::println);

        // 2. Giá từ 500 -> 1500
        List<Product> result2 = products.stream()
                .filter(p -> p.getPrice() >= 500 &&
                        p.getPrice() <= 1500)
                .collect(Collectors.toList());

        System.out.println("\nGiá từ 500 -> 1500");
        result2.forEach(System.out::println);

        // 3. Tên bắt đầu bằng M
        List<Product> result3 = products.stream()
                .filter(p -> p.getName().startsWith("M"))
                .collect(Collectors.toList());

        System.out.println("\nTên bắt đầu bằng M");
        result3.forEach(System.out::println);

        // 4. Giá > 500 và tên chứa chữ o
        List<Product> result4 = products.stream()
                .filter(p ->
                        p.getPrice() > 500 &&
                                p.getName().contains("o")
                )
                .collect(Collectors.toList());

        System.out.println("\nGiá > 500 và tên chứa 'o'");
        result4.forEach(System.out::println);

    }
}