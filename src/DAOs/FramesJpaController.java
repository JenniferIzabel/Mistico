/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import Entidades.Frames;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Placar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class FramesJpaController implements Serializable {

    public FramesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Frames frames) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Placar placar = frames.getPlacar();
            if (placar != null) {
                placar = em.getReference(placar.getClass(), placar.getIdPlacar());
                frames.setPlacar(placar);
            }
            em.persist(frames);
            if (placar != null) {
                placar.getFramesList().add(frames);
                placar = em.merge(placar);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFrames(frames.getIdFrames()) != null) {
                throw new PreexistingEntityException("Frames " + frames + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Frames frames) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Frames persistentFrames = em.find(Frames.class, frames.getIdFrames());
            Placar placarOld = persistentFrames.getPlacar();
            Placar placarNew = frames.getPlacar();
            if (placarNew != null) {
                placarNew = em.getReference(placarNew.getClass(), placarNew.getIdPlacar());
                frames.setPlacar(placarNew);
            }
            frames = em.merge(frames);
            if (placarOld != null && !placarOld.equals(placarNew)) {
                placarOld.getFramesList().remove(frames);
                placarOld = em.merge(placarOld);
            }
            if (placarNew != null && !placarNew.equals(placarOld)) {
                placarNew.getFramesList().add(frames);
                placarNew = em.merge(placarNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = frames.getIdFrames();
                if (findFrames(id) == null) {
                    throw new NonexistentEntityException("The frames with id " + id + " no longer exists.");
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
            Frames frames;
            try {
                frames = em.getReference(Frames.class, id);
                frames.getIdFrames();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The frames with id " + id + " no longer exists.", enfe);
            }
            Placar placar = frames.getPlacar();
            if (placar != null) {
                placar.getFramesList().remove(frames);
                placar = em.merge(placar);
            }
            em.remove(frames);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Frames> findFramesEntities() {
        return findFramesEntities(true, -1, -1);
    }

    public List<Frames> findFramesEntities(int maxResults, int firstResult) {
        return findFramesEntities(false, maxResults, firstResult);
    }

    private List<Frames> findFramesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Frames.class));
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

    public Frames findFrames(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Frames.class, id);
        } finally {
            em.close();
        }
    }

    public int getFramesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Frames> rt = cq.from(Frames.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
