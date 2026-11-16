package com.iban;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class IbanController {

    public final IbanService ibanService;

    public IbanController(IbanService ibanService) {
        this.ibanService = ibanService;
    }

    @GetMapping("/iban/validate")
    public ValidationResponse validateIban(@RequestParam("value") String value) {
        return ibanService.validateIban(value);
    }

    @PostMapping("/iban")
    public UUID saveIban(@RequestBody String value) {
        UUID savedIban = ibanService.saveIban(value);
        return savedIban;
    }
}