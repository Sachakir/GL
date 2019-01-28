package sacha.kir;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncrytedPasswordUtils {
 
    // Encryte Password with BCryptPasswordEncoder
    public static String encryptePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
 
    public static void main(String[] args) {
        String password = "123";
        String encrytedPassword = encryptePassword(password);
 
        System.out.println("Encryted Password: " + encrytedPassword);
    }
 
}
