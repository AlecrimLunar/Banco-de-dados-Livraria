package Entities;

import Controle.ConexaoException;
import Controle.Funcoes;
import Controle.NaoTemConexaoException;

import java.util.*;

public class Sistema extends Controle.GerenciaBd {

    private static Funcoes fun;
    private static Carrinho carrinho;
    public Sistema() {
        fun = new Funcoes();
        carrinho = new Carrinho();
        setUsuarioBanco(0);
    }

    /**
     * Inicia o sistema, exibindo as opções disponíveis ao usuário.
     */
    public void Iniciar() {
        setUsuarioBanco(0);
        Scanner sc = new Scanner(System.in);
        LinkedList<Livro> destaques = fun.Destaques(0);

        System.out.print("""
                ========================================================
                BEM-VINDO AO SISTEMA DIGITAL DA LIVRARIA MOOGLES
                ========================================================
                """);
        int a;
        while(true) {
            System.out.print("livros em destaque:\n" +
                    fun.PrintDestaques(destaques) + "\n" +
                    "4 - Procurar livro\n" +
                    "5 - Carrinho" +
                    "6 - Realizar login\n" +
                    "7 - Cadastrar-se\n" +
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

                case 4 -> carrinho = fun.Pesquisa(sc, carrinho, 0);
                case 5 -> {
                    carrinho = fun.Carrinho(sc, carrinho);

                    System.out.print("Deseja finalizar a compra?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim")) {
                        System.out.print("""
                                Para finalizar a compra é necessário ter um conta criada.
                                Por favor faça o login ou crie uma conta.
                                """);
                        Login(sc, true, true);
                    }
                }
                case 6 -> Login(sc, null, false);
                case 7 -> Cadastrar(sc, false);
                case 0 -> {
                    System.out.print("Tem certeza que deseja sair?\n");
                    if (sc.nextLine().equalsIgnoreCase("sim"))
                        System.exit(1);
                }
                default -> System.out.println("Opção invalida");
            }

        }
    }

    /**
     * Método responsável por realizar o login do usuário.
     * @param sc Scanner para receber as informações do usuário.
     * @param c Indica se o usuário deseja realizar login como vendedor ou cliente.
     * @param compra Indica se o usuário está realizando o login para finalizar uma compra.
     */
    public void Login(Scanner sc, Boolean c, boolean compra) {
        boolean loginEfetuado = false;
        int quem = 0;

        String tabela = "";
        if(c == null) {
            System.out.print("Deseja realizar login como Vendedor ou cliente?\n");
            tabela = sc.nextLine().equalsIgnoreCase("vendedor") ? "vendedores_info.vendedor" : "clientes_info.cliente";
        } else if(c) {
            tabela = "clientes_info.cliente";
        }

        try{
            System.out.print("Deseja realizar [1]-login ou [2]-criar uma conta?\n");
            if("2".equalsIgnoreCase(sc.nextLine())){
                Cadastrar(sc, compra);
            } else {
                String user = "";
                String senha = "";
                while (!loginEfetuado) {
                    System.out.print("Usuário: ");
                    user = sc.nextLine();

                    System.out.print("Senha: ");
                    senha = sc.nextLine();

                    int aux = login(user, senha, tabela);

                    switch (aux) {
                        case -1 -> System.out.println("Erro no banco");
                        case 0 -> System.out.println("Usuário não existente");
                        case 1 -> {
                            System.out.println("login efetuado com sucesso");
                            loginEfetuado = true;
                        }
                        case 2 -> System.out.println("Senha incorreta");
                    }
                }

                if (tabela.equalsIgnoreCase("vendedores_info.vendedor")) {
                    quem = 1;
                    Vendedor vendedor = fun.recuperaVendedorF(user, senha, quem);
                    vendedor.menuVendedor(sc);
                } else {
                    Cliente cliente = fun.recuperaCliente(user, senha, carrinho, quem);
                    cliente.MenuCliente(sc, compra);
                }
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

    /**
     * Método responsável por realizar o cadastro do usuário.
     * @param sc Scanner para receber as informações do usuário.
     * @param compra Indica se o usuário está realizando o cadastro para finalizar uma compra.
     */
    public void Cadastrar(Scanner sc, boolean compra) {
        String nome, cpf, email, senha, rua, user;
        boolean isF, isS, one;
        int numero;
        System.out.print("""
                ========================================================
                Cadastro de cliente
                ========================================================
                """);
        while(true) {
            System.out.print("Digite seu nome completo: ");
            nome = sc.nextLine();

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

        while(true) {
            System.out.print("Digite seu CPF: ");
            cpf = sc.nextLine();

            if(!fun.regexCPF(cpf))
                System.out.print("Escreva um CPF válido! Somente números.");
            else break;
        }

        do {
            System.out.print("Digite seu email: ");
            email = sc.nextLine();
        } while (!fun.regexEmail(email));

        System.out.print("Digite sua Rua: ");
        rua = sc.nextLine();

        System.out.print("Digite o número da sua casa: ");
        numero = Integer.parseInt(sc.nextLine());

        System.out.print("Digite seu usuário para o login: ");
        user = sc.nextLine();

        System.out.print("Digite sua senha: ");
        senha = sc.nextLine();

        System.out.print("""
                --As próximas perguntas responda com sim ou não!--
                Você torce pro flamengo?
                """);
        isF = sc.nextLine().equalsIgnoreCase("sim");

        System.out.print("Você é de Sousa?\n");
        isS = sc.nextLine().equalsIgnoreCase("sim");

        System.out.print("Você assiste one piece?\n");
        one = sc.nextLine().equalsIgnoreCase("sim");

        try{
            Cliente novoCliente = new Cliente(nome, cpf, rua, numero, email ,one, isF, isS, user, senha, carrinho);
            int id = InsertRetornando("clientes_info.client", "senha, usuario, nome, cpf, rua, numero, email, is_flamengo, is_sousa, one_piece",
                    senha + user + nome + cpf + rua + numero + email + isF + isS + one);
            novoCliente.setId(id);
            Login(sc, true, compra);
        } catch (NaoTemConexaoException e) {
            fun.trataException(e, 0);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        } catch (ConexaoException e) {
            fun.trataException(e, 0);
            System.out.print("""
                             ========================================================
                             Erro na conexão.
                             Tente novamente mais tarde.
                             ========================================================
                             """);
        }
    }
}
