package com.task.cryptotracker.service;
import com.task.cryptotracker.entity.Currency;
import com.task.cryptotracker.entity.User;
import com.task.cryptotracker.entity.Alert;

import com.task.cryptotracker.repository.CurrencyRepository;
import com.task.cryptotracker.repository.UserRepository;
import com.task.cryptotracker.repository.AlertRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.task.cryptotracker.UnsupportedCurrencyCreationException;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    @Autowired
    private AlertRepository repository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean readyAck(int id){
        boolean check1 = isTriggered(id);
        String currency = repository.findById(id).get().getCurrency();
        float currentPrice = currencyRepository.findByName(currency).get().getPrice();
        float  targetPrice = repository.findById(id).get().getTarget_price();
        return targetPrice <= currentPrice || check1;
    }

    public boolean isTargetReached(int id){
        Alert alert = repository.findById(id).get();
        String currency = alert.getCurrency();
        String status = alert.getStatus();
        float currentPrice = currencyRepository.findByName(currency).get().getPrice();
        float  targetPrice = repository.findById(id).get().getTarget_price();
        return targetPrice  <= currentPrice && !status.equals("ACKED");
    }

    public void trigger(){
        List<User> users = userRepository.findAll();
        if(users.size() == 0) return;
        for(User user : users) {
            List<Alert> alerts = user.getAlerts();
            for (Alert alert : alerts) {
                if (isTargetReached(alert.getAlertid())) {
                    alert.setStatus("TRIGGERED");
                    repository.save(alert);
                }
            }
        }
    }

    public boolean alertExists(int id){
        Optional<Alert> alert = repository.findById(id);
        return alert.isPresent();
    }

    public Alert getAlertById(int id){
        Alert alert = repository.findById(id).get();
        return alert;
    }

    public boolean addAlert(Alert alert){
        repository.save(alert);
        return true;
    }

    public boolean editPrice(int id, int newTP){
        Alert alert = repository.findById(id).get();
        alert.setTarget_price(newTP);
        repository.save(alert);
        return true;
    }

    public boolean isTriggered(int id){
        Alert alert = repository.findById(id).get();
        return alert.getStatus().equals("TRIGGERED");
    }
    public boolean isCancelled(int id){
        Alert alert = repository.findById(id).get();
        return alert.getStatus().equals("CANCELLED");
    }
    public boolean isAcked(int id){
        Alert alert = repository.findById(id).get();
        return alert.getStatus().equals("ACKED");
    }


    public boolean isNew(int id){
        Alert alert = repository.findById(id).get();
        return alert.getStatus().equals("NEW");
    }

    public boolean ackAlert(int id){
        Alert alert = repository.findById(id).get();
        alert.setStatus("ACKED");
        repository.save(alert);
        return true;
    }

    public boolean cancelAlert(int id){
        Alert alert = repository.findById(id).get();
        alert.setStatus("CANCELLED");
        repository.save(alert);
        return true;
    }

    public boolean deleteAlert(int id, String username){
        repository.deleteById(id);
        return true;
    }
}
