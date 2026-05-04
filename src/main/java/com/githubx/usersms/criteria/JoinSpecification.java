package com.githubx.usersms.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;


public interface JoinSpecification<T> {
    void apply(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
