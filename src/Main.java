
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ControlaBD controle = new ControlaBD();
        Scanner tc = new Scanner(System.in);
        IniciaSistema(controle, tc);
    }

    public static void IniciaSistema(ControlaBD controle, Scanner tc){

        if (controle.Quantos("", "vendedor") == 0){
            System.out.println("-----------------------------------------------------------------------------\n" +
                    "BEM VINDO AO SISTEMA! NENHUM REGISTRO DE VENDEDORES FOI ENCONTRADO." +
                    "PARA UTILIZAR O SISTEMA \nÉ NECESSÁRIO SER UM VENDEDOR. DESEJA CADASTRAR UM" +
                    "NOVO VENDEDOR?\nATENÇÃO: É NECESSÁRIO UM VENDEDOR PARA UTILIZAR O SISTEMA, CASO " +
                    "NÃO SEJA CADASTRADO UM NOVO VENDEDOR O SISTEMA IRÁ DESLIGAR!\n" +
                    "-----------------------------------------------------------------------------");
            //dei uma formatação nesse print pra ficar bonitinho :D

            if (tc.nextLine().equalsIgnoreCase("sim") || tc.nextLine().equalsIgnoreCase("s")){
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

            } else{
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
        do {
            System.out.print("Usuário: ");

            String nome = tc.nextLine();
            System.out.print("\nSenha: ");
            String senha = tc.nextLine();

            if (controle.login(nome, senha)) {
                System.out.print("\nLOGIN EFETUADO COM SUCESSO!");
                break;
            } else {
                System.out.print("SENHA OU USUÁRIO INCORRETO! \nTENTE NOVAMENTE\n\n");
            }
        }while(true);


    }
}