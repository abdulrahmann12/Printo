package com.team.printo.specification;

import com.team.printo.model.Product;
import com.team.printo.dto.ProductSearchRequest;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;

public class ProductSpecification {

    public static Specification<Product> getProductSpecification(ProductSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (request.getName() != null && !request.getName().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + request.getName().toLowerCase() + "%"
                    )
                );
            }

            if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("category").get("name")),
                        "%" + request.getCategoryName().toLowerCase() + "%"
                    )
                );
            }

            if (request.getMinPrice() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }

            return predicate;
        };
    }
}
