package com.githubx.usersms.criteria;

import com.githubx.usersms.criteria.models.FilterRequest;
import com.githubx.usersms.criteria.models.SearchRequest;
import com.githubx.usersms.criteria.models.SortRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SearchSpecification<T> implements Specification<T> {

    private final transient SearchRequest request;
    private final List<JoinSpecification<T>> joinSpecifications;

    public SearchSpecification(SearchRequest request) {
        this.request = request;
        this.joinSpecifications = new ArrayList<>();
    }

    public static Pageable getPageable(Integer page, Integer size) {
        return PageRequest.of(Objects.requireNonNullElse(page, 0), Objects.requireNonNullElse(size, 100));
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.equal(cb.literal(Boolean.TRUE), Boolean.TRUE);

        for (FilterRequest filter : this.request.getFilters()) {
            log.info("Filter: {} {} {} {}", filter.getKey(), filter.getOperator().toString(), filter.getValue(), filter.getValues());
            if (filter.getKey().contains(".")) {
                predicate = filter.getOperator().build(root, filter.getKey(), cb, filter, predicate);
            } else {
                predicate = filter.getOperator().build(root, cb, filter, predicate);
            }
        }

        List<Order> orders = new ArrayList<>();
        for (SortRequest sort : this.request.getSorts()) {
            orders.add(sort.getDirection().build(root, cb, sort));
        }
        for (JoinSpecification<T> joinSpecification : joinSpecifications) {

            joinSpecification.apply(root, query, cb);
        }

        query.orderBy(orders);
        return predicate;
    }

}
