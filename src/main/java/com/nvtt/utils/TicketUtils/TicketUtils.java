package com.nvtt.utils.TicketUtils;

import com.nvtt.pojo.Ticket;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.ticket.ResTicketDTO;
import com.nvtt.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketUtils {

    @Autowired
    private UserService userSerivce;

    public ResTicketDTO convertToResTicketDTO(Ticket ticket) {
        try {
            Long orderId = ticket.getOrder().getId();
            Long eventId = ticket.getEvent().getId();
            User u = ticket.getAttendee();
            if (u == null) {
                throw new RuntimeException("Dont have the owner for this ticket");
            }
            ResTicketDTO dto = new ResTicketDTO(ticket.getId(), orderId, eventId,
                    u.getEmail(), ticket.getTicketCode(), ticket.getCheckInTime());
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert ticket to response dto. Pls check order id, event id or attendee id");
        }
    }

    public List<ResTicketDTO> convertToResTicketDTOList(List<Ticket> tickets) {
        List<ResTicketDTO> dtos = new ArrayList<>();
        for (Ticket ticket : tickets) {
            dtos.add(this.convertToResTicketDTO(ticket));
        }
        return dtos;
    }
}
