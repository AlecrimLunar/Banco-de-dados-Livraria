package Entities;
import Controle.*;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Vendedor {
    private Integer id;
    private String nome;
    private Long cpf;
    private ControlaBD controle;

    public Vendedor() {
        controle = new ControlaBD();
    }

    public boolean cadastraCliente(Scanner tc) {

        loop:
        while (true) {
            System.out.println("Insira a seguir as seguintes informações do cliente:");

            System.out.print("Nome: ");
            String nome = tc.nextLine();

            System.out.print("\nCPF: ");
            long cpf = Long.parseLong(tc.nextLine());

            System.out.print("\nRua: ");
            String rua = tc.nextLine();

            System.out.print("\nNúmero: ");
            int numero = Integer.parseInt(tc.nextLine());

            System.out.print("\nEmail: ");
            String email = tc.nextLine();

            System.out.print("\nTorce para o Flamengo? ");
            boolean flamengo;
            if (tc.nextLine().equalsIgnoreCase("sim"))
                flamengo = true;
            else
                flamengo = false;

            System.out.print("\nÉ nascido em Sousa? ");
            boolean sousa;
            if (tc.nextLine().equalsIgnoreCase("sim"))
                sousa = true;
            else
                sousa = false;

            System.out.print("\nAssiste a One Piece? ");
            boolean onePiece;
            if (tc.nextLine().equalsIgnoreCase("sim"))
                onePiece = true;
            else
                onePiece = false;

            String user = "";
            while (true) {
                System.out.print("\nNome para login no sistema: ");
                user = tc.nextLine();
                if (controle.Existe(user, "cliente", "user"))
                    System.out.print("\nJÁ EXISTE UM USUÁRIO COM ESSE NOME");
                else
                    break;
            }

            System.out.print("\nSenha para acesso ao sistema: ");
            String senha = tc.nextLine();

            System.out.println("\nAS INFORMAÇÕES ABAIXO ESTÃO CORRETAS?\n Nome: " + nome + "\nCPF: " +
                    cpf + "\nEndereço: " + rua + " " + numero);
            if (tc.nextLine().equalsIgnoreCase("sim")) {

                String adiciona = user + ", " + nome + ", " + cpf + ", " + senha + ", " + rua + ", " +
                        numero + ", " + email + ", " + flamengo + ", " + sousa + ", " + onePiece;

                if (controle.Insert("cliente", adiciona)) {
                    System.out.println("CADASTRO CONCLUÍDO COM SUCESSO\n" +
                            "----------------------------------------------------------------------------");
                    return true;
                }
                else
                    return false;

            } else {
                System.out.println("DESEJA INSERIR AS INFORMAÇÕES NOVAMENTE?");
                if (!tc.nextLine().equalsIgnoreCase("sim")) {
                    break loop;
                }


            }
        }
        return false;
    }

    public boolean cadastraCompra(Scanner tc){
        Compra c = new Compra();
        do{

            System.out.println("\nDigite o codigo do livro: ");
            int idLivro = tc.nextInt();

            if(controle.Existe(idLivro + "", "livro", "id")){

                try {

                    ResultSet rt = controle.Select("nome, preco, autor, genero, tipo, from_mari",
                            "livro", "" + idLivro + "", "id");

                    Livro l1 = new Livro(idLivro, rt.getString("nome"), rt.getDouble("preco"),
                            rt.getString("autor"), rt.getString("genero"),
                            rt.getString("tipo"), rt.getBoolean("from_mari"));

                    c.addLivro(l1);
                    rt.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("Livro adicionado");

            } else {

                System.out.println("Livro não encontrado");

            }
            c.getcompra();

            System.out.println("O livro adicionado é o correto? ");
            String in = tc.nextLine();

            if(in.equalsIgnoreCase("não")){
                c.remove();
            }

            System.out.println("\nJá adicionou todos os livros da compra? ");
            in = tc.nextLine();

            if(in.equalsIgnoreCase("sim")){
                double precoT = 0;

                for(int i = 0; i < c.getsize(); i++){
                    precoT = precoT + c.getLivro(i).getPreco();
                }

                System.out.println("Preço total: R$" + precoT + "\nQual a forma de pagamento? ");
                String formaPagamento = tc.nextLine();

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                if(controle.Insert("compra", "DEFAULT, " + precoT + ", " + formaPagamento
                        + ", "+ sdf.format(date) + ", " + getId())) {
                    System.out.println("Compra efetuada");
                }
                break;
            }
        }while(true);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }
}
