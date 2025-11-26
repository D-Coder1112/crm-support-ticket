package com.sofzenix.crm.scheduler;

import com.sofzenix.crm.model.Status;
import com.sofzenix.crm.model.Ticket;
import com.sofzenix.crm.repository.TicketDao;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Singleton
public class SlaTimer {

    private static final Duration SLA_THRESHOLD = Duration.ofHours(48); // 48 hours

    @Inject
    TicketDao ticketDao;

    @Schedule(hour = "*", minute = "*/30", persistent = false)
    public void checkSLA() {
        try {
            List<Ticket> tickets = ticketDao.findAll();
            Instant now = Instant.now();

            for (Ticket t : tickets) {

                // Only check tickets in progress
                if (t.getStatus() == Status.IN_PROGRESS) {

                    Instant createdAt = getCreatedAt(t);
                    if (createdAt == null) {
                        continue;
                    }

                    Duration timePassed = Duration.between(createdAt, now);

                    if (timePassed.compareTo(SLA_THRESHOLD) > 0) {
                        t.setStatus(Status.ESCALATED);
                        ticketDao.update(t);
                        System.out.println("SLA Escalated ticket id = " + t.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Instant getCreatedAt(Ticket t) {
        try {
            // If Ticket has Instant createdAt
            return t.getCreatedAt();
        } catch (Throwable ignored) {
        }

        try {
            // If Ticket uses LocalDateTime createdOn
            return t.getCreatedOn()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toInstant();
        } catch (Throwable ignored) {
        }

        return null;
    }
}
