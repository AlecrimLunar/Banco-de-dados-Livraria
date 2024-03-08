import Controle.*;
import Entities.*;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner tc = new Scanner(System.in);
        Vendedor vendedor = IniciaSistema(tc);

        System.out.println("-----------------------------------------------------------------------------\n" +
                "BEM-VINDO AO MENU DE VENDEDOR\n\n");

        do{

            System.out.println("QUAL A LEITURA DE HOJE?\n(ESCOLHA UMA DAS OPÇÕES ABAIXO)" +
                    "-----------------------------------------------------------------------------\n" +
                    "1 - Cadastrar compra\n" +
                    "2 - Cadastrar Cliente" +
                    "3 - Alterar informação de cliente\n" +
                    "4 - Alterar informação de vendedor\n" +
                    "5 - Gerar relatório de vendas\n" +
                    "6 - Gerar relatório de um vendedor\n" +
                    "7 - Consultar estoque\n" +
                    "8 - Consultar cliente\n" +
                    "9 - sair\n" +
                    "-----------------------------------------------------------------------------\n");
            int a = tc.nextInt();

            switch (a){
                case 1:

                    vendedor.cadastraCompra(tc);

                    break;
                case 2:

                    vendedor.cadastraCliente(tc);



                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;
                case 9:
                    System.out.println("Desligando...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("OPÇÃO INVÁLIDA!");
                    break;
            }
        }while(true);

    }


    public static Vendedor IniciaSistema(Scanner tc) {

        ControlaBD controle = new ControlaBD();
        if (controle.Quantos("", "vendedor") == 0) {
            System.out.println("-----------------------------------------------------------------------------\n" +
                    "BEM VINDO AO SISTEMA! NENHUM REGISTRO DE VENDEDORES FOI ENCONTRADO." +
                    "PARA UTILIZAR O SISTEMA \nÉ NECESSÁRIO SER UM VENDEDOR. DESEJA CADASTRAR UM" +
                    "NOVO VENDEDOR?\nATENÇÃO: É NECESSÁRIO UM VENDEDOR PARA UTILIZAR O SISTEMA, CASO " +
                    "NÃO SEJA CADASTRADO UM NOVO VENDEDOR O SISTEMA IRÁ DESLIGAR!\n" +
                    "-----------------------------------------------------------------------------");
            //dei uma formatação nesse print pra ficar bonitinho :D

            if (tc.nextLine().equalsIgnoreCase("sim") || tc.nextLine().equalsIgnoreCase("s")) {
                while (true) {
                    System.out.print("MUITO BEM, INSIRA AS SEGUINTES INFORMAÇÕES:\nNome: ");
                    String nome = tc.nextLine();

                    System.out.print("\nCFP (Apenas números): ");
                    long CPF = Long.parseLong(tc.nextLine());

                    System.out.print("\nNome de acesso: ");
                    String user = tc.nextLine();
                    //adicionei o nome de acesso pra fazer o login de forma mais facil. Do q pelo nome completo

                    System.out.print("\nSenha de acesso: ");
                    String senha = tc.nextLine();


                    System.out.println("\n\nMUITO BEM, VERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS. SE SIM DIGITE " +
                            "'Sim', SE NÃO DIGITE 'Não'\nNome: " + nome + "\nCPF: " + CPF + "\n");

                    if (tc.nextLine().equalsIgnoreCase("sim")) {
                        String insert = "DEFAULT, '" + nome + "', '" + senha + "', " + CPF + ", '" + user + "'";
                        //como ce pode ver o nome de usuario fica no final ->-->-->-->-->-->-->-->-->-^
                        if (controle.Insert("vendedor", insert)) {
                            System.out.println("CADASTRO CONCLUÍDO COM SUCESSO! PARA LOGAR, UTILIZE O ID: E A" +
                                    "SENHA INFORMADA." +
                                    "---------------------------------------------------------------------");
                            break;
                        }
                    }
                }

            } else {
                System.exit(0);
            }
        }
        System.out.print("LOGIN NO SISTEMA\n" +
                "-------------------------------------------------\n");

        /*não tenho ideia de como fazer isso aqui, acho que vamos precisar de duas chamadas
         * do select, uma pra pegar os usuários com aquele nome e outra pra pegar eles com a senha.
         * talvez a gente possa fazer isso de forma diferente pq n tem pq da select ja q a gente so
         * quer saber se tem ou não*/

        /*Nico: Ta resolvido meu querido :D*/
        Vendedor vendedor = new Vendedor();

        do {
            System.out.print("Usuário: ");

            String nome = tc.nextLine();
            System.out.print("\nSenha: ");
            String senha = tc.nextLine();

            if (controle.login(nome, senha)) {
                try {
                    ResultSet rt = controle.Select("nome, cpf, id", "vendedor", nome, "usuario");
                    vendedor.setNome(rt.getString("nome"));
                    vendedor.setId(rt.getInt("id"));
                    vendedor.setCpf(rt.getLong("cpf"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.print("\nLOGIN EFETUADO COM SUCESSO!\n\n");
                break;
            } else {
                System.out.print("SENHA OU USUÁRIO INCORRETO! \nTENTE NOVAMENTE\n\n");
            }
        } while (true);
        controle = null;
        return vendedor;
    }

}