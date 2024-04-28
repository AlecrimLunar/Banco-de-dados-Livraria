package Entities;

import Controle.GerenciaBd;

import java.sql.ResultSet;
import java.util.Scanner;

public class DonoLivraria extends GerenciaBd {
    public void cadastraVendedor(Scanner tc){
        while (true) {
            System.out.print("INSIRA AS INFORMAÇÕES:\nNome: ");
            String nome = tc.nextLine();

            System.out.print("CPF (Apenas números): ");
            String CPF = tc.nextLine();

            System.out.print("Nome de acesso: ");
            String user = tc.nextLine();

            System.out.print("Senha de acesso: ");
            String senha = tc.nextLine();


            System.out.print("-------------------------------------------------------------------------" +
                    "\n\nMUITO BEM, VERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS. SE SIM DIGITE " +
                    "'Sim', SE NÃO DIGITE 'Não'\nNome: " + nome + "\nCPF: " + CPF + "\n");

            if (tc.nextLine().equalsIgnoreCase("sim")) {
                String insert = "'" + nome + "', '" + user + "', '" + CPF + "', '" + senha + "'";

                if (controle.Insert("vendedor", insert, false, "nome, usuario, cpf, senha") != -2) {
                    System.out.println("CADASTRO CONCLUÍDO COM SUCESSO! PARA LOGAR, UTILIZE O USUÁRIO: " +
                            user + " E A " +
                            "SENHA INFORMADA.");
                    break;
                }
            }
        }
    }

    public void removeVendedor(Scanner tc){
        while (true) {
            System.out.print("------------------------------------------------------------------" +
                    "\nID DO VENDEDOR A SER REMOVIDO: ");
            String idVendedor = tc.nextLine();

            if (controle.Quantos(idVendedor, "vendedor", "") > 0) {
                try {
                    ResultSet rt = controle.Select("nome, cpf", "vendedor", idVendedor,
                            "id_vendedor");
                    System.out.println("O VENDEDOR ABAIXO É O VENDEDOR QUE DESEJA REMOVER? REPONDA COM 'Sim' ou 'Não' " +
                            "\nNome: " + rt.getString("nome") + "\nCPF: " + rt.getString("cpf"));

                    if (tc.nextLine().equalsIgnoreCase("sim")) {
                        if (controle.delete("vendedor", "id_vendedor", idVendedor, false))
                            System.out.println("REMOÇÃO FEITA COM SUCESSO!!" +
                                    "\n---------------------------------------------------------------------");
                        else
                            System.out.println("ERRO!! TENTE NOVAMENTE" +
                                    "\n---------------------------------------------------------------------");
                    }

                } catch (Exception e) {
                    System.out.println("ERRO: " + e);
                }

                System.out.println("DESEJA REMOVER OUTRO VENDEDOR? REPONDA COM 'Sim' ou 'Não'");
                if (!tc.nextLine().equalsIgnoreCase("sim")) {
                    return;
                }
            }
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
