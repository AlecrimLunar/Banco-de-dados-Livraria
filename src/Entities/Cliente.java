package Entities;

public class Cliente {
    private Integer id;
    private String nome;
    private Long cpf;
    private String rua;
    private int numero;
    private Boolean onePiece;
    private Boolean flamengo;
    private Boolean souza;
    private String user;
    private String senha;

    public Cliente(String nome, Long cpf, String rua, int numero,
                   Boolean onePiece, Boolean flamengo, Boolean souza, String user,
                   String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.rua = rua;
        this.numero = numero;
        this.onePiece = onePiece;
        this.flamengo = flamengo;
        this.souza = souza;
        this.user = user;
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Long getCpf() {
        return cpf;
    }

    public String getrua() {
        return rua;
    }

    public int getNumero(){
        return numero;
    }

    public Boolean getOnePiece() {
        return onePiece;
    }

    public Boolean getFlamengo() {
        return flamengo;
    }

    public Boolean getSouza() {
        return souza;
    }
}
