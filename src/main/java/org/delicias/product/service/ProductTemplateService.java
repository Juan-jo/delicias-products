package org.delicias.product.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.common.dto.PagedResult;
import org.delicias.product.domain.model.ProductTemplate;
import org.delicias.product.domain.repository.ProductTemplateRepository;
import org.delicias.product.dto.CreateProductTmplDTO;
import org.delicias.product.dto.ProductTmplDTO;
import org.delicias.product.dto.ProductTmplFilterItemDTO;
import org.delicias.product.dto.ProductTmplFilterReqDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ProductTemplateService {

    @Inject
    ProductTemplateRepository repository;

    @ConfigProperty(name = "delicias.defaultPicture")
    String defaultPicture;

    @Transactional
    public Map<String, Integer> create(CreateProductTmplDTO req) {

        ProductTemplate entity = ProductTemplate.builder()
                .restaurantTmplId(req.restaurantTmplId())
                .name(req.name())
                .description(req.description())
                .listPrice(BigDecimal.valueOf(0.0))
                .salesOk(false)
                .build();

        repository.persist(entity);

        return Map.of("productTmplId", entity.getId());
    }

    @Transactional
    public void update(ProductTmplDTO req) {

        ProductTemplate entity = repository.findById(req.id());

        if(entity == null) {
            throw new NotFoundException("ProductTemplate Not Found");
        }

        entity.setName(req.name());
        entity.setDescription(req.description());
        entity.setListPrice(req.listPrice());
        entity.setSalesOk(req.salesOk());
    }


    @Transactional
    public void patch(Integer productTmplId, Map<String, Object> mapData) {

        ProductTemplate entity = repository.findById(productTmplId);

        if(entity == null) {
            throw new NotFoundException("ProductTemplate Not Found");
        }

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ProductTemplate patched = mapper.convertValue(mapData, ProductTemplate.class);

        if (patched.getListPrice() != null) {
            entity.setListPrice(patched.getListPrice());
        }

        if (patched.getSalesOk() != null) {
            entity.setSalesOk(patched.getSalesOk()); // Boolean
        }

        if (patched.getName() != null) {
            entity.setName(patched.getName());
        }

        if (patched.getDescription() != null) {
            entity.setDescription(patched.getDescription());
        }

    }


    public ProductTmplDTO findById(Integer productTmplId) {

        ProductTemplate entity = repository.findById(productTmplId);

        if(entity == null) {
            throw new NotFoundException("ProductTemplate Not Found");
        }

        return ProductTmplDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .listPrice(entity.getListPrice())
                .salesOk(entity.getSalesOk())
                .restaurantTmplId(entity.getRestaurantTmplId())
                .build();
    }

    @Transactional
    public void deleteById(Integer id) {
        var deleted = repository.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("ProductTemplate Not Found");
        }
    }

    public PagedResult<ProductTmplFilterItemDTO> filterSearch(
            ProductTmplFilterReqDTO req
    ) {
        List<ProductTmplFilterItemDTO> filtered = repository.searchByFilter(
                        req.getName(),
                        req.getRestaurantTmplId(),
                        req.getPage(),
                        req.getSize(),
                        req.getOrderColumn(),
                        req.toOrderDirection()
                )
                .stream().map(it -> ProductTmplFilterItemDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .description(it.getDescription())
                        .listPrice(Optional.ofNullable(it.getListPrice()).orElse(BigDecimal.ZERO))
                        .picture(Optional.ofNullable(it.getPicture()).orElse(defaultPicture))
                        .build()).toList();

        long total = repository.countByName(req.getName(), req.getRestaurantTmplId());

        return new PagedResult<>(
                filtered,
                total,
                req.getPage(),
                req.getSize()
        );
    }

}
