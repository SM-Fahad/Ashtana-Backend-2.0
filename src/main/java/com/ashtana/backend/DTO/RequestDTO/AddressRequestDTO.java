package com.ashtana.backend.DTO.RequestDTO;

import lombok.Data;

@Data
public class AddressRequestDTO {

    private String street;
    private String city;
    private String country;
    private String postalCode;
}
