@FunctionalInterface
interface Animal {
    //Được tính là 1 abstract method vì body rỗng
    void eat();
    //Không được tính là abstract method vì đã có phần body
    default void sleep() {
        System.out.println("Dog is Sleeping");
    }
    //Không được tính là abstract method vì đã có phần body
    static void run() {
        System.out.println("Dog is Running");
    }
    //Là method kế thừa Object k đc tính là abstract method
    String toString();

}
public class Functional_Interface {
    public static void main(String[] args) {

        Animal dog = () -> System.out.println("Dog is eating");

        dog.eat();

        dog.sleep();

        Animal.run();
    }
}
