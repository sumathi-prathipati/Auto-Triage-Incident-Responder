package com.cybersecurity.responder.repository;

import com.cybersecurity.responder.entity.Alert;
import com.cybersecurity.responder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAssignedAnalyst(User analyst);
    List<Alert> findByAssignedAnalystId(Long analystId);
}
