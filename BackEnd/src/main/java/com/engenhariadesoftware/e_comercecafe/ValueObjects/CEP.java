package com.engenhariadesoftware.e_comercecafe.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CEP {
    private String value;

    public void setValue(String value) {
        if (!value.matches("\\d{5}-?\\d{3}")) {
            throw new IllegalArgumentException("CEP inválido: " + value);
        }
        this.value = value;
    }
}