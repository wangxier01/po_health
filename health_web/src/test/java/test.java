import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("1234", "$2a$10$ZajmWJZmCMtNsE0l/oC47.ZHrVHMa.tXkWnvSD5TWc3oFSs1c6HH6"));
    }
}
