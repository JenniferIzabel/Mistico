/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "HistoricoJogador")
@NamedQueries({
    @NamedQuery(name = "HistoricoJogador.findAll", query = "SELECT h FROM HistoricoJogador h")})
public class HistoricoJogador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idHistoricoJogador")
    private Integer idHistoricoJogador;
    @Basic(optional = false)
    @Column(name = "pontos")
    private String pontos;
    @Basic(optional = false)
    @Column(name = "faltas")
    private String faltas;
    @ManyToMany(mappedBy = "historicoJogadorList")
    private List<Equipe> equipeList;
    @JoinColumn(name = "Partida_idPartida", referencedColumnName = "idPartida")
    @ManyToOne(optional = false)
    private Partida partida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicoJogador")
    private List<Jogador> jogadorList;

    public HistoricoJogador() {
    }

    public HistoricoJogador(Integer idHistoricoJogador) {
        this.idHistoricoJogador = idHistoricoJogador;
    }

    public HistoricoJogador(Integer idHistoricoJogador, String pontos, String faltas) {
        this.idHistoricoJogador = idHistoricoJogador;
        this.pontos = pontos;
        this.faltas = faltas;
    }

    public Integer getIdHistoricoJogador() {
        return idHistoricoJogador;
    }

    public void setIdHistoricoJogador(Integer idHistoricoJogador) {
        this.idHistoricoJogador = idHistoricoJogador;
    }

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }

    public String getFaltas() {
        return faltas;
    }

    public void setFaltas(String faltas) {
        this.faltas = faltas;
    }

    public List<Equipe> getEquipeList() {
        return equipeList;
    }

    public void setEquipeList(List<Equipe> equipeList) {
        this.equipeList = equipeList;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
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
        hash += (idHistoricoJogador != null ? idHistoricoJogador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoricoJogador)) {
            return false;
        }
        HistoricoJogador other = (HistoricoJogador) object;
        if ((this.idHistoricoJogador == null && other.idHistoricoJogador != null) || (this.idHistoricoJogador != null && !this.idHistoricoJogador.equals(other.idHistoricoJogador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.HistoricoJogador[ idHistoricoJogador=" + idHistoricoJogador + " ]";
    }
    
}
