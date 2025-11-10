package com.engenhariadesoftware.e_comercecafe.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CPF {

    private String value;

    public void setValue(String value) {
        if (!value.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido: " + value);
        }
        this.value = value;
    }
}