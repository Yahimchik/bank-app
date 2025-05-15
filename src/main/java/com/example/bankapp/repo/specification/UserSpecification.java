package com.example.bankapp.repo.specification;

import com.example.bankapp.dto.user.UserFilterDTO;
import com.example.bankapp.entities.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> withFilters(UserFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), filter.getName() + "%"));
            }

            if (filter.getDateOfBirth() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("dateOfBirth"), filter.getDateOfBirth()));
            }

            if (filter.getEmailData() != null) {
                predicates.add(criteriaBuilder.equal(root.get("emailData").get("email"), filter.getEmailData()));
            }

            if (filter.getPhoneData() != null) {
                predicates.add(criteriaBuilder.equal(root.get("phoneData").get("phone"), filter.getPhoneData()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

