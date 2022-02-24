package club.qqtim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class UserObjective {
    @JsonProperty("periodId")
    private Integer periodId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("objectiveList")
    private List<Objective> objectiveList;
    @JsonProperty("leaders")
    private String leaders;
    @JsonProperty("orgPaths")
    private List<Integer> orgPaths;
    @JsonProperty("exec_date")
    private String execDate;
}
