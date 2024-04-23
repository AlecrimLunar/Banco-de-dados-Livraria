package Controle;

import Entities.Livro;
import Entities.FuncoesEstaticas;

import java.util.*;

public class Sistema  {

    private ControlaBD con;
    private FuncoesEstaticas fun;
    private LinkedList<Livro> carrinho;
    public Sistema() throws ConexaoException {
        con = new ControlaBD(0);
        fun = new FuncoesEstaticas();
        carrinho = new LinkedList<>();
    }

    public void Iniciar() throws ConexaoException {
        Scanner sc = new Scanner(System.in);
        LinkedList<Livro> destaques = fun.Destaques();

        while(true) {
            System.out.print("========================================================\n" +
                    "BEM-VINDO AO SISTEMA DIGITAL DA LIVRARIA MOOGLES\n\n" +
                    "livros em destaque:\n" +
                    fun.PrintDestaques(destaques) + "\n" +
                    "4 - Procurar livro\n" +
                    "5 - Carrinho" +
                    "6 - Realizar login\n");
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

                case 4 -> {}
                case 5 -> {
                    if(carrinho.isEmpty()){
                        System.out.print("========================================================\n" +
                                "Seu carrinho está vazio!\n" +
                                "========================================================\n");
                    } else {
                        System.out.print("========================================================\n" +
                                "Seu carrinho:\n");
                        for (Livro l : carrinho) {
                            System.out.println(l.toString() + "\n");
                        }
                        System.out.print("========================================================\n");
                    }
                }
                case 6 -> {
                    System.out.print("Deseja realizar login como Vendedor ou cliente?\n");
                    String tabela = sc.nextLine().equalsIgnoreCase("vendedor") ? "Vendedores_info.vendedor" : "Clientes_info.cliente";

                    System.out.print("Usuário: ");
                    String user = sc.nextLine();

                    System.out.print("Senha: ");
                    String senha = sc.nextLine();
                    
                    con.login(user, senha, tabela);
                }
                default -> System.out.println("Opção invalida:");
            }
        }
    }
}
