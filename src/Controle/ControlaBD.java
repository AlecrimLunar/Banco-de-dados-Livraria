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
     * Função responsável por fazer o insert no banco de dados
     * quando se quer receber o id criado para essa nova inserção.
     * @param tabela a tabela onde o insert será feito.
     * @param infos as informações que serão inseridas na
     *              tabela.
     * @param atributos as colunas que receberão as infos na
     *                  tabela.
     * @param retornando a condição para retornar o valor desejado.
     * @param con a conexão com o banco
     * @return -1 caso a inserção não tenha acontecido.<br>
     * Qualquer outro inteiro positivo representado o que foi
     * pedido para que fosse retornado.
     * @throws SQLException
     */
    protected int InsertRetornando(String tabela, String infos,
                                   String atributos, String retornando, Connection con) throws SQLException{

        String consulta = "INSERT INTO " + tabela + " (" + atributos +
                ") VALUES (" + infos + ") " + retornando + ";";

        try (PreparedStatement st = con.prepareStatement(consulta);
            ResultSet rt = st.executeQuery()){

            if (rt.next())
                return rt.getInt("id_" + tabela);
            else
                return -1;

        }
    }

    /**
     * Função responsável pelos inserts no banco.
     * @param tabela a tabela onde o insert será feito.
     * @param infos as informações que serão inseridas na
     *              tabela.
     * @param atributos as colunas que receberão as infos na
     *                  tabela.
     * @param con a conexão com o banco de dados.
     * @return 0 caso a inserção não tenha acontecido.<br>
     * 1 caso o insert tenha sido bem-sucedido.
     * @throws SQLException
     */
    protected int Insert(String tabela, String infos,
                         String atributos, Connection con) throws SQLException{

        String consulta = "INSERT INTO " + tabela + " (" + atributos +
                ") VALUES (" + infos + ");";

        try (PreparedStatement st = con.prepareStatement(consulta)) {

            return st.executeUpdate();

        }
    }


    /**
     * Função responsável por executar qualquer SELECT desejado.
     * <P>Executa o SQL: SELECT 'coluna' FROM 'tabela' WHERE pesquisa.
     * @param tabela a tabela onde será executado o SELECT.
     * @param coluna as colunas que serão retornadas.
     * @param pesquisa a condição para o retorno.
     * @param con a conexão com o banco de dados.
     * @return Um ResultSet com o retornado do banco de dados.
     * @throws SQLException
     */
    protected ResultSet Select(String tabela, String coluna, String pesquisa,
                               Connection con) throws SQLException {

        String consulta = "SELECT " + coluna + " FROM " + tabela +
                " WHERE " + pesquisa + ";";

        try (PreparedStatement st = con.prepareStatement(consulta)){
            return st.executeQuery(consulta);

        }
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
    protected int update(String tabela, String mudancas, String condicao,
                         Connection con) throws SQLException{

        String consulta = "UPDATE " + tabela + " SET " + mudancas +
                " WHERE " + condicao + ";";

        try (PreparedStatement st = con.prepareStatement(consulta)) {


            return st.executeUpdate();

        }
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

}
