package com.member.koptani.service;

import com.member.koptani.dto.MemberListResponse;
import com.member.koptani.dto.MemberRequest;
import com.member.koptani.dto.MemberResponse;

import java.util.List;

public interface MemberService {

    // Get All Member
    List<MemberListResponse> getMembers();

    // Get By Id
    MemberResponse getMemberById(Integer id);

    // Add Member
    MemberResponse addMember(MemberRequest memberRequest);

    // Update Member
    MemberResponse updateMember(Integer id, MemberRequest memberRequest);

    // Delete Member
    String deleteMember(Integer id);

    // Update Status Member
    public String updateStatusMember(Integer id, String status);
}
