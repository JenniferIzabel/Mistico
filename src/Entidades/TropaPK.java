/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jennifer
 */
@Embeddable
public class TropaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idTropa")
    private int idTropa;
    @Basic(optional = false)
    @Column(name = "Jogador_idJogador")
    private int jogadoridJogador;

    public TropaPK() {
    }

    public TropaPK(int idTropa, int jogadoridJogador) {
        this.idTropa = idTropa;
        this.jogadoridJogador = jogadoridJogador;
    }

    public int getIdTropa() {
        return idTropa;
    }

    public void setIdTropa(int idTropa) {
        this.idTropa = idTropa;
    }

    public int getJogadoridJogador() {
        return jogadoridJogador;
    }

    public void setJogadoridJogador(int jogadoridJogador) {
        this.jogadoridJogador = jogadoridJogador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTropa;
        hash += (int) jogadoridJogador;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TropaPK)) {
            return false;
        }
        TropaPK other = (TropaPK) object;
        if (this.idTropa != other.idTropa) {
            return false;
        }
        if (this.jogadoridJogador != other.jogadoridJogador) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TropaPK[ idTropa=" + idTropa + ", jogadoridJogador=" + jogadoridJogador + " ]";
    }
    
}
