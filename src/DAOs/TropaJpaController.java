/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Jogador;
import Entidades.Personagem;
import Entidades.Tropa;
import Entidades.TropaPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class TropaJpaController implements Serializable {

    public TropaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tropa tropa) throws PreexistingEntityException, Exception {
        if (tropa.getTropaPK() == null) {
            tropa.setTropaPK(new TropaPK());
        }
        if (tropa.getPersonagemList() == null) {
            tropa.setPersonagemList(new ArrayList<Personagem>());
        }
        tropa.getTropaPK().setJogadoridJogador(tropa.getJogador().getIdJogador());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jogador jogador = tropa.getJogador();
            if (jogador != null) {
                jogador = em.getReference(jogador.getClass(), jogador.getIdJogador());
                tropa.setJogador(jogador);
            }
            List<Personagem> attachedPersonagemList = new ArrayList<Personagem>();
            for (Personagem personagemListPersonagemToAttach : tropa.getPersonagemList()) {
                personagemListPersonagemToAttach = em.getReference(personagemListPersonagemToAttach.getClass(), personagemListPersonagemToAttach.getPersonagemPK());
                attachedPersonagemList.add(personagemListPersonagemToAttach);
            }
            tropa.setPersonagemList(attachedPersonagemList);
            em.persist(tropa);
            if (jogador != null) {
                jogador.getTropaList().add(tropa);
                jogador = em.merge(jogador);
            }
            for (Personagem personagemListPersonagem : tropa.getPersonagemList()) {
                personagemListPersonagem.getTropaList().add(tropa);
                personagemListPersonagem = em.merge(personagemListPersonagem);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTropa(tropa.getTropaPK()) != null) {
                throw new PreexistingEntityException("Tropa " + tropa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tropa tropa) throws NonexistentEntityException, Exception {
        tropa.getTropaPK().setJogadoridJogador(tropa.getJogador().getIdJogador());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tropa persistentTropa = em.find(Tropa.class, tropa.getTropaPK());
            Jogador jogadorOld = persistentTropa.getJogador();
            Jogador jogadorNew = tropa.getJogador();
            List<Personagem> personagemListOld = persistentTropa.getPersonagemList();
            List<Personagem> personagemListNew = tropa.getPersonagemList();
            if (jogadorNew != null) {
                jogadorNew = em.getReference(jogadorNew.getClass(), jogadorNew.getIdJogador());
                tropa.setJogador(jogadorNew);
            }
            List<Personagem> attachedPersonagemListNew = new ArrayList<Personagem>();
            for (Personagem personagemListNewPersonagemToAttach : personagemListNew) {
                personagemListNewPersonagemToAttach = em.getReference(personagemListNewPersonagemToAttach.getClass(), personagemListNewPersonagemToAttach.getPersonagemPK());
                attachedPersonagemListNew.add(personagemListNewPersonagemToAttach);
            }
            personagemListNew = attachedPersonagemListNew;
            tropa.setPersonagemList(personagemListNew);
            tropa = em.merge(tropa);
            if (jogadorOld != null && !jogadorOld.equals(jogadorNew)) {
                jogadorOld.getTropaList().remove(tropa);
                jogadorOld = em.merge(jogadorOld);
            }
            if (jogadorNew != null && !jogadorNew.equals(jogadorOld)) {
                jogadorNew.getTropaList().add(tropa);
                jogadorNew = em.merge(jogadorNew);
            }
            for (Personagem personagemListOldPersonagem : personagemListOld) {
                if (!personagemListNew.contains(personagemListOldPersonagem)) {
                    personagemListOldPersonagem.getTropaList().remove(tropa);
                    personagemListOldPersonagem = em.merge(personagemListOldPersonagem);
                }
            }
            for (Personagem personagemListNewPersonagem : personagemListNew) {
                if (!personagemListOld.contains(personagemListNewPersonagem)) {
                    personagemListNewPersonagem.getTropaList().add(tropa);
                    personagemListNewPersonagem = em.merge(personagemListNewPersonagem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TropaPK id = tropa.getTropaPK();
                if (findTropa(id) == null) {
                    throw new NonexistentEntityException("The tropa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TropaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tropa tropa;
            try {
                tropa = em.getReference(Tropa.class, id);
                tropa.getTropaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tropa with id " + id + " no longer exists.", enfe);
            }
            Jogador jogador = tropa.getJogador();
            if (jogador != null) {
                jogador.getTropaList().remove(tropa);
                jogador = em.merge(jogador);
            }
            List<Personagem> personagemList = tropa.getPersonagemList();
            for (Personagem personagemListPersonagem : personagemList) {
                personagemListPersonagem.getTropaList().remove(tropa);
                personagemListPersonagem = em.merge(personagemListPersonagem);
            }
            em.remove(tropa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tropa> findTropaEntities() {
        return findTropaEntities(true, -1, -1);
    }

    public List<Tropa> findTropaEntities(int maxResults, int firstResult) {
        return findTropaEntities(false, maxResults, firstResult);
    }

    private List<Tropa> findTropaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tropa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tropa findTropa(TropaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tropa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTropaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tropa> rt = cq.from(Tropa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
