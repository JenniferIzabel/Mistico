/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jennifer
 */
@Entity
@Table(name = "Frames")
@NamedQueries({
    @NamedQuery(name = "Frames.findAll", query = "SELECT f FROM Frames f")})
public class Frames implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idFrames")
    private Integer idFrames;
    @Column(name = "bola1")
    private Integer bola1;
    @Column(name = "bola2")
    private Integer bola2;
    @Column(name = "bola3")
    private Integer bola3;
    @JoinColumn(name = "Placar_idPlacar", referencedColumnName = "idPlacar")
    @ManyToOne(optional = false)
    private Placar placar;

    public Frames() {
    }

    public Frames(Integer idFrames) {
        this.idFrames = idFrames;
    }

    public Integer getIdFrames() {
        return idFrames;
    }

    public void setIdFrames(Integer idFrames) {
        this.idFrames = idFrames;
    }

    public Integer getBola1() {
        return bola1;
    }

    public void setBola1(Integer bola1) {
        this.bola1 = bola1;
    }

    public Integer getBola2() {
        return bola2;
    }

    public void setBola2(Integer bola2) {
        this.bola2 = bola2;
    }

    public Integer getBola3() {
        return bola3;
    }

    public void setBola3(Integer bola3) {
        this.bola3 = bola3;
    }

    public Placar getPlacar() {
        return placar;
    }

    public void setPlacar(Placar placar) {
        this.placar = placar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFrames != null ? idFrames.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Frames)) {
            return false;
        }
        Frames other = (Frames) object;
        if ((this.idFrames == null && other.idFrames != null) || (this.idFrames != null && !this.idFrames.equals(other.idFrames))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Frames[ idFrames=" + idFrames + " ]";
    }
    
}
