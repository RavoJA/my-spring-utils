package jean.aime.myutils.services;

import jean.aime.myutils.annotation.FirstUpperCaseService;
import jean.aime.myutils.core.AbstractCriteria;
import jean.aime.myutils.dba.querydsl.MyQueryDSLRepository;
import jean.aime.myutils.domain.AbstractEntity;
import jean.aime.myutils.errorhandler.AppResponseEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Abstract calss for managing a dynamic search with QueryDsl
 *
 * @param <SC> search criteria type for search
 * @param <E>  entity type
 * @param <RP> the repository
 * @author Jean Aimé R
 */

@Transactional
@Slf4j
@Service
public abstract class AbstractMySearchCriteriaServiceImpl<E extends AbstractEntity<ID>, ID extends Serializable,
        RP extends JpaRepository<E, ID>, SC extends AbstractCriteria<ID>>
        implements MySearchCriteriaService<E, ID, SC> {

    /**
     * the managed entity name
     */
    private final String targetEntity;

    private final FirstUpperCaseService firstUpperCaseService;

    public AbstractMySearchCriteriaServiceImpl(String targetEntity, FirstUpperCaseService firstUpperCaseService) {
        this.targetEntity = targetEntity;
        this.firstUpperCaseService = firstUpperCaseService;
    }

    /**
     * The repository must be redefined for each reel implementation
     */
    public abstract RP repository();

    @Override
    public Optional<E> fetchOneByCriteria(SC searchCriteria) {
        MyQueryDSLRepository repo = (MyQueryDSLRepository) repository();
        log.info("[Fecthing by criteria], getting one {} ", targetEntity);
        return repo.fetchOne(searchCriteria);
    }

    @Override
    public List<E> fetchAllByAndCriteria(SC searchCriteria) {
        MyQueryDSLRepository repo = (MyQueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all {}", targetEntity);
        return repo.findAll(searchCriteria);
    }

    @Override
    public List<SC> fetchAllByCriteriaToDto(SC searchCriteria) {
        MyQueryDSLRepository repo = (MyQueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all to dto {}", targetEntity);
        return repo.findAllDTo(searchCriteria);
    }

    @Override
    public Page<E> findAll(SC searchCriteria, Pageable pageable) {
        MyQueryDSLRepository repo = (MyQueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all by page {} ", targetEntity);
        return repo.findAll(searchCriteria, pageable);
    }

    @Override
    public Page<SC> findAllByCriteriaByPageToDto(SC searchCriteria, Pageable pageable) {
        MyQueryDSLRepository repo = (MyQueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all by page to DTO {} ", targetEntity);
        return repo.findAllDtoByPage(searchCriteria, pageable);
    }

    @Override
    public Long count(SC searchCriteria) {
        MyQueryDSLRepository repo = (MyQueryDSLRepository) repository();
        log.info("[Fecthing by criteria] count record {} ", targetEntity);
        return repo.count(searchCriteria);
    }

    @Override
    public E saveRecord(E entity) throws AppResponseEntityException, IllegalAccessException {
        log.info("[Validating] data of {} ", targetEntity);
        E entityValidate = validate(entity);
        log.info("[Ckecking] if the entity has an attributes  to be FirstUpperCase {} ", targetEntity);
        firstUpperCaseService.setFirstUpperCase(entity, entity.getClass());
        log.info("[About to save] new record of {} ", targetEntity);
        return repository().save(entityValidate);
    }

    @Override
    public List<E> fetchAllRecord() {
        log.info("[Fecthing all record] of {} ", targetEntity);
        return repository().findAll();
    }

    @Override
    public Page<E> fetchAllRecordByPage(Pageable pageable) {
        return repository().findAll(pageable);
    }

    @Override
    public E fetchOneRecordById(ID id) throws AppResponseEntityException {
        log.info("[Fetching record ] {} with ID {} ", targetEntity, id);
        return repository().findById(id)
                .orElseThrow(() -> new AppResponseEntityException("Unable to find " + targetEntity + " with ID " + id));
    }

    @Override
    public ID deleteRecord(ID id, boolean softDelete) throws AppResponseEntityException {
        if (!softDelete) {
            log.info("[Deleting record ] physically {} with ID {} ", targetEntity, id);
            repository().deleteById(id);
        } else {
            E entity = fetchOneRecordById(id);
            log.info("[Deleting record ], executing soft delete {} with ID {} ", targetEntity, id);
            entity.setDeleted(true);
            repository().save(entity);
        }
        return id;
    }
}
