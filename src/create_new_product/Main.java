package create_new_product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static List<Product> createListProduct() {

        List<Product> products = new ArrayList<>(
                Arrays.asList(
                        new Product(1, "Laptop", 1,
                                LocalDate.of(2026,1,1),
                                280,
                                false),
                        new Product(2, "Iphone", 2,
                                LocalDate.of(2025,11,16),
                                240,
                                true),
                        new Product(3, "Samsung", 3,
                                LocalDate.of(2025,12,9),
                                203,
                                false),
                        new Product(4, "PC", 4,
                                LocalDate.of(2025,10,10),
                                201,
                                true),
                        new Product(5, "Mouse", 5,
                                LocalDate.of(2025,8,19),
                                200,
                                false),
                        new Product(6, "Ipad", 6,
                                LocalDate.of(2025,12,27),
                                202,
                                false),
                        new Product(7, "Macbook", 7,
                                LocalDate.of(2026,1,21),
                                92,
                                true),
                        new Product(8, "Headphone", 8,
                                LocalDate.of(2026,1,16),
                                29,
                                false),
                        new Product(9, "Screen", 9,
                                LocalDate.of(2026,1,1),
                                29,
                                false),
                        new Product(10, "Chair", 10,
                                LocalDate.of(2025,12,1),
                                220,
                                true)
    )
        );

        return products;
    }

    //k dùng steam
//    public static String filterProductById(List<Product> listProduct, int idProduct) {
//
//        for (Product product : listProduct) {
//            if (product.getId() == idProduct) {
//                return product.getName();
//            }
//        }
//
//        return null;
//    }
    //dùng stream
    public static String filterProductById(List<Product> listProduct, int idProduct) {

        return listProduct.stream()
                .filter(product -> product.getId() == idProduct)
                .map(Product::getName)
                .findFirst()
                .orElse(null);
    }

    //k dùng stream
//    public static List<Product> filterProductByQulity(List<Product> listProduct) {
//        List<Product> result = new ArrayList<>();
//        for (Product product : listProduct) {
//            if (product.getQulity() > 0 && !product.isDelete())
//            result.add(product);
//        }
//        return result;
//    }
    //dùng stream
    public static List<Product> filterProductByQulity(List<Product> listProduct) {
        return listProduct.stream()
                .filter(product -> product.getQulity() > 0)
                .filter(product -> !product.isDelete())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Product> products = createListProduct();

        String name = filterProductById(products,7);
        List<Product> result = filterProductByQulity(products);

        System.out.println(name);
        result.forEach(System.out::println);
    }
}
