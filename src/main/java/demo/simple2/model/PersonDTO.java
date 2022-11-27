package demo.simple2.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class PersonDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String personId;

    @NotNull
    @Size(max = 255)
    private String name;

    public PersonDTO() {}
}
