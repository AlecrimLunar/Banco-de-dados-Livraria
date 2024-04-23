package Controle;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;

/**
 * Classe responsável pela comunicação com o banco de dados.
 * */
public class ControlaBD {
    public ControlaBD(){

    }

    /**
     * Função responsável pelos inserts quando se quer saber
     * o id criado pelo banco para a linha inserida.
     * @Retorna:
     * <ul>
     * <li>-1 caso um erro tenha acontecido;
     * <li>0 caso não tenha conseguido inserir;
     * <li>Qualquer outro inteiro positivo referente ao id atribuído pelo
     * banco de dados àquela linha.</li>
     * </ul>
     * @Excessão: caso tenha havido algum erro com o SQL, e após encerrar
     * a conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected int InsertRetornando(String tabela, String infos,
                                   String atributos, Connection con) throws ConexaoException{
        PreparedStatement st = null;
        ResultSet rt = null;
        try {
            String consulta = "INSERT INTO " + tabela + " (" + atributos +
                    ") VALUES (" + infos + ") RETURNING id_" + tabela + ";";


            /*
            * aqui é feito a consultal. utilizamos o 'executeQuerry' em vez do
            * 'executeUpdate' porque queremos que a consulta nos retorne algo
            */
            st = con.prepareStatement(consulta);
            rt = st.executeQuery();

            if (rt.next())
                return rt.getInt("id_" + tabela);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConexaoException();
        } finally {
            try{
                if (st != null)
                    st.close();

                if (rt != null)
                    rt.close();
            } catch (Exception e){}
        }
        return -1;
    }

    /**
     * Função responsável pelos inserts onde não se quer saber
     * o id criado pelo banco para a linha inserida.
     * Por isso, essa função aceita uma lista de strings,
     * possibilitando a inserção de várias (tanto a tabela
     * quanto as colunas onde os valores serão inseridos
     * precisam ser fixos).
     * @Retorna:
     * <ul>
     * <li>-1 caso um erro tenha acontecido;
     * <li>0 caso não tenha conseguido inserir todos os valores;
     * <li>1 caso as inserções tenham acontecido.</li>
     * </ul>
     * @Excessão:
     * Caso tenha ocorrido algum erro com o SQL e após o fechamento
     * da conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected int Insert(String tabela, ArrayList<String> infos,
                         String atributos, Connection con) throws ConexaoException{
        PreparedStatement st = null;

        try {
            con.setAutoCommit(false);


            String consulta = "INSERT INTO " + tabela + " (" + atributos +
                    ") VALUES (?) RETURNING id_" + tabela + ";";

            for (String s : infos) {
                st = con.prepareStatement(consulta);
                st.setString(1, s);

                int numLinhasInseridas = st.executeUpdate();
                if (numLinhasInseridas == 0) {
                    /*
                     * verificação de que algo foi realmente inserido no
                     * banco de dados. caso nada tenha sido adicionado, o
                     * processo precisa ser reiniciado
                    */
                    con.rollback();
                    return -1;
                }
            }
            con.commit();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                con.rollback();
                throw new ConexaoException();
            } catch (SQLException f) {
            }
        } finally {
            try{
                if (st != null)
                    st.close();
            } catch (Exception e){}
        }
        return -1;
    }


    /**
     * Função responsável por executar qualquer SELECT desejado no banco
     * de dados. Irá executar o SQL "SELECT 'argumentos' FROM 'tabela'
     * WHERE 'pesquisa';".
     * @Parâmetros: Recebe o nome da tabela, as colunas que deseja receber
     * e a condição para retorno das linhas.
     * @Retorna: Um ResultSet com todas as linhas que a consulta devolveu
     * @Excessão: Caso tenha havido algum erro com o SQL, e após encerrar
     * a conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected ResultSet Select(String tabela, String coluna, String pesquisa, Connection con) throws ConexaoException {
        PreparedStatement st = null;
        try {
            String consulta = "SELECT " + coluna + " FROM " + tabela + " WHERE id_" +
                    tabela + " >= 0 AND " + pesquisa + ";";
            st = con.prepareStatement(consulta);

            return st.executeQuery(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConexaoException();

        } finally {
            try{
                if (st != null)
                    st.close();
            } catch (Exception e){}
        }
    }

    /**
     * Função responsável por verificar se o login fornecido está
     * ou não presente no banco de dados e se a senha informada equivale
     * à senha desse login.
     * @Parâmetros: recebe o nome da tabela, o nome do usário (nome de login)
     * e a senha daquele login.
     * @Retorna:
     * <ul>
     * <li>-1 caso algum erro tenha acontecido;</li>
     * <li>0 caso não exista o login;
     * <li>1 caso exista o login e senha esteja correta;
     * <li>2 caso exista o login mas a senha fornecida estava incorreta.</li>
     * </ul>
     * @Excessão: caso tenha havido algum erro com o SQL, e após encerrar
     * a conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected int login(String user, String password, String tabela, Connection con) throws ConexaoException {
        ResultSet rt = null;
        Se
        try {
            rt = Select(tabela, "*", " WHERE usuario = '" + user + "'");

            if (rt.next()){
                if (password.equalsIgnoreCase(rt.getString("senha")))
                    return 1;
                else
                    return 2;
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();

            try {
                con.close();
                criaCon(donoCon);
            } catch (SQLException f) {
                f.printStackTrace();

            } catch (ConexaoException c){
                throw new ConexaoException();
            }
        } finally {
            try{
                if (rt != null)
                    rt.close();
            } catch (Exception e){}
        }
        return -1;
    }

    /**
     * Função responsável por realizar updates na tabela. Irá executar o SQL
     * "UPDATE 'tabela' SET 'coluna' = 'novo' WHERE 'condicao';".
     * <P>Essa função pode executar múltiplos updates de uma vez em forma de
     * transações. Para isso, basta enviar os ArrayLists com o mesmo tamanho
     * contendo todas as informações para cada UPDATE desejado.</P>
     * <P>Note que essa função realiza o UPDATE em apenas uma coluna por
     * vez.</P>
     * @Parâmetros: Recebe o nome da tabela, um ArrayList contendo o nome da
     * coluna que vai ser atualizada em cada update, um ArrayList contendo o
     * novo valor que será atribuído a coluna em cada update e a condição das
     * linhas que serão atualizadas em cada update.
     * @Retorna:
     * <ul>
     * <li>-1 caso algum erro tenha acontecido;</li>
     * <li>0 caso nenhum update tenha sido realizado;
     * <li>Qualquer outro inteiro positivo correspondente ao número total de
     * updates realizados.
     * </ul>
     * @Excessão: Caso tenha ocorrido algum erro com o SQL e após o fechamento
     * da conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected int update(String tabela, @NotNull ArrayList<String> coluna, @NotNull ArrayList<String> novo,
                      @NotNull ArrayList<String> condicao, Connection con) throws ConexaoException{

        PreparedStatement st = null;
        try {
            String consulta = "UPDATE " + tabela + " SET ? = ? WHERE id_" +
            tabela + " >= 0 AND ?;";
            con.setAutoCommit(false);

            int quantosUpdatesTotal = 0;
            for (int i = 0; i < coluna.size(); ++i){
                st = con.prepareStatement(consulta);
                st.setString(1, coluna.get(i));
                st.setString(2, novo.get(i));
                st.setString(3, condicao.get(i));

                int quantosUpdate = st.executeUpdate();
                quantosUpdatesTotal += quantosUpdate;
            }
            con.commit();
            con.setAutoCommit(true);

            return quantosUpdatesTotal;

        } catch (SQLException e) {
            e.printStackTrace();

            try {
                con.rollback();
                con.close();
                criaCon(donoCon);
            } catch (SQLException f) {
                f.printStackTrace();

            } catch (ConexaoException c){
                throw new ConexaoException();
            }
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();

        } finally{
            try{
                if (st != null)
                    st.close();
            } catch (Exception e){}
        }
        return -1;
    }


    /**
     * Função responsável por executar deletes que respeitem uma condição.
     * Irá executar um SQL do tipo "DELETE FROM 'tabela' WHERE 'condição';".
     * @Parâmetros: Recebe o nome da tabela e a condição (que não pode ser
     * vazia) para o delete.
     * @Retorna:
     * <ul>
     * <li>-1 caso algum erro tenha acontecido;</li>
     * <li>0 caso nenhum delete tenha sido executado;
     * <li>Qualquer outro valor inteiro positivo correspondente à quantidade
     * de linhas deletadas da tabela.
     * </ul>
     * @Excessão: Caso tenha havido algum erro com o SQL e após encerrar
     * a conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected int delete(String tabela,
                      @NotNull String condicao, Connection con) throws ConexaoException{
        PreparedStatement st = null;

        try {
            String consulta = "DELETE FROM " + tabela + " WHERE id_" +
                    tabela + " >= 0 AND " + condicao;

            st = con.prepareStatement(consulta);
            return st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

            throw new ConexaoException();
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();

        } finally{
            try{
                if (st != null)
                    st.close();
            } catch (Exception e){}
        }
        return -1;
    }

    /**
     * Função responsável por executar deletes sem restrição
     * (apagar uma tabela toda).
     * Irá executar um SQL do tipo "DELETE FROM 'tabela'".
     * @Parâmetros: Recebe o nome da tabela.
     * @Retorna:
     * <ul>
     * <li>-1 caso algum erro tenha acontecido;</li>
     * <li>0 caso nenhum delete tenha sido executado;
     * <li>Qualquer outro valor inteiro positivo correspondente à quantidade
     * de linhas deletadas da tabela.
     * </ul>
     * @Excessão: Caso tenha havido algum erro com o SQL e após encerrar
     * a conexão não tenha sido possível criar outra, ele irá retornar
     * ConexaoException
     */
    protected int deleteAll(String tabela, Connection con) throws ConexaoException{
        PreparedStatement st = null;
        try{
            String consulta = "DELETE FROM " + tabela + " WHERE id_" + tabela + ">= 0;";
            st = con.prepareStatement(consulta);

            return st.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();

            try {
                con.close();
                criaCon(donoCon);

            } catch (SQLException f) {
                f.printStackTrace();

            } catch (ConexaoException c){

                throw new ConexaoException();
            }
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();

        } finally{
            try{
                if (st != null)
                    st.close();
            } catch (Exception e){}
        }
        return -1;
    }
}
