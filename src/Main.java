import Entities.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner tc = new Scanner(System.in);
        Vendedor vendedor = FuncoesEstaticas.IniciaSistema(tc);

        System.out.println("-----------------------------------------------------------------------------\n" +
                "BEM-VINDO AO MENU DE VENDEDOR\n\n");

        do{

            System.out.println("QUAL A LEITURA DE HOJE?\n(ESCOLHA UMA DAS OPÇÕES ABAIXO)\n" +
                    "-----------------------------------------------------------------------------\n" +
                    "1 - Cadastrar\n" +
                    "2 - Alterar informação\n" +
                    "3 - Remover\n" +
                    "4 - Consultar\n" +
                    "6 - sair\n" +
                    "-----------------------------------------------------------------------------\n");
            int a = Integer.parseInt(tc.nextLine());

            /* levei tudo pra classe vendedor. nesse nosso codigo, é o vendedor que está realizando todas
            * as ações, por isso ele deveria realizar elas. alem disso o codigo fica bem mais clean né? */

            switch (a){
                case 1 -> {
                    System.out.println("O que você deseja cadastrar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Compra\n" +
                            "3 - Livro\n" +
                            "4 - Voltar ao menu principal");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a){
                        case 1 -> vendedor.cadastraCliente(tc);
                        case 2 -> vendedor.cadastraCompra(tc);
                        case 3 -> vendedor.cadastraLivro(tc);
                        case 4 -> {/*n faz nada, logo vai pra próxima execução do do/while*/}
                         //casdastrar vendedor
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 2 -> {
                    System.out.println("O que você deseja alterar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Livro\n" +
                            "4 - Voltar ao menu principal");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a){
                        case 1 -> vendedor.alteraCliente(tc);
                        case 2 -> vendedor.alteraVendedor(tc);
                        case 3 -> vendedor.alteraLivro(tc);
                        case 4 -> {/*n faz nada, logo vai pra próxima execução do do/while*/}
                        //alterar compra
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }

                }
                case 3 ->{
                    System.out.println("O que você deseja remover?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Livro\n" +
                            "4 - Voltar ao menu principal");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a){
                        case 1 -> vendedor.removeCliente(tc);
                        case 2 -> vendedor.removeVendedor(tc);
                        case 3 -> vendedor.removeLivro(tc);
                        case 4 -> {/*n faz nada, logo vai pra próxima execução do do/while*/}
                        //remover compra
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 4 -> {
                    System.out.println("O que você deseja consultar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Livro\n" +
                            "4 - Compra\n" +
                            "5 - Voltar ao menu principal");
                    a = Integer.parseInt(tc.nextLine());

                    System.out.println("Deseja consultar só um ou todos?");
                    boolean all = tc.nextLine().equalsIgnoreCase("todos");

                    switch (a){
                        case 1 -> {
                            if(all)
                                vendedor.printCliente();
                            else
                                System.out.println("aaaaaaaaaaaaa");
                                /*função de consultar só um :D*/
                        }
                        case 2 -> {
                            if(all)
                                vendedor.printVendedor();
                            else
                                System.out.println("aaaaaaaaaaaaa");
                            /*função de consultar só um :D*/
                        }
                        case 3 -> {
                            if(all)
                                vendedor.printLivro();
                            else
                                System.out.println("aaaaaaaaaaaaa");
                            /*função de consultar só um :D*/
                        }
                        case 4 -> {
                            if(all)
                                vendedor.printCompra();
                            else
                                System.out.println("aaaaaaaaaaaaa");
                            /*função de consultar só um :D*/
                        }
                        case 5 -> {/*n faz nada, logo vai pra próxima execução do do/while*/}
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 6 -> {
                    System.out.println("DESLIGANDO...");
                    System.exit(0);
                }
                default -> System.out.println("OPÇÃO INVÁLIDA!");
            }
        }while(true);

    }
}