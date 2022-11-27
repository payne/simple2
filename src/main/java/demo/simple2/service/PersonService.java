package demo.simple2.service;

import demo.simple2.domain.Person;
import demo.simple2.model.PersonDTO;
import demo.simple2.model.SimplePage;
import demo.simple2.repos.PersonRepository;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public SimplePage<PersonDTO> findAll(final Pageable pageable) {
        final Page<Person> page = personRepository.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(person -> mapToDTO(person, new PersonDTO()))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable);
    }

    public PersonDTO get(final Long id) {
        return personRepository.findById(id)
                .map(person -> mapToDTO(person, new PersonDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final PersonDTO personDTO) {
        final Person person = new Person();
        mapToEntity(personDTO, person);
        return personRepository.save(person).getId();
    }

    public void update(final Long id, final PersonDTO personDTO) {
        final Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(personDTO, person);
        personRepository.save(person);
    }

    public void delete(final Long id) {
        personRepository.deleteById(id);
    }

    private PersonDTO mapToDTO(final Person person, final PersonDTO personDTO) {
        personDTO.setId(person.getId());
        personDTO.setPersonId(person.getPersonId());
        personDTO.setName(person.getName());
        return personDTO;
    }

    private Person mapToEntity(final PersonDTO personDTO, final Person person) {
        person.setPersonId(personDTO.getPersonId());
        person.setName(personDTO.getName());
        return person;
    }

    public boolean personIdExists(final String personId) {
        return personRepository.existsByPersonIdIgnoreCase(personId);
    }

}
