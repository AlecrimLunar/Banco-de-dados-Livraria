package Entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Livro {
    private Integer id;
    private String nome;
    private Double preco;
    private String autor;
    private String genero;
    private String tipo;
    private Boolean mari;

    public Livro(Integer id, String nome, Double preco, String autor, String genero, String tipo, Boolean mari) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.autor = autor;
        this.genero = genero;
        this.tipo = tipo;
        this.mari = mari;
    }

    public Livro(int idLivro, String nome, double preco){
        this.id = idLivro;
        this.nome = nome;
        this.preco = preco;
    }

    public Livro (ResultSet rt) throws SQLException{
        this.id = rt.getInt("id_livro");
        this.tipo = rt.getNString("tipo");
        this.nome = rt.getNString("nome");
        this.mari = rt.getBoolean("from_mari");
        this.autor = rt.getNString("autor");
        this.genero = rt.getNString("genero");
        this.preco = Double.parseDouble(rt.getNString("preco"));
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nome + ", " + autor + "\nR$ " + String.format("%.2f", preco) + "\n" + tipo + "\n");
        sb.append(getGeneroAndMari());
        return sb.toString();
    }

    public String getGeneroAndMari(){
        return mari ? genero + ", feito em Mari." : genero + ".";
    }
}
