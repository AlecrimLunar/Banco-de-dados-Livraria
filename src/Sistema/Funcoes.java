package Sistema;

import Controle.GerenciaBd;
import Entities.*;
import Exceptions.ConexaoException;
import Exceptions.NaoTemConexaoException;
import Users.Cliente;
import Users.DonoLivraria;
import Users.Vendedor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funcoes extends GerenciaBd {

    private final int qualCon = 0;

    public Funcoes(int quem){
        //setUsuario
        try {
            criaCon(quem);
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
    }

    //=============================================================Destaques=============================================================
    /**
     * Esta função recupera os livros chamados 'Destaques', apenas os mais vendidos da livraria.
     *
     * @param quem aquele conecta ao banco.
     * @return uma lista ligada de objetos Livro representando os livros destacados
     */
    public LinkedList<Livro> Destaques(int quem) {

        LinkedList<Livro> destaques;

        try {
            ResultSet rt = Destaques();
            destaques = RecuperaLivro(rt, false);

            return destaques;
        } catch (SQLException e) {
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        } catch (ConexaoException e) {
            trataException(e, qualCon);
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        } catch (NaoTemConexaoException e){
            trataException(e, qualCon);
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        }
    }

    /**
     * Esta função recebe uma lista de livros destaque e imprime esses livros em uma tabela formatada.
     * A tabela possui três colunas, cada uma exibindo o nome de um livro destaque, e uma quarta coluna
     * mostrando o preço de cada livro.
     * Caso o nome de um livro destaque seja muito longo para caber na tabela, a função
     * divide o nome em duas partes e exibe essas partes em linhas separadas.
     *
     * @param livros uma lista de livros destaque
     * @return uma string contendo a tabela formatada de livros destaque.
     */
    public String PrintDestaques(LinkedList<Livro> livros){
        if(livros == null ||livros.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("+=====================1=====================+=====================2=====================+=====================3=====================+\n");

        if (livros == null)
            return "";

        Livro aux = livros.get(0);
        ArrayList<String> nome1 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome1 = DividirNoMeio(aux.getNome());
        } else {
            nome1.add(aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome1.get(0)));

        aux = livros.get(1);
        ArrayList<String> nome2 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome2 = DividirNoMeio(aux.getNome());
        } else {
            nome2.add(aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome2.get(0)));

        aux = livros.get(2);
        ArrayList<String> nome3 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome3 = DividirNoMeio(aux.getNome());
        } else {
            nome3.add(aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome3.get(0)) + "|\n");

        if(nome1.size() > 1){
            sb.append("|" + AdicionaEspacos(nome1.get(1)));
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getAutor()));
        }

        if(nome2.size() > 1){
            sb.append("|" + AdicionaEspacos(nome2.get(1)));
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getAutor()));
        }

        if(nome3.size() > 1){
            sb.append("|" + AdicionaEspacos(nome3.get(1)));
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|\n");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getAutor()));
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getTipo()));
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getAutor()));
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getTipo()));
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getAutor()));
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|\n");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getTipo()));
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()));
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getTipo()));
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()));
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getTipo()));
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|\n");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()));
        } else {
            sb.append("|" + " ".repeat(43));
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()));
        } else {
            sb.append("|" + " ".repeat(43));
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|\n");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        sb.append("\n+===========================================+===========================================+===========================================+\n");
        sb.append("|" + AdicionaEspacos("R$ " + String.format("%.2f", livros.get(0).getPreco())) + "|" + AdicionaEspacos("R$ " +
                String.format("%.2f", livros.get(1).getPreco())) + "|" +
                AdicionaEspacos("R$ " + String.format("%.2f", livros.get(2).getPreco())) + "|\n");
        sb.append("+===========================================+===========================================+===========================================+\n");

        return sb.toString();
    }

    /**
     * Dividir um nome em dois caso seja maior que 43 caracteres, deixando um espaço no meio.
     *
     * @param nome o nome que será dividido.
     * @return uma lista de strings, onde a primeira string é a parte esquerda do nome e a
     * segunda string é a parte direita do nome, com um espaço no meio.
     */
    public static ArrayList<String> DividirNoMeio(String nome){
        ArrayList<String> nomeA = new ArrayList<>();
        int auxI = nome.split(" ").length/2;
        int indiceEncontrado = -1;
        int contadorEspacos = 0;

        for (int i = 0; i < nome.length(); i++) {
            char caractere = nome.charAt(i);
            if (caractere == ' ') {
                contadorEspacos++;
                if (contadorEspacos == auxI + 1) {
                    indiceEncontrado = i;
                    break;
                }
            }
        }
        nomeA.set(0 , nome.substring(0, indiceEncontrado).trim());
        nomeA.set(1, nome.substring(indiceEncontrado + 1).trim());

        return nomeA;
    }

    /**
     * Adiciona espaços a um nome para que ele fique alinhado no centro de uma tabela.
     *
     * @param nome o nome que será alinhado
     * @return o nome com espaços adicionados
     */
    public static String AdicionaEspacos(String nome){
        int espEsq = (43 - nome.length())/2, espDir = (43-nome.length()-espEsq);

        return " ".repeat(espEsq) +
                nome +
                " ".repeat(espDir);
    }

    //=============================================================Vendedor=============================================================
    /**
     * Recupera um vendedor pelo seu usuário e senha.
     *
     * @param user  O usuário do vendedor.
     * @param senha  A senha do vendedor.
     * @return  Um objeto Vendedor caso a consulta seja bem-sucedida, caso contrário, retorna null.
     */
    public Vendedor recuperaVendedorF(String user, String senha, int quem){
        try {
            criaCon(2);
            ResultSet rt = recuperaVendedor("'" + user + "'", "'" + senha + "'");

            rt.next();
            return new Vendedor(rt.getInt("id_vendedor"), rt.getString("nome"), rt.getString("cpf"));
        } catch (NaoTemConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (ConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        }
    }


    //=============================================================Cliente=============================================================
    /**
     * Este método recupera um cliente do banco de dados com base no seu usuário e senha.
     * Ele também recebe um carrinho como parâmetro, que é utilizado para definir o carrinho do cliente.
     *
     * @param user O usuário do cliente.
     * @param senha A senha do cliente.
     * @param carrinho O carrinho do cliente.
     * @return Um novo objeto do tipo Cliente com os dados recuperados do banco de dados.
     * */
    public Cliente recuperaCliente(String user, String senha, Carrinho carrinho, int quem) {
        try {
            ResultSet rt = recuperaCliente("'" + user + "'", "'" + senha + "'");
            assert rt != null;
            if (rt.next()) {
                return new Cliente(rt.getString("nome"), rt.getString("cpf"), rt.getString("rua"),
                        rt.getInt("numero"), rt.getString("email"), rt.getBoolean("one_piece"), rt.getBoolean("is_flamengo"),
                        rt.getBoolean("is_sousa"), rt.getString("usuario"), rt.getString("senha"), carrinho, rt.getInt("id_cliente"));
            }

        } catch (SQLException e) {
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        } catch (ConexaoException e) {
            trataException(e, qualCon);
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        } catch (NaoTemConexaoException e){
            trataException(e, qualCon);
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        }
        return null;
    }

    //=============================================================Dono=============================================================

    public DonoLivraria recuperaDono(String user, String senha, int quem) {
        setUsuarioBanco(2);
        try{
            criaCon(2);
            ResultSet rt = recuperaDono(user, senha);
            assert rt != null;

            if (rt.next())
                return new DonoLivraria(rt.getString("nome"), rt.getString("cpf"));

        } catch (NaoTemConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (ConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;

        }
        return null;
    }
    //=============================================================Livro=============================================================
    /**
     * Recebe um Scanner e um Carrinho, e retorna o Carrinho após realizar uma pesquisa por nome, autor ou gênero.
     *
     * @param sc um Scanner que receberá as entradas do usuário
     * @param carrinho um Carrinho que será modificado com base nas entradas do usuário
     * @param quem um inteiro que representa a conexão com o banco para o tratamento de erros
     * @return um Carrinho modificado com base nas entradas do usuário
     */
    public Carrinho Pesquisa(Scanner sc, Carrinho carrinho, int quem) {
        LinkedList<Livro> l = new LinkedList<>();
        int aux;
        while(true) {

            System.out.print("""
                    Pesquisar por:
                    1 - Nome
                    2 - Autor
                    3 - Genero
                    0 - Sair
                    """);
            while(true) {

                String resp = sc.nextLine();

                if(regexNum(resp)) {
                    aux = Integer.parseInt(resp);
                    break;
                }
                else
                    System.out.println("Digite apenas números!");
            }

            switch (aux) {
                case 1 -> {
                    System.out.print("Digite o nome do livro: ");
                    String str = sc.nextLine();

                    l = PesquisaLivroF(str, "nome", quem);
                }
                case 2 -> {
                    System.out.print("Digite o nome do autor: ");
                    String str = sc.nextLine();

                    l = PesquisaLivroF(str, "autor", quem);
                }
                case 3 -> {
                    System.out.print("Digite o gênero do livro: ");
                    String str = sc.nextLine();

                    l = PesquisaLivroF(str, "genero", quem);
                }
                case 0 -> {return carrinho;}
                default -> System.out.println("Opção inválida");
            }

            if(!l.isEmpty()){
                PrintLivro(l);

                System.out.print("""
                        Deseja adicionar algum ao carrinho?
                        (Digite o número que se encontra antes do nome do livro caso deseje sair digite '0')
                        """);

                while(true) {
                    String resp = sc.nextLine();

                    if(regexNum(resp)) {
                        aux = Integer.parseInt(resp);
                        break;
                    }
                    else
                        System.out.println("Digite apenas números!");
                }

                while(true) {
                    if (aux > 0 && aux <= l.size()) {
                        if(l.get(aux-1).getQuantidade() == 0){
                            System.out.println("Livro indisponível");
                        } else {
                            carrinho.setLivro(l.get(aux - 1));
                            l.get(aux - 1).setQuantidade();
                            System.out.print("Livro adicionado ao carrinho!\n");
                        }
                        System.out.print("""
                                    Deseja adicionar outro?
                                    (Digite o número que se encontra antes do nome do livro caso deseje sair digite '0')
                                    """);
                        while (true) {
                            String resp = sc.nextLine();

                            if (regexNum(resp)) {
                                aux = Integer.parseInt(resp);
                                break;
                            } else
                                System.out.println("Digite apenas números!");
                        }
                    } else if (aux > l.size()) {
                        System.out.println("Livro inválido");
                    } else if(aux == 0){
                        break;
                    }
                }
            }
        }
    }


    /**
     * Recebe um ResultSet da tabela livros e coloca todos os livros retornados em uma LinkedList de livros
     * e a retorna.
     * @param rt o ResultSet que contém os dados dos livros
     * @param carrinho um booleano que indica se os livros serão adicionados ao carrinho
     * @return uma LinkedList de Livros
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados
     * **/
    public static LinkedList<Livro> RecuperaLivro(ResultSet rt, boolean carrinho) throws SQLException{
        if (rt == null)
            return null;

        LinkedList<Livro> l = new LinkedList<>();
        if(rt == null){
            return new LinkedList<>();
        }
        try{

            while(rt.next()) {
                Livro aux = new Livro(rt.getInt("id_livro"), rt.getString("nome"),
                        Double.parseDouble(rt.getString("preco").substring(3).replaceAll(",", ".")),
                        rt.getString("autor"), rt.getString("genero"), rt.getString("tipo"),
                        rt.getBoolean("from_mari"), rt.getInt("quantidade_estoque"));
                if(carrinho){
                    for(int i = 1; i <= rt.getInt("qunatidade"); i++){
                        l.add(aux);
                    }
                } else {
                    l.add(aux);
                }
            }

        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro SQL
                             ========================================================
                             """);
            return null;
        }

        return l;
    }

    /**
     * Método responsável por pesquisar um livro no banco de dados.
     *
     * @param str O valor a ser pesquisado.
     * @param coluna O atributo a ser pesquisado.
     * @param quem um inteiro que representa a conexão com o banco para o tratamento de erros
     * @return Um LinkedList de Livros caso encontre algum, caso contrário retorna null.
     */
    public LinkedList<Livro> PesquisaLivroF(String str, String coluna, int quem) {
        LinkedList<Livro> l = new LinkedList<>();

        try{

            int aux = Existe("livro", "*", coluna + " LIKE '%" + str + "%'");

            if(aux > 0){
                ResultSet rt = pesquisaLivro(str, coluna);

                assert rt != null;
                l = RecuperaLivro(rt, false);
            } else if(aux == 0) {
                System.out.print("Não temos nenhum livro com esse " + coluna + "!\n\n");
            } else if(aux == -1){
                System.out.print("Erro com a pesquisa tente novamente com um atributo diferente!\n\n");
            }

        } catch (NaoTemConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (ConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        }

        return l;
    }

    /**
     * Método responsável por imprimir os detalhes de cada livro no carrinho.
     * @param l lista de livros.
     */
    public static void PrintLivro(LinkedList<Livro> l){
        int escolha = 1;
        for (Livro livro : l) {
            System.out.println("================================\n" +
                    escolha + " - " + livro.toString() + "\nQuantidade no Estoque: " + livro.getQuantidade() + "\n");
            escolha++;
        }
        System.out.println("================================\n");
    }

    //=============================================================Carrinho=============================================================
    /**
     * Método responsável por gerenciar o carrinho de compras.
     * @param sc Scanner que receberá as entradas do usuário.
     * @param carrinho Carrinho que será manipulado.
     * @return Carrinho atualizado.
     */
    public Carrinho Carrinho(Scanner sc, Carrinho carrinho) {

        do{
            if(carrinho.isEmpty()){
                System.out.print("""
                                    ========================================================
                                    Seu carrinho está vazio!
                                    ========================================================
                                    """);
            } else {
                System.out.print("""
                                    ========================================================
                                    Seu carrinho:
                                    """);
                    System.out.print("================================\n" +
                            carrinho + "================================\n");
                    System.out.print("""
                            Deseja retirar algum do carrinho?
                            (Digite o número que se encontra antes do nome do livro para retirá-lo)
                            (Caso não queira alterar o carrinho digite '0')
                            """);
                    int aux;
                    while(true) {

                        String resp = sc.nextLine();

                        if(regexNum(resp)) {
                            aux = Integer.parseInt(resp);
                            break;
                        }
                        else
                            System.out.println("Digite apenas números!");
                    }
                    if (aux > 0 && aux <= carrinho.getLivros().size()) {
                        carrinho.removeLivro(aux - 1);
                        System.out.println("""
                                    Livro reirado do carrinho!
                                    """);
                    } else if (aux > carrinho.getLivros().size()) {
                        System.out.println("Livro inválido");
                    } else if(aux == 0){
                        break;
                    }

                System.out.print("========================================================\n");
            }
        }while(!carrinho.isEmpty());
        return carrinho;
    }

    /**
     * Método responsável por carregar um carrinho de compras a partir de um ‘ID’.
     * Ele irá buscar todos os livros que estão no carrinho e, caso consiga,
     * irá setar esses livros no carrinho.
     * @param idC ID do cliente no qual tem um carrinho compras a ser buscado.
     * @param quem Parâmetro utilizado para tratar de erros de conexão com o banco de dados.
     * @return Carrinho de compras preenchido com os livros encontrados. Caso não consiga, retorna null.
     */
    public Carrinho CarregaCarrinho(Carrinho carrinho, int idC, int quem) {

        try {

            ResultSet rt = carregaCarrinho(idC);

            if(rt.next()) {
                carrinho.setId(rt.getInt("id_carrinho"));
                LinkedList<Livro> l = RecuperaLivro(rt, true);

                assert l != null;
                carrinho.setCarrinho(l);
            } else {
                return new Carrinho();
            }

        } catch (NaoTemConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return new Carrinho();
        } catch (ConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return new Carrinho();
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return new Carrinho();
        }

        return carrinho;
    }

    /*public void InsertCarrinho(Carrinho carrinho, int idC){

        Insert();
    }*/

    //=============================================================Compras=============================================================
    /**
     * Método responsável por finalizar a compra do cliente.
     * Ele recebe como parâmetros o Scanner, o carrinho de compras, um array de booleanos
     * indicando as desconto aplicadas, o id do cliente.
     * O método imprime o valor total da compra e solicita ao usuário qual forma de pagamento ele deseja utilizar.
     * Depois, ele chama o método {@link #SolicitarCompra(int, double, int, int, int, Carrinho)} com o tipo de pagamento escolhido,
     * o valor total da compra, o id do cliente, o id do carrinho e o id do cliente para finalizar a compra.
     * @param sc Scanner utilizado para ler as entradas do usuário
     * @param carrinho Carrinho de compras que contém os livros selecionados pelo cliente
     * @param desc Array de booleanos indicando as desconto aplicadas (assiste One Piece, torce pro Flamengo, é de Sousa)
     * @param idCliente Id do cliente que está realizando a compra
     * @param quem um inteiro que representa a conexão com o banco para o tratamento de erros
     */
    public void Compra(Scanner sc, Carrinho carrinho, boolean[] desc, int idCliente, int quem) {
        double precoT = 0, precoD;

        System.out.print("""
                        ================================================
                        FINALIZAR COMPRA
                        ================================================
                        """
                );
        System.out.println(carrinho);

        for(Livro l : carrinho.getLivros()){
            precoT += l.getPreco();
        }

        if(desc[0] || desc[1] || desc[2]){

            String des = desc[0] ? "assiste One Piece" : desc[1] ? "torce pro Flamengo" : "é de Sousa";
            precoD = precoT/0.7;
            System.out.println("Preço total: R$" + String.format("%.2f", precoT) +
                    "\nComo você " + des + "você recebe um desconto de 30%!\n" +
                    "Preço atual: R$" + String.format("%.2f", precoD)
            );

        } else {
            precoD = precoT;
            System.out.print("Preço total: R$" + String.format("%.2f", precoD));
        }

        System.out.print("""

                ================================================
                O pagamento será feito por meio de:
                1 - Berries
                2 - Pix
                3 - Boleto
                4 - Cartão
                0 - cancelar
                """);

        int a = Integer.parseInt(sc.nextLine());

        switch (a) {
            case 1 -> SolicitarCompra(1, precoD, idCliente, carrinho.getId(), quem, carrinho);
            case 2 -> SolicitarCompra(2, precoD, idCliente, carrinho.getId(), quem, carrinho);
            case 3 -> SolicitarCompra(3, precoD, idCliente, carrinho.getId(), quem, carrinho);
            case 4 -> SolicitarCompra(4, precoD, idCliente, carrinho.getId(), quem, carrinho);
            case 0 -> System.out.println("COMPRA CANCELADA");
            default -> System.out.println("Opção inválida");
        }

    }

    /**
     * Método responsável por solicitar a compra no banco de dados.
     * @param tipoP 1 - Berries, 2 - Pix, 3 - Boleto, 4 - Cartão
     * @param precoT valor total da compra
     * @param id_cliente id do cliente
     * @param id_carrinho id do carrinho
     * @param quem um inteiro que representa a conexão com o banco para o tratamento de erros
     */
    public void SolicitarCompra(int tipoP, double precoT, int id_cliente, int id_carrinho, int quem, Carrinho carrinho) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formaP = tipoP == 1 ? "Berries" : tipoP == 2 ? "Pix" : tipoP == 3 ? "Boleto" : "Cartão";
        try {
            String tabela = "compra";
            String infos = "forma_pagamento, data, valor, id_carrinho, id_cliente";
            String atributos = "'" + formaP + "', date('" + sdf.format(date) + "'), " + ((int)precoT) + ", " + id_carrinho + ", " + id_cliente;

            int id = InsertCompra(tabela, atributos, infos);

            tabela = "carrinho";
            infos = "id_cliente, id_compra";
            atributos = id_cliente + ", " + id;
            int idC = InsertRetornando(tabela, atributos, infos);

            LinkedList<Integer> aux = new LinkedList<>();
            for(Livro l : carrinho.getLivros()) {
                if(!aux.contains(l.getId())) {
                    tabela = "carrinho_livro";
                    infos = "id_carrinho, id_livro, quantidade";
                    atributos = idC + ", " + l.getId() + ", " + carrinho.getQuantidade(l.getId());

                    InsertRetornando(tabela, atributos, infos);
                }

                aux.add(l.getId());
            }

            criaCon(2);
            ArrayList<String> coluna = new ArrayList<>();
            ArrayList<String> novoU = new ArrayList<>();
            ArrayList<String> condicao = new ArrayList<>();
            coluna.add("id_carrinho");
            novoU.add(idC + "");
            condicao.add("id_compra = " + id);
            variosUpdates("compra", coluna, novoU, condicao);
            criaCon(0);


            if(id > 0){
                System.out.println("================================================\n" +
                        "COMPRA FINALIZADA\n" +
                        "================================================\n" +
                        "O seu pedido está em processamento! Logo logo será aprovada, não se preocupe!\n" +
                        "Número do pedido: " + id +
                        "\n================================================\n");
            } else if(id == 0){
                System.out.print("Erro no cadastro da compra");
            }
        } catch (NaoTemConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (ConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método responsável por mostrar os pedidos do usuário.
     * @param sc Scanner para ler a entrada do usuário
     * @param id Id do pedido que deseja ver
     * @param quem Um inteiro que representa a conexão com o banco para o tratamento de erros
     */
    public void verPedidos(Scanner sc, int id, int quem) {
        try{
            ResultSet rt = recuperaCompra(id);
            LinkedList<Compra> compras = recuperaCompra(rt);

            if(!compras.isEmpty()) {
                while(true) {
                System.out.print("""
                        ========================================================
                        PEDIDOS
                        ========================================================
                        """);
                    printCompras(compras);


                    System.out.println("""
                            ========================================================
                            Caso deseje ver mais detalhes digite o numero do pedido
                            (Se deseja sair digite 0)
                            """);
                    int a = Integer.parseInt(sc.nextLine());


                    if (a > 0 && a <= compras.size()) {
                        ResultSet rts = recuperaLivrosCompras(compras.get(a-1).getId_carrinho());
                        LinkedList<Livro> livros = RecuperaLivro(rts, false);
                        System.out.print("========================================================\n" +
                                "Pedido - " + compras.get(a-1).getIdCompra() + "\n");

                        assert livros != null;

                        PrintLivro(livros);
                        System.out.print("""
                                ========================================================
                                Para continuar pressione ENTER;
                                """);
                        sc.nextLine();
                    } else if (a > compras.size()) {
                        System.out.println("compra inválido");
                    } else if(a == 0){
                        break;
                    }

                }

            } else {
                System.out.println("""
                        ========================================================
                        NENHUM PEDIDO ENCONTRADO!
                        ========================================================
                        """);
            }
        } catch (NaoTemConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (ConexaoException e) {
            trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
    }

    /**
     * Método responsável por printar as compras do usuário.
     */
    public void printCompras(LinkedList<Compra> c){

        int contador = 1;
        for(Compra c1 : c){
            System.out.print(contador + " - " + c1.toString() + "\nStatus: ");

            if(c1.getIdVendedor() == -1) {
                System.out.println("Em processamento.\n");
            } else {
                System.out.println("Aprovado.\n");
            }
            contador++;
        }

    }

    /**
     * Método responsável por recuperar as compras do usuário.
     * @param rt ResultSet contendo as informações das compras.
     * @return LinkedList de Compra, contendo todas as compras recuperadas.
     */
    public LinkedList<Compra> recuperaCompra(ResultSet rt) {
        LinkedList<Compra> compras = new LinkedList<>();

        try{
            if(rt == null){
                return new LinkedList<>();
            }
            while(rt.next()){
                Compra c = new Compra(rt.getInt("id_compra"), rt.getString("forma_pagamento"),
                        rt.getDate("data"), rt.getInt("valor"), rt.getInt("id_Carrinho"),
                        rt.getInt("Id_vendedor"));
                compras.add(c);
            }

        } catch (SQLException e) {
            System.out.print("""
                    ========================================================
                    Não foi possível receber as informações do banco de
                    dados devido a problemas com a conexão.
                    Tente novamente mais tarde.
                    ========================================================
                    """);
            return null;
        }

        return compras;
    }

    //=============================================================Regex=============================================================
    /* esse método irá verificar se alguma palavra reservada
     * do SQL está sendo utilizada */
    public boolean verificaComandoSQL(String s){
        Pattern verificaComandoSQL = Pattern.compile(" *drop +| *insert +| *" +
                "grant +| *update +| *delete +| *select +| *create +| *" +
                "alter +| *union +", Pattern.CASE_INSENSITIVE);
        Matcher matcher = verificaComandoSQL.matcher(s);

        if (matcher.find()){
            System.out.println("PALAVRA NÃO PERMITIDA UTILIZADA! TENTE NOVAMENTE");
            return false;
        }
        return true;
    }
    public boolean regexEmail(String s){
        //verifica se a string possui alguma palavra reservada sql
        if (verificaComandoSQL(s)) {
            //verifica se o email está dentro do padrão de emails
            if (Pattern.matches("[a-zA-Z0-9.ÀÃÂÁÇÉÊÍÎÓÔÕÚÛÜàãâáçéêíîóôõúûü\\-_*$#&%()/+]+@(gmail.com|" +
                    "yahoo.com.br|hotmail.com)", s))
                return true;
            else {
                System.out.println("O EMAIL NÃO ESTÁ NO FORMATO SUPORTADO! TENTE NOVAMENTE\n" +
                        "São suportados apenas: 'gmail.com', 'yahoo.com.br', " +
                        "'hotmail.com'");
                return false;
            }
        }
        return false;
    }
    public boolean regexCPF(String s){
        if (Pattern.matches("[0-9]{11}", s))
            return true;
        else {
            System.out.println("INSIRA APENAS 11 NÚMEROS!");
            return false;
        }
    }

    public boolean regexNome(String s) {
        if (verificaComandoSQL(s)) {
            return Pattern.matches("[A-Za-zÀÃÂÁÇÉÊÍÎÓÔÕÚÛÜàãâáçéêíîóôõúûü ]+", s);
        }
        return false;
    }

    public boolean regexNum(String s){
        return Pattern.matches("[0-9]+", s);
    }
    public boolean regexPreco(String s){
        return Pattern.matches("[0-9.]+", s);
    }

    //=============================================================Tratamento de Erro=============================================================
    /**
     * Função responsável por tratar a excessão se ter uma
     * conexão com o banco de dados que está com problemas.
     * Ela irá tentar fechar ela e depois iniciar uma nova
     * conexão.
     * <P>Caso não consiga, irá encerrar o programa.</P>
     * @param conexaoException
     */
    public boolean trataException (ConexaoException conexaoException, int quem){
        try {
            close();
        } catch (SQLException f) {
            System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            A CONEXÃO COM O BANCO DE DADOS APRESENTOU ERROS E
                            NÃO FOI POSSÍVEL ENCERRÁ-LA
                            ========================================================
                            """);
            System.exit(1);
        }
        try {
            criaCon(quem);
        } catch (SQLException f) {
            System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                            DE DADOS
                            ========================================================
                            """);
            System.exit(1);
        }
        return true;
    }

    /**
     * Função responsável por tratar a excessão de não ter uma conexão
     * com o banco de dados. Ela irá tentar iniciar uma nova conexão.
     * <P>Caso não consiga, irá encerrar o programa.</P>
     * @param naoTemConexaoException
     */
    public boolean trataException (NaoTemConexaoException naoTemConexaoException, int quem){
        try {
            criaCon(quem);
        } catch (SQLException f) {
            System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                            DE DADOS
                            ========================================================
                            """);
            System.exit(1);
        }
        return true;
    }

    public static void printa(ResultSet rt){
        try{
            ResultSetMetaData rtMetaData = rt.getMetaData();
            int numeroDeColunas = rtMetaData.getColumnCount();

            while (rt.next()) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]\n");
                for (int coluna = 1; coluna <= numeroDeColunas; coluna++) {
                    String nomeDaColuna = rtMetaData.getColumnName(coluna);
                    joiner.add(nomeDaColuna + " = " + rt.getString(coluna));
                }
                System.out.print(joiner);
            }

        } catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }
}