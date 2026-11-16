package com.iban;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IbanRepository extends JpaRepository<IbanEntity, UUID> {

    default UUID saveIban(String iban) {
        IbanEntity savedEntity = save(new IbanEntity(iban));
        return savedEntity.getId();
    }
}
