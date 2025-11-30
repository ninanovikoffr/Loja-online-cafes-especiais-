package com.engenhariadesoftware.e_comercecafe.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemCarrinhoDTO {
    private int quantidade;
}