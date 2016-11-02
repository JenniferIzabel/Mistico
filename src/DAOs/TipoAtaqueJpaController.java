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
import Entidades.Personagem;
import Entidades.TipoAtaque;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class TipoAtaqueJpaController implements Serializable {

    public TipoAtaqueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoAtaque tipoAtaque) throws PreexistingEntityException, Exception {
        if (tipoAtaque.getPersonagemList() == null) {
            tipoAtaque.setPersonagemList(new ArrayList<Personagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personagem> attachedPersonagemList = new ArrayList<Personagem>();
            for (Personagem personagemListPersonagemToAttach : tipoAtaque.getPersonagemList()) {
                personagemListPersonagemToAttach = em.getReference(personagemListPersonagemToAttach.getClass(), personagemListPersonagemToAttach.getPersonagemPK());
                attachedPersonagemList.add(personagemListPersonagemToAttach);
            }
            tipoAtaque.setPersonagemList(attachedPersonagemList);
            em.persist(tipoAtaque);
            for (Personagem personagemListPersonagem : tipoAtaque.getPersonagemList()) {
                personagemListPersonagem.getTipoAtaqueList().add(tipoAtaque);
                personagemListPersonagem = em.merge(personagemListPersonagem);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoAtaque(tipoAtaque.getIdTipoAtaque()) != null) {
                throw new PreexistingEntityException("TipoAtaque " + tipoAtaque + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAtaque tipoAtaque) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoAtaque persistentTipoAtaque = em.find(TipoAtaque.class, tipoAtaque.getIdTipoAtaque());
            List<Personagem> personagemListOld = persistentTipoAtaque.getPersonagemList();
            List<Personagem> personagemListNew = tipoAtaque.getPersonagemList();
            List<Personagem> attachedPersonagemListNew = new ArrayList<Personagem>();
            for (Personagem personagemListNewPersonagemToAttach : personagemListNew) {
                personagemListNewPersonagemToAttach = em.getReference(personagemListNewPersonagemToAttach.getClass(), personagemListNewPersonagemToAttach.getPersonagemPK());
                attachedPersonagemListNew.add(personagemListNewPersonagemToAttach);
            }
            personagemListNew = attachedPersonagemListNew;
            tipoAtaque.setPersonagemList(personagemListNew);
            tipoAtaque = em.merge(tipoAtaque);
            for (Personagem personagemListOldPersonagem : personagemListOld) {
                if (!personagemListNew.contains(personagemListOldPersonagem)) {
                    personagemListOldPersonagem.getTipoAtaqueList().remove(tipoAtaque);
                    personagemListOldPersonagem = em.merge(personagemListOldPersonagem);
                }
            }
            for (Personagem personagemListNewPersonagem : personagemListNew) {
                if (!personagemListOld.contains(personagemListNewPersonagem)) {
                    personagemListNewPersonagem.getTipoAtaqueList().add(tipoAtaque);
                    personagemListNewPersonagem = em.merge(personagemListNewPersonagem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoAtaque.getIdTipoAtaque();
                if (findTipoAtaque(id) == null) {
                    throw new NonexistentEntityException("The tipoAtaque with id " + id + " no longer exists.");
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
            TipoAtaque tipoAtaque;
            try {
                tipoAtaque = em.getReference(TipoAtaque.class, id);
                tipoAtaque.getIdTipoAtaque();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoAtaque with id " + id + " no longer exists.", enfe);
            }
            List<Personagem> personagemList = tipoAtaque.getPersonagemList();
            for (Personagem personagemListPersonagem : personagemList) {
                personagemListPersonagem.getTipoAtaqueList().remove(tipoAtaque);
                personagemListPersonagem = em.merge(personagemListPersonagem);
            }
            em.remove(tipoAtaque);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoAtaque> findTipoAtaqueEntities() {
        return findTipoAtaqueEntities(true, -1, -1);
    }

    public List<TipoAtaque> findTipoAtaqueEntities(int maxResults, int firstResult) {
        return findTipoAtaqueEntities(false, maxResults, firstResult);
    }

    private List<TipoAtaque> findTipoAtaqueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAtaque.class));
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

    public TipoAtaque findTipoAtaque(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoAtaque.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAtaqueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAtaque> rt = cq.from(TipoAtaque.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
