import Controle.*;
import Entities.*;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner tc = new Scanner(System.in);
        Vendedor vendedor = FuncoesEstaticas.IniciaSistema(tc);

        System.out.println("-----------------------------------------------------------------------------\n" +
                "BEM-VINDO AO MENU DE VENDEDOR\n");

        do{

            System.out.println("QUAL A LEITURA DE HOJE?\n(ESCOLHA UMA DAS OPÇÕES ABAIXO)\n" +
                    "-----------------------------------------------------------------------------\n" +
                    "1 - Cadastrar compra\n" +
                    "2 - Cadastrar Cliente\n" +
                    "3 - Alterar informação de cliente\n" +
                    "4 - Alterar informação de um cliente\n" +
                    "5 - Alterar informações do vendedor\n" +
                    "6 - Gerar relatório de um vendedor\n" +
                    "7 - Consultar estoque\n" +
                    "8 - Consultar cliente\n" +
                    "9 - sair\n");
            int a = Integer.parseInt(tc.nextLine());
            System.out.println("---------------------------------------------------------------------------");

            /* levei tudo pra classe vendedor. nesse nosso codigo, é o vendedor que está realizando todas
            * as ações, por isso ele deveria realizar elas. alem disso o codigo fica bem mais clean né? */
            switch (a){
                case 1 -> {
                    if (!vendedor.cadastraCompra(tc))
                        System.out.println("Deu errado");
                }

                case 2 -> {
                    if (!vendedor.cadastraCliente(tc))
                        System.out.println("Deu errado");
                }

                case 3 -> {}

                case 4 -> vendedor.alteraCliente(tc);

                case 5 -> vendedor.alteraVendedor(tc);

                case 6 -> {}

                case 7 -> {}

                case 8 -> {}

                case 9 -> {
                    System.out.println("Desligando...");
                    System.exit(0);
                }
                default -> System.out.println("OPÇÃO INVÁLIDA!");
            }
        }while(true);

    }
}