package jean.aime.myutils.dba.querydsl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.JPQLQuery;
import jean.aime.myutils.core.AbstractCriteria;
import jean.aime.myutils.domain.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Jean Aimé
 **/
@Repository
public abstract class AbstractQueryDSLRepositoryImpl<E extends AbstractEntity<ID>,
        ID extends Serializable, SC extends AbstractCriteria<ID>>
        extends QuerydslRepositorySupport implements QueryDSLRepository<E, ID, SC> {

    public AbstractQueryDSLRepositoryImpl(Class<?> domainClass) {
        super(domainClass);
    }

    abstract EntityPath<E> entitypath();

    abstract JPQLQuery<SC> buildSearchQueryDTO();

    abstract BooleanBuilder buildCriteria(SC searchCriteria);

    @Override
    public Optional<E> fetchOne(SC searchCriteria) {
        Assert.notNull(searchCriteria, "The search criteria must not null");
        JPQLQuery<E> vQuery = from(entitypath()).where(buildCriteria(searchCriteria));
        return vQuery.fetchCount() == 0 ? Optional.empty() : Optional.of(vQuery.fetchFirst());
    }

    @Override
    public List<E> findAll(SC searchCriteria) {
        Assert.notNull(searchCriteria, "The search criteria cannot be null");
        JPQLQuery<E> vQuery = from(entitypath()).where(buildCriteria(searchCriteria));
        return vQuery.fetch();
    }

    @Override
    public Page<E> findAll(SC searchCriteria, Pageable pageable) {
        Assert.notNull(searchCriteria, "The search criteria cannot be null");
        JPQLQuery<E> vQuery = Objects.requireNonNull(getQuerydsl())
                .applyPagination(pageable, from(entitypath()).where(buildCriteria(searchCriteria)));
        long count = vQuery.fetchCount();
        return new PageImpl<>(vQuery.fetch(), pageable, count);
    }

    @Override
    public Long count(SC searchCriteria) {
        Assert.notNull(searchCriteria, "The search criteria cannot be null");
        JPQLQuery<E> vQuery = from(entitypath()).where(buildCriteria(searchCriteria));
        return vQuery.fetchCount();
    }

    @Override
    public List<SC> findAllDTo(SC searchCriteria) {
        Assert.notNull(searchCriteria, "The search criteria cannot be null");
        JPQLQuery<SC> vQuery = buildSearchQueryDTO().where(buildCriteria(searchCriteria));
        return vQuery.fetch();
    }

    @Override
    public Page<SC> findAllDtoByPage(SC searchCriteria, Pageable pageable) {
        Assert.notNull(searchCriteria, "The search criteria cannot be null");
        JPQLQuery<SC> vQuery = Objects.requireNonNull(getQuerydsl())
                .applyPagination(pageable, buildSearchQueryDTO().where(buildCriteria(searchCriteria)));
        long count = vQuery.fetchCount();
        return new PageImpl<>(vQuery.fetch(), pageable, count);
    }
}
