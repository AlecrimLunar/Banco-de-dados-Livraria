import java.sql.*;

public class ControlaBD {

    private Connection con;

    public ControlaBD(){
        String dbURL = "jdbc:postgresql://localhost:5432/livraria";
        String login = "alecrim";
        String password = "21092004nicolas";
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
             banco de dados com o valor pesquisado, então retorna false*/
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
            String consulta = "SELECT COUNT(" + pesquisa + ") FROM " + tabela + ";";

            ResultSet rt = st.executeQuery(consulta);
            return rt.next() ? rt.getInt(1) : -1;
            /*Nico: tive que adicionar a linha acima pq tava dando erro aqui. basicamente onde eu
             * posso botar .next eu coloco pra funcionar*/

        }catch (Exception e){
            System.out.println("ERRO - QUERRY: " + e);
        }
        return -1;
    }

    /*Nico:tive que criar pra fazer o login e ta funfando certinho, só add a coluna usuario na
     * tua tabela visse*/
    public boolean login(String user, String password){
        try {
            Statement st = con.createStatement();
            String consulta = "SELECT senha FROM vendedor WHERE usuario = '" + user + "';";

            ResultSet rt = st.executeQuery(consulta);

            rt.next();
            /*esse next ta ajeitando o "ponteiro" pra pegar a string. Sabe o index de quando se
             * lê um arquivo .txt? então aquele bagulho lá*/
            return password.equalsIgnoreCase(rt.getString(1));
        }catch (Exception e){
            System.out.println("ERRO - QUERRY: " + e);
            return false;
        }
    }

    public boolean alter(String tabela, String coluna,String novo, String antigo){
        try{
            Statement st = con.createStatement();
            String consulta = "UPDATE " + tabela + " SET " + coluna + " = '" + novo + "' WHERE " + coluna + " '" + antigo + "';";

            int a = st.executeUpdate(consulta);
            if(a == 1)
                return true;
        } catch (Exception e){
            System.out.println("ERRO - UPDATE: " + e);
        }
        return false;
    }
    /*Nico: Esses são os UPDATES, como até agora a gente só tem string e long da pra deixar assim.
     * to usando o bagulho de overwrite ou sobre-escrita. Onde só mudando oq a função recebe o programa escolhe a certa
     * só pude fazer isso hoje... amanha (segunda) pego nesse cod de novo*/
    public boolean alter(String tabela, String coluna, long novo, long antigo){
        try{
            Statement st = con.createStatement();
            String consulta = "UPDATE " + tabela + " SET " + coluna + " = '" + novo + "' WHERE " + coluna + " '" + antigo + "';";

            int a = st.executeUpdate(consulta);
            if(a == 1)
                return true;
        } catch (Exception e){
            System.out.println("ERRO - UPDATE: " + e);
        }
        return false;
    }
}