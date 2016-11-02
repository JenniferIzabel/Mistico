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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "TipoAtaque")
@NamedQueries({
    @NamedQuery(name = "TipoAtaque.findAll", query = "SELECT t FROM TipoAtaque t")})
public class TipoAtaque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idTipoAtaque")
    private Integer idTipoAtaque;
    @Column(name = "tipo_ataque")
    private String tipoAtaque;
    @ManyToMany(mappedBy = "tipoAtaqueList")
    private List<Personagem> personagemList;

    public TipoAtaque() {
    }

    public TipoAtaque(Integer idTipoAtaque) {
        this.idTipoAtaque = idTipoAtaque;
    }

    public Integer getIdTipoAtaque() {
        return idTipoAtaque;
    }

    public void setIdTipoAtaque(Integer idTipoAtaque) {
        this.idTipoAtaque = idTipoAtaque;
    }

    public String getTipoAtaque() {
        return tipoAtaque;
    }

    public void setTipoAtaque(String tipoAtaque) {
        this.tipoAtaque = tipoAtaque;
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
        hash += (idTipoAtaque != null ? idTipoAtaque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAtaque)) {
            return false;
        }
        TipoAtaque other = (TipoAtaque) object;
        if ((this.idTipoAtaque == null && other.idTipoAtaque != null) || (this.idTipoAtaque != null && !this.idTipoAtaque.equals(other.idTipoAtaque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TipoAtaque[ idTipoAtaque=" + idTipoAtaque + " ]";
    }
    
}
