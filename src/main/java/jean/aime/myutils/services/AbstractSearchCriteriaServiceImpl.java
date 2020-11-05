package jean.aime.myutils.services;



import jean.aime.myutils.annotation.AnnotationServiceImpl;
import jean.aime.myutils.core.AbstractCriteria;
import jean.aime.myutils.core.GeneratorUtils;
import jean.aime.myutils.dba.querydsl.QueryDSLRepository;
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
public abstract class AbstractSearchCriteriaServiceImpl<E extends AbstractEntity<ID>, ID extends Serializable,
        RP extends JpaRepository<E, ID>, SC extends AbstractCriteria<ID>>
        implements SearchCriteriaService<E, ID, SC> {

    /**
     * the managed entity name
     */
    private final String targetEntity;

    private final AnnotationServiceImpl annotationServiceImpl;


    public AbstractSearchCriteriaServiceImpl(String targetEntity, AnnotationServiceImpl annotationServiceImpl) {
        this.targetEntity = targetEntity;
        this.annotationServiceImpl = annotationServiceImpl;
    }

    /**
     * The repository must be redefined for each reel implementation
     */
    public abstract RP repository();

    @Override
    public Optional<E> fetchOneByCriteria(SC searchCriteria) {
        QueryDSLRepository repo = (QueryDSLRepository) repository();
        log.info("[Fecthing by criteria], getting one {} ", targetEntity);
        return repo.fetchOne(searchCriteria);
    }

    @Override
    public List<E> fetchAllByAndCriteria(SC searchCriteria) {
        QueryDSLRepository repo = (QueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all {}", targetEntity);
        return repo.findAll(searchCriteria);
    }

    @Override
    public List<SC> fetchAllByCriteriaToDto(SC searchCriteria) {
        QueryDSLRepository repo = (QueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all to dto {}", targetEntity);
        return repo.findAllDTo(searchCriteria);
    }

    @Override
    public Page<E> findAll(SC searchCriteria, Pageable pageable) {
        QueryDSLRepository repo = (QueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all by page {} ", targetEntity);
        return repo.findAll(searchCriteria, pageable);
    }

    @Override
    public Page<SC> findAllByCriteriaByPageToDto(SC searchCriteria, Pageable pageable) {
        QueryDSLRepository repo = (QueryDSLRepository) repository();
        log.info("[Fecthing by criteria] getting all by page to DTO {} ", targetEntity);
        return repo.findAllDtoByPage(searchCriteria, pageable);
    }

    @Override
    public Long count(SC searchCriteria) {
        QueryDSLRepository repo = (QueryDSLRepository) repository();
        log.info("[Fecthing by criteria] count record {} ", targetEntity);
        return repo.count(searchCriteria);
    }

    @Override
    public E saveRecord(E entity) throws AppResponseEntityException, IllegalAccessException {

        try {
            annotationServiceImpl.makeUpperCase(entity, entity.getClass());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        log.info("[Validating] data of {} ", entity);
        E entityValidate = validate(entity);

        log.info("[Ckeking unique value] ");

        List<E> uniqueChecks = annotationServiceImpl.checkUniqValue(entity, entity.getClass());

        if (entity.getId() != null && !entity.getId().toString().isEmpty()) {
            if (!uniqueChecks.isEmpty()) {
                if (entity.getId().equals(uniqueChecks.get(0).getId())) {
                    log.info("[Updating]  {} ", targetEntity);
                    return repository().save(entityValidate);
                } else {
                    throw new AppResponseEntityException("Duplicated record");
                }
            } else {
                log.info("[Updating]  {} ", targetEntity);
                return repository().save(entityValidate);
            }
        } else {
            if (!uniqueChecks.isEmpty())
                throw new AppResponseEntityException("Duplicated record");
            if (entity.getId() instanceof String) {
                entity.setId((ID) GeneratorUtils.uuid());
            } else {
                entity.setId((ID) GeneratorUtils.longUuid());
            }
            log.info("[Saving] new {} ", targetEntity);
            return repository().save(entityValidate);
        }
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
            repository().flush();
        } else {
            E entity = fetchOneRecordById(id);
            log.info("[Deleting record ], executing soft delete {} with ID {} ", targetEntity, id);
            entity.setDeleted(true);
            repository().save(entity);
        }
        return id;
    }

}
