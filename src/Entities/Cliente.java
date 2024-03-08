package Entities;

public class Cliente {
    private Integer id;
    private String nome;
    private Long cpf;
    private String endereco;
    private Boolean onePiece;
    private Boolean flamengo;
    private Boolean souza;

    public Cliente(Integer id, String nome, Long cpf, String endereco, Boolean onePiece, Boolean flamengo, Boolean souza) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.onePiece = onePiece;
        this.flamengo = flamengo;
        this.souza = souza;
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

    public String getEndereco() {
        return endereco;
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
