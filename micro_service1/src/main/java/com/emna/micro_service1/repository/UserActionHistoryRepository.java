package com.emna.micro_service1.repository;

import com.emna.micro_service1.entities.UserActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionHistoryRepository extends JpaRepository<UserActionHistory, Long> {
}

