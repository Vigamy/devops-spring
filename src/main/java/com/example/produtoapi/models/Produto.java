package com.example.produtoapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Entity
@Schema(description = "Entidade de um Produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador do Produto")
    long id;
    @NotNull
    @Schema(description = "Nome do Produto")
    String nome;
    @NotNull
    @Schema(description = "Identificador do Produto",
            example = "Um exemplo de descricão")
    String descricao;
    @NotNull
//    @Digits(integer = 10, fraction = 2)
    double preco;
    @NotNull
    @Min(value = 0, message = "O estoque deve que ser maior que 1")
    int qtdEstoque;

    public Produto() {

    }

    @ControllerAdvice
    public class CustomExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public String handleYourException(ErrorMessage ex) {
            return "";
        }
    }

     public Produto(int id, String nome, String descricao, double preco, int qtdEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qtdEstoque = qtdEstoque;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", qtdEstoque=" + qtdEstoque +
                '}';
    }
}
