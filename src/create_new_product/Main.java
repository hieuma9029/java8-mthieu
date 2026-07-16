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
                                LocalDate.of(2026,12,27),
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
                                LocalDate.of(2026,12,1),
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
//        List<Product> result1 = new ArrayList<>();
//        for (Product product : listProduct) {
//            if (product.getQulity() > 0 && !product.isDelete())
//            result1.add(product);
//        }
//        return result1;
//    }
    //dùng stream
    public static List<Product> filterProductByQulity(List<Product> listProduct) {
        return listProduct.stream()
                .filter(product -> product.getQulity() > 0)
                .filter(product -> !product.isDelete())
                .collect(Collectors.toList());
    }

    //k dùng steam
//    public static List<Product> filterProductBySaleDate(List<Product> ListProduct) {
//        List<Product> result2 = new ArrayList<>();
//        for (Product product : ListProduct) {
//            if (product.getSaleDate().isAfter(LocalDate.now()) &&
//                    !product.isDelete()) {
//                result2.add(product);
//            }
//        }
//        return result2;
//    }
    //dùng stream
    public static List<Product> filterProductBySaleDate(List<Product> ListProduct) {
        return ListProduct.stream()
                .filter(product -> product.getSaleDate().isAfter(LocalDate.now()))
                .filter(product -> !product.isDelete())
                .collect(Collectors.toList());
    }

    //k dùng stream
//    public static int totalProduct(List<Product> ListProduct) {
//        int sum = 0;
//        for (Product product : ListProduct) {
//            if (!product.isDelete()) {
//                sum += product.getQulity();
//            }
//        }
//        return sum;
//    }
    //dùng stream
    public static int totalProduct(List<Product> ListProduct) {
        return ListProduct.stream()
                .filter(product -> !product.isDelete())
                .map(Product::getQulity)
                .reduce(0, Integer::sum);
    }

    //k dùng stream
//    public static boolean isHaveProductInCategory(List<Product> listProduct, int categoryId) {
//        for (Product product : listProduct) {
//            if (product.getCategoryId() == categoryId) {
//                return true;
//            }
//        }
//        return false;
//    }
    //dùng stream
    public static boolean isHaveProductInCategory(List<Product> listProduct, int categoryId) {
        return listProduct.stream()
                .anyMatch(product -> product.getCategoryId() == categoryId);
    }

    //k dùng stream
//    public static List<Product> fiterProductBySaleDate(List<Product> listProduct) {
//        List<Product> result = new ArrayList<>();
//        for (Product product : listProduct) {
//            if (product.getSaleDate().isAfter(LocalDate.now()) &&
//            product.getQulity()>0){
//                result.add(product);
//            }
//        }
//        return result;
//    }
    //dùng stream
    public static List<Product> fiterProductBySaleDate(List<Product> listProduct) {
        return listProduct.stream()
                .filter(product -> product.getSaleDate().isAfter(LocalDate.now()))
                .filter(product -> product.getQulity() > 0)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Product> products = createListProduct();

        String name = filterProductById(products,7);
        List<Product> result = filterProductByQulity(products);
        List<Product> result1 = filterProductBySaleDate(products);
        int sum = totalProduct(products);
        boolean result2 = isHaveProductInCategory(products, 2);
        List<Product> result3 = fiterProductBySaleDate(products);

        System.out.println("bài 11");
        System.out.println(name);
        System.out.println("bài 12");
        result.forEach(System.out::println);
        System.out.println("bài 13");
        result1.forEach(System.out::println);
        System.out.println("bài 14");
        System.out.println(sum);
        System.out.println("bài 15");
        System.out.println(result2);
        System.out.println("bài 16");
        result3.forEach(System.out::println);
    }
}
