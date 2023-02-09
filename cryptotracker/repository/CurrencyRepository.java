package com.task.cryptotracker.repository;

import com.task.cryptotracker.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Integer> {

    Optional <Currency> findByName(String name);

}
