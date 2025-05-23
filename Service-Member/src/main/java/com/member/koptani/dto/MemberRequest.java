package com.member.koptani.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "\\+?\\d{9,15}", message = "Phone number must be digits, 9 to 15 chars, optionally starting with +")
    private String phoneNumber;

    @NotBlank(message = "Address must not be blank")
    private String address;

    @NotNull(message = "NIK must not be null")
    @Digits(integer = 16, fraction = 0, message = "NIK must be a 16-digit number")
    private Integer nik;  // Ganti dari Integer ke Long supaya bisa 16 digit

    @NotNull(message = "Birthdate must not be null")
    @Past(message = "Birthdate must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @NotBlank(message = "Gender must not be blank")
    @Pattern(regexp = "L|P", message = "Gender must be 'L' (Laki-laki) or 'P' (Perempuan)")
    private String gender;

    @NotBlank(message = "Status must not be blank")
    @Pattern(regexp = "active|nonactive", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Status must be 'active' or 'nonactive'")
    private String status;
}
