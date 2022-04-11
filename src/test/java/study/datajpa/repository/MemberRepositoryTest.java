package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Test
    public void testMember() {
        Member member = new Member("kiseo");
        Member save = memberRepository.save(member);

        Optional<Member> findMember = memberRepository.findById(save.getId());
        Member member1 = findMember.get();

        Assertions.assertThat(member1.getId()).isEqualTo(member.getId());
        Assertions.assertThat(member1.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1.getId()).isEqualTo(member1.getId());
        assertThat(findMember1).isEqualTo(member1);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        count = memberRepository.count();
        assertThat(count).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA",  10);
        Member m2 = new Member("AAA",  20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA",  10);
        Member m2 = new Member("AAA",  20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameTest(){
        Member m1 = new Member("AAA",  10);
        Member m2 = new Member("BBB",  20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        for (String s : result) {
            System.out.println(s);
        }
    }

    @Test
    public void findUserDtoTest(){

        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",  10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
            
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA",  10);
        Member m2 = new Member("BBB",  20);

        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member m : result) {
            System.out.println("m = " + m);
        }

    }
}