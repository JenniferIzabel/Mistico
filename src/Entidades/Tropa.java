/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "Tropa")
@NamedQueries({
    @NamedQuery(name = "Tropa.findAll", query = "SELECT t FROM Tropa t")})
public class Tropa implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TropaPK tropaPK;
    @ManyToMany(mappedBy = "tropaList")
    private List<Personagem> personagemList;
    @JoinColumn(name = "Jogador_idJogador", referencedColumnName = "idJogador", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Jogador jogador;

    public Tropa() {
    }

    public Tropa(TropaPK tropaPK) {
        this.tropaPK = tropaPK;
    }

    public Tropa(int idTropa, int jogadoridJogador) {
        this.tropaPK = new TropaPK(idTropa, jogadoridJogador);
    }

    public TropaPK getTropaPK() {
        return tropaPK;
    }

    public void setTropaPK(TropaPK tropaPK) {
        this.tropaPK = tropaPK;
    }

    public List<Personagem> getPersonagemList() {
        return personagemList;
    }

    public void setPersonagemList(List<Personagem> personagemList) {
        this.personagemList = personagemList;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tropaPK != null ? tropaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tropa)) {
            return false;
        }
        Tropa other = (Tropa) object;
        if ((this.tropaPK == null && other.tropaPK != null) || (this.tropaPK != null && !this.tropaPK.equals(other.tropaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tropa[ tropaPK=" + tropaPK + " ]";
    }
    
}
