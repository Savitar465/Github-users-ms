package com.githubx.usersms.criteria.enums;

import com.githubx.usersms.criteria.models.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
public enum Operator {

    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = root.get(request.getKey());
            return cb.and(cb.equal(key, value), predicate);
        }

        @Override
        public <T> Predicate build(Root<T> root, String joinEntity, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            String[] keys = joinEntity.split("\\.");
            Join<T, ?> join = root.join(keys[0]);
            Expression<?> key = join.get(keys[1]);
            return cb.and(cb.equal(key, value), predicate);
        }
    },

    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Expression<?> key = root.get(request.getKey());
            return cb.and(cb.notEqual(key, value), predicate);
        }
        @Override
        public <T> Predicate build(Root<T> root, String joinEntity, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            String[] keys = joinEntity.split("\\.");
            Join<T, ?> join = root.join(keys[0]);
            Expression<?> key = join.get(keys[1]);
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    LIKE {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Expression<String> key = root.get(request.getKey());
            return cb.and(cb.like(cb.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"), predicate);
        }
        @Override
        public <T> Predicate build(Root<T> root, String joinEntity, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            String[] keys = joinEntity.split("\\.");
            Join<T, ?> join = root.join(keys[0]);
            Expression<String> key = join.get(keys[1]);
            return cb.and(cb.like(cb.upper(key), "%" + request.getValue().toString().toUpperCase() + "%"), predicate);
        }
    },

    IN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            List<Object> values = request.getValues();
            CriteriaBuilder.In<Object> inClause = cb.in(root.get(request.getKey()));
            for (Object value : values) {
                inClause.value(request.getFieldType().parse(value.toString()));
            }
            return cb.and(inClause, predicate);
        }
        @Override
        public <T> Predicate build(Root<T> root, String joinEntity, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            List<Object> values = request.getValues();
            String[] keys = joinEntity.split("\\.");
            Join<T, ?> join = root.join(keys[0]);
            CriteriaBuilder.In<Object> inClause = cb.in(join.get(keys[1]));
            for (Object value : values) {
                inClause.value(request.getFieldType().parse(value.toString()));
            }
            return cb.and(inClause, predicate);
        }
    },

    BETWEEN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Object valueTo = request.getFieldType().parse(request.getValueTo().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime startDate = (LocalDateTime) value;
                LocalDateTime endDate = (LocalDateTime) valueTo;
                Expression<LocalDateTime> key = root.get(request.getKey());
                return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)), predicate);
            }

            if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                Number start = (Number) value;
                Number end = (Number) valueTo;
                Expression<Number> key = root.get(request.getKey());
                return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
            }
            return predicate;
        }
        @Override
        public <T> Predicate build(Root<T> root, String joinEntity, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Object valueTo = request.getFieldType().parse(request.getValueTo().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime startDate = (LocalDateTime) value;
                LocalDateTime endDate = (LocalDateTime) valueTo;
                String[] keys = joinEntity.split("\\.");
                Join<T, ?> join = root.join(keys[0]);
                Expression<LocalDateTime> key = join.get(keys[1]);
                return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)), predicate);
            }
            if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                Number start = (Number) value;
                Number end = (Number) valueTo;
                String[] keys = joinEntity.split("\\.");
                Join<T, ?> join = root.join(keys[0]);
                Expression<Number> key = join.get(keys[1]);
                return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
            }
            return predicate;
        }
    },

    LESS_THAN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            if (request.getFieldType() == FieldType.DATE) {
                Date date = (Date) value;
                Expression<Date> key = root.get(request.getKey());
                return cb.and(cb.lessThan(key, date), predicate);
            }
            return predicate;
        }
        @Override
        public <T> Predicate build(Root<T> root, String joinEntity, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            if (request.getFieldType() == FieldType.DATE) {
                Date date = (Date) value;
                String[] keys = joinEntity.split("\\.");
                Join<T, ?> join = root.join(keys[0]);
                Expression<Date> key = join.get(keys[1]);
                return cb.and(cb.lessThan(key, date), predicate);
            }
            return predicate;
        }
    };


    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);
    public abstract <T> Predicate build(Root<T> root, String join, CriteriaBuilder cb, FilterRequest request, Predicate predicate);

}
