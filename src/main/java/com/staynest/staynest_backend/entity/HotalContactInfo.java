package com.staynest.staynest_backend.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;



@Embeddable
@Getter
@Setter
public class HotalContactInfo {

    private String address;

    private String phoneNumber;

    private String email;

    private String location;

}
