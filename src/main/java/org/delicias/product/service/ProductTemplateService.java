package org.delicias.product.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.attribute.domain.model.ProductAttribute;
import org.delicias.attribute.domain.repository.ProductAttributeRepository;
import org.delicias.attribute_value.domain.model.ProductAttributeValue;
import org.delicias.attribute_value.domain.repository.ProductAttributeValueRepository;
import org.delicias.common.dto.PagedResult;
import org.delicias.common.dto.product.ProductCandidateShoppingLineDTO;
import org.delicias.common.dto.product.ProductPriceDTO;
import org.delicias.common.dto.product.ProductResumeDTO;
import org.delicias.product.domain.model.ProductTemplate;
import org.delicias.product.domain.repository.ProductTemplateRepository;
import org.delicias.product.dto.CreateProductTmplDTO;
import org.delicias.product.dto.ProductTmplDTO;
import org.delicias.product.dto.ProductTmplFilterItemDTO;
import org.delicias.product.dto.ProductTmplFilterReqDTO;
import org.delicias.supabase.SupabaseStorageService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductTemplateService {

    @Inject
    SupabaseStorageService storageService;

    @Inject
    ProductTemplateRepository repository;

    @Inject
    ProductAttributeRepository attributeRepository;

    @Inject
    ProductAttributeValueRepository valueRepository;

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

        return Map.of("id", entity.getId());
    }

    @Transactional
    public Map<String, String> uploadPicture(Integer restaurantTmplId, FileUpload logoFile) throws IOException {

        ProductTemplate entity = repository.findById(restaurantTmplId);

        if(entity == null) {
            throw new NotFoundException("ProductTemplate Tmpl Not Found");
        }

        String pictureUrl = storageService.uploadFile(logoFile);

        deleteCurrentPicture(entity.getPicture());

        entity.setPicture(pictureUrl);

        return Map.of("picture", pictureUrl);
    }

    private void deleteCurrentPicture(String pictureUrl) {
        if(Optional.ofNullable(pictureUrl).isPresent()) {
            storageService.deleteFile(pictureUrl);
        }
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
                .listPrice(Optional.ofNullable(entity.getListPrice()).orElse(BigDecimal.ZERO))
                .salesOk(entity.getSalesOk())
                .restaurantTmplId(entity.getRestaurantTmplId())
                .picture(Optional.ofNullable(entity.getPicture()).orElse(defaultPicture))
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

    public List<ProductResumeDTO> findByIds(List<Integer> ids) {

        return repository.findByIds(ids)
                .stream().map(it -> ProductResumeDTO.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .description(it.getDescription())
                        .listPrice(Optional.ofNullable(it.getListPrice()).orElse(BigDecimal.ZERO))
                        .pictureUrl(Optional.ofNullable(it.getPicture()).orElse(defaultPicture))
                        .build()).toList();
    }

    public ProductCandidateShoppingLineDTO getCandidateShoppingLine(Integer productTmplId) {

        ProductTemplate entity = repository.findById(productTmplId);

        if (entity == null) {
            throw new NotFoundException("ProductTemplate Not Found");
        }

        var attrs = attributeRepository.findByProduct(entity.getId());

        var values = valueRepository.findByAttributes(
                attrs.stream().map(ProductAttribute::getId).collect(Collectors.toSet())
        );

        return ProductCandidateShoppingLineDTO.builder()
                .productTmplId(entity.getId())
                .restaurantTmplId(entity.getRestaurantTmplId())
                .listPrice(entity.getListPrice())
                .attrValues(values.stream()
                        .map(it -> ProductCandidateShoppingLineDTO.AttributeValueDTO.builder()
                                .attrValueId(it.getId())
                                .extraPrice(Optional.ofNullable(it.getExtraPrice()).orElse(BigDecimal.ZERO))
                                .build())
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public List<ProductPriceDTO> getProductPrices(List<Integer> ids) {

        List<ProductTemplate> products = repository.findByIds(ids);

        Map<Integer, List<ProductAttribute>> allAttrs = attributeRepository.findByIds(ids)
                .stream().collect(Collectors.groupingBy(p -> p.getProduct().getId()));

        Set<Integer> attributeIds = allAttrs.values().stream()
                .flatMap(List::stream)
                .map(ProductAttribute::getId)
                .collect(Collectors.toSet());

        Map<Integer, List<ProductAttributeValue>> allValues = valueRepository.findByAttributes(attributeIds)
                .stream().collect(Collectors.groupingBy(it -> it.getAttribute().getId()));

        return products.stream().map(it -> {

            List<ProductAttribute> attrs = allAttrs.get(it.getId());

            return ProductPriceDTO.builder()
                    .productTmplId(it.getId())
                    .name(it.getName())
                    .description(it.getDescription())
                    .listPrice(Optional.ofNullable(it.getListPrice()).orElse(BigDecimal.ZERO))
                    .pictureUrl(Optional.ofNullable(it.getPicture()).orElse(defaultPicture))
                    .attributes(attrs.stream().map(attr -> {

                        List<ProductAttributeValue> values = allValues.getOrDefault(attr.getId(), Collections.emptyList());

                        return ProductPriceDTO.AttributeDTO.builder()
                                .attrId(attr.getId())
                                .name(attr.getName())
                                .values(values.stream().map(val ->
                                        ProductPriceDTO.AttributeValueDTO.builder()
                                                .attrValueId(val.getId())
                                                .name(val.getName())
                                                .extraPrice(val.getExtraPrice())
                                                .build()
                                ).collect(Collectors.toList()))
                                .build();
                    }).collect(Collectors.toList()))
                    .build();
        }).collect(Collectors.toList());

    }

}
