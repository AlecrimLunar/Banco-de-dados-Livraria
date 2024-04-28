package Entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe responsável por guardar as informações das compras.
 * É útil na classe vendedor para guardar as compras que necessitam
 * de ser confirmadas.
 */
public class InfosCompra {
    private int idCompra;
    private String formaPagamento;
    private Date data;
    private int valor;
    private int id_carrinho;
    private ArrayList<Livro> livrosAdquiridos;

    public InfosCompra(int idCompra, String formaPagamento, Date data,
                       int valor, int id_carrinho){
        this.idCompra = idCompra;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.valor = valor;
        this.id_carrinho = id_carrinho;
        livrosAdquiridos = new ArrayList<>();
    }

    public Date getData() {
        return data;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public int getValor() {
        return valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public int getId_carrinho() {
        return id_carrinho;
    }
}