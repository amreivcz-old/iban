package com.iban;

import java.io.Serializable;

public record ValidationResponse(ValidationStatus status, String message, String bankName) implements Serializable {
    public ValidationResponse(ValidationStatus status, String message) {
            this(status, message, null);
    }
}
