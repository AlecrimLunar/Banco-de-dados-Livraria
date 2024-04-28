package Entities;
import Controle.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Vendedor extends GerenciaBd{
    private Integer id;
    private String nome;
    private Long cpf;
    Funcoes funcoes;

    public Vendedor(Integer id, String nome, Long cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        funcoes = new Funcoes();
    }



    public boolean cadastraCliente(Scanner tc) {


        while (true) {
            System.out.println("Insira a seguir as seguintes informações do cliente:");

            System.out.print("Nome: ");
            String nome = tc.nextLine();

            System.out.print("CPF: ");
            String cpf = tc.nextLine();

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
                if (controle.Quantos( "", "cliente", " WHERE usuario = " + "'" + user + "'") > 0)
                    System.out.println("\nJÁ EXISTE UM USUÁRIO COM ESSE NOME");
                else
                    break;
            }

            System.out.print("Senha para acesso ao sistema: ");
            String senha = tc.nextLine();

            System.out.println("\nAS INFORMAÇÕES ABAIXO ESTÃO CORRETAS?\nNome: " + nome + "\nCPF: " +
                    cpf + "\nEndereço: " + rua + " " + numero);
            if (tc.nextLine().equalsIgnoreCase("sim")) {

                String adiciona = "'" + senha + "', '" + user +  "', '" + nome + "', '" +
                        cpf + "', '" + rua + "', " + numero + ", '" + email + "', " + flamengo + ", " +
                        sousa + ", " + onePiece;

                if (controle.Insert("cliente", adiciona, false, "senha, usuario, " +
                        "nome, cpf, rua, numero, email, is_flamengo, is_sousa, one_piece") != -2) {
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
   /* public boolean cadastraCompra(Scanner tc){
        Compra c = new Compra();
        while(true){

            System.out.print("--------------------------------------------------------------" +
                    "\nDigite o codigo do livro: ");
            int idLivro = Integer.parseInt(tc.nextLine());

            if(controle.Quantos( "", "livro", " WHERE id_livro = '" + idLivro + "'") > 0){

                try {

                    ResultSet rt = controle.Select("nome, preco, autor, genero, tipo, " +
                                    "from_mari, quantidade_estoque", "livro",
                            "id_livro", Integer.toString(idLivro));
                    rt.next();

                    //pega a quantidade de livros que tem em estoque
                    int quantidadeEstoque = rt.getInt("quantidade_estoque");
                    //verifica se tem esse livro em estoque
                    if (quantidadeEstoque == 0){
                        System.out.println("ESSE LIVRO NÃO POSSUI CÓPIAS EM ESTOQUE!");
                        continue;
                    }

                    //remove o R$
                    String trataPreco = rt.getString("preco").substring(3);
                    //troca a vírgula por ponto
                    trataPreco = trataPreco.replaceAll(",", ".");

                    Livro l1 = new Livro(idLivro, rt.getString("nome"), Double.parseDouble(trataPreco),
                            rt.getString("autor"), rt.getString("genero"),
                            rt.getString("tipo"), rt.getBoolean("from_mari"));

                    while (true) {
                        System.out.print("\nQuantidade adquirida do livro " + l1.getNome() + ": ");
                        int quantidadeAdiquirida = Integer.parseInt(tc.nextLine());

                        if (quantidadeAdiquirida <= quantidadeEstoque) {
                            c.addLivro(l1, quantidadeAdiquirida);
                            //livroFoiAdquirido(idLivro, quantidadeAdiquirida);
                            break;
                        } else {
                            System.out.println("O NÚMERO DE LIVROS ADQUIRIDOS É MAIOR QUE O NÚMERO" +
                                    "EM ESTOQUE!\nDESEJA TENTAR NOVAMENTE?");
                            if (!tc.nextLine().equalsIgnoreCase("sim"))
                                break;
                        }
                    }

                    rt.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("Livro adicionado");

            } else {

                //adicionei isso aqui pra ter um redirecionamento pra parte de adição de livro
                System.out.println("LIVRO NÃO ENCONTRADO! DESEJA ADICIONAR UM LIVRO?\n");
                if (tc.nextLine().equalsIgnoreCase("sim")){
                    cadastraLivro(tc);
                    continue;
                }

            }
            c.getcompra(); //printa as informaões do livro

            System.out.println("\nO livro adicionado é o correto? ");

            if(tc.nextLine().equalsIgnoreCase("não")){
                c.remove();
            }

            System.out.println("\nJá adicionou todos os livros da compra? ");
            if (tc.nextLine().equalsIgnoreCase("sim")){
                break;
            }

        }
        double precoT = 0;

        for(int i = 0; i < c.getsize(); i++){
            precoT = precoT + c.getLivro(i).getPreco() * c.getQuantidade(i);
        }

        System.out.println("\nPreço total: R$" + precoT + "\nQual a forma de pagamento? ");
        String formaPagamento = tc.nextLine();

        //verifica o cadastro do cliente pra pegar o id dele pra adicionar na compra
        System.out.print("O cliente possui cadastro na loja?\n");
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
            if (rs != null)
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
            int id_carrinho = controle.Insert("carrinho", id_cliente + ", -1", true,
                    "id_cliente, id_compra");
            if (id_carrinho == -2) {
                System.out.print("\nERRO");
                return false;
            }

            // adiciona todo o carrinho no banco e decrementa a quantidade de livros no banco
            for (int i = 0; i < c.getsize(); i++){
                controle.Insert("carrinho_livro", id_carrinho + ", " +
                                c.getLivro(i).getId().toString() + ", " + c.getQuantidade(i) ,false,
                        "id_carrinho, id_livro, quantidade");
            }

            int id_compra = controle.Insert("compra", "'" + formaPagamento
                    + "', date('" + sdf.format(date) + "'), " + precoT + ", " + this.id + ", " + id_carrinho +
                    ", " + id_cliente, true, "forma_pagamento, data, valor," +
                    " id_vendedor, id_carrinho, id_cliente");
            if (id_compra != -2) {
                System.out.println("Compra efetuada");
                controle.update("carrinho", "id_compra", Integer.toString(id_compra),
                        "WHERE id_compra = " + -1);
                c = null;
                return true;
            }
        } catch (Exception e){
            System.out.print("ERRO: " + e);
        }

        c = null;
        return false;
    }*/

    /**
     * Função responsável por realizar a alteração da quantidade de livros
     * em estoque. Essa função deve ser utilizada quando se quer
     * aumentar a quantidade de livros já existentes em estoque.
     */
    public void adicionaLivrosEstoque(Scanner tc){
        ArrayList<Integer> livros = new ArrayList<>();
        ArrayList<Integer> quantidades = new ArrayList<>();
        String testes;

        while (true) {
            System.out.print("""
                    ========================================================
                    BEM-VINDO AO SISTEMA DE ADICIONAR LIVROS.
                    INSIRA AS INFORMAÇÕES A SEGUIR PARA QUE A ADIÇÃO POSSA
                    SER REALIZADA
                    ========================================================
                    """);

            while (true) {
                System.out.print("""
                        QUAL O ID DO LIVRO RECEBIDO?
                        """);

                testes = tc.nextLine();

                if (!funcoes.regexNum(testes)){
                    System.err.print("""
                            INSIRA APENAS UM VALOR NUMÉRICO.
                            ========================================================
                            """);
                } else
                    break;
            }

            livros.add(Integer.parseInt(testes));

            while (true) {
                System.out.print("""
                        QUAL A QUANTIDADE DE LIVROS FOI RECEBIDA?
                        """);

                testes = tc.nextLine();

                if (!funcoes.regexNum(testes)){
                    System.err.print("""
                            INSIRA APENAS UM VALOR NUMÉRICO.
                            ========================================================
                            """);
                } else
                    break;
            }

            quantidades.add(Integer.parseInt(testes));

            System.out.print("""
                    OUTRO LIVRO FOI RECEBIDO?
                    """);

            if (!tc.nextLine().equalsIgnoreCase("sim"))
                break;
        }

        int verifica = -1;
        try{
            do {
                verifica = adicionaQuantidadeLivroEstoque(livros, quantidades);
            } while (verifica == -1);
        } catch (ConexaoException e){
            try {
                close();
            } catch (SQLException f){
                System.err.println("""
                ========================================================
                FALHA CRÍTICA NO SISTEMA!
                A CONEXÃO COM O BANCO DE DADOS APRESENTOU ERROS E
                NÃO FOI POSSÍVEL ENCERRÁ-LA
                ========================================================
                """);
                System.exit(1);
            }
        } catch (NaoTemConexaoException e){
            try{
                criaCon(1);
            } catch (SQLException f){
                System.err.println("""
                ========================================================
                FALHA CRÍTICA NO SISTEMA!
                NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                DE DADOS
                ========================================================
                """);
                System.exit(1);
            }
        }

        if (verifica == 0){
            System.out.print("""
                    NENHUMA ADIÇÃO FOI REALIZADA!
                    ========================================================
                    """);
        } else
            System.out.print("""
                    ADIÇÕES FEITAS COM SUCESSO!
                    ========================================================
                    """);
    }

    /**
     * Função responsável por cadastrar novos livros no sistema.
     */
    public void cadastraLivro(Scanner tc){
        System.out.print("-------------------------------ADICIONAR LIVRO------------------------------\n"+
                "DIGITE O NOME DO LIVRO: ");
        String nome = tc.nextLine();

        System.out.print("O LIVRO É NOVO OU USADO? ");
        String tipo = tc.nextLine();
        if (tipo.equalsIgnoreCase("novo"))
            tipo = "novo";
        else
            tipo = "usado";

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

        String info = "'" + tipo + "', '" + nome + "', " + Integer.toString(quantidade) + ", "
                + Boolean.toString(from_mari) + ", '" + autor + "', '" + genero + "', " + Double.toString(preco);
        ArrayList<String> argumento = new ArrayList<>();
        argumento.add(info);
        int idLivro = -1;

        try {
            idLivro = Insert("livro", argumento,  "tipo, nome, " +
                    "quantidade_estoque, from_mari, autor, genero, preco");
        } catch (ConexaoException e){
            try {
                close();
            } catch (SQLException f){
                System.err.println("""
                ========================================================
                FALHA CRÍTICA NO SISTEMA!
                A CONEXÃO COM O BANCO DE DADOS APRESENTOU ERROS E
                NÃO FOI POSSÍVEL ENCERRÁ-LA
                ========================================================
                """);
                System.exit(1);
            }
        } catch (NaoTemConexaoException e){
            try{
                criaCon(1);
            } catch (SQLException f){
                System.err.println("""
                ========================================================
                FALHA CRÍTICA NO SISTEMA!
                NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                DE DADOS
                ========================================================
                """);
                System.exit(1);
            }
        }
        if (idLivro != -1) {
            System.out.println("\nLIVRO ADICIONADO COM SUCESSO!\nSEU NÚMERO DE CADASTRO É: " + idLivro + "\n");
        } else {
            System.out.println("ERRO NA INSERÇÃO DO LIVRO");
        }
    }

    public void confirmarCompra(Scanner tc){
        ArrayList<Compra> compras;
        try (ResultSet rt = recuperaComprasNaoConfirmadas()){
            compras = trataCompra(rt);
        } catch (SQLException e) {}
    }

    private ArrayList<Compra> trataCompra(ResultSet rt) throws SQLException{
        ArrayList<Compra> retorno = new ArrayList<>();

        while (rt.next()) {
            int idCompra = rt.getInt("id_compra");
            String formaPagamento = rt.getNString("forma_pagamento");
            Date data = rt.getDate("data");
            int valor = rt.getInt("valor");
            int idCarrinho = rt.getInt("id_carrinho");

            retorno.add(new Compra(idCompra, formaPagamento,
                    data, valor, idCarrinho));
        }

        return retorno;
    }

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