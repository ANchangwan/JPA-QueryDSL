package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.repository.MemberJpaRepostiory;
import study.querydsl.repository.MemberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepostiory memberJpaRepostiory;
    private final MemberRepository memberRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition){
        return memberJpaRepostiory.search(condition);
    }

    @GetMapping("/my/members")
    public Map<String, Object> findMembers(){
        List<Member> membersList = memberRepository.findAll();
        List<MemberDto> memberDtos = membersList.stream()
                .map(o -> new MemberDto(o.getUsername(), o.getAge()))
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("members", memberDtos);
        return result;
    }
}
