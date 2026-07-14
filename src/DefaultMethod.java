interface Animalss {

    void eat();

    default void sleep() {
        System.out.println("Animalss is sleeping");
    }

}

class Dog implements Animalss {

    @Override
    public void eat() {
        System.out.println("Dog is eating");
    }

    @Override
    public void sleep() {
        System.out.println("Dog is sleeping");
    }

}

public class DefaultMethod {

    public static void main(String[] args) {

        Dog dog = new Dog();

        dog.eat();

        dog.sleep();

    }

}