package Entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe responsável por guardar as informações das compras.
 * É útil na classe vendedor para guardar as compras que necessitam
 * de ser confirmadas.
 */
public class Compra {
    private int idCompra;
    private String formaPagamento;
    private Date data;
    private int valor;
    private int id_carrinho;
    private int idVendedor;
    private ArrayList<Livro> livrosAdquiridos;

    public Compra(int idCompra, String formaPagamento, Date data,
                  int valor, int id_carrinho, int idVendedor){
        this.idCompra = idCompra;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.valor = valor;
        this.id_carrinho = id_carrinho;
        this.idVendedor = idVendedor;
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

    public int getIdVendedor() { return idVendedor; }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return  "Código do pedido:" + getIdCompra() + "\n" +
                "Forma de pagamento: " + getFormaPagamento() + "\n" +
                "Data: " + sdf.format(getData()) + "\n" +
                "Valor total: " + getValor() + "\n";
    }
}