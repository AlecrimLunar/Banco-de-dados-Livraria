package Entities;

import Controle.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Cliente extends GerenciaBd {
    private Integer id;
    private String nome;
    private Long cpf;
    private String rua;
    private int numero;
    private Boolean onePiece;
    private Boolean flamengo;
    private Boolean souza;
    private String user;
    private String senha;
    private static Carrinho carrinho;
    private static Funcoes fun;
    private static LinkedList<Integer> pedidos;

    public Cliente(String nome, Long cpf, String rua, int numero,
                   Boolean onePiece, Boolean flamengo, Boolean souza, String user,
                   String senha, Carrinho carrinho) {
        this.nome = nome;
        this.cpf = cpf;
        this.rua = rua;
        this.numero = numero;
        this.onePiece = onePiece;
        this.flamengo = flamengo;
        this.souza = souza;
        this.user = user;
        this.senha = senha;
        Cliente.carrinho = carrinho;
        fun = new Funcoes();
    }

    public void MenuCliente(Scanner sc, boolean compra) throws NaoTemConexaoException, SQLException, ConexaoException {

        if(compra){
            fun.Compra(sc, carrinho, new boolean[]{getOnePiece(), getOnePiece(), getSouza()}, getId());
        }

        carrinho = fun.CarregaCarrinho(carrinho, getId());

        LinkedList<Livro> destaques = fun.Destaques(0);

        System.out.print("""
                ========================================================
                BEM-VINDO AO SISTEMA DIGITAL DA LIVRARIA MOOGLES
                ========================================================
                """);

        loop:while(true) {

            System.out.print("livros em destaque:\n" +
                    fun.PrintDestaques(destaques) + "\n" +
                    "4 - Procurar livro\n" +
                    "5 - Carrinho" +
                    "6 - Minha conta\n" +
                    "7 - Realizar logout\n" +
                    "0 - Sair\n" +
                    "========================================================\n");

            int a = Integer.parseInt(sc.nextLine());

            switch (a) {
                case 1 -> {
                    System.out.print("Adicionar " + destaques.get(0).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.setLivro(destaques.get(0));
                }
                case 2 -> {
                    System.out.print("Adicionar " + destaques.get(1).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.setLivro(destaques.get(1));
                }
                case 3 -> {
                    System.out.print("Adicionar " + destaques.get(2).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.setLivro(destaques.get(2));
                }

                case 4 -> carrinho = fun.Pesquisa(sc, carrinho);
                case 5 -> {
                    carrinho = fun.Carrinho(sc, carrinho);

                    System.out.print("Deseja finalizar a compra?\n");
                    if(sc.nextLine().equalsIgnoreCase("sim")){
                        fun.Compra(sc, carrinho, new boolean[]{getOnePiece(), getOnePiece(), getSouza()}, getId());
                    }
                }
                case 6 -> MenuConta(sc);
                case 7 ->{break loop;}
                case 0 -> {
                    System.out.print("Tem certeza que deseja sair?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        System.exit(1);
                }
                default -> System.out.println("Opção invalida:");
            }
        }
    }

    public void MenuConta(Scanner sc) throws NaoTemConexaoException, SQLException, ConexaoException {

        loop:while(true) {
            System.out.println("========================================================\n" +
                    "MENU CONTA - BEM VINDO " + getNome() + "!\n");
            System.out.println("""
                    ========================================================
                    1 - Ver meus pedidos
                    2 - Alterar informações
                    3 - Deletar conta
                    0 - Sair
                    ========================================================
                    """);
            int a = Integer.parseInt(sc.nextLine());

            switch (a) {
                case 1 -> fun.verPedidos(sc, getId());
                case 2 -> alteraCliente(sc);
                case 3 -> {
                    removeCliente(sc);
                    break loop;
                }
                case 0 -> {break loop;}
            }
        }
    }

    // Ainda tem que fazer o tratamento de erros aq!
    public void removeCliente(Scanner tc) throws NaoTemConexaoException, ConexaoException {

        System.out.print("""
                RESPONDA COM 'Sim' OU 'Não'.\s
                DESEJA REMOVER SEU CADASTRO DA LOJA? ISSO NÃO IRÁ REMOVER SEU HISTÓRICO DE COMPRAS NO SISTEMA DA LOJA!
                """);

        if (tc.nextLine().equalsIgnoreCase("sim")){
            delete("cliente", "id_cliente = " + getId());
        }

    }

    // Precisa de tratamento de dados PARA CARALHO, mas são duas da manhã eu vou dormir
    public void alteraCliente(Scanner tc){
        try {
            loop: while (true) {

                System.out.println("SELECIONE O CAMPO QUE DESEJA ALTERAR \n1 - Nome \n2 - CPF \n3 - Rua \n4 - Número " +
                        "\n5 - Email \n6 - Torcer para o Flamengo \n7 - Nascer em Sousa \n8 - Assistir a One Piece " +
                        "\n0 - Voltar ao menu principal");
                int escolha = Integer.parseInt(tc.nextLine());
                System.out.println("===========================================================================");

                switch (escolha) {
                    case 1 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("Insira o novo nome: ");
                        String novo = tc.nextLine();

                        coluna.add("nome");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 2 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("Insira o novo CPF: ");
                        int novo = Integer.parseInt(tc.nextLine());

                        coluna.add("cpf");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 3 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("Insira a nova rua: ");
                        String novo = tc.nextLine();

                        coluna.add("rua");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 4 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("Insira o novo número de residência: ");
                        int novo = Integer.parseInt(tc.nextLine());

                        coluna.add("numero");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 5 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("Insira o novo email: ");
                        String novo = tc.nextLine();

                        coluna.add("email");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 6 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("RESPONDA COM 'sim' OU 'não'\nTorce para o flamengo? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");

                        coluna.add("is_flamengo");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 7 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("RESPONDA COM 'sim' OU 'não'\nNasceu em Sousa? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");

                        coluna.add("is_sousa");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 8 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();

                        System.out.print("RESPONDA COM 'sim' OU 'não'\nAssiste a One Piece? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");

                        coluna.add("one_piece");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                    }

                    case 0 -> {
                        break loop;
                    }
                    default -> System.out.println("OPÇÃO INVÁLIDA!");
                }
            }
        }catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Long getCpf() {
        return cpf;
    }

    public String getrua() {
        return rua;
    }

    public int getNumero(){
        return numero;
    }

    public Boolean getOnePiece() {
        return onePiece;
    }

    public Boolean getFlamengo() {
        return flamengo;
    }

    public Boolean getSouza() {
        return souza;
    }

    public void setId(int id) {
        this.id = id;
    }
}
