import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Category {

    private final String name;
    private final List<Product> products;

    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }
}

public class FlatMap {

    public static void main(String[] args) {
        List<Category> categories = Arrays.asList(

                new Category(
                        "Laptop",
                        Arrays.asList(
                                new Product(1, "Dell", 2500),
                                new Product(2, "Macbook", 3500)
                        )
                ),

                new Category(
                        "Phone",
                        Arrays.asList(
                                new Product(3, "iPhone", 2000),
                                new Product(4, "Samsung", 1800)
                        )
                )
        );
        categories.stream()
                .map(Category::getName)
                .forEach(System.out::println);

        List<String> names = categories.stream()
                //lấy phần Product của Category
                .flatMap(category -> category.getProducts().stream())
                .filter(product -> product.getPrice() >= 2000)
                .map(Product::getName)
                .collect(Collectors.toList());

        System.out.println(names);

        List<List<Integer>> numbers = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5, 6)
        );
        List<Integer> result1 = numbers.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        System.out.println(result1);
    }
}