package club.qqtim.dimension;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 输入单元
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputUnit {
    private Long id;

    private String name;

    private Integer age;

    private SubUnit subUnit;

    private List<String> weaponList;

    public InputUnit(Long id) {
        this.id = id;
    }
}
