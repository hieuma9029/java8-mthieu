package abc;

import abc.config.AppConfig;
import abc.entity.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        Student s1 = context.getBean(Student.class);
        Student s2 = context.getBean(Student.class);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s1 == s2);
    }

}