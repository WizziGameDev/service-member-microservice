package com.member.koptani.service;

import com.member.koptani.dto.MemberListResponse;
import com.member.koptani.dto.MemberRequest;
import com.member.koptani.dto.MemberResponse;
import com.member.koptani.entity.Member;
import com.member.koptani.exception.ApiException;
import com.member.koptani.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Cacheable(value = "MemberService.getMembers", key = "'members'")
    public List<MemberListResponse> getMembers() {
        return memberRepository.findAllByDeletedAtIsNull().stream()
                .map(data -> MemberListResponse.builder()
                        .id(data.getId())
                        .slug(data.getSlug())
                        .name(data.getName())
                        .phoneNumber(data.getPhoneNumber())
                        .gender(data.getGender())
                        .status(data.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "MemberService.getMemberById", key = "#id")
    public MemberResponse getMemberById(Integer id) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApiException("Member Not Found", HttpStatus.NOT_FOUND));

        return MemberResponse.builder()
                .id(member.getId())
                .slug(member.getSlug())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .nik(member.getNik())
                .birthdate(member.getBirthdate())
                .gender(member.getGender())
                .status(member.getStatus())
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = "MemberService.getMembers", key = "'members'")
    public MemberResponse addMember(MemberRequest memberRequest) {

        Member member = new Member();
        member.setSlug(slugify(memberRequest.getName()));
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setPhoneNumber(memberRequest.getPhoneNumber());
        member.setAddress(memberRequest.getAddress());
        member.setNik(memberRequest.getNik());
        member.setBirthdate(memberRequest.getBirthdate());
        member.setGender(memberRequest.getGender());
        member.setStatus(memberRequest.getStatus());
        member.setCreatedAt(Instant.now().getEpochSecond());

        Member saveMember = memberRepository.save(member);

        return MemberResponse.builder()
                .id(saveMember.getId())
                .slug(saveMember.getSlug())
                .name(saveMember.getName())
                .email(saveMember.getEmail())
                .phoneNumber(saveMember.getPhoneNumber())
                .address(saveMember.getAddress())
                .nik(saveMember.getNik())
                .birthdate(saveMember.getBirthdate())
                .gender(saveMember.getGender())
                .status(saveMember.getStatus())
                .build();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "MemberService.getMembers", key = "'members'"),
            @CacheEvict(value = "MemberService.getMemberById", key = "#id")
    })
    public MemberResponse updateMember(Integer id, MemberRequest memberRequest) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApiException("Member Not Found", HttpStatus.NOT_FOUND));

        member.setSlug(slugify(memberRequest.getName()));
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setPhoneNumber(memberRequest.getPhoneNumber());
        member.setAddress(memberRequest.getAddress());
        member.setNik(memberRequest.getNik());
        member.setBirthdate(memberRequest.getBirthdate());
        member.setGender(memberRequest.getGender());
        member.setStatus(memberRequest.getStatus());
        member.setUpdatedAt(Instant.now().getEpochSecond());

        Member saveMember = memberRepository.save(member);

        return MemberResponse.builder()
                .id(saveMember.getId())
                .slug(saveMember.getSlug())
                .name(saveMember.getName())
                .email(saveMember.getEmail())
                .phoneNumber(saveMember.getPhoneNumber())
                .address(saveMember.getAddress())
                .nik(saveMember.getNik())
                .birthdate(saveMember.getBirthdate())
                .gender(saveMember.getGender())
                .status(saveMember.getStatus())
                .build();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "MemberService.getMembers", key = "'members'"),
            @CacheEvict(value = "MemberService.getMemberById", key = "#id")
    })
    public String deleteMember(Integer id) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ApiException("Member Not Found", HttpStatus.NOT_FOUND));
        member.setDeletedAt(Instant.now().getEpochSecond());
        memberRepository.save(member);

        return "Successfully deleted Member";
    }

    public static String slugify(String input) {
        String baseSlug = input
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");

        int randomNumber = new Random().nextInt(9000) + 1000; // random 4 digit antara 1000-9999
        return baseSlug + "-" + randomNumber;
    }
}
