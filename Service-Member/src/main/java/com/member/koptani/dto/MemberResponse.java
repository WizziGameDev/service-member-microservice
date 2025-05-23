package com.member.koptani.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Integer id;

    private String slug;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private Integer nik;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String gender;

    private String status;
}
