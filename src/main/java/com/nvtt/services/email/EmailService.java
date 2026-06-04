package com.nvtt.services.email;

import com.nvtt.utils.constants.EmailType;

public interface EmailService {

    void sendAccountNotification(
            String to,
            String name,
            EmailType type
    );
}
