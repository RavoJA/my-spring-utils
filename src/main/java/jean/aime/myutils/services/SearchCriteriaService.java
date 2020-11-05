package jean.aime.myutils.services;


import jean.aime.myutils.core.AbstractCriteria;
import jean.aime.myutils.domain.AbstractEntity;
import jean.aime.myutils.errorhandler.AppResponseEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * The inteface for managing an entity
 *
 * @param <ID> the ID type
 * @param <E>  the entity type
 * @param <SC> the searchCriteria type
 */
public interface SearchCriteriaService<E extends AbstractEntity<ID>, ID extends Serializable,
        SC extends AbstractCriteria<ID>> {

    /**
     * Fetch one record by criteria if exist
     *
     * @param searchCriteria the searchCriteria param
     */
    Optional<E> fetchOneByCriteria(SC searchCriteria);

    /**
     * Fetch all record by criteria
     *
     * @param searchCriteria the searchCriteria param
     */
    List<E> fetchAllByAndCriteria(SC searchCriteria);

    /**
     * Fetch all record by criteria with result dto
     *
     * @param searchCriteria the searchCriteria param
     */
    List<SC> fetchAllByCriteriaToDto(SC searchCriteria);

    /**
     * Fetch all record by page by criteria
     *
     * @param searchCriteria the searchCriteria param
     * @param pageable       the pageable param
     */
    Page<E> findAll(SC searchCriteria, Pageable pageable);

    /**
     * Fetch all record by page by criteria with result DTO
     *
     * @param searchCriteria the searchCriteria param
     * @param pageable       the pageable param
     */
    Page<SC> findAllByCriteriaByPageToDto(SC searchCriteria, Pageable pageable);


    /**
     * Count all matched record by criteria
     *
     * @param searchCriteria the searchCriteria param
     */
    Long count(SC searchCriteria);

    /**
     * Save the object  sent from client
     *
     * @param entity object to save
     * @return the object saved
     * @throws AppResponseEntityException an exception is thrown if something wrong goes here
     */
    E saveRecord(E entity) throws AppResponseEntityException, IllegalAccessException;

    /***
     * Fetch all data
     * @return list of Object T queried
     * */
    List<E> fetchAllRecord();

    /**
     * Fetch all data page by page
     *
     * @param pageable the pageable param
     * @return list of Object T queried
     */
    Page<E> fetchAllRecordByPage(Pageable pageable);

    /**
     * Fecth one Object type of T
     *
     * @param id the id of object T to find
     * @return the Object found with the specified parameter
     */
    E fetchOneRecordById(ID id) throws AppResponseEntityException;

    /**
     * Excecute an update of T instance to avoid a physical delete
     *
     * @param id         the id of object to delete
     * @param softDelete to tell that the data is to be update or to be delete
     *                   if the value is true, then the physical delete will apply
     */

    ID deleteRecord(ID id, boolean softDelete) throws AppResponseEntityException;

    /**
     * Validate an entity before saving
     *
     * @param entity the entity to validate
     */
    E validate(E entity) throws AppResponseEntityException;
}
