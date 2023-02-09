package com.task.cryptotracker.controller;
import com.task.cryptotracker.entity.Alert;
import com.task.cryptotracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
//403 -->>>> forbidden
//TESTED
@RestController
@RequestMapping(path = "alertAPI")
public class AlertController {

    @Autowired
    private AlertService service;
    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyService currencyService;
    //User can create/edit/delete the alerts
    // we have post, put, delete
    //post alert
    //put(cancel, ack, target price)
    //delete by id, currency
    //User can cancel the alert if it is not triggered yet
    //User can acknowledge the alert when he is notified.(The target price was reached)

    //done
    @PostMapping("/addAlert")
    public ResponseEntity<String> addAlert(@RequestBody Alert alert, @RequestParam(required=true) String username){
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!currencyService.currencyExists(alert.getCurrency())) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        alert.setUsername(username);
        boolean response = service.addAlert(alert);
        return new ResponseEntity<>("Alert successfully added", HttpStatus.OK);
    }

    @GetMapping("/alert/{id}")
    public Alert getAlert(@PathVariable int id, @RequestParam(required=true) String username) {
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!service.alertExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        return service.getAlertById(id);
    }


    //Edit target price
    //done
    @PutMapping("/editTargetPrice/{id}")
    public ResponseEntity<String> updateAlert(@PathVariable int id, @RequestParam(required=true) int newTP, @RequestParam(required=true) String username){
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!service.alertExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        boolean response = service.editPrice(id, newTP);
        return new ResponseEntity<>("Target price for alert " + id + " successfully updated to " + newTP, HttpStatus.OK);
    }

    //acknowledge alert ONLY if target reached
    //done
    @PutMapping("/acknowledge/{id}")
    public ResponseEntity<String> ackAlert(@PathVariable int id, @RequestParam(required=true) String username){
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!service.alertExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        if(service.isCancelled(id)) return new ResponseEntity<>("Alert " + id + " was cancelled", HttpStatus.OK);
        if(!service.readyAck(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Not acknowledged: Currency has not reached target price");
        if(service.isAcked(id)) return new ResponseEntity<>("Alert " + id + " Already acked", HttpStatus.OK);
        boolean response = service.ackAlert(id);
        return new ResponseEntity<>("Alert " + id + " successfully acknowledged", HttpStatus.OK);
    }

    //cancel alert ONLY if NEW
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAlert(@PathVariable int id, @RequestParam(required=true) String username){
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!service.alertExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        if(service.isCancelled(id)) return new ResponseEntity<>("Alert " + id + " Already cancelled", HttpStatus.OK);
        if(service.readyAck(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Not cancelled: Alert was triggered");
        //if(service.isNew(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Not cancelled: Currency has not reached target price");
        boolean response = service.cancelAlert(id);
        return new ResponseEntity<>("Alert " + id + "successfully cancelled", HttpStatus.OK);
    }

    //delete 1 alert
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable int id, @RequestParam(required=true) String username){
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!service.alertExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found");
        boolean response = service.deleteAlert(id, username);
        return new ResponseEntity<>("Alert " + id + "successfully deleted", HttpStatus.OK);
    }

}
