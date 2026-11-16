package com.iban;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

class IbanServiceTest {

    private IbanRepository ibanRepository;
    private BankRepository bankRepository;
    private IbanService service;

    @BeforeEach
    void setUp() {
        ibanRepository = Mockito.mock(IbanRepository.class);
        bankRepository = Mockito.mock(BankRepository.class);
        service = new IbanService(ibanRepository, bankRepository);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"DE", "DE8937040044053201300", " "})
    void incompleteIbanIsIncomplete(String iban) {
        Assertions.assertEquals(ValidationStatus.INCOMPLETE, service.validateIban(iban).status());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "FR89370400440532013000",
            "de89370400440532013000",
            "DE8937040044053201300A",
            "DE893704004405320130000",
            "DE89 3704 0044 0532 0130 00"
    })
    void invalidFormatIsRejected(String iban) {
        Assertions.assertEquals(ValidationStatus.INVALID, service.validateIban(iban).status());
    }

    @Test
    void invalidCheckDigitsAreRejected() {
        Assertions.assertEquals(new ValidationResponse(ValidationStatus.INVALID, "Check digits are invalid"),
                service.validateIban("DE88370400440532013000"));
    }


    @Test
    void validIbanPassesChecksumWithoutNumericOverflowAndReturnsBankName() {
        Mockito.when(bankRepository.findBankNameByBankCode("37040044")).thenReturn(Optional.of("Awesome Bank!"));
        Assertions.assertEquals(new ValidationResponse(ValidationStatus.VALID, "IBAN is valid", "Awesome Bank!"),
                service.validateIban("DE89370400440532013000"));
        Mockito.verifyNoInteractions(ibanRepository);
    }

    @Test
    void validIbanPassesButBankIsNotFound() {
        Assertions.assertEquals(new ValidationResponse(ValidationStatus.VALID, "IBAN is valid", "Unbekannte Bank"),
                service.validateIban("DE12500105170648489890"));
        Mockito.verifyNoInteractions(ibanRepository);
    }

    @Test
    void saveValidIbanReturnsRepositoryId() {
        String iban = "DE89370400440532013000";
        UUID id = UUID.randomUUID();
        Mockito.when(ibanRepository.saveIban(iban)).thenReturn(id);

        Assertions.assertEquals(id, service.saveIban(iban));

        Mockito.verify(ibanRepository).saveIban(iban);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"DE", "FR89370400440532013000", "DE88370400440532013000"})
    void saveInvalidOrIncompleteIbanDoesNotCallRepository(String iban) {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.saveIban(iban));

        Assertions.assertEquals("IBAN is invalid", exception.getMessage());
        Mockito.verifyNoInteractions(ibanRepository);
    }
}
