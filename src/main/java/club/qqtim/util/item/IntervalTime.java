package club.qqtim.util.item;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntervalTime {

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

}
