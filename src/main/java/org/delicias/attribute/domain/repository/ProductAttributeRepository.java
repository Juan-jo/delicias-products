package org.delicias.attribute.domain.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.attribute.domain.model.ProductAttribute;

import java.util.List;

@ApplicationScoped
public class ProductAttributeRepository implements PanacheRepositoryBase<ProductAttribute, Integer> {

    public List<ProductAttribute> findByProduct(Integer restaurantTmplId) {
        return list("product.id", Sort.ascending("sequence"), restaurantTmplId);
    }

    public List<ProductAttribute> findByIds(List<Integer> ids) {
        return find("product.id in ?1", Sort.ascending("sequence"), ids).list();
    }
}
