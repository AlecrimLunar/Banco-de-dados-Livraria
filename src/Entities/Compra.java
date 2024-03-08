package Entities;

import java.util.LinkedList;

public class Compra {
    private LinkedList<Livro> compra = new <Livro>LinkedList();

    public Compra() {
    }

    public void addLivro(Livro livro){
        compra.add(livro);
    }

    public void remove (){
        compra.remove(compra.size() - 1);
    }

    public void getcompra(){
        for(int i=0; i<compra.size(); i++)
            System.out.println("ID:" + compra.get(i).getId() +
                    " Nome:" + compra.get(i).getNome() + " Preço: R$" + compra.get(i).getPreco() + "\n");
    }

    public int getsize(){
        return compra.size();
    }

    public Livro getLivro(int index){
        return compra.get(index);
    }
}
