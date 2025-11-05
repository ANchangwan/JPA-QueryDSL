package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepostioryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepostiory memberJpaRepostiory;

    @Test
    public void basicTest(){
        Member member = new Member("member1", 10);
        memberJpaRepostiory.save(member);

        Member findMember = memberJpaRepostiory.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberJpaRepostiory.findAll_querydsl();
        assertThat(result1).containsExactly(member);

        List<Member> result2 = memberJpaRepostiory.findByUsername_querydsl("member1");
        assertThat(result2).containsExactly(member);
    }



}