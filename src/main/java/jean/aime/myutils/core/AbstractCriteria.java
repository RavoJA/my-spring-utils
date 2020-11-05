package jean.aime.myutils.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Data Transfert Object Base class
 *
 * @author Jean Aimé Ravomanana
 */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AbstractCriteria<ID> {

    /**
     * The identifer
     */
    protected ID id;

    /**
     * The id if the current user
     */
    protected Long connectedUserId;
}
