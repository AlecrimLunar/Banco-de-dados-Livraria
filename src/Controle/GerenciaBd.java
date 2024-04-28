package Controle;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe responsável por fazer as chamadas de ControlaBD.
 * <P>Nela são tratados os erros possíveis que possam ter acontecidos
 * e é gerenciada a conexão com o banco de dados.
 */
public abstract class GerenciaBd implements AutoCloseable{

    private HashMap<String, String> qualNomeTabelaBanco = null;
    private Connection connection;
    private int usuarioBanco;


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
    private int InsertRetornando(String tabela, String infos,
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
    private int Insert(String tabela, String infos,
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
    private ResultSet Select(String tabela, String coluna, String pesquisa,
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
    private int update(String tabela, String mudancas, String condicao,
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
    private int delete(String tabela,
                       String condicao, Connection con) throws SQLException{

        String consulta = "DELETE FROM " + tabela + " WHERE " + condicao + ";";
        try (PreparedStatement st = con.prepareStatement(consulta)){
            return st.executeUpdate();
        }
    }

    /**
     * Função responsável por montar SETs para o update.
     * <P>Recebe uma string de colunas que serão alteradas
     * e uma string com os novos valores que serão atribuídos
     * a essas colunas.
     * @param coluna as colunas que terão os valores alterados.
     *               Cada nome de coluna deve estar separado por
     *               um espaço.
     * @param novo oas novos valores que serão atribuídos a cada
     *             colua. Os valores devem estar separados por
     *             um espaço.
     * @return
     */
    private String montaConsultaUpdate(String coluna, String novo){
        /*
         * Primeiro separamos as colunas que serão alteradas
         * e os valores novos para cada uma dessas colunas.
         * Cada alteração possui um índice no arrayList e
         * cada coluna alterada está separada por um espaço.
         */
        String mudancas = "";
        String[]colunasAlteradas = coluna.split(" ");
        String[]valorColunasAlteradas = novo.split(" ");

        /*
         * Aqui fazemos a criação do SET do UPDATE.
         */
        for (int j = 0; j < colunasAlteradas.length; ++j) {
            if (j != coluna.length() - 1)
                mudancas += colunasAlteradas[j] + " = " + valorColunasAlteradas[j] + ", ";
            else
                mudancas += colunasAlteradas[j] + " = " + valorColunasAlteradas[j];
        }

        return mudancas;
    }

    /**
     * Cria uma condição de select, update ou delete conforme
     * as necessidades do banco.
     * @param condicao a condição inicial para a operação
     *                 no banco.
     * @param tabela a tabela onde será realizada a operação.
     * @return uma string contendo a nova operação.
     */
    private String montaCondicao(String condicao, String tabela){
        if (!tabela.equalsIgnoreCase("carrinho_livro") &&
                !tabela.equalsIgnoreCase("relatorio_venda")) {
            if (condicao.isEmpty())
                condicao = "id_" + tabela + " >= 0";
            else
                condicao = "id_" + tabela + " >= 0 AND " + condicao;

            return condicao;
        } else
            return condicao;
    }


    /**
     * Função responsável por criar na memória o HashMap
     * para pegar os nomes das tabelas no banco
     */
    private void criaQualNomeTabelaBanco(){
        qualNomeTabelaBanco.put("livro", "Estoque.livro");
        qualNomeTabelaBanco.put("vendedor", "Vendedores_Info.vendedor");
        qualNomeTabelaBanco.put("relatorio", "Vendedores_Info.relatorio");
        qualNomeTabelaBanco.put("relatorio_venda", "Vendedores_Info.relatorio_venda");
        qualNomeTabelaBanco.put("cliente", "Clientes_Info.cliente");
        qualNomeTabelaBanco.put("carrinho", "Clientes_Info.carrinho");
        qualNomeTabelaBanco.put("carrinho_livro", "Clientes_Info.carrinho_livro");
        qualNomeTabelaBanco.put("compra", "Compras_Info.compra");
    }

    /**
     * função responsável por criar as conexões com o banco de
     * dados sempre que necessário.
     * retorna o erro de não ter conexão caso ele não consiga criar
     * a conexão
     */
    protected void criaCon(int quem) throws SQLException{

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

        if (qualNomeTabelaBanco == null){
            criaQualNomeTabelaBanco();
        }
        try {
            connection = DriverManager.getConnection(dbURL, login, password);
        } finally {
            connection = null;
        }
    }

    /**
     * Função responsável pelos inserts onde o id do que foi
     * inserido deseja ser retornado.
     * @param tabela a tabela onde será inserido.
     * @param infos as informações que serão inseridas na
     *              tabela.
     * @param atributos as colunas que irão receber as
     *                  informações.
     * @return -1 caso algum erro tenha acontecido
     * e a função tenha conseguido lidar com ele.
     * Qualquer outro inteiro positivo ou zero
     * representado o valor retornado.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int InsertRetornando(String tabela, String infos, String atributos)
            throws NaoTemConexaoException, ConexaoException{
        if (connection != null) {
            try {
                String retornando = "RETURNING id_" + tabela;
                return InsertRetornando(qualNomeTabelaBanco.get(tabela),
                        infos, atributos, retornando, connection);

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                }
                try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f) {
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }

            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por realizar um ou mais inserts.
     * @param tabela a tabela onde os inserts serão realizados
     * @param infos as informações que serão inseridas. Para
     *              cada objeto do ArrayList, uma inserção
     *              será realizada.
     * @param atributos as colunas que receberão as informações
     *                  em infos.
     * @return -1 caso algum erro tenha acontecido e a função
     * tenha conseguido lidar com ele.<br>
     * Qualquer outro inteiro positivo ou zero representado o
     * número de inserts realizados.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int Insert(String tabela, ArrayList<String> infos,
                         String atributos) throws NaoTemConexaoException, ConexaoException{
        if (connection != null) {
            try {
                int funcionou = 0;
                connection.setAutoCommit(false);

                for (String s : infos) {
                    funcionou += Insert(qualNomeTabelaBanco.get(tabela),
                            s, atributos, connection);
                }
                connection.setAutoCommit(true);
                connection.commit();

                return funcionou;
            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }

            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por verificar se algo já existe
     * no banco de dados.
     * <P>Executa o SQL: SELECT * FROM 'tabela' WHERE 'coluna'
     * = 'condicao';.
     * @param tabela a tabela que será verificada.
     * @param coluna a coluna que será comparada.
     * @param condicao a condição que deve ser atingida.
     * @return -1 caso algum erro tenha acontecido e a função
     * tenha conseguido lidar com ele.<br>
     * 0 caso não exista nada com a condição especificada.
     * 1 caso exista pelo menos uma linha que atenda a
     * condição especificada.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int Existe(String tabela, String coluna, String condicao)
            throws NaoTemConexaoException, ConexaoException {

        if (connection != null){

            try (ResultSet rt = Select(qualNomeTabelaBanco.get(tabela), coluna,
                    condicao, connection)) {

                if (rt.next())
                    return 1;
                else
                    return 0;

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }

            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }


    /**
     * Função responsável por executar qualquer SELECT.
     * Executa o SQL: SELECT 'coluna' FROM 'tabela' WHERE 'pesquisa';.
     * @param tabela a tabela onde o SELECT será feito.
     * @param colunas as colunas que serão recebidas do SELECT.
     * @param condicao a condição para retorno das linhas.
     * @return -1 caso algum erro tenha acontecido e a
     * função tenha conseguido lidar com ele.<br>
     * Um ResultSet contendo as linhas retornadas
     * do SELECT.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected ResultSet Select(String colunas, String tabela, String condicao)
            throws NaoTemConexaoException, ConexaoException{
        if (connection != null){
            try{
                return Select(tabela, colunas, condicao, connection);
            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return null;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por verificar se o login inserido
     * pelo usuário existe no banco de dados.
     * @param user o nome do login inserido.
     * @param password a senha referente ao user.
     * @param tabela a tabela onde essas informações estão
     *               guardadas.
     * @return -1 caso algum erro tenha acontecido e a
     * função tenha conseguido lidar com ele. <br>
     * 0 caso não tenha sido encontrado o user no banco
     * de dados.<br>
     * 1 caso o user tenha sido encontrado e a senha
     * informada foi correta.<br>
     * 2 caso o user tenha sido encontrado e a senha
     * informada foi incorreta.<br>
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int login(String user, String password, String tabela)
            throws NaoTemConexaoException, ConexaoException {

        if (connection != null){
            String pesquisa = "usuario = " + user;
            try (ResultSet rt = Select(qualNomeTabelaBanco.get(tabela), "*",
                    pesquisa, connection)){

                if (rt.next()){
                    if (password.equalsIgnoreCase(rt.getString("senha")))
                        return 1;
                    else
                        return 2;
                }
                return 0;

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável pela realização dos updates. Pode realizar
     * um ou mais updates.
     * <P>Para que mais de um update seja realizdo basta adicionar
     * o mesmo número de objetos nos ArrayLists. Cada objeto
     * corresponderá a um update diferente.</P>
     * <P>Para que haja a atualização de diferentes colunas
     * dentro de um mesmo update basta que cada uma das colunas
     * estejam separadas por espaço dentro da string, e que para
     * cada uma das colunas haja um valor para a inserção.</P>
     * @param tabela a tabela onde os updates serão realizados.
     * @param coluna as colunas que serão atualizadas.
     * @param novo os novos valores que serão atribuídos às
     *             colunas.
     * @param condicao a condição para que a linha seja selecionada
     *                 para o update.
     * @return -1 caso algum erro tenha acontecido e a
     * função tenha conseguido lidar com ele. <br>
     * Qualquer outro valor inteiro positivo ou zero
     * representando a quantidade de updates realizados
     * no total.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int variosUpdates(String tabela, ArrayList<String> coluna,
                                ArrayList<String> novo, ArrayList<String> condicao)
            throws NaoTemConexaoException, ConexaoException{
        if (connection != null){
            try {
                connection.setAutoCommit(false);
                int totalQuantosUpdates = 0;

                for (int i = 0; i < condicao.size(); ++i) {
                    String mudancas = montaConsultaUpdate(coluna.get(i), novo.get(i));
                    String condicaoAtual = montaCondicao(condicao.get(i), tabela);

                    /*
                     * Realização do UPDATE
                     */
                    int quantosUpdates = update(qualNomeTabelaBanco.get(tabela),
                            mudancas, condicaoAtual, connection);

                    totalQuantosUpdates += quantosUpdates;
                }
                connection.setAutoCommit(true);
                connection.commit();

                return totalQuantosUpdates;

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por alterar a quantidade de livros
     * presentes em estoque após a solicitação de uma compra.
     * @param idLivro o id do livro que foi comprado
     * @param quantidade a quantidade de livros referente
     *                   àquele id que foi adquirida.
     * @return -1 caso algum erro tenha acontecido e a
     * função tenha conseguido lidar com ele. <br>
     * Qualquer outro valor inteiro positivo ou zero
     * representando a quantidade de updates realizados
     * no total.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int livroComprado(ArrayList<Integer> idLivro, ArrayList<Integer> quantidade)
            throws ConexaoException, NaoTemConexaoException{
        if (connection != null){
            try {
                connection.setAutoCommit(false);
                int totalUpdates = 0;

                for (int i = 0; i < idLivro.size(); ++i) {
                    String tabela = "Estoque.livro";
                    String mudancas = "id_livro = quantidade_estoque - " + quantidade.get(i);
                    String condicao = "id_livro = " + idLivro.get(i);

                    totalUpdates += update(tabela, mudancas, condicao, connection);
                }
                connection.setAutoCommit(true);
                connection.commit();

                return totalUpdates;

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por alterar a quantidade de livros
     * presentes em estoque quando livros forem recebidos
     * na livraria.
     * @param idLivro o id do livro que foi comprado
     * @param quantidade a quantidade de livros referente
     *                   àquele id que foi adquirida.
     * @return -1 caso algum erro tenha acontecido e a
     * função tenha conseguido lidar com ele. <br>
     * Qualquer outro valor inteiro positivo ou zero
     * representando a quantidade de updates realizados
     * no total.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int adicionaQuantidadeLivroEstoque
    (ArrayList<Integer> idLivro, ArrayList<Integer> quantidade)
            throws NaoTemConexaoException, ConexaoException {
        if (connection != null){
            try {
                connection.setAutoCommit(false);
                int totalUpdates = 0;

                for (int i = 0; i < idLivro.size(); ++i) {
                    String tabela = "Estoque.livro";
                    String mudancas = "id_livro = quantidade_estoque + " + quantidade.get(i);
                    String condicao = "id_livro = " + idLivro.get(i);

                    totalUpdates += update(tabela, mudancas, condicao, connection);
                }
                connection.setAutoCommit(true);
                connection.commit();

                return totalUpdates;

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    connection.rollback();
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por realizar deletes no banco
     * de dados.
     * @param tabela a tabela onde o delete será realizado.
     * @param condicao a condição para o delete ser realizado.
     * @return -1 caso algum erro tenha acontecido e a
     * função tenha conseguido lidar com ele. <br>
     * Qualquer outro valor inteiro positivo ou zero
     * representando a quantidade de deletes realizados
     * no total.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected int delete(String tabela, String condicao)
            throws NaoTemConexaoException, ConexaoException{
        if (connection != null) {
            try {

                condicao = montaCondicao(condicao, tabela);
                return delete(tabela, condicao, connection);

            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                } try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f){
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return -1;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por recuperar os livros de uma compra.
     * @param idCompra o id da compra para recuperar os livros feitos nela.
     * @return Null caso algum erro tenha acontecido e a função tenha conseguido
     * lidar com ele.<br>
     * Um ResultSet contendo os livros feitos na determinada compra.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */

    protected ResultSet recuperaLivrosCompras(int idCompra)
            throws NaoTemConexaoException, ConexaoException {
        if (connection != null) {
            String tabela = "Estoque.livro AS L INNER JOIN " +
                    "Clientes_Info.carrinho_livro AS C " +
                    "ON L.id_livro = C.id_livro";
            String pesquisa = "C.id_carrinho = " + idCompra;

            try {
                return Select("*", tabela, pesquisa, connection);
            } catch (SQLException e) {
                /*
                 * Se der erro, vai tentar fechar a conexão atual.
                 */
                try {
                    close();

                } catch (SQLException f) {
                    /*
                     * Se não conseguir ele informa
                     * a quem o chamou que não foi possível
                     * encerrar a conexão com o banco e que
                     * ela é uma conexão defeituosa.
                     */
                    throw new ConexaoException();
                }
                try {
                    /*
                     * O sistema tentar criar outra conexão.
                     */

                    criaCon(usuarioBanco);
                } catch (SQLException f) {
                    /*
                     * Caso ele não consiga, é necessário avisar
                     * que existe um grave problema: não existe conexão
                     * com o banco de dados.
                     */
                    throw new NaoTemConexaoException();
                }
            }
            /*
             * Caso tenha sido possível resolver os erros, a função avisa que ela
             * teve um comportamento inesperado e foi possível resolver ele,
             * por isso ela pode ser chamada novamente.
             */
            return null;
        }
        throw new NaoTemConexaoException();
    }

    /**
     * Função responsável por pegar todas as compras que ainda não
     * foram confirmadas por um vendedor.
     * @return Null caso algum erro tenha acontecido e a função tenha conseguido
     * lidar com ele.<br>
     * Um ResultSet contendo todas as compras ainda não confirmadas
     * por um vendedor.
     * @throws NaoTemConexaoException quando não há
     * nenhuma conexão com o banco de dados.
     * @throws ConexaoException quando a conexão com
     * o banco de dados existente apresentou algum
     * problema mas não foi possível fechá-la.
     */
    protected ResultSet recuperaComprasNaoConfirmadas()
            throws NaoTemConexaoException, ConexaoException {
        String tabela = "Compras_Info.compra";
        String coluna = "forma_pagamento, data, valor";

        try {
            return Select(coluna, tabela, "id_vendedor = -1 AND id_compra >= 0",
                    connection);
        } catch (SQLException e) {
            /*
             * Se der erro, vai tentar fechar a conexão atual.
             */
            try {
                close();

            } catch (SQLException f) {
                /*
                 * Se não conseguir ele informa
                 * a quem o chamou que não foi possível
                 * encerrar a conexão com o banco e que
                 * ela é uma conexão defeituosa.
                 */
                throw new ConexaoException();
            }
            try {
                /*
                 * O sistema tentar criar outra conexão.
                 */

                criaCon(usuarioBanco);
            } catch (SQLException f) {
                /*
                 * Caso ele não consiga, é necessário avisar
                 * que existe um grave problema: não existe conexão
                 * com o banco de dados.
                 */
                throw new NaoTemConexaoException();
            }
        }
        /*
         * Caso tenha sido possível resolver os erros, a função avisa que ela
         * teve um comportamento inesperado e foi possível resolver ele,
         * por isso ela pode ser chamada novamente.
         */
        return null;
    }

    protected void setUsuarioBanco(int usuarioBanco) {
        this.usuarioBanco = usuarioBanco;
    }

    @Override
    public void close() throws SQLException{
        connection.close();
    }
}