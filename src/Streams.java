import java.util.List;
import java.util.Arrays;

class Employee {

    private String name;
    private int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

}

public class Streams {
    public static void main(String[] args) {
        List<Integer> scores = Arrays.asList(11, 42, 53, 6, 3, 7, 8, 10, 2, 9);
        List<String> names = Arrays.asList("hieu", "an", "nam");
        List<Employee> employees = Arrays.asList(
                new Employee("Hiếu", 22),
                new Employee("An", 18),
                new Employee("Nam", 30),
                new Employee("Long", 17)
        );

        scores.stream()
                //phân loại chỉ lấy những điểm lớn hơn 8
                .filter(score -> score >=9)
                //in từng cái ra 1 dòng
                .forEach(System.out::println);
        int sum = scores.stream()
                //tính tổng
                        .reduce(0, Integer::sum);
        System.out.println(sum);
        int max = scores.stream()
                //tìm max
                        .reduce(Integer.MIN_VALUE, Math::max);
        System.out.println(max);
        boolean result1 = scores.stream()
                //cần ít nhất 1 điều kiện thỏa mãn
                        .anyMatch(score -> score > 50);
        System.out.println(result1);
        boolean result2 = scores.stream()
                //cần tất cả các điều kiện thỏa mãn
                        .allMatch(score -> score < 50);
        System.out.println(result2);
        boolean result3 = scores.stream()
                //không có bất cứ điều kiện nào thỏa mãn
                        .noneMatch(score -> score < 6);
        System.out.println(result3);

        names.stream()
                //chuyển từ chữ thường thành chữ hoa
                .map(String::toUpperCase)
                .forEach(System.out::println);
        String result = names.stream()
                //gộp text
                .reduce("", (a,b)->a+b);
        System.out.println(result);

        employees.stream()
                .filter(employee -> employee.getAge() >= 20)
                //chuyển từ Employee thành "name" và "age"
                .map(employee -> employee.getName() +"\n"+ employee.getAge())
                .forEach(System.out::println);
    }
}
