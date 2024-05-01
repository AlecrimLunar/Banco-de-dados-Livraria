package Exceptions;

public class ConexaoException extends Exception{

    /**
     * Exceção utilizada quando não foi possível criar
     * uma conexão com o banco de dados
     */
    public ConexaoException (){
        super();
    }
}