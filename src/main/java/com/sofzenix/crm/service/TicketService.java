package com.sofzenix.crm.service;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import com.sofzenix.crm.model.Ticket;
import com.sofzenix.crm.repository.SupportTeamDao;
import com.sofzenix.crm.repository.TicketDao;

@Singleton
public class TicketService {

    @Inject
    TicketDao ticketDao;

    @Inject
    SupportTeamDao supportTeamDao;

    // ========================================
    // STEP 1 — CREATE TICKET (AUTO GENERATE CODE)
    // ========================================
    @Transactional
    public Ticket create(Ticket ticket) {

        // Auto-generate ticket code
        ticket.setTicketCode(generateTicketCode());

        // Set default status
        ticket.setStatus("New");

        // Auto-timestamp
        ticket.setCreatedAt(LocalDateTime.now());

        return ticketDao.save(ticket);
    }

    // Generate a unique Ticket Code
    private String generateTicketCode() {
        String prefix = "TCK-";
        int randomNumber = (int) (Math.random() * 900000) + 100000;  // 6-digit random number
        return prefix + randomNumber;
    }

    // ========================================
    // STEP 2 — AUTO QUALIFICATION
    // ========================================
    @Transactional
    public Ticket qualify(Long ticketId) {

        Ticket ticket = ticketDao.find(ticketId);

        if (ticket == null) {
            throw new RuntimeException("Ticket not found: " + ticketId);
        }

        // Basic validation (because your Ticket.java has NO description field)
        if (ticket.getProduct() == null
                || ticket.getCategory() == null
                || ticket.getPriority() == null) {

            ticket.setStatus("Junk");
            return ticketDao.update(ticket);
        }

        // Qualified → In Progress
        ticket.setStatus("In Progress");
        return ticketDao.update(ticket);
    }

    // ========================================
    // STEP 3 — RESOLUTION / ESCALATION
    // ========================================
    @Transactional
    public Ticket resolve(Long ticketId, boolean solved) {

        Ticket ticket = ticketDao.find(ticketId);

        if (ticket == null) {
            throw new RuntimeException("Ticket not found: " + ticketId);
        }

        if (solved) {
            ticket.setStatus("Resolved");
        } else {
            ticket.setStatus("Escalated");
        }

        return ticketDao.update(ticket);
    }
}
