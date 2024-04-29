package Controle;

import Entities.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Funcoes extends GerenciaBd {

    public static void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    /****/
    public LinkedList<Livro> Destaques(int quem) {
        setUsuarioBanco(quem);
        LinkedList<Livro> destaques = new LinkedList<>();
        String coluna = "l.*";
        String tabela = "estoque.livro as l";
        String pesquisa = "WHERE l.id_livro >= 0 AND l.quantidade_estoque > 0 AND l.id_livro IN " +
                "(SELECT cl.id_livro FROM clientes_info.carrinho_livro as cl GROUP BY cl.id_livro ORDER BY COUNT(*) DESC LIMIT 3)";
        try {
        ResultSet rt = Select(tabela, coluna, pesquisa);

        destaques = RecuperaLivro(rt, false);

        } catch (SQLException | NaoTemConexaoException e) {
            e.printStackTrace();
        } catch (ConexaoException e) {
            throw new RuntimeException(e);
        }
        return destaques;
    }

    public String PrintDestaques(LinkedList<Livro> livros){
        StringBuilder sb = new StringBuilder();
        sb.append("+=====================1=====================+=====================2=====================+=====================3=====================+\n");

        Livro aux = livros.get(0);
        ArrayList<String> nome1 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome1 = DividirNoMeio(aux.getNome());
        } else {
            nome1.set(0, aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome1.get(0)) + "|");

        aux = livros.get(1);
        ArrayList<String> nome2 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome2 = DividirNoMeio(aux.getNome());
        } else {
            nome2.set(0, aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome2.get(0)) + "|");

        aux = livros.get(2);
        ArrayList<String> nome3 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome3 = DividirNoMeio(aux.getNome());
        } else {
            nome3.set(0, aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome3.get(0)) + "|\n");

        if(nome1.size() > 1){
            sb.append("|" + AdicionaEspacos(nome1.get(1)) + "|");
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        }

        if(nome2.size() > 1){
            sb.append("|" + AdicionaEspacos(nome2.get(1)) + "|");
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        }

        if(nome3.size() > 1){
            sb.append("|" + AdicionaEspacos(nome3.get(1)) + "|");
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|\n");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        sb.append("+===========================================+===========================================+===========================================+\n");
        sb.append("|" + AdicionaEspacos("R$ " + String.format("%.2f", livros.get(0).getPreco())) + "|" + AdicionaEspacos("R$ " +
                String.format("%.2f", livros.get(1).getPreco())) + "|" +
                AdicionaEspacos("R$ " + String.format("%.2f", livros.get(2).getPreco())) + "|\n");
        sb.append("+===========================================+===========================================+===========================================+\n");

        return sb.toString();
    }

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
    public static String AdicionaEspacos(String nome){
        int espEsq = (43 - nome.length()/2), espDir = (43-nome.length()-espEsq);

        return " ".repeat(espEsq) +
                nome +
                " ".repeat(espDir);
    }

    public LinkedList<Livro> PesquisaLivro(String str, String coluna) throws SQLException, NaoTemConexaoException, ConexaoException {
        LinkedList<Livro> l = new LinkedList<>();

        /*
         * Esse existe tem que ser feito com o 'LIKE', se não dá pau. Serve para o mano pesquisar o nome do livro sem ser o
         * nome completo como "Harry Potter" aí vai aparecer todos os Harry Potter */
        int aux = Existe("estoque.livro as l", "l." + coluna, str);

        if(aux > 0){
            /*
            * Essa pesquisa ta beeeeem livre, ela vai pegar a coluna de l, recebendo somente as entradas "nome", "autor", "genero"
            * e vai usar um LIKE pra achar os livros com esse atributo.
            *  */
            ResultSet rt = Select("estoque.livro as l", "l." + coluna, " WHERE l." + coluna + " LIKE '%" + str +
                    "%' AND l.quantidade_estoque > 0");

            assert rt != null;
            l = RecuperaLivro(rt, false);
        } else if(aux == 0) {
            System.out.print("Não temos nenhum livro com esse " + coluna + "!\n\n");
        } else if(aux == -1){
            System.out.print("Erro com a pesquisa tente novamente com um atributo diferente!\n\n");
        }

        return l;
    }

    /**
     * Recebe um ResultSet da tabela livros e coloca todos os livros retornados em uma LinkedList de livros
     * e a retorna.
     * @param rt o ResultSet que contém os dados dos livros
     * @param carrinho um booleano que indica se os livros serão adicionados ao carrinho
     * @return uma LinkedList de Livros
     * @throws SQLException caso ocorra algum erro ao acessar o banco de dados
     */
    public static LinkedList<Livro> RecuperaLivro(ResultSet rt, boolean carrinho) throws SQLException{
        LinkedList<Livro> l = new LinkedList<>();

        while(rt.next()) {
            Livro aux = new Livro(rt.getInt("id_livro"), rt.getString("nome"),
                    Double.parseDouble(rt.getString("preco").substring(3).replaceAll(",", ".")),
                    rt.getString("autor"), rt.getString("genero"), rt.getString("tipo"),
                    rt.getBoolean("from_mari"));
            if(carrinho){
                for(int i = 1; i <= rt.getInt("qunatidade"); i++){
                    l.add(aux);
                }
            } else {
                l.add(aux);
            }
        }

        return l;
    }

    /**
     * Prints the list of books in a formatted way.
     *
     * @param l the list of books to be printed
     */
    public static void PrintLivro(LinkedList<Livro> l){
        int escolha = 1;
        for (Livro livro : l) {
            System.out.println(escolha + " - " + livro.toString());
            escolha++;
        }
    }

    /**
     * Recupera um vendedor pelo seu usuário e senha.
     *
     * @param user  O usuário do vendedor.
     * @param senha  A senha do vendedor.
     * @return  Um objeto Vendedor caso a consulta seja bem-sucedida, caso contrário, retorna null.
     * @throws SQLException  Caso ocorra algum erro ao acessar o banco de dados.
     * @throws NaoTemConexaoException  Caso a conexão com o banco de dados não esteja disponível.
     */
    public Vendedor recuperaVendedor(String user, String senha) throws SQLException, NaoTemConexaoException, ConexaoException {
        ResultSet rt = Select("vendedores_info.vendedor as v", "v.*", "v.usuario = " + user + "AND v.senha = " + senha);
        assert rt != null;
        return new Vendedor(rt.getInt("id_vendedor"), rt.getString("nome"), Long.parseLong(rt.getString("cpf")));
    }

    public Cliente recuperaCliente(String user, String senha, Carrinho carrinho) throws SQLException, NaoTemConexaoException, ConexaoException {

        ResultSet rt = Select("vendedores_info.vendedor as v", "v.*", "v.usuario = " + user + "AND v.senha = " + senha);
        assert rt != null;

        return new Cliente(rt.getString("nome"), Long.parseLong(rt.getString("cpf")), rt.getString("rua"),
                rt.getInt("numero"), rt.getBoolean("one_piece"), rt.getBoolean("is_flamengo"),
                rt.getBoolean("is_sousa"), rt.getString("usuario"), rt.getString("senha"), carrinho);
    }

    public LinkedList<Compra> recuperaCompra(ResultSet rt) throws SQLException {
        LinkedList<Compra> compras = new LinkedList<>();

        while(rt.next()){
            Compra c = new Compra(rt.getInt("id_compra"), rt.getNString("forma_pagamento"),
                    rt.getDate("data"), rt.getInt("valor"), rt.getInt("id_Carrinho"),
                    rt.getInt("Id_vendedor"));
            compras.add(c);
        }

        return compras;
    }

    public Carrinho Pesquisa(Scanner sc, Carrinho carrinho) throws SQLException, NaoTemConexaoException, ConexaoException {
        LinkedList<Livro> l = new LinkedList<>();

        while(true) {

            System.out.print("""
                    Pesquisar por:
                    1 - Nome
                    2 - Autor
                    3 - Genero
                    0 - Sair
                    """);
            int aux = Integer.parseInt(sc.nextLine());

            switch (aux) {
                case 1 -> {
                    System.out.print("Digite o nome do livro: ");
                    String str = sc.nextLine();

                    l = PesquisaLivro(str, "nome");
                }
                case 2 -> {
                    System.out.print("Digite o nome do autor: ");
                    String str = sc.nextLine();

                    l = PesquisaLivro(str, "autor");
                }
                case 3 -> {
                    System.out.print("Digite o gênero do livro: ");
                    String str = sc.nextLine();

                    l = PesquisaLivro(str, "genero");
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
                aux = Integer.parseInt(sc.nextLine());

                while(true) {

                    if (aux > 0 && aux < l.size()) {
                        carrinho.setLivro(l.get(aux - 1));
                        System.out.println("""
                                Livro adicionado ao carrinho!
                                Deseja adicionar outro?
                                (Digite o número que se encontra antes do nome do livro caso deseje sair digite '0')
                                """);
                        aux = Integer.parseInt(sc.nextLine());
                    } else if (aux > l.size()) {
                        System.out.println("Livro inválido");
                    } else if(aux == 0){
                        break;
                    }
                }
            }
        }
    }

    public Carrinho Carrinho(Scanner sc, Carrinho carrinho) {

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
            while(!carrinho.isEmpty()){
                System.out.print(carrinho + "\n");
                System.out.print("""
                        Deseja retirar algum do carrinho?
                        (Digite o número que se encontra antes do nome do livro para retirá-lo)
                        (Caso não queira alterar o carrinho digite '0')
                        """);
                int aux = Integer.parseInt(sc.nextLine());
                if (aux > 0 && aux < carrinho.getLivros().size()) {
                    carrinho.removeLivro(aux - 1);
                    System.out.println("""
                                Livro retirado do carrinho!
                                """);
                } else if (aux > carrinho.getLivros().size()) {
                    System.out.println("Livro inválido");
                } else if(aux == 0){
                    break;
                }
            }
            System.out.print("========================================================\n");
        }
        return carrinho;
    }

    public void Compra(Scanner sc, Carrinho carrinho, boolean[] desc, int idCliente) throws NaoTemConexaoException, ConexaoException {
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
            case 1 -> SolicitarCompra(1, precoD, idCliente, carrinho.getId());
            case 2 -> SolicitarCompra(2, precoD, idCliente, carrinho.getId());
            case 3 -> SolicitarCompra(3, precoD, idCliente, carrinho.getId());
            case 4 -> SolicitarCompra(4, precoD, idCliente, carrinho.getId());
            case 0 -> System.out.println("COMPRA CANCELADA");
            default -> System.out.println("Opção inválida");
        }


    }

    public void SolicitarCompra(int tipoP, double precoT, int id_cliente, int id_carrinho) throws NaoTemConexaoException, ConexaoException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formaP = tipoP == 1 ? "Berries" : tipoP == 2 ? "Pix" : tipoP == 3 ? "Boleto" : "Cartão";

        int id = InsertRetornando("compra", "forma_pagamento, data, valor, id_carrinho, id_cliente",
                formaP + "', date('" + sdf.format(date) + "'), " + precoT + ", " + id_carrinho + ", " + id_cliente);

        if(id > 0){
            System.out.print("================================================\n" +
                    "COMPRA FINALIZADA\n" +
                    "================================================\n" +
                    "O seu pedido está em processamento! Logo logo será aprovada, não se preocupe!\n" +
                    "Número do pedido: " + id);
        } else if(id == 0){
            System.out.print("Erro no cadastro da compra");
        }
    }

    public Carrinho CarregaCarrinho(Carrinho carrinho, int idC) throws SQLException, NaoTemConexaoException, ConexaoException {

        /* SELECT cl.id_carrinho, cl.quantidade, l.* FROM clientes_info.carrinho as c
        INNER JOIN clientes_info.carrinho_livro as cl ON c.id_carrinho = cl.id_carrinho
        INNER JOIN Estoque as l ON cl.id_livro = l.id_livro
        WHERE c.id_cliente = idC AND c.id_compra = -1
        * */

        ResultSet rt = Select("cl.id_carrinho, cl.quantidade, l.*", "clientes_info.carrinho as c " +
                "INNER JOIN clientes_info.carrinho_livro as cl ON c.id_carrinho = cl.id_carrinho " +
                "INNER JOIN Estoque as l ON cl.id_livro = l.id_livro", "c.id_cliente =" + idC + " AND c.id_compra = -1");

        assert rt != null;

        LinkedList<Livro> l = RecuperaLivro(rt, true);

        carrinho.setCarrinho(l);
        carrinho.setId(rt.getInt("id_carrinho"));

        return carrinho;
    }

    public void verPedidos(Scanner sc, int id) throws SQLException, NaoTemConexaoException, ConexaoException {

        ResultSet rt = recuperaCompra(id);
        LinkedList<Compra> compras = recuperaCompra(rt);

        if(!compras.isEmpty()) {
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

            while(true) {

                if (a > 0 && a < compras.size()) {
                    ResultSet rts = recuperaLivrosCompras(compras.get(a-1).getId_carrinho());
                    LinkedList<Livro> livros = RecuperaLivro(rts, false);
                    System.out.print("========================================================\n" +
                            "Pedido - " + compras.get(a-1).getIdCompra() + "\n");
                    PrintLivro(livros);
                    System.out.print("""
                            ========================================================
                            """);
                } else if (a > compras.size()) {
                    System.out.println("compra inválido");
                } else if(a == 0){
                    break;
                }
            }

        } else {
            System.out.println("""
                    NENHUM PEDIDO ENCONTRADO!
                    ========================================================
                    """);
        }
    }

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
            if (Pattern.matches("[a-zA-Z0-9.ÀÃÂÁÇÉÊÍÎÓÔÕÚÛÜàãâáçéêíîóôõúûü]+@(gmail.com|" +
                    "yahoo.com.br|hotmail.com)", s))
                return true;
            else {
                System.out.println("O EMAIL NÃO ESTÁ NO FORMATO SUPORTADO! TENTE NOVAMENTE\n" +
                        "São suportados apenas: 'gmail.com', 'yahoo.com.br', " +
                        "'hotmail.com.br'");
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
        return Pattern.matches("[0-9.]+", s);
    }


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