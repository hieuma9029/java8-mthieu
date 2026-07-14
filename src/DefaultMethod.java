interface Animalss {

    void eat();

    default void sleep() {
        System.out.println("Animal is sleeping");
    }
}
interface Pet {
    default void sleep() { System.out.println("Pet is sleeping"); }
}

class Dog implements Animalss, Pet {

    @Override
    public void eat() {
        System.out.println("Dog is eating");
    }

    @Override
//    public void sleep() {
//        System.out.println("Dog is sleeping");
//    }
    public void sleep() { Pet.super.sleep(); }

}

public class DefaultMethod {

    public static void main(String[] args) {

        Dog dog = new Dog();

        dog.eat();

        dog.sleep();
    }
}