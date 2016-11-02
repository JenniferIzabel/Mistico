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
public class PersonagemPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idPersonagem")
    private int idPersonagem;
    @Basic(optional = false)
    @Column(name = "Distancia_idDistancia")
    private int distanciaidDistancia;

    public PersonagemPK() {
    }

    public PersonagemPK(int idPersonagem, int distanciaidDistancia) {
        this.idPersonagem = idPersonagem;
        this.distanciaidDistancia = distanciaidDistancia;
    }

    public int getIdPersonagem() {
        return idPersonagem;
    }

    public void setIdPersonagem(int idPersonagem) {
        this.idPersonagem = idPersonagem;
    }

    public int getDistanciaidDistancia() {
        return distanciaidDistancia;
    }

    public void setDistanciaidDistancia(int distanciaidDistancia) {
        this.distanciaidDistancia = distanciaidDistancia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPersonagem;
        hash += (int) distanciaidDistancia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonagemPK)) {
            return false;
        }
        PersonagemPK other = (PersonagemPK) object;
        if (this.idPersonagem != other.idPersonagem) {
            return false;
        }
        if (this.distanciaidDistancia != other.distanciaidDistancia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PersonagemPK[ idPersonagem=" + idPersonagem + ", distanciaidDistancia=" + distanciaidDistancia + " ]";
    }
    
}
