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

    private static final int qualCon = 2;
    private static final Funcoes funcoes = new Funcoes();

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

                int verificaInsert = -1;
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
            int verifica = -1;
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

    public void removeLivro (Scanner tc) {
        while (true) {
            System.out.print("------------------------------------------------------------------" +
                    "\nID DO LIVRO A SER REMOVIDO: ");
            String idLivro = tc.nextLine();

            if (controle.Quantos(idLivro, "livro", "") > 0) {
                try {
                    ResultSet rt = controle.Select("nome, autor, tipo", "livro", idLivro, "id_livro");
                    rt.next();
                    System.out.println("O LIVRO ABAIXO É O LIVRO QUE DESEJA REMOVER? REPONDA COM 'Sim' ou 'Não' " +
                            "\nNome: " + rt.getString("nome") + "\nAutor: " + rt.getString("autor") +
                            "\nTipo: " + rt.getString("tipo"));

                    if (tc.nextLine().equalsIgnoreCase("sim")) {
                        if (controle.delete("livro", "id_livro", idLivro, false))
                            System.out.println("REMOÇÃO FEITA COM SUCESSO!!" +
                                    "\n---------------------------------------------------------------------");
                        else
                            System.out.println("ERRO!! TENTE NOVAMENTE" +
                                    "\n---------------------------------------------------------------------");
                    }

                } catch (Exception e) {
                    System.out.println("ERRO: " + e);
                }

                System.out.println("DESEJA REMOVER OUTRO LIVRO? REPONDA COM 'Sim' ou 'Não'");
                if (!tc.nextLine().equalsIgnoreCase("sim")) {
                    return;
                }
            }
        }
    }

}
