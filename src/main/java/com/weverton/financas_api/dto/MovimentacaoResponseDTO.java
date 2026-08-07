package com.weverton.financas_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoResponseDTO {
     private Long id;
     private String descricao;
     private BigDecimal valor;
     private LocalDate data;
     private String nomeCategoria;
}
