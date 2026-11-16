package com.iban;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bankcode")
public class BankCodeEntity {

    @Id
    @Column(name = "bankcode", nullable = false, length = 8)
    private String bankCode;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "short_name", nullable = false, columnDefinition = "text")
    private String shortName;

    protected BankCodeEntity() {
    }

    public BankCodeEntity(String bankCode, String name, String postcode, String city, String shortName) {
        this.bankCode = bankCode;
        this.name = name;
        this.shortName = shortName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
