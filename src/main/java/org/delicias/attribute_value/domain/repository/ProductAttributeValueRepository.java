package org.delicias.attribute_value.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.delicias.attribute_value.domain.model.ProductAttributeValue;

import java.util.List;

@ApplicationScoped
public class ProductAttributeValueRepository implements PanacheRepositoryBase<ProductAttributeValue, Integer> {

    public List<ProductAttributeValue> findByAttribute(Integer attributeId) {
        return list("attribute.id", Sort.ascending("sequence"), attributeId);
    }

    @Transactional
    public void deleteByAttribute(Integer attributeId) {
        delete("attribute.id", attributeId);
    }
}
