package com.task.cryptotracker.service;
import com.task.cryptotracker.entity.Currency;
import com.task.cryptotracker.entity.User;

import com.task.cryptotracker.repository.CurrencyRepository;
import com.task.cryptotracker.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.task.cryptotracker.UnsupportedCurrencyCreationException;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository repository;

    public boolean currencyExists(String currency){
        Optional<Currency> currByName = repository.findByName(currency);
        return currByName.isPresent();
    }

    public boolean currencyExists(int id){
        Optional<Currency> currency = repository.findById(id);
        return currency.isPresent();
    }

    public boolean currencySupported(Currency currency){
        String symbol = currency.getSymbol();
        if (symbol.equals("ETH") || symbol.equals("LTC") || symbol.equals("ZKN") || symbol.equals("MRD") || symbol.equals("LPR") || symbol.equals("GBZ"))
            return false;
        return true;
    }


    public boolean saveCurrency(Currency currency){
        repository.save(currency);
        return true;
    }


    public List<Currency> getCurrencies() {
        return repository.findAll();
    }

    public Currency getCurrencyById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Currency getCurrencyByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public boolean deleteCurrency(int id) {
        try {
            repository.deleteById(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean deleteCurrencyByName(String name) {
        int id = getCurrencyByName(name).getId();
        repository.deleteById(id);
        return true;
    }

    public boolean updateCurrency(String name,float price) {
        Currency existingCurrency = repository.findByName(name).get();
        existingCurrency.setPrice(price);
        repository.save(existingCurrency);
        return true;
    }
}
