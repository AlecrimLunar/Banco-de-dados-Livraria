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

                if (controle.Insert("cliente", adiciona, false) != -2) {
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

            System.out.print("--------------------------------------------------------------" +
                    "\nDigite o codigo do livro: ");
            int idLivro = tc.nextInt();

            if(controle.Existe(idLivro + "", "livro", "id")){

                try {

                    ResultSet rt = controle.Select("nome, preco, autor, genero, tipo, from_mari",
                            "livro", "" + idLivro + "", "id");

                    Livro l1 = new Livro(idLivro, rt.getString("nome"), rt.getDouble("preco"),
                            rt.getString("autor"), rt.getString("genero"),
                            rt.getString("tipo"), rt.getBoolean("from_mari"));

                    System.out.print("\nQuantidade do livro " + l1.getNome() + ": ");
                    c.addLivro(l1, Integer.parseInt(tc.nextLine()));

                    rt.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("Livro adicionado");

            } else {

                //adicionei isso aqui pra ter um redirecionamento pra parte de adição de livro
                System.out.println("LIVRO NÃO ENCONTRADO! DESEJA ADICIONAR UM LIVRO?\n" +
                        "ATENÇÃO!! ISSO IRÁ CANCELAR A OPERAÇÃO ANTERIOR");
                if (tc.nextLine().equalsIgnoreCase("sim")){
                    adicionaLivro();
                    return false;
                }

            }
            c.getcompra();

            System.out.println("O livro adicionado é o correto? ");

            if(tc.nextLine().equalsIgnoreCase("não")){
                c.remove();
            }

            System.out.println("\nJá adicionou todos os livros da compra? ");

            //a partir daqui a compra está finalizada e precisamos pegar as informações para
            //atualizar o banco de dados
            if(tc.nextLine().equalsIgnoreCase("sim")){
                double precoT = 0;

                for(int i = 0; i < c.getsize(); i++){
                    precoT = precoT + c.getLivro(i).getPreco();
                }

                System.out.println("Preço total: R$" + precoT + "\nQual a forma de pagamento? ");
                String formaPagamento = tc.nextLine();

                //verifica o cadastro do cliente pra pegar o id dele pra adicionar na compra
                System.out.println("O cliente possui cadastro na loja?\n");
                if (!tc.nextLine().equalsIgnoreCase("sim")){
                    System.out.println("PARA REALIZAÇÃO DA COMPRA É NECESSÁRIO ESTAR CADASTRADO!\n" +
                            "REALIZE O CADASTRO DO CLIENTE");

                    cadastraCliente(tc);

                    System.out.print("-----------------------------------------------------------\n");
                }

                String user;
                String senha;
                while (true) {
                    System.out.print("Usuário: ");
                    user = tc.nextLine();
                    System.out.print("\nSenha: ");
                    senha = tc.nextLine();
                    if (controle.login(user, senha))
                        break;
                    else
                        System.out.print("INFORMAÇÕES INCORRETAS! TENTE NOVAMENTE");
                }

                try {
                //pega o id do cliente
                    ResultSet rs = controle.Select("id_cliente", "cliente",
                        "login_cliente", user);
                    String id_cliente = rs.getString("id_cliente");
                    rs.close();

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    //insere no carrinho e pega o id dele
                    int id_carrinho = controle.Insert("carrinho", id_cliente + ", 0", true);
                    if (id_carrinho == -2) {
                        System.out.print("\nERRO");
                        return false;
                    }

                    /* adiciona todo o carrinho no banco */
                    //tem q fazer um update para decrementar a quantidade d livros no banco
                    for (int i = 0; i < c.getsize(); i++){
                        controle.Insert("carrinho_livro", id_carrinho + ", " +
                                c.getLivro(i).getId().toString() + ", " + c.getQuantidade(i) ,false);
                    }

                    if (controle.Insert("compra", "DEFAULT, " + formaPagamento
                            + ", " + sdf.format(date) + ", " + precoT + ", " + this.id + ", " + id_carrinho +
                            ", " + id_cliente, false) != -2) {
                        System.out.println("Compra efetuada");
                        c = null;
                        return true;
                    }
                } catch (Exception e){
                    System.out.print("ERRO: " + e);
                }
                break;
            }
        }while(true);
        c = null;
        return false;
    }

    /* ta faltando implementar esses métodos */
    public void adicionaLivro(){}
    public void removeLivro () {}
    public void livroFoiAdquirido () {} //essa função faz update no banco sobre a quantidade dos livros

    public void adicionaLivro_noEstoque() {} //aqui adiciona quando já existe
    public void adicionaNovoLivro_noEstoque() {} //aqui adiciona quando n existe. pode ser uma função privada
    //que só é chamada da adicionaLivro_noEstoque

    public void alteraCliente(){}
    public void removeCliente() {}


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
