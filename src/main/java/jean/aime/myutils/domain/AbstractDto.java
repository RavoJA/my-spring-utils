package jean.aime.myutils.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * The Dto base class
 *
 * @param <ID> the id type parameter
 * @author Jean Aimé
 */

@Data
public abstract class AbstractDto<ID> implements Serializable {
    /**
     * The ID of the target dto
     */

    protected ID id;

    /**
     * The current connected user
     */
    private String connectedUserId;

}
