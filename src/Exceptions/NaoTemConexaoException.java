package Exceptions;

/**
 * Exceção utilizada quando não foi possível criar
 * uma conexão com o banco de dados
 */
public class NaoTemConexaoException extends Exception{
    public NaoTemConexaoException (){
        super();
    }
}
