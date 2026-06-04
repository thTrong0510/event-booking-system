package com.nvtt.services.impl;

import com.nvtt.pojo.Event;
import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import com.nvtt.repositories.TicketRepository;
import com.nvtt.services.EventService;
import com.nvtt.services.TicketService;
import com.nvtt.utils.UserUtils.UserUtils;
import com.nvtt.utils.exceptions.ServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private EventService eventService;

    @Override
    public List<Ticket> getTickets(Map<String, String> params) {
        try {
            return ticketRepository.getTickets(params);
        } catch (Exception e) {
            throw new ServiceException("Failed to get tickets: " + e.getMessage());
        }
    }

    @Override
    public List<ResTicketDTO> getMyTickets(Map<String, String> params) {
        try {
            User current = userUtils.getCurrentUser();
            if (params.containsKey("attendeeId")) {
                throw new ServiceException("field attendeeId is not allowed ");
            }
            params.put("attendeeId", current.getId().toString());
            return ticketRepository.getTicketDTOs(params);
        } catch (Exception e) {
            throw new ServiceException("Failed to get my tickets: " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> getOrganizerTickets(Map<String, String> params) {
        try {
            List<Ticket> tickets = new ArrayList<>();
            if (params.containsKey("eventId")) {
                Event event = eventService.getOwnEventById(Long.valueOf(params.get("eventId")));
                if (event != null) {
                    tickets = ticketRepository.getTickets(params);
                    return tickets;
                } else {
                    return tickets;
                }
            } else {
                throw new ServiceException("field eventId is requirement");
            }

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
