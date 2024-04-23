package Entities;

import Controle.ConexaoException;
import Controle.ControlaBD;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringJoiner;

public class FuncoesEstaticas {
    public static void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    public LinkedList<Livro> Destaques() throws ConexaoException {
        ControlaBD con = new ControlaBD(0);
        LinkedList<Livro> destaques = new LinkedList<>();
        String coluna = "l.*";
        String tabela = "estoque.livro as l";
        String pesquisa = "WHERE l.id_livro >= 0 AND l.quantidade_estoque > 0 AND l.id_livro IN " +
                "(SELECT cl.id_livro FROM clientes_info.carrinho_livro as cl GROUP BY cl.id_livro ORDER BY COUNT(*) DESC LIMIT 3)";
        try {
        ResultSet rt = con.Select(tabela, coluna, pesquisa);

        destaques = RecuperaLivro(rt);

        while (rt.next()) { }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destaques;
    }

    public String PrintDestaques(LinkedList<Livro> livros){
        StringBuilder sb = new StringBuilder();
        sb.append("+=====================1=====================+=====================2=====================+=====================3=====================+\n");

        Livro aux = livros.get(0);
        ArrayList<String> nome1 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome1 = DividirNoMeio(aux.getNome());
        } else {
            nome1.set(0, aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome1.get(0)) + "|");

        aux = livros.get(1);
        ArrayList<String> nome2 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome2 = DividirNoMeio(aux.getNome());
        } else {
            nome2.set(0, aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome2.get(0)) + "|");

        aux = livros.get(2);
        ArrayList<String> nome3 = new ArrayList<>();

        if(aux.getNome().length() > 43){
            nome3 = DividirNoMeio(aux.getNome());
        } else {
            nome3.set(0, aux.getNome());
        }
        sb.append("|" + AdicionaEspacos(nome3.get(0)) + "|\n");

        if(nome1.size() > 1){
            sb.append("|" + AdicionaEspacos(nome1.get(1)) + "|");
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        }

        if(nome2.size() > 1){
            sb.append("|" + AdicionaEspacos(nome2.get(1)) + "|");
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        }

        if(nome3.size() > 1){
            sb.append("|" + AdicionaEspacos(nome3.get(1)) + "|");
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|\n");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getAutor()) + "|");
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        } else {
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        } else {
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getTipo()) + "|");
        } else {
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        }

        if(nome1.size() > 1){
            aux = livros.get(0);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        if(nome2.size() > 1){
            aux = livros.get(1);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        if(nome3.size() > 1){
            aux = livros.get(2);
            sb.append("|" + AdicionaEspacos(aux.getGeneroAndMari()) + "|");
        } else {
            sb.append("|" + " ".repeat(43) + "|");
        }

        sb.append("+===========================================+===========================================+===========================================+\n");
        sb.append("|" + AdicionaEspacos("R$ " + livros.get(0).getPreco()) + "|" + AdicionaEspacos("R$ " + livros.get(1).getPreco()) + "|" +
                AdicionaEspacos("R$ " + livros.get(2).getPreco()) + "|\n");
        sb.append("+===========================================+===========================================+===========================================+\n");

        return sb.toString();
    }

    public static ArrayList<String> DividirNoMeio(String nome){
        ArrayList<String> nomeA = new ArrayList<>();
        int auxI = nome.split(" ").length/2;
        int indiceEncontrado = -1;
        int contadorEspacos = 0;

        for (int i = 0; i < nome.length(); i++) {
            char caractere = nome.charAt(i);
            if (caractere == ' ') {
                contadorEspacos++;
                if (contadorEspacos == auxI + 1) {
                    indiceEncontrado = i;
                    break;
                }
            }
        }
        nomeA.set(0 , nome.substring(0, indiceEncontrado).trim());
        nomeA.set(1, nome.substring(indiceEncontrado + 1).trim());

        return nomeA;
    }
    public static String AdicionaEspacos(String nome){
        int espEsq = (43 - nome.length()/2), espDir = (43-nome.length()-espEsq);

        String auxSb = " ".repeat(espEsq) +
                nome +
                " ".repeat(espDir);

        return auxSb;
    }

    public LinkedList<Livro> PesquisaLivro(ControlaBD con, String str, String coluna) throws ConexaoException, SQLException {
        LinkedList<Livro> l = new LinkedList<>();

        int aux = con.Existe("estoque.livro as l", "l." + coluna, str, 1);

        if(aux > 0){
            ResultSet rt = con.Select("estoque.livro as l", "l." + coluna, " WHERE l." + coluna + " LIKE '%" + str +
                    "%' AND l.quantidade_estoque > 0");

            l = RecuperaLivro(rt);
        } else if(aux == 0) {
            System.out.print("Não temos nenhum livro com esse " + coluna + "!\n\n");
        } else if(aux == -1){
            System.out.print("Erro com a pesquisa tente novamente com um atributo diferente!\n\n");
        }

        return l;
    }

    public static LinkedList<Livro> RecuperaLivro(ResultSet rt) throws SQLException{
        LinkedList<Livro> l = new LinkedList<>();

        while(rt.next()) {
            Livro aux = new Livro(rt.getInt("id_livro"), rt.getString("nome"),
                    Double.parseDouble(rt.getString("preco").substring(3).replaceAll(",", ".")),
                    rt.getString("autor"), rt.getString("genero"), rt.getString("tipo"),
                    rt.getBoolean("from_mari"));
            l.add(aux);
        }

        return l;
    }

    public void PrintLivro(LinkedList<Livro> l){
        int escolha = 1;
        for (Livro livro : l) {
            System.out.println( escolha + " - " + livro.toString());
            escolha++;
        }
    }

    /* esse método irá verificar se alguma palavra reservada
     * do SQL está sendo utilizada */
    private static boolean verificaComandoSQL(String s){
        Pattern verificaComandoSQL = Pattern.compile(" *drop +| *insert +| *" +
                "grant +| *update +| *delete +| *select +| *create +| *" +
                "alter +| *union +", Pattern.CASE_INSENSITIVE);
        Matcher matcher = verificaComandoSQL.matcher(s);

        if (matcher.find()){
            System.out.println("PALAVRA NÃO PERMITIDA UTILIZADA! TENTE NOVAMENTE");
            return false;
        }
        return true;
    }
    public static boolean regexEmail(String s){
        //verifica se a string possui alguma palavra reservada sql
        if (verificaComandoSQL(s)) {
            //verifica se o email está dentro do padrão de emails
            if (Pattern.matches("[a-zA-Z0-9.ç]+@(gmail.com|" +
                    "yahoo.com.br|hotmail.com)", s))
                return true;
            else {
                System.out.println("O EMAIL NÃO ESTÁ NO FORMATO SUPORTADO! TENTE NOVAMENTE\n" +
                        "São suportados apenas: 'gmail.com', 'yahoo.com.br', " +
                        "'hotmail.com.br'");
                return false;
            }
        }
        return false;
    }
    public static boolean regexCPF(String s){
        if (Pattern.matches("[0-9]{11}", s))
            return true;
        else {
            System.out.println("INSIRA APENAS 11 NÚMEROS!");
            return false;
        }
    }

    public static boolean regexNome(String s){
        if (verificaComandoSQL(s)){
            return Pattern.matches("[A-Za-z]+", s);
        }
        return false;
    }

    public static void printa(ResultSet rt){
        try{
            ResultSetMetaData rtMetaData = rt.getMetaData();
            int numeroDeColunas = rtMetaData.getColumnCount();

            while (rt.next()) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]\n");
                for (int coluna = 1; coluna <= numeroDeColunas; coluna++) {
                    String nomeDaColuna = rtMetaData.getColumnName(coluna);
                    joiner.add(nomeDaColuna + " = " + rt.getString(coluna));
                }
                System.out.print(joiner.toString());
            }

        } catch (Exception e){
            System.out.println("ERRO: " + e);
        }
    }
}