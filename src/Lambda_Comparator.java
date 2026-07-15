import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Student {

    private String name;
    private int age;
    private double gpa;

    public Student(String name, int age, double gpa) {
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        //-6s rộng 6 kí tự, căn trái (-)
        //2d rộng 2 ô
        //1f chỉ lấy 1 kí tự sau dấu .
        return String.format("%-6s Age:%2d  GPA: %.1f", name, age, gpa);
    }
}

public class Lambda_Comparator {

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        students.add(new Student("Tom", 20, 3.8));
        students.add(new Student("Alex", 18, 3.9));
        students.add(new Student("Bob", 20, 3.5));
        students.add(new Student("David", 20, 3.8));
        students.add(new Student("Chris", 18, 3.6));

        // 1. Sắp xếp theo Age tăng dần
        students.sort(
                Comparator.comparingInt(Student::getAge)
        );
        System.out.println("\nSắp xếp tuổi tăng dần:\n");
        students.forEach(System.out::println);

        // 2. Sắp xếp theo GPA giảm dần
        students.sort(
                Comparator.comparingDouble(Student::getGpa)
                        .reversed()
        );
        System.out.println("\nSắp xếp GPA giảm dần:\n");
        students.forEach(System.out::println);

        // 3. Sắp xếp theo Name A -> Z
        students.sort(
                Comparator.comparing(Student::getName)
        );
        System.out.println("\nSắp xếp theo tên:\n");
        students.forEach(System.out::println);

        // 4. GPA giảm dần
        //    Nếu bằng GPA thì Name A -> Z
        students.sort(
                Comparator.comparingDouble(Student::getGpa)
                        .reversed()
                        .thenComparing(Student::getName)
        );
        System.out.println("\nSắp xếp GPA giảm dần và tên:\n");
        students.forEach(System.out::println);

        // 5. Age tăng dần
        //    Nếu bằng Age thì GPA giảm dần
        //    Nếu vẫn bằng thì Name A -> Z
        students.sort(
                Comparator.comparingInt(Student::getAge)
                        .thenComparing(
                                Comparator.comparingDouble(Student::getGpa)
                                        .reversed()
                        )
                        .thenComparing(Student::getName)
        );
        System.out.println("\nSắp xếp tuổi tăng dần đến GPA giảm dần rồi đến tên:\n");
        students.forEach(System.out::println);

    }
}