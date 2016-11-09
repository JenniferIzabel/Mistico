/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "Jogador")
@NamedQueries({
    @NamedQuery(name = "Jogador.findAll", query = "SELECT j FROM Jogador j")})
public class Jogador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idJogador")
    private Integer idJogador;
    @Basic(optional = false)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "senha")
    private String senha;
    @Basic(optional = false)
    @Column(name = "qtdVitorias")
    private int qtdVitorias;
    @Basic(optional = false)
    @Column(name = "qtdderrotas")
    private int qtdderrotas;
    @JoinTable(name = "Tropa", joinColumns = {
        @JoinColumn(name = "Jogador_idJogador", referencedColumnName = "idJogador")}, inverseJoinColumns = {
        @JoinColumn(name = "Personagem_idPersonagem", referencedColumnName = "idPersonagem")})
    @ManyToMany
    private List<Personagem> personagemList;

    public Jogador() {
    }

    public Jogador(Integer idJogador) {
        this.idJogador = idJogador;
    }

    public Jogador(String usuario, String senha, int qtdVitorias, int qtdderrotas) {
        this.usuario = usuario;
        this.senha = senha;
        this.qtdVitorias = qtdVitorias;
        this.qtdderrotas = qtdderrotas;
    }

    public Integer getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(Integer idJogador) {
        this.idJogador = idJogador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getQtdVitorias() {
        return qtdVitorias;
    }

    public void setQtdVitorias(int qtdVitorias) {
        this.qtdVitorias = qtdVitorias;
    }

    public int getQtdderrotas() {
        return qtdderrotas;
    }

    public void setQtdderrotas(int qtdderrotas) {
        this.qtdderrotas = qtdderrotas;
    }

    public List<Personagem> getPersonagemList() {
        return personagemList;
    }

    public void setPersonagemList(List<Personagem> personagemList) {
        this.personagemList = personagemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJogador != null ? idJogador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jogador)) {
            return false;
        }
        Jogador other = (Jogador) object;
        if ((this.idJogador == null && other.idJogador != null) || (this.idJogador != null && !this.idJogador.equals(other.idJogador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Jogador[ idJogador=" + idJogador + " ]";
    }
    
}
