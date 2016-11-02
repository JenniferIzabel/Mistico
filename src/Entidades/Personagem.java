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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
@Table(name = "Personagem")
@NamedQueries({
    @NamedQuery(name = "Personagem.findAll", query = "SELECT p FROM Personagem p")})
public class Personagem implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PersonagemPK personagemPK;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "ataque")
    private int ataque;
    @Basic(optional = false)
    @Column(name = "defesa")
    private int defesa;
    @Basic(optional = false)
    @Column(name = "hp")
    private int hp;
    @Basic(optional = false)
    @Lob
    @Column(name = "descricao")
    private String descricao;
    @JoinTable(name = "Tropa_has_Personagem", joinColumns = {
        @JoinColumn(name = "Personagem_idPersonagem", referencedColumnName = "idPersonagem")}, inverseJoinColumns = {
        @JoinColumn(name = "Tropa_idTropa", referencedColumnName = "idTropa")})
    @ManyToMany
    private List<Tropa> tropaList;
    @JoinTable(name = "Personagem_has_TipoAtaque", joinColumns = {
        @JoinColumn(name = "Personagem_idPersonagem", referencedColumnName = "idPersonagem")}, inverseJoinColumns = {
        @JoinColumn(name = "TipoAtaque_idTipoAtaque", referencedColumnName = "idTipoAtaque")})
    @ManyToMany
    private List<TipoAtaque> tipoAtaqueList;
    @JoinColumn(name = "Distancia_idDistancia", referencedColumnName = "idDistancia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Distancia distancia;

    public Personagem() {
    }

    public Personagem(PersonagemPK personagemPK) {
        this.personagemPK = personagemPK;
    }

    public Personagem(PersonagemPK personagemPK, String nome, int ataque, int defesa, int hp, String descricao) {
        this.personagemPK = personagemPK;
        this.nome = nome;
        this.ataque = ataque;
        this.defesa = defesa;
        this.hp = hp;
        this.descricao = descricao;
    }

    public Personagem(int idPersonagem, int distanciaidDistancia) {
        this.personagemPK = new PersonagemPK(idPersonagem, distanciaidDistancia);
    }

    public PersonagemPK getPersonagemPK() {
        return personagemPK;
    }

    public void setPersonagemPK(PersonagemPK personagemPK) {
        this.personagemPK = personagemPK;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Tropa> getTropaList() {
        return tropaList;
    }

    public void setTropaList(List<Tropa> tropaList) {
        this.tropaList = tropaList;
    }

    public List<TipoAtaque> getTipoAtaqueList() {
        return tipoAtaqueList;
    }

    public void setTipoAtaqueList(List<TipoAtaque> tipoAtaqueList) {
        this.tipoAtaqueList = tipoAtaqueList;
    }

    public Distancia getDistancia() {
        return distancia;
    }

    public void setDistancia(Distancia distancia) {
        this.distancia = distancia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personagemPK != null ? personagemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personagem)) {
            return false;
        }
        Personagem other = (Personagem) object;
        if ((this.personagemPK == null && other.personagemPK != null) || (this.personagemPK != null && !this.personagemPK.equals(other.personagemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Personagem[ personagemPK=" + personagemPK + " ]";
    }
    
}
