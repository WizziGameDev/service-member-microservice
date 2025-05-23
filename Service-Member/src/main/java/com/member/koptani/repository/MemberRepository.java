package com.member.koptani.repository;

import com.member.koptani.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByIdAndDeletedAtIsNull(Integer id);

    List<Member> findAllByDeletedAtIsNull();
}
