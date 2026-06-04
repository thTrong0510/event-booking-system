package com.nvtt.repositories;

import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import java.util.List;
import java.util.Map;

public interface TicketRepository {

    List<ResTicketDTO> getTicketDTOs(Map<String, String> params);

    List<Ticket> getTickets(Map<String, String> params);
}
