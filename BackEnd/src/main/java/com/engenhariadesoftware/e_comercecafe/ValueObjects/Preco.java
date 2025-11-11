package com.engenhariadesoftware.e_comercecafe.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Preco {

    private Double value;

    public void setValue(Double value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo ou nulo");
        }
        this.value = value;
    }
}