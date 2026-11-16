package com.iban;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BankRepository extends JpaRepository<BankCodeEntity, String> {

    @Query("select bank.name from BankCodeEntity bank where bank.bankCode = :bankCode")
    Optional<String> findBankNameByBankCode(@Param("bankCode") String bankCode);
}
