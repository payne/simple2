package demo.simple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import antlr.PreservingFileWriter;
import demo.simple2.model.PersonDTO;
import demo.simple2.model.TeamDTO;
import demo.simple2.repos.PersonRepository;
import demo.simple2.repos.TeamRepository;
import demo.simple2.service.PersonService;
import demo.simple2.service.TeamService;


@SpringBootApplication
public class Simple2Application implements  CommandLineRunner {

    PersonService ps;
    TeamService ts;

    public Simple2Application(PersonService ps, TeamService ts) {
        this.ps = ps;
        this.ts = ts;
    }


    public static void main(final String[] args) {
        SpringApplication.run(Simple2Application.class, args);
    }

    public void run(String... args) {
        Long teamRowId = setupTeamData();
        TeamDTO teamDTO = dumpTeam(teamRowId);
        List<Long> peopleLst = teamDTO.getTeamPersons();
        peopleLst.remove(1);
        System.out.println(peopleLst.size());
        teamDTO.setTeamPersons(peopleLst);
        ts.update(teamRowId, teamDTO);
        System.out.println("***********");
        dumpTeam(teamRowId);
    }

    private TeamDTO dumpTeam(Long teamRowId) {
        TeamDTO t = ts.get(teamRowId);
        for (Long pid: t.getTeamPersons()) {
           PersonDTO p = ps.get(pid);
           System.out.println("\t"+pid + " : "  + p.getName());
        }
        return t;
    }


    private Long setupTeamData() {
        List<String> names = Arrays.asList("Jill","Bill","Joe");
        List<Long> peopleRowIds =  makePeople(names);
        TeamDTO teamDTO = TeamDTO.builder()
        .teamId("t1")
        .name("Team 1")
        .teamPersons(peopleRowIds)
        .build(); 
        Long teamRowId = ts.create(teamDTO);
        return teamRowId;
    }


    private List<Long> makePeople(List<String> names) {
        List<Long> lstRowIds=new ArrayList<>();
        int pn=0;
        for (String name: names) {
            PersonDTO personDTO = PersonDTO.builder()
            .name(name)
            .personId("p"+pn)
            .build();
            pn++;
            Long personRowId = ps.create(personDTO);
            lstRowIds.add(personRowId);
        }
        return lstRowIds;
    }

}
