package com.iban;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

@Service
public class IbanService {

    private final IbanRepository ibanRepository;
    private final BankRepository bankRepository;

    public IbanService(IbanRepository ibanRepository, BankRepository bankRepository) {
        this.ibanRepository = ibanRepository;
        this.bankRepository = bankRepository;
    }

    public ValidationResponse validateIban(String iban) {
        if (iban == null || iban.isEmpty()) {
            return new ValidationResponse(ValidationStatus.INCOMPLETE, "IBAN is incomplete");
        } else if (iban.length() < 22) {
            return new ValidationResponse(ValidationStatus.INCOMPLETE, "IBAN is not long enough, should be 22 characters");
        } else if (!iban.matches("^DE[0-9]{20}$")){
            return new ValidationResponse(ValidationStatus.INVALID, "IBAN should consist of 22 characters, starting with DE and followed by 20 digits");
        } else if (!validateCheckDigits(iban)) {
            return new ValidationResponse(ValidationStatus.INVALID, "Check digits are invalid");
        } else {
            return new ValidationResponse(ValidationStatus.VALID, "IBAN is valid", getBankName(iban));
        }
    }

    private boolean validateCheckDigits(String iban) {
        var reorderedString = iban.substring(4) + "1314" + iban.substring(2, 4);
        return new BigInteger(reorderedString)
                .mod(BigInteger.valueOf(97))
                .equals(BigInteger.ONE);
    }

    private String getBankName(String iban) {
        return bankRepository.findBankNameByBankCode(iban.substring(4, 12)).orElse("Unbekannte Bank");
    }

    public UUID saveIban(String ibanToSave) {
        ValidationResponse validationResponse = validateIban(ibanToSave);
        if (validationResponse.status() == ValidationStatus.VALID) {
            return ibanRepository.saveIban(ibanToSave);
        } else {
            throw new IllegalArgumentException("IBAN is invalid");
        }
    }
}
