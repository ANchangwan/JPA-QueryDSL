package study.querydsl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;

import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;


import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;

@SpringBootTest
public class QuerydslMiddleTest {

    @PersistenceContext
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void test() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .limit(10)
                .fetch();

        for(Tuple res : result) {
            String username = res.get(member.username);
            Integer age = res.get(member.age);
            System.out.println("username: "+username);
            System.out.println("age: "+age);
        }

    }

    @Test // 프로퍼티 접근
    public void test2(){
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for(MemberDto res : result) {
            String username = res.getUsername();
            Integer age = res.getAge();
            System.out.println("username: "+username);
            System.out.println("age: "+age);
        }
    }

    @Test
    public void test3(){
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(Projections.fields(
                        UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub), "age")


                ))
                .from(member)
                .limit(4)
                .fetch();

        for(UserDto res : result) {
            System.out.println(res);
            String username = res.getName();
            System.out.println("username: "+ username);
            Integer age = res.getAge();
            System.out.println("age: "+age);
        }

    }

    @Test
    public void testConstructor(){
        List<MemberDto> list = queryFactory
                .select(Projections.constructor(MemberDto.class, member.username, member.age))
                .from(member)
                .limit(4)
                .fetch();

        for(MemberDto res : list) {
            System.out.println("username: "+ res.getUsername());
            System.out.println("age : " + res.getAge());
        }
    }

    @Test
    public void testAnnotationProjection(){
        List<MemberDto> list = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for(MemberDto res : list) {
            System.out.println(res);

        }
    }

    @Test
    public void 동적쿼리_BooleanBuilder(){
        String username = "member1";
        Integer age = null;

        searchParam(username, age);
        assertThat(searchParam(username, age)).isNotNull();
        assertThat(searchParam(username, age).size()).isEqualTo(1);


    }
    private List<Member> searchParam(String usernameCond, Integer ageCond){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(usernameCond != null){
            booleanBuilder.and(member.username.eq(usernameCond));
        }
        if(ageCond != null){
            booleanBuilder.and(member.age.eq(ageCond));
        }
        return queryFactory
                .selectFrom(member)
                .where(booleanBuilder)
                .fetch();

    }

    @Test
    public void 동적쿼리2_where다중쿼리(){
        String username = "member1";
        Integer age = null;
        List<Member> list = queryFactory
                .selectFrom(member)
                .where(
                        usernameEq(username),
                        ageEq(age)
                ).fetch();

        for(Member res : list) {
            System.out.println(res);
        }

        assertThat(list.size()).isEqualTo(1);
    }

    private BooleanExpression usernameEq(String usernameCond){
        return usernameCond  != null? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond){
        return ageCond != null? member.age.eq(ageCond) : null;
    }



}
