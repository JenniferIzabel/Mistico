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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "Equipe")
@NamedQueries({
    @NamedQuery(name = "Equipe.findAll", query = "SELECT e FROM Equipe e")})
public class Equipe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idEquipe")
    private Integer idEquipe;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "vitorias")
    private int vitorias;
    @Basic(optional = false)
    @Column(name = "derrotas")
    private int derrotas;
    @Basic(optional = false)
    @Column(name = "qtdeVitoriasCamp")
    private int qtdeVitoriasCamp;
    @JoinTable(name = "HistoricoJogador_has_Equipe", joinColumns = {
        @JoinColumn(name = "Equipe_idEquipe", referencedColumnName = "idEquipe")}, inverseJoinColumns = {
        @JoinColumn(name = "HistoricoJogador_idHistoricoJogador", referencedColumnName = "idHistoricoJogador")})
    @ManyToMany
    private List<HistoricoJogador> historicoJogadorList;
    @JoinTable(name = "Equipe_has_Partida", joinColumns = {
        @JoinColumn(name = "Equipe_idEquipe", referencedColumnName = "idEquipe")}, inverseJoinColumns = {
        @JoinColumn(name = "Partida_idPartida", referencedColumnName = "idPartida")})
    @ManyToMany
    private List<Partida> partidaList;
    @OneToMany(mappedBy = "equipe")
    private List<Jogador> jogadorList;

    public Equipe() {
    }

    public Equipe(Integer idEquipe) {
        this.idEquipe = idEquipe;
    }

    public Equipe(Integer idEquipe, String nome, int vitorias, int derrotas, int qtdeVitoriasCamp) {
        this.idEquipe = idEquipe;
        this.nome = nome;
        this.vitorias = vitorias;
        this.derrotas = derrotas;
        this.qtdeVitoriasCamp = qtdeVitoriasCamp;
    }

    public Integer getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(Integer idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getQtdeVitoriasCamp() {
        return qtdeVitoriasCamp;
    }

    public void setQtdeVitoriasCamp(int qtdeVitoriasCamp) {
        this.qtdeVitoriasCamp = qtdeVitoriasCamp;
    }

    public List<HistoricoJogador> getHistoricoJogadorList() {
        return historicoJogadorList;
    }

    public void setHistoricoJogadorList(List<HistoricoJogador> historicoJogadorList) {
        this.historicoJogadorList = historicoJogadorList;
    }

    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {
        this.partidaList = partidaList;
    }

    public List<Jogador> getJogadorList() {
        return jogadorList;
    }

    public void setJogadorList(List<Jogador> jogadorList) {
        this.jogadorList = jogadorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEquipe != null ? idEquipe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipe)) {
            return false;
        }
        Equipe other = (Equipe) object;
        if ((this.idEquipe == null && other.idEquipe != null) || (this.idEquipe != null && !this.idEquipe.equals(other.idEquipe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Equipe[ idEquipe=" + idEquipe + " ]";
    }
    
}
