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
import Entidades.Distancia;
import Entidades.Personagem;
import Entidades.PersonagemPK;
import Entidades.Tropa;
import java.util.ArrayList;
import java.util.List;
import Entidades.TipoAtaque;
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
        if (personagem.getPersonagemPK() == null) {
            personagem.setPersonagemPK(new PersonagemPK());
        }
        if (personagem.getTropaList() == null) {
            personagem.setTropaList(new ArrayList<Tropa>());
        }
        if (personagem.getTipoAtaqueList() == null) {
            personagem.setTipoAtaqueList(new ArrayList<TipoAtaque>());
        }
        personagem.getPersonagemPK().setDistanciaidDistancia(personagem.getDistancia().getIdDistancia());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distancia distancia = personagem.getDistancia();
            if (distancia != null) {
                distancia = em.getReference(distancia.getClass(), distancia.getIdDistancia());
                personagem.setDistancia(distancia);
            }
            List<Tropa> attachedTropaList = new ArrayList<Tropa>();
            for (Tropa tropaListTropaToAttach : personagem.getTropaList()) {
                tropaListTropaToAttach = em.getReference(tropaListTropaToAttach.getClass(), tropaListTropaToAttach.getTropaPK());
                attachedTropaList.add(tropaListTropaToAttach);
            }
            personagem.setTropaList(attachedTropaList);
            List<TipoAtaque> attachedTipoAtaqueList = new ArrayList<TipoAtaque>();
            for (TipoAtaque tipoAtaqueListTipoAtaqueToAttach : personagem.getTipoAtaqueList()) {
                tipoAtaqueListTipoAtaqueToAttach = em.getReference(tipoAtaqueListTipoAtaqueToAttach.getClass(), tipoAtaqueListTipoAtaqueToAttach.getIdTipoAtaque());
                attachedTipoAtaqueList.add(tipoAtaqueListTipoAtaqueToAttach);
            }
            personagem.setTipoAtaqueList(attachedTipoAtaqueList);
            em.persist(personagem);
            if (distancia != null) {
                distancia.getPersonagemList().add(personagem);
                distancia = em.merge(distancia);
            }
            for (Tropa tropaListTropa : personagem.getTropaList()) {
                tropaListTropa.getPersonagemList().add(personagem);
                tropaListTropa = em.merge(tropaListTropa);
            }
            for (TipoAtaque tipoAtaqueListTipoAtaque : personagem.getTipoAtaqueList()) {
                tipoAtaqueListTipoAtaque.getPersonagemList().add(personagem);
                tipoAtaqueListTipoAtaque = em.merge(tipoAtaqueListTipoAtaque);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonagem(personagem.getPersonagemPK()) != null) {
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
        personagem.getPersonagemPK().setDistanciaidDistancia(personagem.getDistancia().getIdDistancia());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personagem persistentPersonagem = em.find(Personagem.class, personagem.getPersonagemPK());
            Distancia distanciaOld = persistentPersonagem.getDistancia();
            Distancia distanciaNew = personagem.getDistancia();
            List<Tropa> tropaListOld = persistentPersonagem.getTropaList();
            List<Tropa> tropaListNew = personagem.getTropaList();
            List<TipoAtaque> tipoAtaqueListOld = persistentPersonagem.getTipoAtaqueList();
            List<TipoAtaque> tipoAtaqueListNew = personagem.getTipoAtaqueList();
            if (distanciaNew != null) {
                distanciaNew = em.getReference(distanciaNew.getClass(), distanciaNew.getIdDistancia());
                personagem.setDistancia(distanciaNew);
            }
            List<Tropa> attachedTropaListNew = new ArrayList<Tropa>();
            for (Tropa tropaListNewTropaToAttach : tropaListNew) {
                tropaListNewTropaToAttach = em.getReference(tropaListNewTropaToAttach.getClass(), tropaListNewTropaToAttach.getTropaPK());
                attachedTropaListNew.add(tropaListNewTropaToAttach);
            }
            tropaListNew = attachedTropaListNew;
            personagem.setTropaList(tropaListNew);
            List<TipoAtaque> attachedTipoAtaqueListNew = new ArrayList<TipoAtaque>();
            for (TipoAtaque tipoAtaqueListNewTipoAtaqueToAttach : tipoAtaqueListNew) {
                tipoAtaqueListNewTipoAtaqueToAttach = em.getReference(tipoAtaqueListNewTipoAtaqueToAttach.getClass(), tipoAtaqueListNewTipoAtaqueToAttach.getIdTipoAtaque());
                attachedTipoAtaqueListNew.add(tipoAtaqueListNewTipoAtaqueToAttach);
            }
            tipoAtaqueListNew = attachedTipoAtaqueListNew;
            personagem.setTipoAtaqueList(tipoAtaqueListNew);
            personagem = em.merge(personagem);
            if (distanciaOld != null && !distanciaOld.equals(distanciaNew)) {
                distanciaOld.getPersonagemList().remove(personagem);
                distanciaOld = em.merge(distanciaOld);
            }
            if (distanciaNew != null && !distanciaNew.equals(distanciaOld)) {
                distanciaNew.getPersonagemList().add(personagem);
                distanciaNew = em.merge(distanciaNew);
            }
            for (Tropa tropaListOldTropa : tropaListOld) {
                if (!tropaListNew.contains(tropaListOldTropa)) {
                    tropaListOldTropa.getPersonagemList().remove(personagem);
                    tropaListOldTropa = em.merge(tropaListOldTropa);
                }
            }
            for (Tropa tropaListNewTropa : tropaListNew) {
                if (!tropaListOld.contains(tropaListNewTropa)) {
                    tropaListNewTropa.getPersonagemList().add(personagem);
                    tropaListNewTropa = em.merge(tropaListNewTropa);
                }
            }
            for (TipoAtaque tipoAtaqueListOldTipoAtaque : tipoAtaqueListOld) {
                if (!tipoAtaqueListNew.contains(tipoAtaqueListOldTipoAtaque)) {
                    tipoAtaqueListOldTipoAtaque.getPersonagemList().remove(personagem);
                    tipoAtaqueListOldTipoAtaque = em.merge(tipoAtaqueListOldTipoAtaque);
                }
            }
            for (TipoAtaque tipoAtaqueListNewTipoAtaque : tipoAtaqueListNew) {
                if (!tipoAtaqueListOld.contains(tipoAtaqueListNewTipoAtaque)) {
                    tipoAtaqueListNewTipoAtaque.getPersonagemList().add(personagem);
                    tipoAtaqueListNewTipoAtaque = em.merge(tipoAtaqueListNewTipoAtaque);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PersonagemPK id = personagem.getPersonagemPK();
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

    public void destroy(PersonagemPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personagem personagem;
            try {
                personagem = em.getReference(Personagem.class, id);
                personagem.getPersonagemPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personagem with id " + id + " no longer exists.", enfe);
            }
            Distancia distancia = personagem.getDistancia();
            if (distancia != null) {
                distancia.getPersonagemList().remove(personagem);
                distancia = em.merge(distancia);
            }
            List<Tropa> tropaList = personagem.getTropaList();
            for (Tropa tropaListTropa : tropaList) {
                tropaListTropa.getPersonagemList().remove(personagem);
                tropaListTropa = em.merge(tropaListTropa);
            }
            List<TipoAtaque> tipoAtaqueList = personagem.getTipoAtaqueList();
            for (TipoAtaque tipoAtaqueListTipoAtaque : tipoAtaqueList) {
                tipoAtaqueListTipoAtaque.getPersonagemList().remove(personagem);
                tipoAtaqueListTipoAtaque = em.merge(tipoAtaqueListTipoAtaque);
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

    public Personagem findPersonagem(PersonagemPK id) {
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
