package com.example.taskmanager.domain.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressDTO(
        @NotBlank @Pattern(regexp = "\\d{8}") String cep,
        @NotBlank String street,
        @NotBlank String neighborhood,
        @NotBlank String city,
        @NotBlank String uf,
        String ibge,
        String complement,
        String number){
    public AddressDTO(Address address) {
        this(address.getCep(),
            address.getStreet(),
            address.getNeighborhood(),
            address.getCity(),
            address.getUf(),
            address.getIbge(),
            address.getComplement(),
            address.getNumber());
    }
}
