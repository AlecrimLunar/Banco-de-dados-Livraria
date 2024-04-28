package Entities;

import java.util.Date;
import java.util.LinkedList;

public class Compra {
    private int id;
    private String formaP;
    private Date date;
    private int valor;
    private int idV;
    private int idCarrinho;
    private int idC;

    public Compra() {
        compra = new LinkedList<Livro>();
        quantidade = new LinkedList<Integer>();
    }

    /*adicionei esse quantidade pq é importante saber a quantidade d um determinado livro
    que foi comprado*/
    public void addLivro(Livro livro, int quantidade){
        compra.add(livro);
        this.quantidade.add(quantidade);
    }

    public void remove (){
        compra.remove(compra.size() - 1);
    }

    public void getcompra(){
        for(int i=0; i<compra.size(); i++)
            System.out.print("ID: " + compra.get(i).getId() +
                    " | Nome: " + compra.get(i).getNome() + " | Preço: R$" + compra.get(i).getPreco() + "\n");
    }

    public int getsize(){
        return compra.size();
    }
    public int getQuantidade(int index){
        return quantidade.get(index);
    }

    public Livro getLivro(int index){
        return compra.get(index);
    }
}
