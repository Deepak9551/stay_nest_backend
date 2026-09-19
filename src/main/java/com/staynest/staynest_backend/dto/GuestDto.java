package com.staynest.staynest_backend.dto;

import com.staynest.staynest_backend.entity.User;
import com.staynest.staynest_backend.entity.enums.Gender;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestDto{
    private Long id;

    private User user; // Reference to the associated user


    private String name;


    private Gender gender;

    private Integer age;

}
