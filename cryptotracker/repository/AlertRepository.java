package com.task.cryptotracker.repository;

import com.task.cryptotracker.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface AlertRepository extends JpaRepository<Alert,Integer> {
}
