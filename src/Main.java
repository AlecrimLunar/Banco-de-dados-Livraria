
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ControlaBD controle = new ControlaBD();

        System.out.println("LOGIN NO SISTEMA \n\nUsuário:");
        Scanner tc = new Scanner(System.in);
        long id = Long.parseLong(tc.nextLine());
    }
}