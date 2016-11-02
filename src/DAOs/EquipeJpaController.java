/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import Entidades.Equipe;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.HistoricoJogador;
import java.util.ArrayList;
import java.util.List;
import Entidades.Partida;
import Entidades.Jogador;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jennifer
 */
public class EquipeJpaController implements Serializable {

    public EquipeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Equipe equipe) throws PreexistingEntityException, Exception {
        if (equipe.getHistoricoJogadorList() == null) {
            equipe.setHistoricoJogadorList(new ArrayList<HistoricoJogador>());
        }
        if (equipe.getPartidaList() == null) {
            equipe.setPartidaList(new ArrayList<Partida>());
        }
        if (equipe.getJogadorList() == null) {
            equipe.setJogadorList(new ArrayList<Jogador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<HistoricoJogador> attachedHistoricoJogadorList = new ArrayList<HistoricoJogador>();
            for (HistoricoJogador historicoJogadorListHistoricoJogadorToAttach : equipe.getHistoricoJogadorList()) {
                historicoJogadorListHistoricoJogadorToAttach = em.getReference(historicoJogadorListHistoricoJogadorToAttach.getClass(), historicoJogadorListHistoricoJogadorToAttach.getIdHistoricoJogador());
                attachedHistoricoJogadorList.add(historicoJogadorListHistoricoJogadorToAttach);
            }
            equipe.setHistoricoJogadorList(attachedHistoricoJogadorList);
            List<Partida> attachedPartidaList = new ArrayList<Partida>();
            for (Partida partidaListPartidaToAttach : equipe.getPartidaList()) {
                partidaListPartidaToAttach = em.getReference(partidaListPartidaToAttach.getClass(), partidaListPartidaToAttach.getIdPartida());
                attachedPartidaList.add(partidaListPartidaToAttach);
            }
            equipe.setPartidaList(attachedPartidaList);
            List<Jogador> attachedJogadorList = new ArrayList<Jogador>();
            for (Jogador jogadorListJogadorToAttach : equipe.getJogadorList()) {
                jogadorListJogadorToAttach = em.getReference(jogadorListJogadorToAttach.getClass(), jogadorListJogadorToAttach.getIdJogador());
                attachedJogadorList.add(jogadorListJogadorToAttach);
            }
            equipe.setJogadorList(attachedJogadorList);
            em.persist(equipe);
            for (HistoricoJogador historicoJogadorListHistoricoJogador : equipe.getHistoricoJogadorList()) {
                historicoJogadorListHistoricoJogador.getEquipeList().add(equipe);
                historicoJogadorListHistoricoJogador = em.merge(historicoJogadorListHistoricoJogador);
            }
            for (Partida partidaListPartida : equipe.getPartidaList()) {
                partidaListPartida.getEquipeList().add(equipe);
                partidaListPartida = em.merge(partidaListPartida);
            }
            for (Jogador jogadorListJogador : equipe.getJogadorList()) {
                Equipe oldEquipeOfJogadorListJogador = jogadorListJogador.getEquipe();
                jogadorListJogador.setEquipe(equipe);
                jogadorListJogador = em.merge(jogadorListJogador);
                if (oldEquipeOfJogadorListJogador != null) {
                    oldEquipeOfJogadorListJogador.getJogadorList().remove(jogadorListJogador);
                    oldEquipeOfJogadorListJogador = em.merge(oldEquipeOfJogadorListJogador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEquipe(equipe.getIdEquipe()) != null) {
                throw new PreexistingEntityException("Equipe " + equipe + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Equipe equipe) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipe persistentEquipe = em.find(Equipe.class, equipe.getIdEquipe());
            List<HistoricoJogador> historicoJogadorListOld = persistentEquipe.getHistoricoJogadorList();
            List<HistoricoJogador> historicoJogadorListNew = equipe.getHistoricoJogadorList();
            List<Partida> partidaListOld = persistentEquipe.getPartidaList();
            List<Partida> partidaListNew = equipe.getPartidaList();
            List<Jogador> jogadorListOld = persistentEquipe.getJogadorList();
            List<Jogador> jogadorListNew = equipe.getJogadorList();
            List<HistoricoJogador> attachedHistoricoJogadorListNew = new ArrayList<HistoricoJogador>();
            for (HistoricoJogador historicoJogadorListNewHistoricoJogadorToAttach : historicoJogadorListNew) {
                historicoJogadorListNewHistoricoJogadorToAttach = em.getReference(historicoJogadorListNewHistoricoJogadorToAttach.getClass(), historicoJogadorListNewHistoricoJogadorToAttach.getIdHistoricoJogador());
                attachedHistoricoJogadorListNew.add(historicoJogadorListNewHistoricoJogadorToAttach);
            }
            historicoJogadorListNew = attachedHistoricoJogadorListNew;
            equipe.setHistoricoJogadorList(historicoJogadorListNew);
            List<Partida> attachedPartidaListNew = new ArrayList<Partida>();
            for (Partida partidaListNewPartidaToAttach : partidaListNew) {
                partidaListNewPartidaToAttach = em.getReference(partidaListNewPartidaToAttach.getClass(), partidaListNewPartidaToAttach.getIdPartida());
                attachedPartidaListNew.add(partidaListNewPartidaToAttach);
            }
            partidaListNew = attachedPartidaListNew;
            equipe.setPartidaList(partidaListNew);
            List<Jogador> attachedJogadorListNew = new ArrayList<Jogador>();
            for (Jogador jogadorListNewJogadorToAttach : jogadorListNew) {
                jogadorListNewJogadorToAttach = em.getReference(jogadorListNewJogadorToAttach.getClass(), jogadorListNewJogadorToAttach.getIdJogador());
                attachedJogadorListNew.add(jogadorListNewJogadorToAttach);
            }
            jogadorListNew = attachedJogadorListNew;
            equipe.setJogadorList(jogadorListNew);
            equipe = em.merge(equipe);
            for (HistoricoJogador historicoJogadorListOldHistoricoJogador : historicoJogadorListOld) {
                if (!historicoJogadorListNew.contains(historicoJogadorListOldHistoricoJogador)) {
                    historicoJogadorListOldHistoricoJogador.getEquipeList().remove(equipe);
                    historicoJogadorListOldHistoricoJogador = em.merge(historicoJogadorListOldHistoricoJogador);
                }
            }
            for (HistoricoJogador historicoJogadorListNewHistoricoJogador : historicoJogadorListNew) {
                if (!historicoJogadorListOld.contains(historicoJogadorListNewHistoricoJogador)) {
                    historicoJogadorListNewHistoricoJogador.getEquipeList().add(equipe);
                    historicoJogadorListNewHistoricoJogador = em.merge(historicoJogadorListNewHistoricoJogador);
                }
            }
            for (Partida partidaListOldPartida : partidaListOld) {
                if (!partidaListNew.contains(partidaListOldPartida)) {
                    partidaListOldPartida.getEquipeList().remove(equipe);
                    partidaListOldPartida = em.merge(partidaListOldPartida);
                }
            }
            for (Partida partidaListNewPartida : partidaListNew) {
                if (!partidaListOld.contains(partidaListNewPartida)) {
                    partidaListNewPartida.getEquipeList().add(equipe);
                    partidaListNewPartida = em.merge(partidaListNewPartida);
                }
            }
            for (Jogador jogadorListOldJogador : jogadorListOld) {
                if (!jogadorListNew.contains(jogadorListOldJogador)) {
                    jogadorListOldJogador.setEquipe(null);
                    jogadorListOldJogador = em.merge(jogadorListOldJogador);
                }
            }
            for (Jogador jogadorListNewJogador : jogadorListNew) {
                if (!jogadorListOld.contains(jogadorListNewJogador)) {
                    Equipe oldEquipeOfJogadorListNewJogador = jogadorListNewJogador.getEquipe();
                    jogadorListNewJogador.setEquipe(equipe);
                    jogadorListNewJogador = em.merge(jogadorListNewJogador);
                    if (oldEquipeOfJogadorListNewJogador != null && !oldEquipeOfJogadorListNewJogador.equals(equipe)) {
                        oldEquipeOfJogadorListNewJogador.getJogadorList().remove(jogadorListNewJogador);
                        oldEquipeOfJogadorListNewJogador = em.merge(oldEquipeOfJogadorListNewJogador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = equipe.getIdEquipe();
                if (findEquipe(id) == null) {
                    throw new NonexistentEntityException("The equipe with id " + id + " no longer exists.");
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
            Equipe equipe;
            try {
                equipe = em.getReference(Equipe.class, id);
                equipe.getIdEquipe();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipe with id " + id + " no longer exists.", enfe);
            }
            List<HistoricoJogador> historicoJogadorList = equipe.getHistoricoJogadorList();
            for (HistoricoJogador historicoJogadorListHistoricoJogador : historicoJogadorList) {
                historicoJogadorListHistoricoJogador.getEquipeList().remove(equipe);
                historicoJogadorListHistoricoJogador = em.merge(historicoJogadorListHistoricoJogador);
            }
            List<Partida> partidaList = equipe.getPartidaList();
            for (Partida partidaListPartida : partidaList) {
                partidaListPartida.getEquipeList().remove(equipe);
                partidaListPartida = em.merge(partidaListPartida);
            }
            List<Jogador> jogadorList = equipe.getJogadorList();
            for (Jogador jogadorListJogador : jogadorList) {
                jogadorListJogador.setEquipe(null);
                jogadorListJogador = em.merge(jogadorListJogador);
            }
            em.remove(equipe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Equipe> findEquipeEntities() {
        return findEquipeEntities(true, -1, -1);
    }

    public List<Equipe> findEquipeEntities(int maxResults, int firstResult) {
        return findEquipeEntities(false, maxResults, firstResult);
    }

    private List<Equipe> findEquipeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Equipe.class));
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

    public Equipe findEquipe(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipe.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Equipe> rt = cq.from(Equipe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
