package Entities;

import Controle.ConexaoException;
import Controle.ControlaBD;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.*;

public class FuncoesEstaticas {
    public static void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    public static Vendedor IniciaSistema(Scanner tc) {

        try {
            ControlaBD controle = new ControlaBD(0);
        } catch (ConexaoException e){

        }
        if (controle.Existe("", "vendedor", "") == 1) {
            System.out.println("-----------------------------------------------------------------------------\n" +
                    "BEM VINDO AO SISTEMA! NENHUM REGISTRO DE VENDEDORES FOI ENCONTRADO." +
                    " PARA UTILIZAR O SISTEMA \nÉ NECESSÁRIO SER UM VENDEDOR. DESEJA CADASTRAR UM" +
                    " NOVO VENDEDOR?" );
            //dei uma formatação nesse print pra ficar bonitinho :D

            if (tc.nextLine().equalsIgnoreCase("sim") || tc.nextLine().equalsIgnoreCase("s")) {
                while (true) {
                    System.out.print("MUITO BEM, INSIRA AS SEGUINTES INFORMAÇÕES:\nNome: ");
                    String nome = tc.nextLine();

                    System.out.print("CPF (Apenas números): ");
                    String CPF = tc.nextLine();

                    System.out.print("Nome de acesso: ");
                    String user = tc.nextLine();
                    //adicionei o nome de acesso pra fazer o login de forma mais facil. Do q pelo nome completo

                    System.out.print("Senha de acesso: ");
                    String senha = tc.nextLine();


                    System.out.print("-------------------------------------------------------------------------" +
                            "\n\nMUITO BEM, VERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS. SE SIM DIGITE " +
                            "'Sim', SE NÃO DIGITE 'Não'\nNome: " + nome + "\nCPF: " + CPF + "\n");

                    if (tc.nextLine().equalsIgnoreCase("sim")) {
                        String insert = "'" + nome + "', '" + user + "', '" + CPF + "', '" + senha + "'";
                        //como ce pode ver o nome de usuario fica no final ->-->-->-->-->-->-->-->-->-^
                        if (controle.InsertRetornando("vendedor", insert,
                                "nome, usuario, cpf, senha") != -2) {
                            System.out.println("CADASTRO CONCLUÍDO COM SUCESSO! PARA LOGAR, UTILIZE O USUÁRIO: " +
                                    user + " E A " +
                                    "SENHA INFORMADA.");
                            break;
                        }
                    }
                }

            } else {
                System.exit(0);
            }
        }
        System.out.print("----------------------------------------------------" +
                "\nLOGIN NO SISTEMA\n\n");

        /*não tenho ideia de como fazer isso aqui, acho que vamos precisar de duas chamadas
         * do select, uma pra pegar os usuários com aquele nome e outra pra pegar eles com a senha.
         * talvez a gente possa fazer isso de forma diferente pq n tem pq da select ja q a gente so
         * quer saber se tem ou não*/

        /*Nico: Ta resolvido meu querido :D*/
        Vendedor vendedor = new Vendedor();

        do {
            System.out.print("Usuário: ");

            String user = tc.nextLine();
            System.out.print("Senha: ");
            String senha = tc.nextLine();

            ResultSet rt = controle.login(user, senha, "vendedor");
            if (rt != null) {
                try {
                    vendedor.setNome(rt.getString("nome"));
                    vendedor.setId(rt.getInt("id_vendedor"));
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

    /* esse método irá verificar se alguma palavra reservada
     * do SQL está sendo utilizada */
    private static boolean verificaComandoSQL(String s){
        Pattern verificaComandoSQL = Pattern.compile(" *drop +| *insert +| *" +
                "grant +| *update +| *delete +| *select +| *create +| *" +
                "alter +| *union +", Pattern.CASE_INSENSITIVE);
        Matcher matcher = verificaComandoSQL.matcher(s);

        if (matcher.find()){
            System.out.println("PALAVRA NÃO PERMITIDA UTILIZADA! TENTE NOVAMENTE");
            return false;
        }
        return true;
    }
    public static boolean regexEmail(String s){
        //verifica se a string possui alguma palavra reservada sql
        if (verificaComandoSQL(s)) {
            //verifica se o email está dentro do padrão de emails
            if (Pattern.matches("[a-zA-Z0-9.ç]+@(gmail.com|" +
                    "yahoo.com.br|hotmail.com)", s))
                return true;
            else {
                System.out.println("O EMAIL NÃO ESTÁ NO FORMATO SUPORTADO! TENTE NOVAMENTE\n" +
                        "São suportados apenas: 'gmail.com', 'yahoo.com.br', " +
                        "'hotmail.com.br'");
                return false;
            }
        }
        return false;
    }
    public static boolean regexCPF(String s){
        if (Pattern.matches("[0-9]{11}", s))
            return true;
        else {
            System.out.println("INSIRA APENAS 11 NÚMEROS!");
            return false;
        }
    }

    public static boolean regexNome(String s){
        if (verificaComandoSQL(s)){
            return Pattern.matches("[A-Za-z]+", s);
        }
        return false;
    }

    public static void printa(ResultSet rt){
        try{
            ResultSetMetaData rtMetaData = rt.getMetaData();
            int numeroDeColunas = rtMetaData.getColumnCount();

            while (rt.next()) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]\n");
                for (int coluna = 1; coluna <= numeroDeColunas; coluna++) {
                    String nomeDaColuna = rtMetaData.getColumnName(coluna);
                    joiner.add(nomeDaColuna + " = " + rt.getString(coluna));
                }
                System.out.print(joiner.toString());
            }

        } catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }

    public static Cliente cadastroCliente(){

    }
}