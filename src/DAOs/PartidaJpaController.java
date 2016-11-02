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
import Entidades.Placar;
import Entidades.Equipe;
import java.util.ArrayList;
import java.util.List;
import Entidades.HistoricoJogador;
import Entidades.Partida;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class PartidaJpaController implements Serializable {

    public PartidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Partida partida) throws PreexistingEntityException, Exception {
        if (partida.getEquipeList() == null) {
            partida.setEquipeList(new ArrayList<Equipe>());
        }
        if (partida.getHistoricoJogadorList() == null) {
            partida.setHistoricoJogadorList(new ArrayList<HistoricoJogador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Placar placar = partida.getPlacar();
            if (placar != null) {
                placar = em.getReference(placar.getClass(), placar.getIdPlacar());
                partida.setPlacar(placar);
            }
            List<Equipe> attachedEquipeList = new ArrayList<Equipe>();
            for (Equipe equipeListEquipeToAttach : partida.getEquipeList()) {
                equipeListEquipeToAttach = em.getReference(equipeListEquipeToAttach.getClass(), equipeListEquipeToAttach.getIdEquipe());
                attachedEquipeList.add(equipeListEquipeToAttach);
            }
            partida.setEquipeList(attachedEquipeList);
            List<HistoricoJogador> attachedHistoricoJogadorList = new ArrayList<HistoricoJogador>();
            for (HistoricoJogador historicoJogadorListHistoricoJogadorToAttach : partida.getHistoricoJogadorList()) {
                historicoJogadorListHistoricoJogadorToAttach = em.getReference(historicoJogadorListHistoricoJogadorToAttach.getClass(), historicoJogadorListHistoricoJogadorToAttach.getIdHistoricoJogador());
                attachedHistoricoJogadorList.add(historicoJogadorListHistoricoJogadorToAttach);
            }
            partida.setHistoricoJogadorList(attachedHistoricoJogadorList);
            em.persist(partida);
            if (placar != null) {
                placar.getPartidaList().add(partida);
                placar = em.merge(placar);
            }
            for (Equipe equipeListEquipe : partida.getEquipeList()) {
                equipeListEquipe.getPartidaList().add(partida);
                equipeListEquipe = em.merge(equipeListEquipe);
            }
            for (HistoricoJogador historicoJogadorListHistoricoJogador : partida.getHistoricoJogadorList()) {
                Partida oldPartidaOfHistoricoJogadorListHistoricoJogador = historicoJogadorListHistoricoJogador.getPartida();
                historicoJogadorListHistoricoJogador.setPartida(partida);
                historicoJogadorListHistoricoJogador = em.merge(historicoJogadorListHistoricoJogador);
                if (oldPartidaOfHistoricoJogadorListHistoricoJogador != null) {
                    oldPartidaOfHistoricoJogadorListHistoricoJogador.getHistoricoJogadorList().remove(historicoJogadorListHistoricoJogador);
                    oldPartidaOfHistoricoJogadorListHistoricoJogador = em.merge(oldPartidaOfHistoricoJogadorListHistoricoJogador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPartida(partida.getIdPartida()) != null) {
                throw new PreexistingEntityException("Partida " + partida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Partida partida) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partida persistentPartida = em.find(Partida.class, partida.getIdPartida());
            Placar placarOld = persistentPartida.getPlacar();
            Placar placarNew = partida.getPlacar();
            List<Equipe> equipeListOld = persistentPartida.getEquipeList();
            List<Equipe> equipeListNew = partida.getEquipeList();
            List<HistoricoJogador> historicoJogadorListOld = persistentPartida.getHistoricoJogadorList();
            List<HistoricoJogador> historicoJogadorListNew = partida.getHistoricoJogadorList();
            List<String> illegalOrphanMessages = null;
            for (HistoricoJogador historicoJogadorListOldHistoricoJogador : historicoJogadorListOld) {
                if (!historicoJogadorListNew.contains(historicoJogadorListOldHistoricoJogador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistoricoJogador " + historicoJogadorListOldHistoricoJogador + " since its partida field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (placarNew != null) {
                placarNew = em.getReference(placarNew.getClass(), placarNew.getIdPlacar());
                partida.setPlacar(placarNew);
            }
            List<Equipe> attachedEquipeListNew = new ArrayList<Equipe>();
            for (Equipe equipeListNewEquipeToAttach : equipeListNew) {
                equipeListNewEquipeToAttach = em.getReference(equipeListNewEquipeToAttach.getClass(), equipeListNewEquipeToAttach.getIdEquipe());
                attachedEquipeListNew.add(equipeListNewEquipeToAttach);
            }
            equipeListNew = attachedEquipeListNew;
            partida.setEquipeList(equipeListNew);
            List<HistoricoJogador> attachedHistoricoJogadorListNew = new ArrayList<HistoricoJogador>();
            for (HistoricoJogador historicoJogadorListNewHistoricoJogadorToAttach : historicoJogadorListNew) {
                historicoJogadorListNewHistoricoJogadorToAttach = em.getReference(historicoJogadorListNewHistoricoJogadorToAttach.getClass(), historicoJogadorListNewHistoricoJogadorToAttach.getIdHistoricoJogador());
                attachedHistoricoJogadorListNew.add(historicoJogadorListNewHistoricoJogadorToAttach);
            }
            historicoJogadorListNew = attachedHistoricoJogadorListNew;
            partida.setHistoricoJogadorList(historicoJogadorListNew);
            partida = em.merge(partida);
            if (placarOld != null && !placarOld.equals(placarNew)) {
                placarOld.getPartidaList().remove(partida);
                placarOld = em.merge(placarOld);
            }
            if (placarNew != null && !placarNew.equals(placarOld)) {
                placarNew.getPartidaList().add(partida);
                placarNew = em.merge(placarNew);
            }
            for (Equipe equipeListOldEquipe : equipeListOld) {
                if (!equipeListNew.contains(equipeListOldEquipe)) {
                    equipeListOldEquipe.getPartidaList().remove(partida);
                    equipeListOldEquipe = em.merge(equipeListOldEquipe);
                }
            }
            for (Equipe equipeListNewEquipe : equipeListNew) {
                if (!equipeListOld.contains(equipeListNewEquipe)) {
                    equipeListNewEquipe.getPartidaList().add(partida);
                    equipeListNewEquipe = em.merge(equipeListNewEquipe);
                }
            }
            for (HistoricoJogador historicoJogadorListNewHistoricoJogador : historicoJogadorListNew) {
                if (!historicoJogadorListOld.contains(historicoJogadorListNewHistoricoJogador)) {
                    Partida oldPartidaOfHistoricoJogadorListNewHistoricoJogador = historicoJogadorListNewHistoricoJogador.getPartida();
                    historicoJogadorListNewHistoricoJogador.setPartida(partida);
                    historicoJogadorListNewHistoricoJogador = em.merge(historicoJogadorListNewHistoricoJogador);
                    if (oldPartidaOfHistoricoJogadorListNewHistoricoJogador != null && !oldPartidaOfHistoricoJogadorListNewHistoricoJogador.equals(partida)) {
                        oldPartidaOfHistoricoJogadorListNewHistoricoJogador.getHistoricoJogadorList().remove(historicoJogadorListNewHistoricoJogador);
                        oldPartidaOfHistoricoJogadorListNewHistoricoJogador = em.merge(oldPartidaOfHistoricoJogadorListNewHistoricoJogador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = partida.getIdPartida();
                if (findPartida(id) == null) {
                    throw new NonexistentEntityException("The partida with id " + id + " no longer exists.");
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
            Partida partida;
            try {
                partida = em.getReference(Partida.class, id);
                partida.getIdPartida();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partida with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<HistoricoJogador> historicoJogadorListOrphanCheck = partida.getHistoricoJogadorList();
            for (HistoricoJogador historicoJogadorListOrphanCheckHistoricoJogador : historicoJogadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partida (" + partida + ") cannot be destroyed since the HistoricoJogador " + historicoJogadorListOrphanCheckHistoricoJogador + " in its historicoJogadorList field has a non-nullable partida field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Placar placar = partida.getPlacar();
            if (placar != null) {
                placar.getPartidaList().remove(partida);
                placar = em.merge(placar);
            }
            List<Equipe> equipeList = partida.getEquipeList();
            for (Equipe equipeListEquipe : equipeList) {
                equipeListEquipe.getPartidaList().remove(partida);
                equipeListEquipe = em.merge(equipeListEquipe);
            }
            em.remove(partida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Partida> findPartidaEntities() {
        return findPartidaEntities(true, -1, -1);
    }

    public List<Partida> findPartidaEntities(int maxResults, int firstResult) {
        return findPartidaEntities(false, maxResults, firstResult);
    }

    private List<Partida> findPartidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Partida.class));
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

    public Partida findPartida(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Partida.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Partida> rt = cq.from(Partida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
