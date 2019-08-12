package br.com.poc.jms.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class UserDto implements Serializable {
    private String id;
    private String nome;
    private Integer idade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

}
