package Entities;

import Controle.ConexaoException;
import Controle.GerenciaCon;
import Controle.NaoTemConexaoException;
import Controle.FuncoesEstaticas;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

public class Cliente extends GerenciaCon {
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
    private static LinkedList<Livro> carrinho;
    private static FuncoesEstaticas fun;

    public Cliente(String nome, Long cpf, String rua, int numero,
                   Boolean onePiece, Boolean flamengo, Boolean souza, String user,
                   String senha, LinkedList<Livro> carrinho) {
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
        fun = new FuncoesEstaticas();
    }

    public void MenuCliente(Scanner sc) throws NaoTemConexaoException, SQLException, ConexaoException {
        LinkedList<Livro> destaques = fun.Destaques(0);

        System.out.print("""
                ========================================================
                BEM-VINDO AO SISTEMA DIGITAL DA LIVRARIA MOOGLES
                ========================================================
                """);

        while(true) {

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
                        carrinho.add(destaques.get(0));
                }
                case 2 -> {
                    System.out.print("Adicionar " + destaques.get(1).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.add(destaques.get(1));
                }
                case 3 -> {
                    System.out.print("Adicionar " + destaques.get(2).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.add(destaques.get(2));
                }

                case 4 -> carrinho = fun.Pesquisa(sc, carrinho);
                case 5 -> {
                    carrinho = fun.Carrinho(sc, carrinho);

                    System.out.print("Deseja finalizar a compra?\n");
                    if(sc.nextLine().equalsIgnoreCase("sim")){
                        //compra();
                    }
                }
                case 6 ->{
                    /*Alterar infos e pedidos feitos :D*/
                }
                case 7 ->{return;}
                case 0 -> {
                    System.out.print("Tem certeza que deseja sair?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        System.exit(1);
                }
                default -> System.out.println("Opção invalida:");
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
