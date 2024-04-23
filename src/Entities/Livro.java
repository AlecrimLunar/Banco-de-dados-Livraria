package Entities;

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

    public String getGenero() {
        return genero;
    }

    public String getTipo() {
        return tipo;
    }

    public Boolean getMari() {
        return mari;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nome + ", " + autor + "\nR$ " + String.format("%02f", preco) + "\n" + tipo + "\n");
        sb.append(getGeneroAndMari());
        return sb.toString();
    }

    public String getGeneroAndMari(){
        return mari ? genero + ", feito em Mari." : genero + ".";
    }
}
