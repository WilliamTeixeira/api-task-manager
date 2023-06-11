package com.example.taskmanager.domain.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String uf;
    private String ibge;
    private String complement;
    private String number;

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
