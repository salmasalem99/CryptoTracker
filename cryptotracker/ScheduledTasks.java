package com.task.cryptotracker;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.task.cryptotracker.repository.*;
import com.task.cryptotracker.service.*;
import com.task.cryptotracker.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AlertService alertService;



    private String notification(){
        String notify = "";
        List<User> users = userRepository.findAll();
        if(users.size() == 0) return notify = "No users in system";
        for(User user : users){
            List<Alert> alerts = user.getAlerts();
            notify = notify + "\r\n{" + user.getUsername() + ":";
            if(alerts.size() == 0) notify = notify + "No alerts in system for this user} \r\n";
            else {
                for(Alert alert : alerts){
                    if(alertService.isTargetReached(alert.getAlertid()))
                        notify = notify+ "\r\n" + alert + " ----> Target price reached";
                }
                notify = notify + "} \r\n";
            }
        }
        return notify;
    }

    @Scheduled(fixedRate = 30000)
    public void notifyUser() {
        alertService.trigger();
        String output = notification();
        log.info(output, dateFormat.format(new Date()));

    }


}
