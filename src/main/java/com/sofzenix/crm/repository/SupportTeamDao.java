package com.sofzenix.crm.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofzenix.crm.model.Priority;
import com.sofzenix.crm.model.SupportTeam;

@ApplicationScoped
public class SupportTeamDao {

    @PersistenceContext
    EntityManager em;

    public SupportTeam save(SupportTeam team) {
        em.persist(team);
        return team;
    }

    public SupportTeam find(Long id) {
        return em.find(SupportTeam.class, id);
    }

    public SupportTeam update(SupportTeam team) {
        return em.merge(team);
    }

    public List<SupportTeam> findAll() {
        return em.createQuery("FROM SupportTeam", SupportTeam.class).getResultList();
    }

    // Select best matching team (highest skillLevel)
    public SupportTeam findBest(String product, String category, Priority priority) {
        try {
            return em.createQuery(
                    "SELECT s FROM SupportTeam s "
                    + "WHERE s.product = :p AND s.category = :c "
                    + "ORDER BY s.skillLevel DESC",
                    SupportTeam.class)
                    .setParameter("p", product)
                    .setParameter("c", category)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // no matching team
        }
    }
}
