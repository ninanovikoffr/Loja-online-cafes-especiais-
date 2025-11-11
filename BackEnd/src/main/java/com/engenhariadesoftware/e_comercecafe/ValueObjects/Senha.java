package com.engenhariadesoftware.e_comercecafe.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Senha {
    private String value;

    public void setValue(String value) {
        if (value == null || value.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
        this.value = value;
    }
}