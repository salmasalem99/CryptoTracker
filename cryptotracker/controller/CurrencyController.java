package com.task.cryptotracker.controller;
import com.task.cryptotracker.entity.Currency;
import com.task.cryptotracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.task.cryptotracker.UnsupportedCurrencyCreationException;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//TESTED
@RestController
@RequestMapping(path = "currencyAPI")
public class CurrencyController {

    @Autowired
    private CurrencyService service;
    @Autowired
    private UserService userService;

    @PostMapping("/addCurrency")
    public ResponseEntity<String> addCurrency(@RequestBody Currency currency, @RequestParam(required=true) String username) {
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!userService.isAdmin(username)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: only admin users can add currencies");
        if(!service.currencySupported(currency)) throw new UnsupportedCurrencyCreationException("Currency is unsupported ");
        if(service.currencyExists(currency.getName())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency already exists in database");
        boolean result = service.saveCurrency(currency);
        return new ResponseEntity<>("Currency successfully added to database", HttpStatus.OK);
    }


    @GetMapping("/currencies")
    public List<Currency> findAllCurrencies() {
        return service.getCurrencies();
    }

    @GetMapping("/{id}")
    public Currency findCurrencyById(@PathVariable("id") int id) {
        if(service.getCurrencyById(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        return service.getCurrencyById(id);
    }

    @GetMapping("/currency/{name}")
    public Currency findCurrencyByName(@PathVariable String name) {
        if(service.getCurrencyByName(name) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return service.getCurrencyByName(name);
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<String> updateCurrency(@PathVariable String name, @RequestParam(required=true) float price, @RequestParam(required=true) String username) {
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!userService.isAdmin(username)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: only admin users can update currencies");
        if(!service.currencyExists(name)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        boolean response = service.updateCurrency(name, price);
        return new ResponseEntity<>("Currency updated", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable int id, @RequestParam(required=true) String username) {
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!userService.isAdmin(username)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: only admin users can delete currencies");
        if(service.deleteCurrency(id) == false )
            return new ResponseEntity<>("Currency with ID: " + id + " does not exist", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>("Currency deleted", HttpStatus.OK);
    }

    @DeleteMapping("/deletebyname/{name}")
    public ResponseEntity<String> deleteCurrencyByName(@PathVariable("name") String name, @RequestParam(value = "username", required=true) String username) {
        if(!userService.isPresent(username)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if(!userService.isAdmin(username)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: only admin users can delete currencies");
        if(!service.currencyExists(name)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        boolean response = service.deleteCurrencyByName(name);
        return new ResponseEntity<>("Currency deleted", HttpStatus.OK);
    }
}
