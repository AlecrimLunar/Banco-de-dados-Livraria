package Entities;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe que guarda os livros.
 */
public class Livro {
    private Integer id;
    private String nome;
    private Double preco;
    private String autor;
    private String genero;
    private String tipo;
    private Boolean mari;

    private int quantidade;


    public Livro(Integer id, String nome, Double preco, String autor, String genero, String tipo, Boolean mari, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.autor = autor;
        this.genero = genero;
        this.tipo = tipo;
        this.mari = mari;
        this.quantidade = quantidade;
    }

    public Livro(int idLivro, String nome, double preco){
        this.id = idLivro;
        this.nome = nome;
        this.preco = preco;
    }

    public Livro(ResultSet rt) throws SQLException {
        if (rt.next()) {
            this.id = rt.getInt("id_livro");
            this.nome = rt.getString("nome");
            this.preco = Double.parseDouble(rt.getString("preco").substring(3).replaceAll(",", "."));
            this.autor = rt.getString("autor");
            this.genero = rt.getString("genero");
            this.tipo = rt.getString("tipo");
            this.mari = rt.getBoolean("from_mari");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getPreco() {
        return preco;
    }

    public String getAutor() {
        return autor;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(){
        quantidade--;
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: " + nome + ", " + autor + "\nPreço: R$ " + String.format("%.2f", preco) + ", " + tipo + "\nGenero: ");
        sb.append(getGeneroAndMari());
        return sb.toString();
    }

    public String getGeneroAndMari(){
        return mari ? genero + ", feito em Mari." : genero + ".";
    }
}
