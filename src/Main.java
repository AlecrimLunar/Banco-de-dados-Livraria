
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ControlaBD controle = new ControlaBD();
        Scanner tc = new Scanner(System.in);
        IniciaSistema(controle, tc);
    }

    public static void IniciaSistema(ControlaBD controle, Scanner tc){

        if (controle.Quantos("", "vendedor") == 0){
            System.out.println("-----------------------------------------------------------------------------" +
                    "BEM VINDO AO SISTEMA! NENHUM REGISTRO DE VENDEDORES FOI ENCONTRADO." +
                    "PARA UTILIZAR O SISTEMA \nÉ NECESSÁRIO SER UM VENDEDOR. DESEJA CADASTRAR UM" +
                    "NOVO VENDEDOR?\nATENÇÃO: É NECESSÁRIO UM VENDEDOR PARA UTILIZAR O SISTEMA, CASO " +
                    "NÃO SEJA CADASTRADO UM NOVO VENDEDOR O SISTEMA IRÁ DESLIGAR!");

            if (tc.nextLine().equalsIgnoreCase("sim")){
                while (true) {
                    System.out.print("MUITO BEM, INSIRA AS SEGUINTES INFORMAÇÕES:\nNome: ");
                    String nome = tc.nextLine();

                    System.out.print("\nCFP (Apenas números): ");
                    long CPF = Long.parseLong(tc.nextLine());

                    System.out.print("\nSenha de acesso: ");
                    String senha = tc.nextLine();

                    System.out.println("\n\nMUITO BEM, VERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS. SE SIM DIGITE " +
                            "'Sim', SE NÃO DIGITE 'Não'\nNome: " + nome + "\nCPF: " + CPF + "\n");

                    if (tc.nextLine().equalsIgnoreCase("sim")) {
                        String insert = "'" + nome + "', '" + senha + "', " + CPF;
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
                "-------------------------------------------------" +
                " \nUsuário: ");

        long id = Long.parseLong(tc.nextLine());
        System.out.print("\nSenha: ");

        /*não tenho ideia de como fazer isso aqui, acho que vamos precisar de duas chamadas
        * do select, uma pra pegar os usuários com aquele nome e outra pra pegar eles com a senha.
        * talvez a gente possa fazer isso de forma diferente pq n tem pq da select ja q a gente so
        * quer saber se tem ou não*/

    }
}