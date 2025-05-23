package com.member.koptani.controller;

import com.member.koptani.dto.MemberListResponse;
import com.member.koptani.dto.MemberRequest;
import com.member.koptani.dto.MemberResponse;
import com.member.koptani.dto.WebResponse;
import com.member.koptani.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@RestController
@RequestMapping(value = "/api/v1")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping(
            value = "/members",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<MemberListResponse>> getListMember() {
        return runVirtual(() -> WebResponse.<List<MemberListResponse>>builder()
                .statusCode(200)
                .data(memberService.getMembers())
                .errors(null)
                .build());
    }

    @GetMapping(
            value = "/member/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MemberResponse> getListMemberById(@PathVariable(value = "id") Integer id) {
        return runVirtual(() -> WebResponse.<MemberResponse>builder()
                .statusCode(200)
                .data(memberService.getMemberById(id))
                .errors(null)
                .build());
    }

    @PostMapping(
            value = "/member",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MemberResponse> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        return runVirtual(() -> WebResponse.<MemberResponse>builder()
                .statusCode(200)
                .data(memberService.addMember(memberRequest))
                .errors(null)
                .build());
    }

    @PutMapping(
            value = "/member/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MemberResponse> updateMember(@PathVariable("id") Integer id, @Valid @RequestBody MemberRequest memberRequest) {
        return runVirtual(() -> WebResponse.<MemberResponse>builder()
                .statusCode(200)
                .data(memberService.updateMember(id, memberRequest))
                .errors(null)
                .build());
    }

    @DeleteMapping(
            value = "/member/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteMember(@PathVariable(value = "id") Integer id) {
        return runVirtual(() -> WebResponse.<String>builder()
                .statusCode(200)
                .data(memberService.deleteMember(id))
                .errors(null)
                .build());
    }

    private <T> T runVirtual(Supplier<T> task) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        Thread.startVirtualThread(() -> {
            try {
                result.complete(task.get());
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
        });

        try {
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
