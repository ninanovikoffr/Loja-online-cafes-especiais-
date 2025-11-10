package com.engenhariadesoftware.e_comercecafe.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Email {

    private String value;

    public void setValue(String value) {
        if (!value.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email inválido: " + value);
        }
        this.value = value;
    }
}