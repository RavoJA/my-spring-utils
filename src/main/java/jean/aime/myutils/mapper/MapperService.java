package jean.aime.myutils.mapper;

/**
 * Perform a mapping between two class
 *
 * @param <E> the entity type
 * @param <D> the dto type
 * @author Jean Aimé
 */
public interface MapperService<E, D> {

    /**
     * Map the provided dto to entity
     */
    E mapToEntity(D dto);

    /**
     * Map the entity to Dto
     */
    D mapToDto(E entity);

}
