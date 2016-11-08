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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class PersonagemJpaController implements Serializable {

    public PersonagemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personagem personagem) throws PreexistingEntityException, Exception {
        if (personagem.getJogadorList() == null) {
            personagem.setJogadorList(new ArrayList<Jogador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Jogador> attachedJogadorList = new ArrayList<Jogador>();
            for (Jogador jogadorListJogadorToAttach : personagem.getJogadorList()) {
                jogadorListJogadorToAttach = em.getReference(jogadorListJogadorToAttach.getClass(), jogadorListJogadorToAttach.getIdJogador());
                attachedJogadorList.add(jogadorListJogadorToAttach);
            }
            personagem.setJogadorList(attachedJogadorList);
            em.persist(personagem);
            for (Jogador jogadorListJogador : personagem.getJogadorList()) {
                jogadorListJogador.getPersonagemList().add(personagem);
                jogadorListJogador = em.merge(jogadorListJogador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonagem(personagem.getIdPersonagem()) != null) {
                throw new PreexistingEntityException("Personagem " + personagem + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personagem personagem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personagem persistentPersonagem = em.find(Personagem.class, personagem.getIdPersonagem());
            List<Jogador> jogadorListOld = persistentPersonagem.getJogadorList();
            List<Jogador> jogadorListNew = personagem.getJogadorList();
            List<Jogador> attachedJogadorListNew = new ArrayList<Jogador>();
            for (Jogador jogadorListNewJogadorToAttach : jogadorListNew) {
                jogadorListNewJogadorToAttach = em.getReference(jogadorListNewJogadorToAttach.getClass(), jogadorListNewJogadorToAttach.getIdJogador());
                attachedJogadorListNew.add(jogadorListNewJogadorToAttach);
            }
            jogadorListNew = attachedJogadorListNew;
            personagem.setJogadorList(jogadorListNew);
            personagem = em.merge(personagem);
            for (Jogador jogadorListOldJogador : jogadorListOld) {
                if (!jogadorListNew.contains(jogadorListOldJogador)) {
                    jogadorListOldJogador.getPersonagemList().remove(personagem);
                    jogadorListOldJogador = em.merge(jogadorListOldJogador);
                }
            }
            for (Jogador jogadorListNewJogador : jogadorListNew) {
                if (!jogadorListOld.contains(jogadorListNewJogador)) {
                    jogadorListNewJogador.getPersonagemList().add(personagem);
                    jogadorListNewJogador = em.merge(jogadorListNewJogador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = personagem.getIdPersonagem();
                if (findPersonagem(id) == null) {
                    throw new NonexistentEntityException("The personagem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personagem personagem;
            try {
                personagem = em.getReference(Personagem.class, id);
                personagem.getIdPersonagem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personagem with id " + id + " no longer exists.", enfe);
            }
            List<Jogador> jogadorList = personagem.getJogadorList();
            for (Jogador jogadorListJogador : jogadorList) {
                jogadorListJogador.getPersonagemList().remove(personagem);
                jogadorListJogador = em.merge(jogadorListJogador);
            }
            em.remove(personagem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personagem> findPersonagemEntities() {
        return findPersonagemEntities(true, -1, -1);
    }

    public List<Personagem> findPersonagemEntities(int maxResults, int firstResult) {
        return findPersonagemEntities(false, maxResults, firstResult);
    }

    private List<Personagem> findPersonagemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personagem.class));
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

    public Personagem findPersonagem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personagem.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonagemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personagem> rt = cq.from(Personagem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
