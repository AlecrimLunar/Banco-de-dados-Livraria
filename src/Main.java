import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("LOGIN NO SISTEMA \n\nUsuário:");
        Scanner tc = new Scanner(System.in);
        long id = Long.parseLong(tc.nextLine());
    }

    public static Connection criaConexao(){
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/livraria" +
                    "?user=lutero&password=123456&ssl=true");
        }catch (Exception e){
            System.out.println("Falha na conexão com o banco de dados");
        }
        return null;
    }
}