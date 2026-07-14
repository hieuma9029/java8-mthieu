interface Message{
    void send(String from,String to);
}
interface Animals{
    void speak();
}
interface Calculator{
    int calculate(int a, int b);
}

public class Lambda {
    public static void main(String[] args) {
        //Gửi tin nhắn
        Message message = (from, to) ->
                System.out.println(from + " gửi tin nhắn cho " + to);
        message.send("Hiếu","Nam");
        //Động vật kêu
        Animals dog = () -> System.out.println("Gâu");
        Animals cat = () -> System.out.println("Meo");
        dog.speak();
        cat.speak();
        //Tính toán
        Calculator calculator = (a, b) -> {
            System.out.println("Đang tính...");
            return a * b;
        };
        System.out.println(calculator.calculate(5,3));
    }
}