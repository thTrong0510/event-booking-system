package com.nvtt.services;

import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import java.util.List;
import java.util.Map;

public interface TicketService {

    List<Ticket> getTickets(Map<String, String> params);

    List<ResTicketDTO> getMyTickets(Map<String, String> params);

    List<Ticket> getOrganizerTickets(Map<String, String> params);
}
