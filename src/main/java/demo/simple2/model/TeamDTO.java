package demo.simple2.model;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class TeamDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String teamId;

    @NotNull
    @Size(max = 255)
    private String name;

    private List<Long> teamPersons;

    public TeamDTO() {}
}
