package com.betalpha.fosun.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BondInfoApi {
    private String isin;
    private String grade;
    private String issuer;
}
