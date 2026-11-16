package com.iban;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:bankcode-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class BankRepositoryTest {

    @Autowired
    private BankRepository repository;

    @Test
    void looksUpBankNameAndPreservesLeadingZeroes() {
        repository.saveAndFlush(new BankCodeEntity("01234567", "Example Bank", "01234", "Berlin", "Example"));

        Assertions.assertEquals(Optional.of("Example Bank"), repository.findBankNameByBankCode("01234567"));
    }

    @Test
    void unknownBankCodeReturnsEmpty() {
        Assertions.assertEquals(Optional.empty(), repository.findBankNameByBankCode("99999999"));
    }
}
