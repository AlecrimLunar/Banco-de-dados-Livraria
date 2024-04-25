package Controle;

import Entities.Cliente;
import Entities.Livro;
import Entities.Vendedor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringJoiner;

public class FuncoesEstaticas extends GerenciaCon {
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

        destaques = RecuperaLivro(rt);

        while (rt.next()) { }
        } catch (SQLException | NaoTemConexaoException e) {
            e.printStackTrace();
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
        sb.append("|" + AdicionaEspacos("R$ " + livros.get(0).getPreco()) + "|" + AdicionaEspacos("R$ " + livros.get(1).getPreco()) + "|" +
                AdicionaEspacos("R$ " + livros.get(2).getPreco()) + "|\n");
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


    /**
     * Serve para o cliente pesquisar o livro tanto pelo nome, quanto autor ou genero.
     * É retornado o ResultSet com as tabelas lançando para uma LinkedList de livros.
     * **/
    public static LinkedList<Livro> PesquisaLivro(String str, String coluna) throws SQLException, NaoTemConexaoException {
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
            l = RecuperaLivro(rt);
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
     * **/
    public static LinkedList<Livro> RecuperaLivro(ResultSet rt) throws SQLException{
        LinkedList<Livro> l = new LinkedList<>();

        while(rt.next()) {
            Livro aux = new Livro(rt.getInt("id_livro"), rt.getString("nome"),
                    Double.parseDouble(rt.getString("preco").substring(3).replaceAll(",", ".")),
                    rt.getString("autor"), rt.getString("genero"), rt.getString("tipo"),
                    rt.getBoolean("from_mari"));
            l.add(aux);
        }

        return l;
    }

    public static void PrintLivro(LinkedList<Livro> l){
        int escolha = 1;
        for (Livro livro : l) {
            System.out.println(escolha + " - " + livro.toString());
            escolha++;
        }
    }

    public Vendedor recuperaVendedor(String user, String senha) throws SQLException, NaoTemConexaoException {
        ResultSet rt = Select("vendedores_info.vendedor as v", "v.*", "v.usuario = " + user + "AND v.senha = " + senha);
        assert rt != null;
        return new Vendedor(rt.getInt("id_vendedor"), rt.getString("nome"), Long.parseLong(rt.getString("cpf")));
    }

    public Cliente recuperaCliente(String user, String senha, LinkedList<Livro> carrinho) throws SQLException, NaoTemConexaoException {

        ResultSet rt = Select("vendedores_info.vendedor as v", "v.*", "v.usuario = " + user + "AND v.senha = " + senha);
        assert rt != null;

        return new Cliente(rt.getString("nome"), Long.parseLong(rt.getString("cpf")), rt.getString("rua"),
                rt.getInt("numero"), rt.getBoolean("one_piece"), rt.getBoolean("is_flamengo"),
                rt.getBoolean("is_sousa"), rt.getString("usuario"), rt.getString("senha"), carrinho);
    }

    public LinkedList<Livro> Pesquisa(Scanner sc, LinkedList<Livro> carrinho) throws SQLException, NaoTemConexaoException {
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
                        carrinho.add(l.get(aux - 1));
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

    public LinkedList<Livro> Carrinho(Scanner sc, LinkedList<Livro> carrinho) {

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
                PrintLivro(carrinho);
                System.out.print("""
                        Deseja retirar algum do carrinho?
                        (Digite o número que se encontra antes do nome do livro para retirá-lo)
                        (Caso não queira alterar o carrinho digite '0')
                        """);
                int aux = Integer.parseInt(sc.nextLine());
                if (aux > 0 && aux < carrinho.size()) {
                    carrinho.remove(aux - 1);
                    System.out.println("""
                                Livro reirado do carrinho!
                                """);
                } else if (aux > carrinho.size()) {
                    System.out.println("Livro inválido");
                } else if(aux == 0){
                    break;
                }
            }
            System.out.print("========================================================\n");
        }
        return carrinho;
    }

    /* esse método irá verificar se alguma palavra reservada
     * do SQL está sendo utilizada */
    private static boolean verificaComandoSQL(String s){
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
            return Pattern.matches("[A-Za-zÀÃÂÁÇÉÊÍÎÓÔÕÚÛÜàãâáçéêíîóôõúûü]+", s);
        }
        return false;
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