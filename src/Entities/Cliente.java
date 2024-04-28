package Entities;

import Controle.ConexaoException;
import Controle.GerenciaCon;
import Controle.NaoTemConexaoException;
import Controle.FuncoesEstaticas;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void MenuCliente(Scanner sc, boolean compra) throws NaoTemConexaoException, SQLException {

        if(compra){
            fun.Compra(sc, carrinho, new boolean[]{getOnePiece(), getOnePiece(), getSouza()}, getId());
        }

        carrinho = fun.CarregaCarrinho(carrinho, getId());

        /*Tem que ter uma função que vai buscar o carrinho anterior do cara*/
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
                case 6 ->{

                }
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

    public void MenuConta(Scanner sc){

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
                case 1 -> {

                }
                case 2 -> {

                }
                case 3 -> {

                }
                case 0 -> {break loop;}
            }
        }
    }

    public void removeCliente(Scanner tc) {
        System.out.println("------------------------------------------------------------------------------" +
                "\nLOGIN CLIENTE");
        String user;
        String senha;
        ResultSet rs;
        while (true) {
            System.out.print("Usuário: ");
            user = tc.nextLine();
            System.out.print("Senha: ");
            senha = tc.nextLine();
            rs = controle.login(user, senha, "cliente");
            if (rs != null)
                break;
            else
                System.out.print("INFORMAÇÕES INCORRETAS! TENTE NOVAMENTE");
        }

        System.out.println("RESPONDA COM 'Sim' OU 'Não' \nDESEJA REMOVER SEU CADASTRO DA LOJA? ISSO NÃO" +
                " IRÁ REMOVER SEU HISTÓRICO DE COMPRAS NO SISTEMA DA LOJA");
        if (tc.nextLine().equalsIgnoreCase("sim")){
            try {
                if (controle.delete("cliente", "id_cliente", rs.getString("id_cliente"),
                        false))
                    System.out.println("REMOÇÃO FEITA COM SUCESSO!");
                else
                    System.out.println("ERRO! TENTE NOVAMENTE");
            }catch (Exception e){
                System.out.println("ERRO: " + e);
            }
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
