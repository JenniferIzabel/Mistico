/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DAOs.exceptions.IllegalOrphanException;
import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Frames;
import java.util.ArrayList;
import java.util.List;
import Entidades.Partida;
import Entidades.Placar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class PlacarJpaController implements Serializable {

    public PlacarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Placar placar) throws PreexistingEntityException, Exception {
        if (placar.getFramesList() == null) {
            placar.setFramesList(new ArrayList<Frames>());
        }
        if (placar.getPartidaList() == null) {
            placar.setPartidaList(new ArrayList<Partida>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Frames> attachedFramesList = new ArrayList<Frames>();
            for (Frames framesListFramesToAttach : placar.getFramesList()) {
                framesListFramesToAttach = em.getReference(framesListFramesToAttach.getClass(), framesListFramesToAttach.getIdFrames());
                attachedFramesList.add(framesListFramesToAttach);
            }
            placar.setFramesList(attachedFramesList);
            List<Partida> attachedPartidaList = new ArrayList<Partida>();
            for (Partida partidaListPartidaToAttach : placar.getPartidaList()) {
                partidaListPartidaToAttach = em.getReference(partidaListPartidaToAttach.getClass(), partidaListPartidaToAttach.getIdPartida());
                attachedPartidaList.add(partidaListPartidaToAttach);
            }
            placar.setPartidaList(attachedPartidaList);
            em.persist(placar);
            for (Frames framesListFrames : placar.getFramesList()) {
                Placar oldPlacarOfFramesListFrames = framesListFrames.getPlacar();
                framesListFrames.setPlacar(placar);
                framesListFrames = em.merge(framesListFrames);
                if (oldPlacarOfFramesListFrames != null) {
                    oldPlacarOfFramesListFrames.getFramesList().remove(framesListFrames);
                    oldPlacarOfFramesListFrames = em.merge(oldPlacarOfFramesListFrames);
                }
            }
            for (Partida partidaListPartida : placar.getPartidaList()) {
                Placar oldPlacarOfPartidaListPartida = partidaListPartida.getPlacar();
                partidaListPartida.setPlacar(placar);
                partidaListPartida = em.merge(partidaListPartida);
                if (oldPlacarOfPartidaListPartida != null) {
                    oldPlacarOfPartidaListPartida.getPartidaList().remove(partidaListPartida);
                    oldPlacarOfPartidaListPartida = em.merge(oldPlacarOfPartidaListPartida);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPlacar(placar.getIdPlacar()) != null) {
                throw new PreexistingEntityException("Placar " + placar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Placar placar) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Placar persistentPlacar = em.find(Placar.class, placar.getIdPlacar());
            List<Frames> framesListOld = persistentPlacar.getFramesList();
            List<Frames> framesListNew = placar.getFramesList();
            List<Partida> partidaListOld = persistentPlacar.getPartidaList();
            List<Partida> partidaListNew = placar.getPartidaList();
            List<String> illegalOrphanMessages = null;
            for (Frames framesListOldFrames : framesListOld) {
                if (!framesListNew.contains(framesListOldFrames)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Frames " + framesListOldFrames + " since its placar field is not nullable.");
                }
            }
            for (Partida partidaListOldPartida : partidaListOld) {
                if (!partidaListNew.contains(partidaListOldPartida)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partida " + partidaListOldPartida + " since its placar field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Frames> attachedFramesListNew = new ArrayList<Frames>();
            for (Frames framesListNewFramesToAttach : framesListNew) {
                framesListNewFramesToAttach = em.getReference(framesListNewFramesToAttach.getClass(), framesListNewFramesToAttach.getIdFrames());
                attachedFramesListNew.add(framesListNewFramesToAttach);
            }
            framesListNew = attachedFramesListNew;
            placar.setFramesList(framesListNew);
            List<Partida> attachedPartidaListNew = new ArrayList<Partida>();
            for (Partida partidaListNewPartidaToAttach : partidaListNew) {
                partidaListNewPartidaToAttach = em.getReference(partidaListNewPartidaToAttach.getClass(), partidaListNewPartidaToAttach.getIdPartida());
                attachedPartidaListNew.add(partidaListNewPartidaToAttach);
            }
            partidaListNew = attachedPartidaListNew;
            placar.setPartidaList(partidaListNew);
            placar = em.merge(placar);
            for (Frames framesListNewFrames : framesListNew) {
                if (!framesListOld.contains(framesListNewFrames)) {
                    Placar oldPlacarOfFramesListNewFrames = framesListNewFrames.getPlacar();
                    framesListNewFrames.setPlacar(placar);
                    framesListNewFrames = em.merge(framesListNewFrames);
                    if (oldPlacarOfFramesListNewFrames != null && !oldPlacarOfFramesListNewFrames.equals(placar)) {
                        oldPlacarOfFramesListNewFrames.getFramesList().remove(framesListNewFrames);
                        oldPlacarOfFramesListNewFrames = em.merge(oldPlacarOfFramesListNewFrames);
                    }
                }
            }
            for (Partida partidaListNewPartida : partidaListNew) {
                if (!partidaListOld.contains(partidaListNewPartida)) {
                    Placar oldPlacarOfPartidaListNewPartida = partidaListNewPartida.getPlacar();
                    partidaListNewPartida.setPlacar(placar);
                    partidaListNewPartida = em.merge(partidaListNewPartida);
                    if (oldPlacarOfPartidaListNewPartida != null && !oldPlacarOfPartidaListNewPartida.equals(placar)) {
                        oldPlacarOfPartidaListNewPartida.getPartidaList().remove(partidaListNewPartida);
                        oldPlacarOfPartidaListNewPartida = em.merge(oldPlacarOfPartidaListNewPartida);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = placar.getIdPlacar();
                if (findPlacar(id) == null) {
                    throw new NonexistentEntityException("The placar with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Placar placar;
            try {
                placar = em.getReference(Placar.class, id);
                placar.getIdPlacar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The placar with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Frames> framesListOrphanCheck = placar.getFramesList();
            for (Frames framesListOrphanCheckFrames : framesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Placar (" + placar + ") cannot be destroyed since the Frames " + framesListOrphanCheckFrames + " in its framesList field has a non-nullable placar field.");
            }
            List<Partida> partidaListOrphanCheck = placar.getPartidaList();
            for (Partida partidaListOrphanCheckPartida : partidaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Placar (" + placar + ") cannot be destroyed since the Partida " + partidaListOrphanCheckPartida + " in its partidaList field has a non-nullable placar field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(placar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Placar> findPlacarEntities() {
        return findPlacarEntities(true, -1, -1);
    }

    public List<Placar> findPlacarEntities(int maxResults, int firstResult) {
        return findPlacarEntities(false, maxResults, firstResult);
    }

    private List<Placar> findPlacarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Placar.class));
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

    public Placar findPlacar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Placar.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlacarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Placar> rt = cq.from(Placar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
