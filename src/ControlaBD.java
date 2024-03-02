import java.sql.*;

public class ControlaBD {

    Connection cnn;

    public ControlaBD(){
        try {
            cnn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/livraria" +
                    "?user=lutero&password=123456&ssl=true");
        }catch (Exception e){
            System.out.println("Falha na conexão com o banco de dados");
            System.exit(1);
        }
    }
}
