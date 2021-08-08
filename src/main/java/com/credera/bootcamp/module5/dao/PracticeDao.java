package com.credera.bootcamp.module5.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;

import com.credera.bootcamp.module5.dto.PracticeDto;
import com.credera.bootcamp.module5.model.Practice;

@Component
public class PracticeDao {

    @PersistenceContext(unitName = "default")
    protected EntityManager em;

    public PracticeDto findByShortName(String shortName) {
        TypedQuery<Practice> query = em.createQuery("SELECT practice FROM " + Practice.class.getName() + " practice"
                + " WHERE practice.shortName = :shortName", Practice.class);
        query.setParameter("shortName", shortName);
        Practice practice = query.getSingleResult();

        return convertPracticeToDto(practice);
    }

    public List<PracticeDto> findByLongNameLike(String longName) {
        // This named query is from an annotation on Practice, instead of an orm.xml file
        TypedQuery<Practice> query = em.createNamedQuery("FIND_BY_LONG_NAME_LIKE", Practice.class);
        query.setParameter(1, "%" + longName + "%");
        List<Practice> practices = query.getResultList();

        return convertPracticesToDtos(practices);
    }

    public List<PracticeDto> findAll() {
        // Another way to construct queries
        // See https://www.baeldung.com/jpa-sort
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Practice> criteria = builder.createQuery(Practice.class);
        Root<Practice> root = criteria.from(Practice.class);
        criteria.select(root);

        TypedQuery<Practice> query = em.createQuery(criteria);
        List<Practice> practices = query.getResultList();

        return convertPracticesToDtos(practices);
    }

    private PracticeDto convertPracticeToDto(Practice practice) {
        PracticeDto dto = null;
        if (practice != null) {
            dto = new PracticeDto();
            dto.setId(practice.getId());
            dto.setShortName(practice.getShortName());
            dto.setLongName(practice.getLongName());
            // Not converting lazily loaded employee list
            dto.setCreatedDate(practice.getCreatedDate());
            dto.setUpdatedDate(practice.getUpdatedDate());
        }
        return dto;
    }

    private List<PracticeDto> convertPracticesToDtos(Collection<Practice> practices) {
        List<PracticeDto> retVal;
        if (practices == null || practices.size() == 0) {
            retVal = new ArrayList<>(0);
        } else {
            retVal = practices.stream().map(practiceResult -> convertPracticeToDto(practiceResult))
                    .collect(Collectors.toList());
        }
        return retVal;
    }
}
