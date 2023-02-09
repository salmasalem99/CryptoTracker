package com.task.cryptotracker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Unsupported Currency Creation Exception"
)
public class UnsupportedCurrencyCreationException extends RuntimeException{
    public UnsupportedCurrencyCreationException(String T) {
        super(T);
    }
}
