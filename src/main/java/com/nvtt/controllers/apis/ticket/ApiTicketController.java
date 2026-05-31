/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.apis.ticket;

import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import com.nvtt.services.TicketService;
import com.nvtt.utils.TicketUtils.TicketUtils;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lequa
 */
@RestController
@RequestMapping("/api")
public class ApiTicketController {
    
    private static final Logger logger = LogManager.getLogger(ApiTicketController.class);
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private TicketUtils ticketUtils;
    
    @GetMapping("/secure/tickets")
    public ResponseEntity<List<ResTicketDTO>> getTickets(@RequestParam Map<String, String> params) {
        logger.info("start sql getTickets");
        List<Ticket> tickets = this.ticketService.getTickets(params);
        List<ResTicketDTO> dtos = this.ticketUtils.convertToResTicketDTOList(tickets);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    
    @GetMapping("/secure/my-tickets")
    public ResponseEntity<List<ResTicketDTO>> getMyTickets(@RequestParam Map<String, String> params) {
        logger.info("start sql getMyTickets");
        List<ResTicketDTO> dtos = this.ticketService.getMyTickets(params);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/secure/organizer/tickets")
    public ResponseEntity<List<ResTicketDTO>> getOrganizerTickets(@RequestParam Map<String, String> params) {
        logger.info("start sql getOrganizerTickets");
        List<Ticket> tickets = this.ticketService.getOrganizerTickets(params);
        List<ResTicketDTO> dtos = this.ticketUtils.convertToResTicketDTOList(tickets);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
