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


        while (true) {
            System.out.println("Insira a seguir as seguintes informações do cliente:");

            System.out.print("Nome: ");
            String nome = tc.nextLine();

            System.out.print("CPF: ");
            long cpf = Long.parseLong(tc.nextLine());

            System.out.print("Rua: ");
            String rua = tc.nextLine();

            System.out.print("Número: ");
            int numero = Integer.parseInt(tc.nextLine());

            System.out.print("Email: ");
            String email = tc.nextLine();

            System.out.println("\nRESPONDA AS PERGUNTAS ABAIXO COM SIM OU NÃO");
            System.out.print("Torce para o Flamengo? ");
            boolean flamengo = tc.nextLine().equalsIgnoreCase("sim");

            System.out.print("É nascido em Sousa? ");
            boolean sousa = tc.nextLine().equalsIgnoreCase("sim");

            System.out.print("Assiste a One Piece? ");
            boolean onePiece = tc.nextLine().equalsIgnoreCase("sim");

            System.out.println("\nINSIRA ABAIXO SUAS INFORMAÇÕES PARA LOGIN");
            String user;

            while (true) {
                System.out.print("Nome para login no sistema: ");
                user = tc.nextLine();
                if (controle.Quantos( "usuario = " + "'" + user + "'", "cliente") > 0)
                    System.out.println("\nJÁ EXISTE UM USUÁRIO COM ESSE NOME");
                else
                    break;
            }

            System.out.print("\nSenha para acesso ao sistema: ");
            String senha = tc.nextLine();

            System.out.println("\nAS INFORMAÇÕES ABAIXO ESTÃO CORRETAS?\nNome: " + nome + "\nCPF: " +
                    cpf + "\nEndereço: " + rua + " " + numero);
            if (tc.nextLine().equalsIgnoreCase("sim")) {

                String adiciona = "DEFAULT, '" + senha + "', '" + user +  "', '" + nome + "', " +
                        cpf + ", '" + rua + "', " + numero + ", '" + email + "', " + flamengo + ", " +
                        sousa + ", " + onePiece;

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
                    break;
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

            if(controle.Quantos( "id_livro = '" + idLivro + "'", "livro") > 0){

                try {

                    ResultSet rt = controle.Select("nome, preco, autor, genero, tipo, from_mari",
                            "livro", "id_livro", Integer.toString(idLivro));

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
                    cadastraLivro(tc);
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
                ResultSet rs;
                while (true) {
                    System.out.print("Usuário: ");
                    user = tc.nextLine();
                    System.out.print("Senha: ");
                    senha = tc.nextLine();
                    rs = controle.login(user, senha, "cliente");
                    if (!rs.equals(null))
                        break;
                    else
                        System.out.print("INFORMAÇÕES INCORRETAS! TENTE NOVAMENTE");
                }

                try {
                //pega o id do cliente
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
                    for (int i = 0; i < c.getsize(); i++){
                        controle.Insert("carrinho_livro", id_carrinho + ", " +
                                c.getLivro(i).getId().toString() + ", " + c.getQuantidade(i) ,false);
                        //livroFoiAdquirido(); //função sem implementação
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
    public void cadastraLivro(Scanner tc){
        System.out.print("-------------------------------ADICIONAR LIVRO------------------------------\n"+
                "DIGITE O NOME DO LIVRO: ");
        String nome = tc.nextLine();

        System.out.print("O LIVRO É NOVO OU USADO? ");
        String tipo = tc.nextLine();

        System.out.print("QUANTOS SÃO? ");
        int quantidade = Integer.parseInt(tc.nextLine());

        System.out.print("O LIVRO FOI FEITO EM MARI? ");
        boolean from_mari = tc.nextLine().equalsIgnoreCase("sim");

        System.out.print("DIGITE O NOME DO AUTOR: ");
        String autor = tc.nextLine();

        System.out.print("DIGITE O GÊNERO: ");
        String genero = tc.nextLine();

        System.out.print("DIGITE O PREÇO (apenas números): ");
        Double preco = Double.parseDouble(tc.nextLine());

        String info = "DEFAULT, '" + tipo + "', '" + nome + "', " + Integer.toString(quantidade) + ", "
                + Boolean.toString(from_mari) + ", '" + autor + "', '" + genero + "', " + Double.toString(preco);
        int idLivro = controle.Insert("livro", info, true);
        if (idLivro != -2) {
            System.out.println("\nLIVRO ADICIONADO COM SUCESSO!\nSEU NÚMERO DE CADASTRO É: " + idLivro + "\n");
        } else {
            System.out.println("ERRO NA INSERÇÃO DO LIVRO");
        }
    }
    public void removeLivro () {}

    public void alteraLivro(Scanner tc){
        try {
            while (true) {
                System.out.println("----------------------------------------------------------------------" +
                        "SELECIONE O CAMPO QUE DESEJA ALTERAR \n1 - Nome \n2 - Autor \n3 - Gênero \n 4 - Tipo" +
                        "\n5 - É de Mari? \n6 - Preço \n7 - Voltar ao menu principal");
                int escolha = Integer.parseInt(tc.nextLine());
                System.out.println("---------------------------------------------------------------------------");

                switch (escolha) {
                    case 1 -> {
                        System.out.print("ID DO LIVRO QUE DESEJA ALTERAR: ");
                        String id = tc.nextLine();

                        System.out.print("Insira o novo nome: ");
                        String novo = tc.nextLine();

                        if (controle.update("livro", "nome", "'" + novo + "'",
                                "WHERE id_livro = " + id)) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 2 -> {
                        System.out.print("ID DO LIVRO QUE DESEJA ALTERAR: ");
                        String id = tc.nextLine();

                        System.out.print("Insira o novo nome do autor: ");
                        String novo = tc.nextLine();

                        if (controle.update("livro", "autor", "'" + novo + "'",
                                "WHERE id_livro = " + id)) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 3 -> {
                        System.out.print("ID DO LIVRO QUE DESEJA ALTERAR: ");
                        String id = tc.nextLine();

                        System.out.print("Insira o novo gênero: ");
                        String novo = tc.nextLine();

                        if (controle.update("livro", "genero", "'" + novo + "'",
                                "WHERE id_livro = " + id)) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 4 -> {
                        System.out.print("ID DO LIVRO QUE DESEJA ALTERAR: ");
                        String id = tc.nextLine();

                        System.out.print("Insira o novo tipo: ");
                        String novo = tc.nextLine();

                        if (controle.update("livro", "tipo", "'" + novo + "'",
                                "WHERE id_livro = " + id)) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 5 -> {
                        System.out.print("ID DO LIVRO QUE DESEJA ALTERAR: ");
                        String id = tc.nextLine();

                        System.out.println("RESPONDA COM 'sim' OU 'não'");
                        System.out.print("O livro foi escrito em Mari? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");

                        if (controle.update("livro", "nome", Boolean.toString(novo),
                                "WHERE id_livro = " + id)) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 6 -> {
                        System.out.println("DESEJA ALTERAR O PREÇO DE TODOS OS LIVROS?");
                        if (tc.nextLine().equalsIgnoreCase("sim")){
                            System.out.println("EM QUANTOS PORCENTO (%) O PREÇO DOS LIVROS IRÃO AUMENTAR?");
                            double aumento = Integer.parseInt(tc.nextLine());
                            aumento = aumento/100 + 1;

                            if (controle.update("livro", "preco", "preco * " +
                                    Double.toString(aumento), "")) {
                                System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                            } else
                                System.out.println("ERRO! TENTE NOVAMENTE");
                        } else {
                            System.out.print("ID DO LIVRO QUE DESEJA ALTERAR: ");
                            String id = tc.nextLine();

                            System.out.print("Insira o novo preço do livro: ");
                            double novo = Double.parseDouble(tc.nextLine());

                            if (controle.update("livro", "nome", Double.toString(novo),
                                    "WHERE id_livro = " + id)) {
                                System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                            } else
                                System.out.println("ERRO! TENTE NOVAMENTE");
                        }
                    }

                    case 7 -> {
                        return;
                    }
                    default -> System.out.println("OPÇÃO INVÁLIDA!");
                }
            }
        }catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }
    public void livroFoiAdquirido () {} //essa função faz update no banco sobre a quantidade dos livros

    public void adicionaLivro_noEstoque() {} //aqui adiciona quando já existe

    public void removeVendedor(){}

    public void alteraVendedor(Scanner tc){
        try {
            while (true) {
                System.out.println("----------------------------------------------------------------------" +
                        "SELECIONE O CAMPO QUE DESEJA ALTERAR \n1 - Nome \n2 - CPF" +
                        "\n3. Voltar ao menu principal");
                int escolha = Integer.parseInt(tc.nextLine());
                System.out.println("---------------------------------------------------------------------------");

                switch (escolha) {
                    case 1 -> {
                        System.out.print("Insira o novo nome: ");
                        String novo = tc.nextLine();
                        if (controle.update("vendedor", "nome", "'" + novo + "'",
                                "WHERE id_vendedor = " + Integer.toString(this.id))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 2 -> {
                        System.out.print("Insira o novo CPF: ");
                        int novo = Integer.parseInt(tc.nextLine());
                        if (controle.update("vendedor", "cpf", Integer.toString(novo),
                                "WHERE id_vendedor = " + Integer.toString(this.id))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 3 -> {
                        return;
                    }
                    default -> System.out.println("OPÇÃO INVÁLIDA!");
                }
            }
        }catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }
    public void alteraCliente(Scanner tc){
        try {
            System.out.println("----------------------------------------------------" +
                    "\nLOGIN CLIENTE:");
            String user;
            String senha;
            ResultSet rs;
            while (true) {
                System.out.print("Usuário: ");
                user = tc.nextLine();
                System.out.print("Senha: ");
                senha = tc.nextLine();
                rs = controle.login(user, senha, "cliente");
                if (!rs.equals(null))
                    break;
                else
                    System.out.print("INFORMAÇÕES INCORRETAS! TENTE NOVAMENTE");
            }
            System.out.println("LOGIN REALIZADO COM SUCESSO\n" +
                    "---------------------------------------------------");
            while (true) {
                System.out.println("SELECIONE O CAMPO QUE DESEJA ALTERAR \n1 - Nome \n2 - CPF \n3 - Rua \n4 - Número " +
                        "\n5 - Email \n6 - Torcer para o Flamengo \n7 - Nascer em Sousa \n8 - Assistir a One Piece " +
                        "\n9 - Voltar ao menu principal");
                int escolha = Integer.parseInt(tc.nextLine());
                System.out.println("---------------------------------------------------------------------------");

                switch (escolha) {
                    case 1 -> {
                        System.out.print("Insira o novo nome: ");
                        String novo = tc.nextLine();
                        if (controle.update("cliente", "nome", "'" + novo + "'",
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 2 -> {
                        System.out.print("Insira o novo CPF: ");
                        int novo = Integer.parseInt(tc.nextLine());
                        if (controle.update("cliente", "cpf", Integer.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 3 -> {
                        System.out.print("Insira a nova rua: ");
                        String novo = tc.nextLine();
                        if (controle.update("cliente", "rua", "'" + novo + "'",
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 4 -> {
                        System.out.print("Insira o novo número de residência: ");
                        int novo = Integer.parseInt(tc.nextLine());
                        if (controle.update("cliente", "numero", Integer.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 5 -> {
                        System.out.print("Insira o novo email: ");
                        String novo = tc.nextLine();
                        if (controle.update("cliente", "email", "'" + novo + "'",
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 6 -> {
                        System.out.print("RESPONDA COM 'sim' OU 'não'\nTorce para o flamengo? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");
                        if (controle.update("cliente", "is_flamengo",  Boolean.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 7 -> {
                        System.out.print("RESPONDA COM 'sim' OU 'não'\nNasceu em Sousa? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");
                        if (controle.update("cliente", "is_sousa",  Boolean.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 8 -> {
                        System.out.print("RESPONDA COM 'sim' OU 'não'\nAssiste a One Piece? ");
                        boolean novo = tc.nextLine().equalsIgnoreCase("sim");
                        if (controle.update("cliente", "one_piece",  Boolean.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("ALTERAÇÃO REALIZADA COM SUCESSO!");
                        } else
                            System.out.println("ERRO! TENTE NOVAMENTE");
                    }

                    case 9 -> {
                        return;
                    }
                    default -> System.out.println("OPÇÃO INVÁLIDA!");
                }
            }
        }catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }

    public void removeCliente() {}

    public void printLivro(){controle.printa("livro");}

    public void printCliente(){controle.printa("cliente");}

    public void printVendedor(){controle.printa("vendedor");}

    public void printCompra(){controle.printa("compra");}


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
