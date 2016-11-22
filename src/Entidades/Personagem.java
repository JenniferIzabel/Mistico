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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
    @Id
    @Basic(optional = false)
    @Column(name = "idPersonagem")
    private Integer idPersonagem;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "abreviacao")
    private String abreviacao;
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
    @Column(name = "distMov")
    private int distMov;
    @Basic(optional = false)
    @Column(name = "distAtaq")
    private int distAtaq;
    @Basic(optional = false)
    @Lob
    @Column(name = "descricao")
    private String descricao;
    @ManyToMany(mappedBy = "personagemList")
    private List<Jogador> jogadorList;

    public Personagem() {
    }

    public Personagem(Integer idPersonagem) {
        this.idPersonagem = idPersonagem;
    }

    public Personagem(Integer idPersonagem, String nome, String abreviacao, int ataque, int defesa, int hp, int distMov, int distAtaq, String descricao) {
        this.idPersonagem = idPersonagem;
        this.nome = nome;
        this.abreviacao = abreviacao;
        this.ataque = ataque;
        this.defesa = defesa;
        this.hp = hp;
        this.distMov = distMov;
        this.distAtaq = distAtaq;
        this.descricao = descricao;
    }

    public Integer getIdPersonagem() {
        return idPersonagem;
    }

    public void setIdPersonagem(Integer idPersonagem) {
        this.idPersonagem = idPersonagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
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

    public int getDistMov() {
        return distMov;
    }

    public void setDistMov(int distMov) {
        this.distMov = distMov;
    }

    public int getDistAtaq() {
        return distAtaq;
    }

    public void setDistAtaq(int distAtaq) {
        this.distAtaq = distAtaq;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        hash += (idPersonagem != null ? idPersonagem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personagem)) {
            return false;
        }
        Personagem other = (Personagem) object;
        if ((this.idPersonagem == null && other.idPersonagem != null) || (this.idPersonagem != null && !this.idPersonagem.equals(other.idPersonagem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return abreviacao+" - "+nome+" (HP: "+hp+", ATK: "+ataque+", DEF: "+defesa+")";
    }
    
    public String descricao() {
        return abreviacao+" - "+nome+"\nHP: "+hp+"\nATK: "+ataque+"\nDEF: "+defesa+"\n\n"+descricao;
    }
    
}
