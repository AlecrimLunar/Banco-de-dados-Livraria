package Entities;

import Controle.ConexaoException;
import Controle.Funcoes;
import Controle.GerenciaBd;
import Controle.NaoTemConexaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DonoLivraria extends GerenciaBd {

    private String nome;
    private String cpf;
    private static final int qualCon = 2;
    private static final Funcoes funcoes = new Funcoes(qualCon);

    public DonoLivraria(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public void MenuDono(Scanner tc){
        setUsuarioBanco(qualCon);
        try{
            criaCon(qualCon);
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
        System.out.print("========================================================\n" +
                "BEM-VINDO A SEU MENU, " + nome + "!\n");

        do{

            System.out.print("""

                    QUAL A LEITURA DE HOJE?
                    ========================================================
                    1 - Alterar informações
                    2 - Cadastrar vendedor
                    3 - Remove vendedor
                    4 - Relatorio de vendas
                    0 - Sair
                    ========================================================
                    """);
            int a;

            while(true) {

                String resp = tc.nextLine();

                if(funcoes.regexNum(resp)) {
                    a = Integer.parseInt(resp);
                    break;
                }
                else
                    System.out.println("Digite apenas números!");
            }

            switch (a){
                case 1 -> alteraDono(tc);

                case 2 -> cadastraVendedor(tc);

                case 3 -> removeVendedor(tc);

                case 4 -> {}

                case 0 -> {
                    System.out.print("========================================================");
                    return;
                }

                default -> System.out.println("OPÇÃO INVÁLIDA!");
            }

        }while(true);
    }

    public void cadastraVendedor(Scanner tc){

        while (true) {
            System.out.print("INSIRA AS INFORMAÇÕES:\nNome: ");

            String nome;
            while (true) {
                nome = tc.nextLine();

                if (funcoes.regexNome(nome))
                    if (nome.length() <= 30)
                        break;
                    else
                        System.err.print("""
                                O nome deve possuir no máximo 30 caracteres!
                                """);
                else {
                    System.err.print("Insira um nome válido! Apenas caracteres são aceitos.\n");
                }
            }

            System.out.print("cpf (Apenas números): ");
            String cpf;
            while (true) {
                cpf = tc.nextLine();

                if (funcoes.regexCPF(cpf))
                    break;
            }

            System.out.print("Nome de acesso: ");

            String user;
            while (true) {
                user = tc.nextLine();

                if (funcoes.regexNome(user))
                    if (user.length() <= 15)
                        break;
                    else
                        System.err.print("""
                                O nome de usuário pode conter no máximo 15 caracteres!
                                """);
                else
                    System.err.print("Insira um nome válido! Apenas caracteres são aceitos.\n");
            }

            System.out.print("Senha de acesso: ");
            String senha;
            while (true){
                senha = tc.nextLine();

                if (funcoes.verificaComandoSQL(senha))
                    if (senha.length() <= 15)
                        break;
                    else
                        System.err.print("""
                                A senha deve conter no máximo 15 caracteres!
                                """);
            }


            System.out.print("-------------------------------------------------------------------------" +
                    "\n\nMUITO BEM, VERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS. SE SIM DIGITE " +
                    "'Sim', SE NÃO DIGITE 'Não'\nNome: " + nome + "\ncpf: " + cpf + "\n");

            if (tc.nextLine().equalsIgnoreCase("sim")) {
                String insert = "'" + nome + "', '" + user + "', '" + cpf + "', '" + senha + "'";

                ArrayList<String> infos = new ArrayList<>();
                infos.add(insert);

                int verificaInsert;
                do {
                    try {
                        verificaInsert = Insert("vendedor", infos, "nome, usuario, cpf, senha");
                    } catch (NaoTemConexaoException e) {
                        funcoes.trataException(e, qualCon);
                        System.out.print("""
                             ========================================================
                             Não foi possível inserir as informações no banco
                             devido a problemas com a conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
                        return;

                    } catch (ConexaoException e) {
                        funcoes.trataException(e, qualCon);
                        System.out.print("""
                             ========================================================
                             Não foi possível atualizar as informações no banco
                             devido a problemas com a conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
                        return;
                    }
                } while(verificaInsert == -1);

                if ( verificaInsert == 1) {
                    System.out.println("CADASTRO CONCLUÍDO COM SUCESSO! PARA LOGAR, UTILIZE O USUÁRIO: " +
                            user + " E A " +
                            "SENHA INFORMADA.");
                    break;
                } else
                    System.out.print("""
                            NÃO FOI POSSÍVEL REALIZAR O CADASTRO!
                            TENTE NOVAMENTE MAIS TARDE.
                            """);
            }
        }
    }

    public void removeVendedor(Scanner tc){
        System.out.print("""
                ========================================================
                Qual o código do vendedor que será removido?
                """);

        String id;
        while (true) {
            id = tc.nextLine();
            if (!funcoes.regexNum(id)) {
                System.err.print("""
                        Insira apenas números!
                        """);
            } else
                break;
        }

        Vendedor vendedor = recuperaVendedor(Integer.parseInt(id));

        if (vendedor == null)
            return;

        System.out.print("""
                --------------------------------------------------------
                O vendedor abaixo é o que você deseja remover?
                """);
        System.out.print(vendedor);

        boolean vaiRemover = false;
        while (true){
            String entrada = tc.nextLine();
                if (entrada.equalsIgnoreCase("sim")) {
                    vaiRemover = true;
                    break;
                }
                else if (entrada.equalsIgnoreCase("nao") ||
                        entrada.equalsIgnoreCase("não")) {
                    break;
                }
                else
                    System.out.print("Insira apenas 'sim' ou 'não' como resposta\n");
        }

        if (vaiRemover){
            int verifica;
            do {
                try {
                    verifica = delete("vendedor", "id_vendedor = " + vendedor.getId());
                } catch (ConexaoException e) {
                    funcoes.trataException(e, qualCon);
                    System.out.print("""
                            ========================================================
                            Não foi possível recuperar as informações do banco de
                            dados devido a problemas com a conexão.
                            Tente novamente mais tarde.
                            ========================================================
                            """);
                    return;

                } catch (NaoTemConexaoException e) {
                    funcoes.trataException(e, qualCon);
                    System.out.print("""
                            ========================================================
                            Não foi possível recuperar as informações do banco de
                            dados devido a problemas com a conexão.
                            Tente novamente mais tarde.
                            ========================================================
                            """);
                    return;
                }
            } while (verifica == -1);

            if (verifica == 1){
                System.out.print("""
                        --------------------------------------------------------
                        Remoção realizada com sucesso!
                        --------------------------------------------------------
                        """);
            } else
                System.out.print("""
                        --------------------------------------------------------
                        A remoção não foi realizada!
                        --------------------------------------------------------
                        """);
        }
    }

    private Vendedor recuperaVendedor(int idVendedor){
        try (ResultSet rt = getVendedor(idVendedor)){
            return new Vendedor(rt);
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Não foi possível recuperar as informações do banco de
                             dados devido a problemas com a conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        } catch (ConexaoException e) {
            funcoes.trataException(e, qualCon);
            System.out.print("""
                             ========================================================
                             Não foi possível recuperar as informações do banco de
                             dados devido a problemas com a conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;

        } catch (NaoTemConexaoException e) {
            funcoes.trataException(e, qualCon);
            System.out.print("""
                             ========================================================
                             Não foi possível recuperar as informações do banco de
                             dados devido a problemas com a conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
            return null;
        }
    }

    private void alteraDono(Scanner tc){
        int a;
        try {
            loop: while (true) {
                System.out.print("================================\n" +
                        this + "\nDeseja Alterar alguma informação?");
                if (tc.nextLine().equalsIgnoreCase("sim")) {

                    System.out.println("""
                            SELECIONE O CAMPO QUE DESEJA ALTERAR
                            1 - Nome
                            2 - CPF
                            0 - Voltar ao menu principal""");
                    while (true) {

                        String resp = tc.nextLine();

                        if (funcoes.regexNum(resp)) {
                            a = Integer.parseInt(resp);
                            break;
                        } else
                            System.out.println("Digite apenas números!");
                    }


                    System.out.println("===========================================================================");

                    switch (a) {
                        case 1 -> {
                            ArrayList<String> coluna = new ArrayList<>();
                            ArrayList<String> novoU = new ArrayList<>();
                            ArrayList<String> condicao = new ArrayList<>();
                            String novo;

                            while (true) {

                                System.out.print("Insira o novo nome: ");
                                novo = tc.nextLine();

                                String[] nomes = nome.split(" ");
                                boolean foi = true;

                                for (String aux : nomes) {
                                    if (!funcoes.regexNome(aux)) {
                                        System.out.print("Escreva um nome válido! Sem números.");
                                        foi = false;
                                        break;
                                    }
                                }
                                if (foi) {
                                    break;
                                }

                            }

                            coluna.add("nome");
                            novoU.add("'" + novo + "'");
                            condicao.add("cpf = " + cpf);
                            variosUpdates("donoLivraria", coluna, novoU, condicao);
                            nome = novo;
                        }

                        case 2 -> {
                            ArrayList<String> coluna = new ArrayList<>();
                            ArrayList<String> novoU = new ArrayList<>();
                            ArrayList<String> condicao = new ArrayList<>();
                            String novo;

                            while (true) {
                                System.out.print("Insira o novo CPF: ");
                                novo = tc.nextLine();

                                if (!funcoes.regexCPF(novo))
                                    System.out.print("Escreva um CPF válido! Somente números.");
                                else break;
                            }

                            coluna.add("cpf");
                            novoU.add("'" + novo + "'");
                            condicao.add("cpf = " + cpf);
                            variosUpdates("donoLivraria", coluna, novoU, condicao);
                            cpf = novo;
                        }
                        case 0 -> {
                            break loop;
                        }
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
            }
        } catch (NaoTemConexaoException e) {
            funcoes.trataException(e, 2);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (ConexaoException e) {
            funcoes.trataException(e, 2);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
    }

    public String toStrting() {
        return "Nome: " + nome + "\nCPF: " + cpf + "\n";
    }

}
