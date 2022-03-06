package club.qqtim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Objective {
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("score")
    private Float score;
}
