/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import Entidades.Jogador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Personagem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author jennifer
 */
public class JogadorJpaController implements Serializable {

    public JogadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jogador jogador) throws PreexistingEntityException, Exception {
        if (jogador.getPersonagemList() == null) {
            jogador.setPersonagemList(new ArrayList<Personagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personagem> attachedPersonagemList = new ArrayList<Personagem>();
            for (Personagem personagemListPersonagemToAttach : jogador.getPersonagemList()) {
                personagemListPersonagemToAttach = em.getReference(personagemListPersonagemToAttach.getClass(), personagemListPersonagemToAttach.getIdPersonagem());
                attachedPersonagemList.add(personagemListPersonagemToAttach);
            }
            jogador.setPersonagemList(attachedPersonagemList);
            em.persist(jogador);
            for (Personagem personagemListPersonagem : jogador.getPersonagemList()) {
                personagemListPersonagem.getJogadorList().add(jogador);
                personagemListPersonagem = em.merge(personagemListPersonagem);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJogador(jogador.getIdJogador()) != null) {
                throw new PreexistingEntityException("Jogador " + jogador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jogador jogador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jogador persistentJogador = em.find(Jogador.class, jogador.getIdJogador());
            List<Personagem> personagemListOld = persistentJogador.getPersonagemList();
            List<Personagem> personagemListNew = jogador.getPersonagemList();
            List<Personagem> attachedPersonagemListNew = new ArrayList<Personagem>();
            for (Personagem personagemListNewPersonagemToAttach : personagemListNew) {
                personagemListNewPersonagemToAttach = em.getReference(personagemListNewPersonagemToAttach.getClass(), personagemListNewPersonagemToAttach.getIdPersonagem());
                attachedPersonagemListNew.add(personagemListNewPersonagemToAttach);
            }
            personagemListNew = attachedPersonagemListNew;
            jogador.setPersonagemList(personagemListNew);
            jogador = em.merge(jogador);
            for (Personagem personagemListOldPersonagem : personagemListOld) {
                if (!personagemListNew.contains(personagemListOldPersonagem)) {
                    personagemListOldPersonagem.getJogadorList().remove(jogador);
                    personagemListOldPersonagem = em.merge(personagemListOldPersonagem);
                }
            }
            for (Personagem personagemListNewPersonagem : personagemListNew) {
                if (!personagemListOld.contains(personagemListNewPersonagem)) {
                    personagemListNewPersonagem.getJogadorList().add(jogador);
                    personagemListNewPersonagem = em.merge(personagemListNewPersonagem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jogador.getIdJogador();
                if (findJogador(id) == null) {
                    throw new NonexistentEntityException("The jogador with id " + id + " no longer exists.");
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
            Jogador jogador;
            try {
                jogador = em.getReference(Jogador.class, id);
                jogador.getIdJogador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jogador with id " + id + " no longer exists.", enfe);
            }
            List<Personagem> personagemList = jogador.getPersonagemList();
            for (Personagem personagemListPersonagem : personagemList) {
                personagemListPersonagem.getJogadorList().remove(jogador);
                personagemListPersonagem = em.merge(personagemListPersonagem);
            }
            em.remove(jogador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jogador> findJogadorEntities() {
        return findJogadorEntities(true, -1, -1);
    }

    public List<Jogador> findJogadorEntities(int maxResults, int firstResult) {
        return findJogadorEntities(false, maxResults, firstResult);
    }

    private List<Jogador> findJogadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jogador.class));
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

    public Jogador findJogador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jogador.class, id);
        } finally {
            em.close();
        }
    }
    
    public Jogador findJogador(String user, String senha) {//verifica se o login e senha existem
        EntityManager em = getEntityManager();
        try {
            String sql = "SELECT e FROM Jogador e WHERE e.usuario= ?1 ";
            TypedQuery<Jogador> q = em.createQuery("SELECT e FROM Jogador e WHERE e.usuario= ?1 ", Jogador.class);
            return q.setParameter(1, user).getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getJogadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jogador> rt = cq.from(Jogador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
