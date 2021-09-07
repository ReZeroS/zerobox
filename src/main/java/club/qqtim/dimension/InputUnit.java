package club.qqtim.dimension;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * 输入单元
 */
@Data
@AllArgsConstructor
public class InputUnit {

    private Long id;

    private String name;

    private List<String> weaponList;


}
