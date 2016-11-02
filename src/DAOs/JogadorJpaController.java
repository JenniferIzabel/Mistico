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
import Entidades.Equipe;
import Entidades.HistoricoJogador;
import Entidades.Jogador;
import Entidades.Tropa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        if (jogador.getTropaList() == null) {
            jogador.setTropaList(new ArrayList<Tropa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipe equipe = jogador.getEquipe();
            if (equipe != null) {
                equipe = em.getReference(equipe.getClass(), equipe.getIdEquipe());
                jogador.setEquipe(equipe);
            }
            HistoricoJogador historicoJogador = jogador.getHistoricoJogador();
            if (historicoJogador != null) {
                historicoJogador = em.getReference(historicoJogador.getClass(), historicoJogador.getIdHistoricoJogador());
                jogador.setHistoricoJogador(historicoJogador);
            }
            List<Tropa> attachedTropaList = new ArrayList<Tropa>();
            for (Tropa tropaListTropaToAttach : jogador.getTropaList()) {
                tropaListTropaToAttach = em.getReference(tropaListTropaToAttach.getClass(), tropaListTropaToAttach.getTropaPK());
                attachedTropaList.add(tropaListTropaToAttach);
            }
            jogador.setTropaList(attachedTropaList);
            em.persist(jogador);
            if (equipe != null) {
                equipe.getJogadorList().add(jogador);
                equipe = em.merge(equipe);
            }
            if (historicoJogador != null) {
                historicoJogador.getJogadorList().add(jogador);
                historicoJogador = em.merge(historicoJogador);
            }
            for (Tropa tropaListTropa : jogador.getTropaList()) {
                Jogador oldJogadorOfTropaListTropa = tropaListTropa.getJogador();
                tropaListTropa.setJogador(jogador);
                tropaListTropa = em.merge(tropaListTropa);
                if (oldJogadorOfTropaListTropa != null) {
                    oldJogadorOfTropaListTropa.getTropaList().remove(tropaListTropa);
                    oldJogadorOfTropaListTropa = em.merge(oldJogadorOfTropaListTropa);
                }
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

    public void edit(Jogador jogador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jogador persistentJogador = em.find(Jogador.class, jogador.getIdJogador());
            Equipe equipeOld = persistentJogador.getEquipe();
            Equipe equipeNew = jogador.getEquipe();
            HistoricoJogador historicoJogadorOld = persistentJogador.getHistoricoJogador();
            HistoricoJogador historicoJogadorNew = jogador.getHistoricoJogador();
            List<Tropa> tropaListOld = persistentJogador.getTropaList();
            List<Tropa> tropaListNew = jogador.getTropaList();
            List<String> illegalOrphanMessages = null;
            for (Tropa tropaListOldTropa : tropaListOld) {
                if (!tropaListNew.contains(tropaListOldTropa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tropa " + tropaListOldTropa + " since its jogador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (equipeNew != null) {
                equipeNew = em.getReference(equipeNew.getClass(), equipeNew.getIdEquipe());
                jogador.setEquipe(equipeNew);
            }
            if (historicoJogadorNew != null) {
                historicoJogadorNew = em.getReference(historicoJogadorNew.getClass(), historicoJogadorNew.getIdHistoricoJogador());
                jogador.setHistoricoJogador(historicoJogadorNew);
            }
            List<Tropa> attachedTropaListNew = new ArrayList<Tropa>();
            for (Tropa tropaListNewTropaToAttach : tropaListNew) {
                tropaListNewTropaToAttach = em.getReference(tropaListNewTropaToAttach.getClass(), tropaListNewTropaToAttach.getTropaPK());
                attachedTropaListNew.add(tropaListNewTropaToAttach);
            }
            tropaListNew = attachedTropaListNew;
            jogador.setTropaList(tropaListNew);
            jogador = em.merge(jogador);
            if (equipeOld != null && !equipeOld.equals(equipeNew)) {
                equipeOld.getJogadorList().remove(jogador);
                equipeOld = em.merge(equipeOld);
            }
            if (equipeNew != null && !equipeNew.equals(equipeOld)) {
                equipeNew.getJogadorList().add(jogador);
                equipeNew = em.merge(equipeNew);
            }
            if (historicoJogadorOld != null && !historicoJogadorOld.equals(historicoJogadorNew)) {
                historicoJogadorOld.getJogadorList().remove(jogador);
                historicoJogadorOld = em.merge(historicoJogadorOld);
            }
            if (historicoJogadorNew != null && !historicoJogadorNew.equals(historicoJogadorOld)) {
                historicoJogadorNew.getJogadorList().add(jogador);
                historicoJogadorNew = em.merge(historicoJogadorNew);
            }
            for (Tropa tropaListNewTropa : tropaListNew) {
                if (!tropaListOld.contains(tropaListNewTropa)) {
                    Jogador oldJogadorOfTropaListNewTropa = tropaListNewTropa.getJogador();
                    tropaListNewTropa.setJogador(jogador);
                    tropaListNewTropa = em.merge(tropaListNewTropa);
                    if (oldJogadorOfTropaListNewTropa != null && !oldJogadorOfTropaListNewTropa.equals(jogador)) {
                        oldJogadorOfTropaListNewTropa.getTropaList().remove(tropaListNewTropa);
                        oldJogadorOfTropaListNewTropa = em.merge(oldJogadorOfTropaListNewTropa);
                    }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Tropa> tropaListOrphanCheck = jogador.getTropaList();
            for (Tropa tropaListOrphanCheckTropa : tropaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jogador (" + jogador + ") cannot be destroyed since the Tropa " + tropaListOrphanCheckTropa + " in its tropaList field has a non-nullable jogador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Equipe equipe = jogador.getEquipe();
            if (equipe != null) {
                equipe.getJogadorList().remove(jogador);
                equipe = em.merge(equipe);
            }
            HistoricoJogador historicoJogador = jogador.getHistoricoJogador();
            if (historicoJogador != null) {
                historicoJogador.getJogadorList().remove(jogador);
                historicoJogador = em.merge(historicoJogador);
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
