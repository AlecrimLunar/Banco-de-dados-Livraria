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

    /**
     * Função que exibe o menu do vendedor e mostra as coisas
     * que o vendedor pode realizar.
     * @param tc o scanner para as entradas do usuário.
     */
    public void menuVendedor(Scanner tc){

        System.out.print("========================================================" + "\n" +
                "BEM-VINDO AO MENU DE VENDEDOR");

        do{

            System.out.println("""

                    QUAL A LEITURA DE HOJE?
                    (ESCOLHA UMA DAS OPÇÕES ABAIXO)
                    --------------------------------------------------------
                    1 - Cadastrar livro
                    2 - Alterar informação de um livro
                    3 - Atualizar o estoque
                    4 - Atender a solicitações de compras
                    5 - Sair
                    --------------------------------------------------------
                    """);
            int a = Integer.parseInt(tc.nextLine());

            switch (a){
                case 1 -> cadastraLivro(tc);

                case 2 -> alteraQualLivro(tc);

                case 3 -> adicionaLivrosEstoque(tc);

                case 4 -> confirmaCompras(tc);

                case 5 -> {
                    System.out.print("========================================================");
                    return;
                }

                default -> System.out.println("OPÇÃO INVÁLIDA!");
            }
        }while(true);

    }

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
            try {
                criaCon(1);
            } catch (SQLException f) {
                System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                            DE DADOS
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
        double preco = Double.parseDouble(tc.nextLine());

        String info = "'" + tipo + "', '" + nome + "', " + Integer.toString(quantidade) + ", "
                + Boolean.toString(from_mari) + ", '" + autor + "', '" + genero + "', " + Double.toString(preco);
        ArrayList<String> argumento = new ArrayList<>();
        argumento.add(info);
        int idLivro = -1;

        try {
            idLivro = Insert("livro", argumento,  "tipo, nome, " +
                    "quantidade_estoque, from_mari, autor, genero, preco");
        } catch (ConexaoException e) {
            trataException(e);

        } catch (NaoTemConexaoException e) {
            trataException(e);

        }
        if (idLivro != -1) {
            System.out.println("\nLIVRO ADICIONADO COM SUCESSO!\nSEU NÚMERO DE CADASTRO É: " + idLivro + "\n");
        } else {
            System.out.println("ERRO NA INSERÇÃO DO LIVRO");
        }
    }

    /**
     * Função que realiza a confirmação das compras.
     * @param tc o scanner para as entradas do usuário.
     */
    public void confirmaCompras(Scanner tc) {
        ArrayList<Compra> compras = null;

        try {
            compras = comprasNaoConfirmadas();
        } catch (SQLException e){
            System.err.print("""
                    Não foi possível recuperar as compras!
                    Tente novamente mais tarde.
                    """);
        }

        if (compras == null)
            return;

        if (!compras.isEmpty()) {
            for (Compra compraAtual : compras) {
                compraAtual.printCompras();
                System.out.print("Deseja confirmar essa compra?\n");

                if (tc.nextLine().equalsIgnoreCase("sim")) {
                    int verifica = -1;
                    try {

                        do {
                            verifica = confirmaCompra(compraAtual.getIdCompra(),
                                    this.id);
                        } while (verifica == -1);

                    } catch (ConexaoException e) {
                        trataException(e);

                    } catch (NaoTemConexaoException e) {
                        trataException(e);

                    }

                    if (verifica == 1) {
                        System.out.print(
                                "\nCompra confirmada com sucesso!\n");
                    } else
                        System.out.print("""

                                Não foi possível efetuar a confirmação da compra!
                                Tente novamente mais tarde.
                                """);
                } else {
                    int verifica = -1;
                    try {
                        do {
                            verifica = recusaCompra(compraAtual.getIdCompra(), this.id);
                        } while (verifica == -1);
                    } catch (ConexaoException e) {
                        trataException(e);

                    } catch (NaoTemConexaoException e) {
                        trataException(e);

                    }

                    if (verifica == 1) {
                        System.out.print(
                                "\nCompra confirmada com sucesso!\n");
                    } else
                        System.out.print("""

                                Não foi possível efetuar a confirmação da compra!
                                Tente novamente mais tarde.
                                """);
                }

                System.out.print("""
                        --------------------------------------------------------
                        Deseja ir para a próxima compra as ser confirmada?
                        """);
                if (!tc.nextLine().equalsIgnoreCase("sim")) {
                    break;
                }
            }
        }
        System.out.print("""
                ========================================================
                Todas as compras pendentes foram verificadas!
                ========================================================
                """);
    }


    /**
     * Função responsável por recuperar as compras não confirmadas.
     * @return Um ArrayList contendo as compras que não foram confirmadas
     * ainda.<br>
     * Null caso algum erro tenha acontecido
     * @throws SQLException
     */
    private ArrayList<Compra> comprasNaoConfirmadas() throws SQLException{
        ArrayList<Compra> compras = new ArrayList<>();

        try (ResultSet rt = recuperaComprasNaoConfirmadas()) {
            while (rt.next()) {
                int idCompra = rt.getInt("id_compra");
                String formaPagamento = rt.getNString("forma_pagamento");
                Date data = rt.getDate("data");
                int valor = rt.getInt("valor");
                int idCarrinho = rt.getInt("id_carrinho");

                compras.add(new Compra(idCompra, formaPagamento,
                        data, valor, idCarrinho));
            }
        } catch (ConexaoException e) {
            trataException(e);
        } catch (NaoTemConexaoException e) {
            trataException(e);
        }

        for (int i = 0; i <compras.size(); ++i){
            try(ResultSet rt = recuperaLivrosCompras(compras.get(i).getIdCompra())){
                compras.get(i).preencheLivrosAdquiridos(rt);
            } catch (ConexaoException e) {
                trataException(e);

            } catch (NaoTemConexaoException e) {
                trataException(e);

            } catch (SQLException e) {
                System.err.println("""
                            ========================================================
                            NÃO FOI POSSÍVEL RECUPERAR OS DADOS.
                            TENTE NOVAMENTE MAIS TARDE
                            ========================================================
                            """);
                return null;
            }
        }

        return compras;
    }

    /**
     * Função inicial da alteração de um livro.<br>
     * Irá receber do usuário qual o livro que ele deseja
     * alterar.
     * @param tc o scanner para as entradas do usuário.
     */
    public void alteraQualLivro(Scanner tc){
        String entrada;
        System.out.print("""
                ========================================================
                Bem-vindo ao menu de alterar livros!
                """);
        while (true) {
            System.out.print("--------------------------------------------------------\n" +
                    "Insira o código do livro a ser alterado ou -1 caso\n" +
                    "queira sair.\n");
            do {
                entrada = tc.nextLine();
                if (!funcoes.regexNum(entrada))
                    System.err.print("\nInsira apenas números!\n");
                else break;

            } while (true);

            if (Integer.parseInt(entrada) == -1)
                break;

            Livro livro = recebeLivro(Integer.parseInt(entrada));

            if (livro == null) {
                System.out.print("""
                        Nenhum livro encontrado.
                        """);
                return;
            }

            System.out.print("O livro abaixo é o livro que você deseja alterar?\n" +
                    livro);

            if (!tc.nextLine().equalsIgnoreCase("sim")) {
                continue;
            }

            alteracoesLivro(livro.getId(), tc);
        }
    }

    /**
     * Função responsável por realizar as alterações
     * desejadas no livro.
     * @param idLivro qual livro deseja ser alterado.
     * @param tc o scanner para as entradas do usuário.
     */
    private void alteracoesLivro(int idLivro, Scanner tc){
        ArrayList<String> coluna = new ArrayList<>();
        ArrayList<String> novo = new ArrayList<>();
        ArrayList<String> condicao = new ArrayList<>();

        String entrada;
        while (true){
            System.out.print("""
                        --------------------------------------------------------
                        O que você deseja alterar?
                        ATENÇÃO: Todas as alterações no livro serão salvas
                        quando 7 (Sair) for selecionado.
                        --------------------------------------------------------
                        1 - Nome
                        2 - Tipo (novo ou usado)
                        3 - Local de escrita do livro
                        4 - Autor
                        5 - Gênero literário
                        6 - Preço
                        7 - Sair
                        --------------------------------------------------------
                        """);
            entrada = tc.nextLine();

            if (!funcoes.regexNum(entrada)) {
                System.err.print("Insira apenas números!");
                continue;
            }

            switch (Integer.parseInt(entrada)){
                case 1 ->{
                    System.out.print("Insira o novo nome\n");

                    entrada = tc.nextLine();
                    if (funcoes.regexNome(entrada)) {
                        novo.add(entrada);
                        coluna.add("nome");
                        condicao.add("id_livro = " + idLivro);
                    }
                    else
                        System.err.print("Insira um nome válido! Apenas caracteres são aceitos.\n");
                }
                case 2 ->{
                    System.out.print("Insira o novo tipo\n");

                    entrada = tc.nextLine();
                    if (funcoes.regexNome(entrada)) {
                        if (entrada.equalsIgnoreCase("novo") ||
                                entrada.equalsIgnoreCase("usado")) {
                            novo.add(entrada);
                            coluna.add("tipo");
                            condicao.add("id_livro = " + idLivro);
                        }
                        else
                            System.err.print("Apenas os tipos 'novo' e 'usado' são " +
                                    "aceitos.");
                    }
                    else
                        System.err.print("Insira um nome válido! Apenas caracteres são aceitos.\n");
                }
                case 3 ->{
                    System.out.print("O livro foi feito em Mari?\n");

                    entrada = tc.nextLine();
                    if (entrada.equalsIgnoreCase("sim")) {
                        novo.add("true");
                        coluna.add("from_mari");
                        condicao.add("id_livro = " + idLivro);
                    }
                    else if (entrada.equalsIgnoreCase("nao") ||
                            entrada.equalsIgnoreCase("não")) {
                        novo.add("false");
                        coluna.add("from_mari");
                        condicao.add("id_livro = " + idLivro);
                    }
                    else
                        System.out.print("Insira apenas 'sim' ou 'não' como resposta\n");
                }
                case 4 ->{
                    System.out.print("Insira o novo autor\n");

                    entrada = tc.nextLine();
                    if (funcoes.regexNome(entrada)) {
                        novo.add(entrada);
                        coluna.add("autor");
                        condicao.add("id_livro = " + idLivro);
                    }
                    else
                        System.err.print("Insira um nome válido! Apenas caracteres são aceitos.\n");
                }
                case 5 ->{
                    System.out.print("Insira o novo gênero literário\n");

                    entrada = tc.nextLine();
                    if (funcoes.regexNome(entrada)) {
                        novo.add(entrada);
                        coluna.add("genero");
                        condicao.add("id_livro = " + idLivro);
                    }
                    else
                        System.err.print("Insira um nome válido! Apenas caracteres são aceitos.\n");
                }
                case 6 ->{
                    System.out.print("Insira o novo preço\n");

                    entrada = tc.nextLine();
                    if (funcoes.regexNum(entrada)) {
                        novo.add(entrada);
                        coluna.add("preco");
                        condicao.add("id_livro = " + idLivro);
                    }
                    else
                        System.err.print("""
                                Insira um valor válido! Apenas números são aceitos.
                                Para números decimais, use o ponto como separação.
                                """);
                }
                case 7 ->{
                    boolean verificaCon = false;
                    int verificaUpdate = -1;
                    do {
                        try {
                            verificaUpdate = variosUpdates("livro", coluna, novo, condicao);
                        } catch (ConexaoException e) {
                            verificaCon = trataException(e);

                        } catch (NaoTemConexaoException e) {
                            verificaCon = trataException(e);
                        }
                    } while (verificaCon || verificaUpdate == -1);
                    return;
                }
                default -> System.err.print("Insira uma opção válida.");
            }
        }
    }

    /**
     * Função responsável por converter um resultSet contendo
     * um livro em um objeto livro.
     * @param idLivro o id do livro que se deseja criar o objeto.
     * @return Um objeto Livro contendo as informações do livro
     * desejado.<br>
     * Null caso algum erro tenha acontecido.
     */
    private Livro recebeLivro(int idLivro){
        try (ResultSet rt = getLivro(idLivro)){
            return new Livro(rt);
        } catch (SQLException e) {
            System.err.println("""
                            ========================================================
                            NÃO FOI POSSÍVEL RECUPERAR OS DADOS.
                            TENTE NOVAMENTE MAIS TARDE
                            ========================================================
                            """);
            return null;
        } catch (ConexaoException e) {
            trataException(e);

        } catch (NaoTemConexaoException e) {
            trataException(e);

        }
        return null;
    }

    /**
     * Função responsável por tratar a excessão se ter uma
     * conexão com o banco de dados que está com problemas.
     * Ela irá tentar fechar ela e depois iniciar uma nova
     * conexão.
     * <P>Caso não consiga, irá encerrar o programa.</P>
     * @param conexaoException
     */
    private boolean trataException (ConexaoException conexaoException){
        try {
            close();
        } catch (SQLException f) {
            System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            A CONEXÃO COM O BANCO DE DADOS APRESENTOU ERROS E
                            NÃO FOI POSSÍVEL ENCERRÁ-LA
                            ========================================================
                            """);
            System.exit(1);
        }
        try {
            criaCon(1);
        } catch (SQLException f) {
            System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                            DE DADOS
                            ========================================================
                            """);
            System.exit(1);
        }
        return true;
    }

    /**
     * Função responsável por tratar a excessão de não ter uma conexão
     * com o banco de dados. Ela irá tentar iniciar uma nova conexão.
     * <P>Caso não consiga, irá encerrar o programa.</P>
     * @param naoTemConexaoException
     */
    private boolean trataException (NaoTemConexaoException naoTemConexaoException){
        try {
            criaCon(1);
        } catch (SQLException f) {
            System.err.println("""
                            ========================================================
                            FALHA CRÍTICA NO SISTEMA!
                            NÃO FOI POSSÍVEL ESTABELECER UMA CONEXÃO COM O BANCO
                            DE DADOS
                            ========================================================
                            """);
            System.exit(1);
        }
        return true;
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