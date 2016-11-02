/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import DAOs.exceptions.IllegalOrphanException;
import DAOs.exceptions.NonexistentEntityException;
import DAOs.exceptions.PreexistingEntityException;
import Entidades.Distancia;
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

/**
 *
 * @author jennifer
 */
public class DistanciaJpaController implements Serializable {

    public DistanciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Distancia distancia) throws PreexistingEntityException, Exception {
        if (distancia.getPersonagemList() == null) {
            distancia.setPersonagemList(new ArrayList<Personagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Personagem> attachedPersonagemList = new ArrayList<Personagem>();
            for (Personagem personagemListPersonagemToAttach : distancia.getPersonagemList()) {
                personagemListPersonagemToAttach = em.getReference(personagemListPersonagemToAttach.getClass(), personagemListPersonagemToAttach.getPersonagemPK());
                attachedPersonagemList.add(personagemListPersonagemToAttach);
            }
            distancia.setPersonagemList(attachedPersonagemList);
            em.persist(distancia);
            for (Personagem personagemListPersonagem : distancia.getPersonagemList()) {
                Distancia oldDistanciaOfPersonagemListPersonagem = personagemListPersonagem.getDistancia();
                personagemListPersonagem.setDistancia(distancia);
                personagemListPersonagem = em.merge(personagemListPersonagem);
                if (oldDistanciaOfPersonagemListPersonagem != null) {
                    oldDistanciaOfPersonagemListPersonagem.getPersonagemList().remove(personagemListPersonagem);
                    oldDistanciaOfPersonagemListPersonagem = em.merge(oldDistanciaOfPersonagemListPersonagem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDistancia(distancia.getIdDistancia()) != null) {
                throw new PreexistingEntityException("Distancia " + distancia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Distancia distancia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distancia persistentDistancia = em.find(Distancia.class, distancia.getIdDistancia());
            List<Personagem> personagemListOld = persistentDistancia.getPersonagemList();
            List<Personagem> personagemListNew = distancia.getPersonagemList();
            List<String> illegalOrphanMessages = null;
            for (Personagem personagemListOldPersonagem : personagemListOld) {
                if (!personagemListNew.contains(personagemListOldPersonagem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Personagem " + personagemListOldPersonagem + " since its distancia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Personagem> attachedPersonagemListNew = new ArrayList<Personagem>();
            for (Personagem personagemListNewPersonagemToAttach : personagemListNew) {
                personagemListNewPersonagemToAttach = em.getReference(personagemListNewPersonagemToAttach.getClass(), personagemListNewPersonagemToAttach.getPersonagemPK());
                attachedPersonagemListNew.add(personagemListNewPersonagemToAttach);
            }
            personagemListNew = attachedPersonagemListNew;
            distancia.setPersonagemList(personagemListNew);
            distancia = em.merge(distancia);
            for (Personagem personagemListNewPersonagem : personagemListNew) {
                if (!personagemListOld.contains(personagemListNewPersonagem)) {
                    Distancia oldDistanciaOfPersonagemListNewPersonagem = personagemListNewPersonagem.getDistancia();
                    personagemListNewPersonagem.setDistancia(distancia);
                    personagemListNewPersonagem = em.merge(personagemListNewPersonagem);
                    if (oldDistanciaOfPersonagemListNewPersonagem != null && !oldDistanciaOfPersonagemListNewPersonagem.equals(distancia)) {
                        oldDistanciaOfPersonagemListNewPersonagem.getPersonagemList().remove(personagemListNewPersonagem);
                        oldDistanciaOfPersonagemListNewPersonagem = em.merge(oldDistanciaOfPersonagemListNewPersonagem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = distancia.getIdDistancia();
                if (findDistancia(id) == null) {
                    throw new NonexistentEntityException("The distancia with id " + id + " no longer exists.");
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
            Distancia distancia;
            try {
                distancia = em.getReference(Distancia.class, id);
                distancia.getIdDistancia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distancia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Personagem> personagemListOrphanCheck = distancia.getPersonagemList();
            for (Personagem personagemListOrphanCheckPersonagem : personagemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Distancia (" + distancia + ") cannot be destroyed since the Personagem " + personagemListOrphanCheckPersonagem + " in its personagemList field has a non-nullable distancia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(distancia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Distancia> findDistanciaEntities() {
        return findDistanciaEntities(true, -1, -1);
    }

    public List<Distancia> findDistanciaEntities(int maxResults, int firstResult) {
        return findDistanciaEntities(false, maxResults, firstResult);
    }

    private List<Distancia> findDistanciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Distancia.class));
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

    public Distancia findDistancia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Distancia.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistanciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Distancia> rt = cq.from(Distancia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
