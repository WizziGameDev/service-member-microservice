package com.member.koptani.resolver;

import com.member.koptani.dto.MemberListResponse;
import com.member.koptani.dto.MemberRequest;
import com.member.koptani.dto.MemberResponse;
import com.member.koptani.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Controller
public class MemberResolver {

    @Autowired
    private MemberService memberService;

    @QueryMapping
    public List<MemberListResponse> members() {
        return runVirtual(() -> memberService.getMembers());
    }

    @QueryMapping
    public MemberResponse memberById(@Argument Integer id) {
        return runVirtual(() -> memberService.getMemberById(id));
    }

    @MutationMapping
    public MemberResponse createMember(@Argument("input") @Valid MemberRequest input) {
        return runVirtual(() -> memberService.addMember(input));
    }

    @MutationMapping
    public MemberResponse updateMember(@Argument Integer id,
                                       @Argument("input") @Valid MemberRequest input) {
        return runVirtual(() -> memberService.updateMember(id, input));
    }

    @MutationMapping
    public String deleteMember(@Argument Integer id) {
        return runVirtual(() -> memberService.deleteMember(id));
    }

    /**
     * Virtual Thread
     */
    private <T> T runVirtual(Supplier<T> task) {
        CompletableFuture<T> result = new CompletableFuture<>();
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
