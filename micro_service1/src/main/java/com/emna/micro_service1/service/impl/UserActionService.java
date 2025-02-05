package com.emna.micro_service1.service.impl;

import com.emna.micro_service1.entities.UserActionHistory;
import com.emna.micro_service1.repository.UserActionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionService {

    @Autowired
    private UserActionHistoryRepository historyRepository;

    public void logUserAction(String username, String action, String endpoint, String method) {
        UserActionHistory userActionHistory = new UserActionHistory(username, action, endpoint, method);
        historyRepository.save(userActionHistory);
    }
}
