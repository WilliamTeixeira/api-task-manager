package com.example.taskmanager.domain.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String cep;
    private String street;
    private String  neighborhood;
    private String  city;
    private String  uf;
    private Integer  ibge;
    private String  complement;
    private Integer number;

    public Address(AddressDTO dto){
        this.cep = dto.cep();
        this.street = dto.street();
        this.neighborhood = dto.neighborhood();
        this.city = dto.city();
        this.uf = dto.uf();
        this.ibge = dto.ibge();
        this.complement = dto.complement();
        this.number = dto.number();
    }

}
