package Controle;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe responsável por fazer as chamadas de ControlaBD.
 * <P>Nela são tratados os erros possíveis que possam ter acontecidos
 * e é gerenciada a conexão com o banco de dados.
 */
public abstract class GerenciaCon extends ControlaBD{

    private Connection connection;
    private int usuarioBanco;

    /**
     * função responsável por criar as conexões com o banco de
     * dados sempre que necessário.
     * retorna o erro de não ter conexão caso ele não consiga criar
     * a conexão
     */
    private boolean criaCon(int quem) throws ConexaoException{

        String dbURL = "";
        String login = "";
        String password = "";

        switch (quem){
            case 0 -> {
                dbURL = "jdbc:postgresql://localhost:5432/livraria";
                login = "cliente_role";
                password = "12345678";
            }

            case 1 -> {
                dbURL = "jdbc:postgresql://localhost:5432/livraria";
                login = "vendedor_role";
                password = "123456";
            }
        }
        try {
            connection = DriverManager.getConnection(dbURL, login, password);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            connection = null;
            throw new ConexaoException();
        }
    }

    /**
     * Função responsável pelos inserts quando se quer saber
     * o id criado pelo banco para a linha inserida.
     * @Retorna:
     * <ul>
     * <li>-2 caso um erro tenha acontecido e não tenha sido possível
     * tratar ele ou seja, connection agora é null;</li>
     * <li>-1 caso um erro tenha acontecido e tenha sido possível tratar ele,
     * ou seja, deve-se tentar realizar o insert de novo;
     * <li>0 caso não tenha conseguido inserir;
     * <li>Qualquer outro inteiro positivo referente ao id atribuído pelo
     * banco de dados àquela linha.</li>
     * </ul>
     */
    protected int InsertRetornando(String tabela, String infos, String atributos) throws NaoTemConexaoException{
        if (connection != null) {
            try {
                int funcionou = InsertRetornando(tabela, infos, atributos, connection);
                return funcionou;
            } catch (ConexaoException e) {
                try {
                    connection.close();
                } catch (SQLException f) {

                } finally {
                    try {
                        criaCon(usuarioBanco);
                    } catch (ConexaoException a) {
                        throw new NaoTemConexaoException();
                    }
                }
            }
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável pelos inserts onde não se quer saber
     * o id criado pelo banco para a linha inserida. Pode executar
     * múltipos inserts de uma vez.
     * @Parâmetros: 'tabela' recebe a tabela que vai ser inserida;
     * 'infos' recebe um ArrayList contendo todas as coisas a serem
     * inseridas em cada INSERT; 'atributos' recebe quais as colunas
     * da tabela irão receber os valores em 'infos'.
     * @Retorna:
     * <ul>
     * <li>-2 caso um erro tenha acontecido e não tenha sido possível tratar ele
     * ou seja, não há um conexão estabelecida com o banco de dados;
     * <li>-1 caso um erro tenha acontecido e ele tenha sido tratado;
     * <li>0 caso não tenha conseguido inserir todos os valores;
     * <li>1 caso as inserções tenham acontecido.</li>
     * </ul>
     */
    protected int Insert(String tabela, ArrayList<String> infos,
                       String atributos) throws NaoTemConexaoException{
        if (connection != null) {
            try {
                int funcionou = Insert(tabela, infos, atributos, connection);
                return funcionou;
            } catch (ConexaoException e) {
                try {
                    connection.close();
                } catch (SQLException f) {

                } finally {
                    try {
                        criaCon(usuarioBanco);
                    } catch (ConexaoException a) {
                        throw new NaoTemConexaoException();
                    }
                }
            }
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável para saber se algo existe ou não no banco
     * de dados. Irá executar o SQL "SELECT * FROM 'tabela' WHERE
     * 'coluna' = 'condicao';".
     * @Parâmetros: Recebe o nome da tabela, a coluna a ser usada como
     * comparação e a condição que essa coluna precisa atender.
     * @Retorna:
     * <ul>
     * <li>1 caso exista;
     * <li>0 caso não exista;
     * <li>-1 caso um erro tenha acontecido e tenha sido possível tratar ele,
     * ou seja, deve-se chamar a função de novo;</li>
     * <li>-2 caso um erro tenha acontecido e não tenha sido possível tratar ele
     * ou seja, não há um conexão estabelecida com o banco de dados.
     * </ul>
     */
    protected int Existe(String tabela, String coluna, String condicao) throws NaoTemConexaoException{
        ResultSet rt = null;
        if (connection != null){
            try {
                String pesquisa ="WHERE " + coluna + " = " + condicao + ";";
                rt = Select(tabela, coluna, pesquisa, connection);

                return rt.next() ? rt.getInt(1) : 0;

            } catch (ConexaoException e) {
                try {
                    connection.close();
                } catch (SQLException f) {

                } finally {
                    try {
                        criaCon(usuarioBanco);
                    } catch (ConexaoException a) {
                        throw new NaoTemConexaoException();
                    }
                }
            } catch (SQLException e){
                return -1;
            } finally {
                try {
                    if (rt != null)
                        rt.close();
                }catch (SQLException e){

                }
            }
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    protected ResultSet Select(String tabela, String coluna, String pesquisa) throws NaoTemConexaoException {
        if (connection != null){
            try{
                return Select(tabela, coluna, pesquisa, connection);

            } catch (ConexaoException e) {
                try {
                    connection.close();
                } catch (SQLException f) {

                } finally {
                    try {
                        criaCon(usuarioBanco);
                    } catch (ConexaoException a) {
                        throw new NaoTemConexaoException();
                    }
                }
            }
            return null;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por executar deletes que respeitem uma condição.
     * Irá executar um SQL do tipo "DELETE FROM 'tabela' WHERE 'condição';".
     * @Parâmetros: Recebe o nome da tabela e a condição para o delete.
     * @Retorna:
     * <ul>
     * <li>-2 caso um erro tenha acontecido e não tenha sido possível tratar ele
     * ou seja, não há um conexão estabelecida com o banco de dados;
     * <li>-1 caso um erro tenha acontecido e tenha sido possível tratar ele,
     * ou seja, deve-se chamar a função de novo;</li>
     * <li>0 caso nenhum delete tenha sido executado;
     * <li>Qualquer outro valor inteiro positivo correspondente à quantidade
     * de linhas deletadas da tabela.
     * </ul>
     */
    protected int delete(String tabela, String condicao) throws NaoTemConexaoException{
        if (connection != null) {
            try {
                int verifica = delete(tabela, condicao, connection);
                if (verifica == -1){
                    return -3;
                }
                return verifica;
            } catch (ConexaoException e) {
                try {
                    connection.close();
                } catch (SQLException f) {

                } finally {
                    try {
                        criaCon(usuarioBanco);
                    } catch (ConexaoException a) {
                        throw new NaoTemConexaoException();
                    }
                }
            }
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    protected void setUsuarioBanco(int usuarioBanco) {
        this.usuarioBanco = usuarioBanco;
    }
}
