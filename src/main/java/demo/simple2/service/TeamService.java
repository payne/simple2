package demo.simple2.service;

import demo.simple2.domain.Person;
import demo.simple2.domain.Team;
import demo.simple2.model.SimplePage;
import demo.simple2.model.TeamDTO;
import demo.simple2.repos.PersonRepository;
import demo.simple2.repos.TeamRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final PersonRepository personRepository;

    public TeamService(final TeamRepository teamRepository,
            final PersonRepository personRepository) {
        this.teamRepository = teamRepository;
        this.personRepository = personRepository;
    }

    public SimplePage<TeamDTO> findAll(final Pageable pageable) {
        final Page<Team> page = teamRepository.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(team -> mapToDTO(team, new TeamDTO()))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable);
    }

    public TeamDTO get(final Long id) {
        return teamRepository.findById(id)
                .map(team -> mapToDTO(team, new TeamDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final TeamDTO teamDTO) {
        final Team team = new Team();
        mapToEntity(teamDTO, team);
        return teamRepository.save(team).getId();
    }

    public void update(final Long id, final TeamDTO teamDTO) {
        final Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(teamDTO, team);
        teamRepository.save(team);
    }

    public void delete(final Long id) {
        teamRepository.deleteById(id);
    }

    private TeamDTO mapToDTO(final Team team, final TeamDTO teamDTO) {
        teamDTO.setId(team.getId());
        teamDTO.setTeamId(team.getTeamId());
        teamDTO.setName(team.getName());
        teamDTO.setTeamPersons(team.getTeamPersonPersons() == null ? null : team.getTeamPersonPersons().stream()
                .map(person -> person.getId())
                .collect(Collectors.toList()));
        return teamDTO;
    }

    private Team mapToEntity(final TeamDTO teamDTO, final Team team) {
        team.setTeamId(teamDTO.getTeamId());
        team.setName(teamDTO.getName());
        final List<Person> teamPersons = personRepository.findAllById(
                teamDTO.getTeamPersons() == null ? Collections.emptyList() : teamDTO.getTeamPersons());
        if (teamPersons.size() != (teamDTO.getTeamPersons() == null ? 0 : teamDTO.getTeamPersons().size())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of teamPersons not found");
        }
        team.setTeamPersonPersons(teamPersons.stream().collect(Collectors.toSet()));
        return team;
    }

    public boolean teamIdExists(final String teamId) {
        return teamRepository.existsByTeamIdIgnoreCase(teamId);
    }

}
