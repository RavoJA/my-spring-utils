package jean.aime.myutils.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AbstractCriteria<ID> {
   protected ID id;
}
