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

    private final String queryFilter = "LOWER(name) LIKE LOWER(?1) AND restaurantTmplId = ?2 ";

    public List<ProductTemplate> searchByFilter(
            String name,
            Integer restaurantTmplId,
            int page,
            int size,
            String sortBy,
            Sort.Direction direction
    ) {
        PanacheQuery<ProductTemplate> query;
        Sort sort = Sort.by(sortBy).direction(direction);

        if (name == null || name.isBlank()) {
            query = find("restaurantTmplId = ?1", sort, restaurantTmplId);
        } else {
            String queryFilter = "name LIKE ?1 AND restaurantTmplId = ?2";
            query = find(queryFilter, sort, "%" + name.toLowerCase() + "%", restaurantTmplId);
        }

        return query
                .page(Page.of(page, size))
                .list();
    }

    public List<ProductTemplate> searchByFilter2(
            String name,
            Integer restaurantTmplId,
            int page,
            int size,
            String sortBy,
            Sort.Direction direction
    ) {
        PanacheQuery<ProductTemplate> query;

        if (name == null || name.isBlank()) {
            query = find(" restaurantTmplId = ?2", Sort.by(sortBy, direction), restaurantTmplId);
        } else {
            query = find(
                    queryFilter,
                    Sort.by(sortBy, direction),
                    "%" + name + "%", restaurantTmplId
            );
        }

        return query
                .page(Page.of(page, size))
                .list();
    }

    public long countByName(
            String name,
            Integer restaurantTmplId
    ) {
        if (name == null || name.isBlank()) {
            return count("restaurantTmplId = ?1", restaurantTmplId);
        }
        return count(
                queryFilter,
                "%" + name + "%", restaurantTmplId
        );
    }

    public List<ProductTemplate> findByIds(List<Integer> ids) {
        return find("id in ?1", ids).list();
    }
}
