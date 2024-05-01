package Entities;

import Controle.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Cliente extends GerenciaBd {
    private Integer id;
    private String nome;
    private String cpf;
    private String rua;
    private int numero;
    private String email;
    private Boolean onePiece;
    private Boolean flamengo;
    private Boolean souza;
    private String user;
    private String senha;
    private static Carrinho carrinho;
    private static Funcoes fun;
    private final int quem = 0;
    private boolean delete;

    public Cliente(String nome, String cpf, String rua, int numero, String email,
                   Boolean onePiece, Boolean flamengo, Boolean souza, String user,
                   String senha, Carrinho carrinho, int id) {
        this.nome = nome;
        this.cpf = cpf;
        this.rua = rua;
        this.numero = numero;
        this.email = email;
        this.onePiece = onePiece;
        this.flamengo = flamengo;
        this.souza = souza;
        this.user = user;
        this.senha = senha;
        Cliente.carrinho = carrinho;
        this.id = id;
        fun = new Funcoes(quem);
    }
    public Cliente(String nome, String cpf, String rua, int numero, String email,
                   Boolean onePiece, Boolean flamengo, Boolean souza, String user,
                   String senha, Carrinho carrinho) {
        this.nome = nome;
        this.cpf = cpf;
        this.rua = rua;
        this.numero = numero;
        this.email = email;
        this.onePiece = onePiece;
        this.flamengo = flamengo;
        this.souza = souza;
        this.user = user;
        this.senha = senha;
        Cliente.carrinho = carrinho;
        fun = new Funcoes(quem);
    }

    /**
     * Método responsável por exibir a tela principal do cliente.
     * @param sc Scanner utilizado para ler as entradas do usuário.
     * @param compra Booliano que indica se a compra deve ser realizada.
     */
    public void MenuCliente(Scanner sc, boolean compra) {
        setUsuarioBanco(quem);
        try{
            criaCon(quem);
        } catch (SQLException e) {
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
        if(compra){
            fun.Compra(sc, carrinho, new boolean[]{getOnePiece(), getFlamengo(), getSouza()}, getId(), quem);
        }

        carrinho = fun.CarregaCarrinho(carrinho, getId(), quem);

        LinkedList<Livro> destaques = fun.Destaques(0);

        System.out.print("""
                ========================================================
                BEM-VINDO AO SISTEMA DIGITAL DA LIVRARIA MOOGLES
                ========================================================
                """);
        int a;
        loop:while(true) {

            System.out.print("livros em destaque:\n" +
                    fun.PrintDestaques(destaques) + "\n" +
                    "4 - Procurar livro\n" +
                    "5 - Carrinho\n" +
                    "6 - Minha conta\n" +
                    "7 - Realizar logout\n" +
                    "0 - Sair\n" +
                    "========================================================\n");

            while(true) {

                String resp = sc.nextLine();

                if(fun.regexNum(resp)) {
                    a = Integer.parseInt(resp);
                    break;
                }
                else
                    System.out.println("Digite apenas números!");
            }

            switch (a) {
                case 1 -> {
                    System.out.print("Adicionar " + destaques.get(0).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.setLivro(destaques.get(0));
                }
                case 2 -> {
                    System.out.print("Adicionar " + destaques.get(1).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.setLivro(destaques.get(1));
                }
                case 3 -> {
                    System.out.print("Adicionar " + destaques.get(2).getNome() + " ao carrinho?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        carrinho.setLivro(destaques.get(2));
                }

                case 4 -> carrinho = fun.Pesquisa(sc, carrinho, quem);
                case 5 -> {
                    carrinho = fun.Carrinho(sc, carrinho);
                    if(carrinho.isEmpty()) {
                        System.out.print("Deseja finalizar a compra?\n");
                        if (sc.nextLine().equalsIgnoreCase("sim")) {
                            fun.Compra(sc, carrinho, new boolean[]{getOnePiece(), getFlamengo(), getSouza()}, getId(), quem);
                        }
                    }
                }
                case 6 -> {
                    MenuConta(sc);
                    if(delete){
                        break loop;
                    }
                }
                case 7 ->{break loop;}
                case 0 -> {
                    System.out.print("Tem certeza que deseja sair?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        System.exit(1);
                }
                default -> System.out.println("Opção invalida:");
            }

        }
    }

    /**
     * Método responsável por exibir o menu da conta do cliente.
     * @param sc Scanner utilizado para ler as entradas do usuário.
     */
    public void MenuConta(Scanner sc) {
        int a;
        String[] pNome = getNome().split(" ");
        loop:while(true) {
            System.out.println("========================================================\n" +
                    "MENU CONTA - BEM VINDO " + pNome[0] + "!\n");
            System.out.println("""
                    ========================================================
                    1 - Ver meus pedidos
                    2 - Suas Informações
                    3 - Deletar conta
                    0 - Sair
                    ========================================================
                    """);
            while(true) {

                String resp = sc.nextLine();

                if(fun.regexNum(resp)) {
                    a = Integer.parseInt(resp);
                    break;
                }
                else
                    System.out.println("Digite apenas números!");
            }

            switch (a) {
                case 1 -> fun.verPedidos(sc, getId(), quem);
                case 2 -> alteraCliente(sc);
                case 3 -> {
                    if(removeCliente(sc)){
                        delete = true;
                        break loop;
                    }
                }
                case 0 -> {break loop;}
            }
        }
    }

    /**
     * Método responsável por exibir a opção de remover o cadastro do cliente.
     * @param tc Scanner utilizado para ler as entradas do usuário.
     */
    public boolean removeCliente(Scanner tc) {

        System.out.print("""
                RESPONDA COM 'Sim' OU 'Não'.\s
                DESEJA REMOVER SEU CADASTRO DA LOJA? ISSO NÃO IRÁ REMOVER SEU HISTÓRICO DE COMPRAS NO SISTEMA DA LOJA!
                """);
        boolean remove = tc.nextLine().equalsIgnoreCase("sim");
        if(remove) {
            int verifica;
            do {
                try {
                   verifica = delete("cliente", "id_cliente = " + getId());
                } catch (NaoTemConexaoException e) {
                    fun.trataException(e, quem);
                    System.out.print("""
                            ========================================================
                            Erro na conexão.
                            Tente novamente mais tarde.
                            ========================================================
                            """);
                    return false;
                } catch (ConexaoException e) {
                    fun.trataException(e, quem);
                    System.out.print("""
                            ========================================================
                            Erro na conexão.
                            Tente novamente mais tarde.
                            ========================================================
                            """);
                    return false;
                }
            } while(verifica == -1);

            if(verifica == 1){
                System.out.print("""
                        ========================================================
                        CONTA REMOVIDA COM SUCESSO!
                        ========================================================
                        """);
                return true;

            } else {
                System.out.print("""
                        ========================================================
                        NÃO FOI POSSÍVEL APAGAR A CONTA!
                        POR FAVOR TENTE NOVAMENTE MAIS TARDE.
                        ========================================================
                        """);
                return false;
            }
        } else
            return false;
    }

    /**
     * Método responsável por alterar as informações de um cliente.
     * @param tc Scanner utilizado para ler as entradas do usuário.
     */
    public void alteraCliente(Scanner tc){
        int a;
        try {
            loop: while (true) {
                System.out.print(this + "\nDeseja Alterar alguma informação? ");
                if(tc.nextLine().equalsIgnoreCase("sim")){

                System.out.println("""
                        --SELECIONE O CAMPO QUE DESEJA ALTERAR--
                        1 - Nome
                        2 - CPF
                        3 - Rua
                        4 - Número
                        5 - Email
                        6 - Torcer para o Flamengo
                        7 - Nascer em Sousa
                        8 - Assistir a One Piece
                        0 - Voltar ao menu principal""");
                while(true) {
                    System.out.println();
                    String resp = tc.nextLine();

                    if(fun.regexNum(resp)) {
                        a = Integer.parseInt(resp);
                        break;
                    }
                    else
                        System.out.println("Digite apenas números!");
                }


                System.out.println("===========================================================================");

                switch (a) {
                    case 1 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String novo;

                        while(true){

                            System.out.print("Insira o novo nome: ");
                            novo = tc.nextLine();

                            String[] nomes = nome.split(" ");
                            boolean foi = true;

                            for(String aux : nomes) {
                                if (!fun.regexNome(aux)) {
                                    System.out.print("Escreva um nome válido! Sem números.");
                                    foi = false;
                                    break;
                                }
                            }
                            if(foi){
                                break;
                            }

                        }

                        coluna.add("nome");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        nome = novo;
                    }

                    case 2 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String novo;

                        do{
                            System.out.print("Insira o novo CPF: ");
                            novo = tc.nextLine();
                        }while(!fun.regexCPF(novo));

                        coluna.add("cpf");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        cpf = novo;
                    }

                    case 3 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String novo;

                        do {
                            System.out.print("Insira a nova rua: ");
                            novo = tc.nextLine();
                        } while (!fun.verificaComandoSQL(novo));

                        coluna.add("rua");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        rua = novo;
                    }

                    case 4 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String s;
                        int novo;

                        while(true){
                            System.out.print("Insira o novo número de residência: ");
                            s = tc.nextLine();

                            if(fun.regexNum(s)){
                                novo = Integer.parseInt(s);
                                break;
                            } else {
                                System.out.println("Digite apenas números!");
                            }
                        }

                        coluna.add("numero");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        numero = novo;
                    }

                    case 5 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String novo;

                        do {
                            System.out.print("Insira o novo email: ");
                            novo = tc.nextLine();
                        }while(!fun.regexEmail(novo));

                        coluna.add("email");
                        novoU.add("'" + novo + "'");
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        email = novo;
                    }

                    case 6 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String resp;

                        do{
                            System.out.print("RESPONDA COM 'sim' OU 'não'\nTorce para o flamengo? ");
                            resp = tc.nextLine();
                        }while(!fun.verificaComandoSQL(resp));
                        boolean novo = resp.equalsIgnoreCase("sim");

                        coluna.add("is_flamengo");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        flamengo = novo;
                    }

                    case 7 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String resp;
                        do {
                            System.out.print("RESPONDA COM 'sim' OU 'não'\nNasceu em Sousa? ");
                            resp = tc.nextLine();
                        }while(!fun.verificaComandoSQL(resp));
                        boolean novo = resp.equalsIgnoreCase("sim");

                        coluna.add("is_sousa");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        souza = novo;
                    }

                    case 8 -> {
                        ArrayList<String> coluna = new ArrayList<>();
                        ArrayList<String> novoU = new ArrayList<>();
                        ArrayList<String> condicao = new ArrayList<>();
                        String resp;

                        do{
                            System.out.print("RESPONDA COM 'sim' OU 'não'\nAssiste a One Piece? ");
                            resp = tc.nextLine();
                        }while(!fun.verificaComandoSQL(resp));
                        boolean novo = resp.equalsIgnoreCase("sim");

                        coluna.add("one_piece");
                        novoU.add("" + novo);
                        condicao.add("id_cliente = " + getId());
                        variosUpdates("cliente", coluna, novoU, condicao);
                        onePiece = novo;
                    }

                    case 0 -> {
                        break loop;
                    }
                    default -> System.out.println("OPÇÃO INVÁLIDA!");
                }
                }
                else
                    break;
            }
        } catch (NaoTemConexaoException e) {
            fun.trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (ConexaoException e) {
            fun.trataException(e, quem);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getOnePiece() {
        return onePiece;
    }

    public Boolean getFlamengo() {
        return flamengo;
    }

    public Boolean getSouza() {
        return souza;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "===========================================================================\n" +
                "Nome: " + nome + "\n" +
                "CPF: " + cpf + "\n" +
                "Email: " + email + "\n" +
                "Endereço: " + rua + ", " + numero + "\n" +
                "Assiste One Piece: " + (onePiece ? "Sim" : "Não") + "\n" +
                "Torce pro Flamengo: " + (flamengo ? "Sim" : "Não") + "\n" +
                "É de Sousa: " + (souza ? "Sim" : "Não") + "\n" +
                "===========================================================================";
    }
}
