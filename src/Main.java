import Controle.ConexaoException;
import Controle.ControlaBD;
import Controle.Sistema;
import Entities.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ConexaoException {
        Sistema s = new Sistema();
        s.Iniciar();


        /*
        System.out.print("-----------------------------------------------------------------------------\n" +
                "BEM-VINDO AO MENU DE VENDEDOR");

        do{

            System.out.println("\nQUAL A LEITURA DE HOJE?\n(ESCOLHA UMA DAS OPÇÕES ABAIXO)\n" +
                    "-----------------------------------------------------------------------------\n" +
                    "1 - Cadastrar\n" +
                    "2 - Alterar informação\n" +
                    "3 - Remover\n" +
                    "4 - Consultar\n" +
                    "5 - Adicionar no estoque\n" +
                    "6 - sair\n" +
                    "-----------------------------------------------------------------------------");
            int a = Integer.parseInt(tc.nextLine());

            switch (a){
                case 1 -> {
                    System.out.println("\nO que você deseja cadastrar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Compra\n" +
                            "3 - Livro\n" +
                            "4 - Vendedor\n" +
                            "5 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a){
                        case 1 -> vendedor.cadastraCliente(tc);
                        case 2 -> vendedor.cadastraCompra(tc);
                        case 3 -> vendedor.cadastraLivro(tc);
                        case 4 -> vendedor.cadastraVendedor(tc);
                        case 5 -> {continue;}
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 2 -> {
                    System.out.println("\nO que você deseja alterar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Livro\n" +
                            "4 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a){
                        case 1 -> vendedor.alteraCliente(tc);
                        case 2 -> vendedor.alteraVendedor(tc);
                        case 3 -> vendedor.alteraLivro(tc);
                        case 4 -> {continue;}
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }

                }
                case 3 ->{
                    System.out.println("\nO que você deseja remover?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Livro\n" +
                            "4 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a){
                        case 1 -> vendedor.removeCliente(tc);
                        case 2 -> vendedor.removeVendedor(tc);
                        case 3 -> vendedor.removeLivro(tc);
                        case 4 -> {continue;}
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 4 -> {
                    boolean all = false;
                    System.out.println("\nO que você deseja consultar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Livro\n" +
                            "4 - Compra\n" +
                            "5 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    if(a != 5) {
                        System.out.println("Deseja filtrar consulta?");
                        all = !tc.nextLine().equalsIgnoreCase("sim");
                    }

                    switch (a){
                        case 1 -> {
                            if(all)
                                vendedor.printCliente("nome, cpf, email, is_flamengo, is_sousa, one_piece");
                            else{
                                System.out.print("Digite o ID do cliente: ");
                                String id = tc.nextLine();
                                vendedor.printCliente(id, "nome, cpf, email, is_flamengo, is_sousa, one_piece");
                            }
                        }
                        case 2 -> {
                            if(all)
                                vendedor.printVendedor("nome, cpf");
                            else {
                                System.out.print("Digite o ID do vendedor: ");
                                String id = tc.nextLine();
                                vendedor.printVendedor(id, "nome, cpf");
                            }
                        }
                        case 3 -> {
                            if(all)
                                vendedor.printLivro("*");
                            else{
                                System.out.print("Digite o ID do livro: ");
                                String id = tc.nextLine();
                                vendedor.printLivro(id, "*");
                            }
                        }
                        case 4 -> {
                            if(all)
                                vendedor.printCompra("*");
                            else{
                                System.out.print("Digite o ID do compra: ");
                                String id = tc.nextLine();
                                vendedor.printCompra(id, "*");
                            }
                        }
                        case 5 -> {continue;}
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 5 -> {
                    vendedor.adicionaLivro_noEstoque(tc);
                }
                case 6 -> {
                    System.out.println("DESLIGANDO...");
                    System.exit(0);
                }
                default -> System.out.println("OPÇÃO INVÁLIDA!");
            }
        }while(true);
         */
    }
}