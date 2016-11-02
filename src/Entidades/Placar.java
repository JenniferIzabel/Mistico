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
@Table(name = "Placar")
@NamedQueries({
    @NamedQuery(name = "Placar.findAll", query = "SELECT p FROM Placar p")})
public class Placar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idPlacar")
    private Integer idPlacar;
    @Basic(optional = false)
    @Column(name = "pontosEqu1")
    private int pontosEqu1;
    @Basic(optional = false)
    @Column(name = "pontosEqu2")
    private int pontosEqu2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "placar")
    private List<Frames> framesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "placar")
    private List<Partida> partidaList;

    public Placar() {
    }

    public Placar(Integer idPlacar) {
        this.idPlacar = idPlacar;
    }

    public Placar(Integer idPlacar, int pontosEqu1, int pontosEqu2) {
        this.idPlacar = idPlacar;
        this.pontosEqu1 = pontosEqu1;
        this.pontosEqu2 = pontosEqu2;
    }

    public Integer getIdPlacar() {
        return idPlacar;
    }

    public void setIdPlacar(Integer idPlacar) {
        this.idPlacar = idPlacar;
    }

    public int getPontosEqu1() {
        return pontosEqu1;
    }

    public void setPontosEqu1(int pontosEqu1) {
        this.pontosEqu1 = pontosEqu1;
    }

    public int getPontosEqu2() {
        return pontosEqu2;
    }

    public void setPontosEqu2(int pontosEqu2) {
        this.pontosEqu2 = pontosEqu2;
    }

    public List<Frames> getFramesList() {
        return framesList;
    }

    public void setFramesList(List<Frames> framesList) {
        this.framesList = framesList;
    }

    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {
        this.partidaList = partidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlacar != null ? idPlacar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Placar)) {
            return false;
        }
        Placar other = (Placar) object;
        if ((this.idPlacar == null && other.idPlacar != null) || (this.idPlacar != null && !this.idPlacar.equals(other.idPlacar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Placar[ idPlacar=" + idPlacar + " ]";
    }
    
}
