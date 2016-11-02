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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "Distancia")
@NamedQueries({
    @NamedQuery(name = "Distancia.findAll", query = "SELECT d FROM Distancia d")})
public class Distancia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDistancia")
    private Integer idDistancia;
    @Column(name = "distanciaAtaque")
    private String distanciaAtaque;
    @Column(name = "distanciaMovimento")
    private String distanciaMovimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distancia")
    private List<Personagem> personagemList;

    public Distancia() {
    }

    public Distancia(Integer idDistancia) {
        this.idDistancia = idDistancia;
    }

    public Integer getIdDistancia() {
        return idDistancia;
    }

    public void setIdDistancia(Integer idDistancia) {
        this.idDistancia = idDistancia;
    }

    public String getDistanciaAtaque() {
        return distanciaAtaque;
    }

    public void setDistanciaAtaque(String distanciaAtaque) {
        this.distanciaAtaque = distanciaAtaque;
    }

    public String getDistanciaMovimento() {
        return distanciaMovimento;
    }

    public void setDistanciaMovimento(String distanciaMovimento) {
        this.distanciaMovimento = distanciaMovimento;
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
        hash += (idDistancia != null ? idDistancia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distancia)) {
            return false;
        }
        Distancia other = (Distancia) object;
        if ((this.idDistancia == null && other.idDistancia != null) || (this.idDistancia != null && !this.idDistancia.equals(other.idDistancia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Distancia[ idDistancia=" + idDistancia + " ]";
    }
    
}
