package org.delicias.attribute_value.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.delicias.attribute.domain.model.ProductAttribute;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "product_attribute_value")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_attribute_value_id_seq")
    @SequenceGenerator(
            name = "product_attribute_value_id_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "default_extra_price", precision = 10, scale = 2)
    private BigDecimal extraPrice;

    @Column(name = "sequence")
    private Short sequence;

    @ManyToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private ProductAttribute attribute;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
