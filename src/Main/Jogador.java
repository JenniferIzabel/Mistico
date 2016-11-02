/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author jennifer
 */
public class Jogador {
    
    private int id;
    private String nome;
    private int qtdVitorias;
    private int qtdDerrotas;

    public Jogador(int id, String nome, int qtdVitorias, int qtdDerrotas) {
        this.id = id;
        this.nome = nome;
        this.qtdVitorias = qtdVitorias;
        this.qtdDerrotas = qtdDerrotas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdVitorias() {
        return qtdVitorias;
    }

    public void setQtdVitorias(int qtdVitorias) {
        this.qtdVitorias = qtdVitorias;
    }

    public int getQtdDerrotas() {
        return qtdDerrotas;
    }

    public void setQtdDerrotas(int qtdDerrotas) {
        this.qtdDerrotas = qtdDerrotas;
    }
    
    
    
}
