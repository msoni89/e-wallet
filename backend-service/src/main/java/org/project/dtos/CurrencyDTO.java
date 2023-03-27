package org.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CurrencyDTO {
    private Long id;

    private String name;
    private String  code;
    private String symbol;
}
