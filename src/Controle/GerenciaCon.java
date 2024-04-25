package Controle;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe responsável por fazer as chamadas de ControlaBD.
 * <P>Nela são tratados os erros possíveis que possam ter acontecidos
 * e é gerenciada a conexão com o banco de dados.
 */
public abstract class GerenciaCon extends ControlaBD implements AutoCloseable{

    private HashMap<String, String> qualNomeTabelaBanco = null;
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

        if (qualNomeTabelaBanco == null){
            criaQualNomeTabelaBanco();
        }
        try {
            connection = DriverManager.getConnection(dbURL, login, password);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            throw new ConexaoException();
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
                } catch (ConexaoException f) {
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
                } catch (ConexaoException f){
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
                } catch (ConexaoException f){
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
     * @param coluna as colunas que serão recebidas do SELECT.
     * @param pesquisa a condição para retorno das linhas.
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
    protected ResultSet Select(String tabela, String coluna, String pesquisa)
            throws NaoTemConexaoException, ConexaoException {
        if (connection != null){
            pesquisa = montaCondicao(pesquisa, tabela);
            try{
                return Select(qualNomeTabelaBanco.get(tabela), coluna, pesquisa, connection);

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
                } catch (ConexaoException f){
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
                } catch (ConexaoException f){
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
                } catch (ConexaoException f){
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
                } catch (ConexaoException f){
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

    protected void setUsuarioBanco(int usuarioBanco) {
        this.usuarioBanco = usuarioBanco;
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

    @Override
    public void close() throws SQLException{
        connection.close();
    }
}
