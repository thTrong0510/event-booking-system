/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lequa
 */
public interface TicketRepository {
    List<ResTicketDTO> getTicketDTOs(Map<String, String> params);
    List<Ticket> getTickets(Map<String, String> params);
}
