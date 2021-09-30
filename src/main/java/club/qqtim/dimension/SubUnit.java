package club.qqtim.dimension;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SubUnit implements Comparable<SubUnit>{
    private Integer compare;

    @Override
    public int compareTo(SubUnit o) {
        return compare.compareTo(o.compare);
    }
}
