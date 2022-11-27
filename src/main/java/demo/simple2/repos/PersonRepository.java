package demo.simple2.repos;

import demo.simple2.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByPersonIdIgnoreCase(String personId);

}
