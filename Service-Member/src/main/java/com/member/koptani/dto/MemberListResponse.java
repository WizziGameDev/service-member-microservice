package com.member.koptani.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberListResponse {

    private Integer id;

    private String slug;

    private String name;

    private String phoneNumber;

    private String gender;

    private String status;
}
