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
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "idade")
    private int idade;
    @Basic(optional = false)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @Column(name = "anosExperiencia")
    private int anosExperiencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jogador")
    private List<Tropa> tropaList;
    @JoinColumn(name = "Equipe_idEquipe", referencedColumnName = "idEquipe")
    @ManyToOne
    private Equipe equipe;
    @JoinColumn(name = "HistoricoJogador_idHistoricoJogador", referencedColumnName = "idHistoricoJogador")
    @ManyToOne(optional = false)
    private HistoricoJogador historicoJogador;

    public Jogador() {
    }

    public Jogador(Integer idJogador) {
        this.idJogador = idJogador;
    }

    public Jogador(Integer idJogador, String nome, int idade, String sexo, int anosExperiencia) {
        this.idJogador = idJogador;
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.anosExperiencia = anosExperiencia;
    }

    public Integer getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(Integer idJogador) {
        this.idJogador = idJogador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(int anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public List<Tropa> getTropaList() {
        return tropaList;
    }

    public void setTropaList(List<Tropa> tropaList) {
        this.tropaList = tropaList;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public HistoricoJogador getHistoricoJogador() {
        return historicoJogador;
    }

    public void setHistoricoJogador(HistoricoJogador historicoJogador) {
        this.historicoJogador = historicoJogador;
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
