import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DatenTimeAPI {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalTime time = LocalTime.now();

        LocalDateTime now = LocalDateTime.now();

        LocalDate birthday =
                LocalDate.of(1998,11,30);

        LocalDate d1 =
                LocalDate.of(2025,1,1);
        LocalDate d2 =
                LocalDate.of(2026,1,1);

        Period age = Period.between(birthday,today);

        //ngày giờ hiện tại
        System.out.println(now);
        //thời gian hiện tại
        System.out.println(time);
        //ngày theo thứ tự dd/mm/yyyy
        System.out.println(today.format(formatter));
        //cộng ngày
        System.out.println(today.plusDays(5));
        //trừ ngày
        System.out.println(today.minusDays(10));
        //ngày tự đặt
        System.out.println(birthday);
        //so sánh ngày trả về giá trị t or f
        System.out.println(d1.isBefore(d2));
        //thời gian từ ngày đặt đến ngày hôm nay (năm today-năm đặt)
        System.out.println(age.getYears());
    }
}