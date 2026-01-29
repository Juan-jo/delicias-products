package org.delicias.product.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.product.domain.model.ProductTemplate;

import java.util.List;

@ApplicationScoped
public class ProductTemplateRepository implements PanacheRepositoryBase<ProductTemplate, Integer> {

    private final String queryFilter = "LOWER(name) LIKE LOWER(?1)";

    public List<ProductTemplate> searchByName(
            String name,
            int page,
            int size,
            String sortBy,
            Sort.Direction direction
    ) {
        PanacheQuery<ProductTemplate> query;

        if (name == null || name.isBlank()) {
            query = findAll(
                    Sort.by(sortBy, direction)
            );
        } else {
            query = find(
                    queryFilter,
                    Sort.by(sortBy, direction),
                    "%" + name + "%"
            );
        }

        return query
                .page(Page.of(page, size))
                .list();
    }

    public long countByName(String name) {
        if (name == null || name.isBlank()) {
            return count();
        }
        return count(
                queryFilter,
                "%" + name + "%"
        );
    }
}
