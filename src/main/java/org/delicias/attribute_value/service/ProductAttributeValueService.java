package org.delicias.attribute_value.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.attribute.domain.model.ProductAttribute;
import org.delicias.attribute_value.domain.model.ProductAttributeValue;
import org.delicias.attribute_value.domain.repository.ProductAttributeValueRepository;
import org.delicias.attribute_value.dto.ProductAttributeValueDTO;

import java.util.List;

@ApplicationScoped
public class ProductAttributeValueService {

    @Inject
    ProductAttributeValueRepository repository;


    @Transactional
    public void create(Integer attributeId, ProductAttributeValueDTO req) {
        repository.persist(ProductAttributeValue.builder()
                        .name(req.name())
                        .extraPrice(req.extraPrice())
                        .sequence(req.sequence())
                        .attribute(new ProductAttribute(attributeId))
                .build());
    }

    @Transactional
    public void update(ProductAttributeValueDTO req) {

        ProductAttributeValue entity = repository.findById(req.id());

        if (entity == null) {
            throw new NotFoundException("ProductAttributeValue Not Found");
        }

        entity.setName(req.name());
        entity.setSequence(req.sequence());
        entity.setExtraPrice(req.extraPrice());
    }

    @Transactional
    public void deleteById(Integer id) {
        var deleted = repository.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("ProductAttributeValue Not Found");
        }
    }

    public List<ProductAttributeValueDTO> findByAttribute(Integer attributeId) {

        return repository.findByAttribute(attributeId)
                .stream().map(it -> ProductAttributeValueDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .extraPrice(it.getExtraPrice())
                        .sequence(it.getSequence())
                        .build()).toList();
    }

    public ProductAttributeValueDTO findById(Integer attrValueId) {

        var entity = repository.findById(attrValueId);

        if (entity == null) {
            throw new NotFoundException("ProductAttributeValue Not Found");
        }

        return ProductAttributeValueDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .extraPrice(entity.getExtraPrice())
                .sequence(entity.getSequence())
                .build();
    }

}
