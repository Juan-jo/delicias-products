package org.delicias.attribute.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.attribute.domain.model.ProductAttribute;
import org.delicias.attribute.domain.repository.ProductAttributeRepository;
import org.delicias.attribute.dto.ProductAttributeDTO;
import org.delicias.product.domain.model.ProductTemplate;

import java.util.List;

@ApplicationScoped
public class ProductAttributeService {

    @Inject
    ProductAttributeRepository repository;


    @Transactional
    public void create(Integer productTmplId, ProductAttributeDTO req) {

        repository.persist(ProductAttribute.builder()
                .name(req.name())
                .product(new ProductTemplate(productTmplId))
                .displayType(req.displayType())
                .sequence(req.sequence())
                .build());

    }


    @Transactional
    public void update(ProductAttributeDTO req) {

        ProductAttribute entity = repository.findById(req.id());

        if (entity == null) {
            throw new NotFoundException("ProductAttribute Not Found");
        }

        entity.setName(req.name());
        entity.setSequence(req.sequence());
        entity.setDisplayType(req.displayType());

    }

    @Transactional
    public void deleteById(Integer id) {
        var deleted = repository.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("ProductAttribute Not Found");
        }
    }

    public List<ProductAttributeDTO> findByProduct(Integer productTmplId) {

        return repository.findByProduct(productTmplId)
                .stream().map(it -> ProductAttributeDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .displayType(it.getDisplayType())
                        .sequence(it.getSequence())
                        .build())
                .toList();
    }


}
