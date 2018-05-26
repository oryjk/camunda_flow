package com.betalpha.fosun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseIsIn {
    @Id
    private String id;

    private String grade;

    private String issuer;
}
