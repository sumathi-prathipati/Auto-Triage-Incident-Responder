package com.cybersecurity.responder.repository;

import com.cybersecurity.responder.entity.PredefinedAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredefinedAlertRepository extends JpaRepository<PredefinedAlert, Long> {
}
