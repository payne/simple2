package demo.simple2.repos;

import demo.simple2.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByTeamIdIgnoreCase(String teamId);

}
