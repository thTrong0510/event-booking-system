package com.nvtt.controllers.apis.event_status;

import com.nvtt.controllers.apis.orders.ApiOrdersController;
import com.nvtt.pojo.EventStatus;
import com.nvtt.pojo.dtos.event_status.ResEventStatusDTO;
import com.nvtt.services.EventStatusService;
import com.nvtt.utils.EventStatusUtils.EventStatusUtils;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ApiEventStatusController {

    private static final Logger logger = LogManager.getLogger(ApiOrdersController.class);

    @Autowired
    private EventStatusService eventStatusService;

    @Autowired
    private EventStatusUtils eventStatusUtils;

    @GetMapping("/organizer/event-status")
    public ResponseEntity<List<ResEventStatusDTO>> getEventStatuses() {
        logger.info("start sql getEventStatus");
        List<EventStatus> statuses = this.eventStatusService.getAllStatuses();
        List<ResEventStatusDTO> dtos = eventStatusUtils.convertToListResEventStatusDTO(statuses);
        logger.info("end sql");
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
