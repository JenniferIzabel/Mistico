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
import Entidades.Partida;
import Entidades.Equipe;
import Entidades.HistoricoJogador;
import java.util.ArrayList;
import java.util.List;
import Entidades.Jogador;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class HistoricoJogadorJpaController implements Serializable {

    public HistoricoJogadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistoricoJogador historicoJogador) throws PreexistingEntityException, Exception {
        if (historicoJogador.getEquipeList() == null) {
            historicoJogador.setEquipeList(new ArrayList<Equipe>());
        }
        if (historicoJogador.getJogadorList() == null) {
            historicoJogador.setJogadorList(new ArrayList<Jogador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partida partida = historicoJogador.getPartida();
            if (partida != null) {
                partida = em.getReference(partida.getClass(), partida.getIdPartida());
                historicoJogador.setPartida(partida);
            }
            List<Equipe> attachedEquipeList = new ArrayList<Equipe>();
            for (Equipe equipeListEquipeToAttach : historicoJogador.getEquipeList()) {
                equipeListEquipeToAttach = em.getReference(equipeListEquipeToAttach.getClass(), equipeListEquipeToAttach.getIdEquipe());
                attachedEquipeList.add(equipeListEquipeToAttach);
            }
            historicoJogador.setEquipeList(attachedEquipeList);
            List<Jogador> attachedJogadorList = new ArrayList<Jogador>();
            for (Jogador jogadorListJogadorToAttach : historicoJogador.getJogadorList()) {
                jogadorListJogadorToAttach = em.getReference(jogadorListJogadorToAttach.getClass(), jogadorListJogadorToAttach.getIdJogador());
                attachedJogadorList.add(jogadorListJogadorToAttach);
            }
            historicoJogador.setJogadorList(attachedJogadorList);
            em.persist(historicoJogador);
            if (partida != null) {
                partida.getHistoricoJogadorList().add(historicoJogador);
                partida = em.merge(partida);
            }
            for (Equipe equipeListEquipe : historicoJogador.getEquipeList()) {
                equipeListEquipe.getHistoricoJogadorList().add(historicoJogador);
                equipeListEquipe = em.merge(equipeListEquipe);
            }
            for (Jogador jogadorListJogador : historicoJogador.getJogadorList()) {
                HistoricoJogador oldHistoricoJogadorOfJogadorListJogador = jogadorListJogador.getHistoricoJogador();
                jogadorListJogador.setHistoricoJogador(historicoJogador);
                jogadorListJogador = em.merge(jogadorListJogador);
                if (oldHistoricoJogadorOfJogadorListJogador != null) {
                    oldHistoricoJogadorOfJogadorListJogador.getJogadorList().remove(jogadorListJogador);
                    oldHistoricoJogadorOfJogadorListJogador = em.merge(oldHistoricoJogadorOfJogadorListJogador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoricoJogador(historicoJogador.getIdHistoricoJogador()) != null) {
                throw new PreexistingEntityException("HistoricoJogador " + historicoJogador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistoricoJogador historicoJogador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistoricoJogador persistentHistoricoJogador = em.find(HistoricoJogador.class, historicoJogador.getIdHistoricoJogador());
            Partida partidaOld = persistentHistoricoJogador.getPartida();
            Partida partidaNew = historicoJogador.getPartida();
            List<Equipe> equipeListOld = persistentHistoricoJogador.getEquipeList();
            List<Equipe> equipeListNew = historicoJogador.getEquipeList();
            List<Jogador> jogadorListOld = persistentHistoricoJogador.getJogadorList();
            List<Jogador> jogadorListNew = historicoJogador.getJogadorList();
            List<String> illegalOrphanMessages = null;
            for (Jogador jogadorListOldJogador : jogadorListOld) {
                if (!jogadorListNew.contains(jogadorListOldJogador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jogador " + jogadorListOldJogador + " since its historicoJogador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (partidaNew != null) {
                partidaNew = em.getReference(partidaNew.getClass(), partidaNew.getIdPartida());
                historicoJogador.setPartida(partidaNew);
            }
            List<Equipe> attachedEquipeListNew = new ArrayList<Equipe>();
            for (Equipe equipeListNewEquipeToAttach : equipeListNew) {
                equipeListNewEquipeToAttach = em.getReference(equipeListNewEquipeToAttach.getClass(), equipeListNewEquipeToAttach.getIdEquipe());
                attachedEquipeListNew.add(equipeListNewEquipeToAttach);
            }
            equipeListNew = attachedEquipeListNew;
            historicoJogador.setEquipeList(equipeListNew);
            List<Jogador> attachedJogadorListNew = new ArrayList<Jogador>();
            for (Jogador jogadorListNewJogadorToAttach : jogadorListNew) {
                jogadorListNewJogadorToAttach = em.getReference(jogadorListNewJogadorToAttach.getClass(), jogadorListNewJogadorToAttach.getIdJogador());
                attachedJogadorListNew.add(jogadorListNewJogadorToAttach);
            }
            jogadorListNew = attachedJogadorListNew;
            historicoJogador.setJogadorList(jogadorListNew);
            historicoJogador = em.merge(historicoJogador);
            if (partidaOld != null && !partidaOld.equals(partidaNew)) {
                partidaOld.getHistoricoJogadorList().remove(historicoJogador);
                partidaOld = em.merge(partidaOld);
            }
            if (partidaNew != null && !partidaNew.equals(partidaOld)) {
                partidaNew.getHistoricoJogadorList().add(historicoJogador);
                partidaNew = em.merge(partidaNew);
            }
            for (Equipe equipeListOldEquipe : equipeListOld) {
                if (!equipeListNew.contains(equipeListOldEquipe)) {
                    equipeListOldEquipe.getHistoricoJogadorList().remove(historicoJogador);
                    equipeListOldEquipe = em.merge(equipeListOldEquipe);
                }
            }
            for (Equipe equipeListNewEquipe : equipeListNew) {
                if (!equipeListOld.contains(equipeListNewEquipe)) {
                    equipeListNewEquipe.getHistoricoJogadorList().add(historicoJogador);
                    equipeListNewEquipe = em.merge(equipeListNewEquipe);
                }
            }
            for (Jogador jogadorListNewJogador : jogadorListNew) {
                if (!jogadorListOld.contains(jogadorListNewJogador)) {
                    HistoricoJogador oldHistoricoJogadorOfJogadorListNewJogador = jogadorListNewJogador.getHistoricoJogador();
                    jogadorListNewJogador.setHistoricoJogador(historicoJogador);
                    jogadorListNewJogador = em.merge(jogadorListNewJogador);
                    if (oldHistoricoJogadorOfJogadorListNewJogador != null && !oldHistoricoJogadorOfJogadorListNewJogador.equals(historicoJogador)) {
                        oldHistoricoJogadorOfJogadorListNewJogador.getJogadorList().remove(jogadorListNewJogador);
                        oldHistoricoJogadorOfJogadorListNewJogador = em.merge(oldHistoricoJogadorOfJogadorListNewJogador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historicoJogador.getIdHistoricoJogador();
                if (findHistoricoJogador(id) == null) {
                    throw new NonexistentEntityException("The historicoJogador with id " + id + " no longer exists.");
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
            HistoricoJogador historicoJogador;
            try {
                historicoJogador = em.getReference(HistoricoJogador.class, id);
                historicoJogador.getIdHistoricoJogador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historicoJogador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jogador> jogadorListOrphanCheck = historicoJogador.getJogadorList();
            for (Jogador jogadorListOrphanCheckJogador : jogadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This HistoricoJogador (" + historicoJogador + ") cannot be destroyed since the Jogador " + jogadorListOrphanCheckJogador + " in its jogadorList field has a non-nullable historicoJogador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Partida partida = historicoJogador.getPartida();
            if (partida != null) {
                partida.getHistoricoJogadorList().remove(historicoJogador);
                partida = em.merge(partida);
            }
            List<Equipe> equipeList = historicoJogador.getEquipeList();
            for (Equipe equipeListEquipe : equipeList) {
                equipeListEquipe.getHistoricoJogadorList().remove(historicoJogador);
                equipeListEquipe = em.merge(equipeListEquipe);
            }
            em.remove(historicoJogador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistoricoJogador> findHistoricoJogadorEntities() {
        return findHistoricoJogadorEntities(true, -1, -1);
    }

    public List<HistoricoJogador> findHistoricoJogadorEntities(int maxResults, int firstResult) {
        return findHistoricoJogadorEntities(false, maxResults, firstResult);
    }

    private List<HistoricoJogador> findHistoricoJogadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistoricoJogador.class));
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

    public HistoricoJogador findHistoricoJogador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistoricoJogador.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricoJogadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistoricoJogador> rt = cq.from(HistoricoJogador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
