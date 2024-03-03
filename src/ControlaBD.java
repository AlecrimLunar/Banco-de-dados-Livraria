import java.sql.*;

public class ControlaBD {

    private Connection con;

    public ControlaBD(){
        String dbURL = "jdbc:postgresql://localhost:5432/livraria";
        String login = "lutero";
        String password = "123456";
        try {

            con = DriverManager.getConnection(dbURL, login, password);

        }catch (Exception e){
            System.out.println("Falha na conexão com o banco de dados: " + e);
            System.exit(1);
        }
    }

    public boolean Insert(String tabela, String infos){
        try{
            Statement st = con.createStatement();
            String consulta = "INSERT INTO " + tabela + " VALUES (" + infos + ");";

            int a = st.executeUpdate(consulta);
            if (a == 1)
                return true;
        }catch(Exception e){
            System.out.println("ERRO - INSERT: " + e);
        }
        return false;
    }

    public boolean Existe(String pesquisa, String tabela, String coluna){
        try {
            Statement st = con.createStatement();
            String consulta = "SELECT * FROM " + tabela + " WHERE " + coluna + " = " + pesquisa + ";";

            ResultSet rt = st.executeQuery(consulta);

            /*se rt for nulo, nada foi retornado da consulta, logo não existe nada no
             * banco de dados com o valor pesquisado, então retorna false*/
            return (rt != null);
        }catch (Exception e){
            System.out.println("ERRO - QUERRY: " + e);
            return false;
        }
    }

    public int Quantos(String pesquisa, String tabela){
        try{

            /*caso não seja especificado um campo para procurar, sera realizada a consulta
            * utilizando o *, o que significa que ele ira contar todas as linhas da tabela*/

            if (pesquisa.isEmpty())
                pesquisa = "*";

            Statement st = con.createStatement();
            String consulta = "SELECT * count(" + pesquisa + ") FROM " + tabela + ";";

            ResultSet rt = st.executeQuery(consulta);
            return (Integer.parseInt(rt.toString()));

        }catch (Exception e){
            System.out.println("ERRO - QUERRY: " + e);
        }
        return -1;
    }
}
