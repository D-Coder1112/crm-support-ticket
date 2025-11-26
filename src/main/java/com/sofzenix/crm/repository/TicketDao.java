package com.sofzenix.crm.repository;

import com.sofzenix.crm.model.Ticket;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class TicketDao {

    @PersistenceContext
    EntityManager em;

    public Ticket save(Ticket ticket) {
        em.persist(ticket);
        return ticket;
    }

    public Ticket find(Long id) {
        return em.find(Ticket.class, id);
    }

    public Ticket update(Ticket ticket) {
        return em.merge(ticket);
    }

    public List<Ticket> findAll() {
        return em.createQuery("FROM Ticket", Ticket.class).getResultList();
    }
}
