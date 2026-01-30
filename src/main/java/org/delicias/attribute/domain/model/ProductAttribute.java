package org.delicias.attribute.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.delicias.common.dto.product.DisplayAttrType;
import org.delicias.product.domain.model.ProductTemplate;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_attribute")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_attribute_id_seq")
    @SequenceGenerator(
            name = "product_attribute_id_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_type")
    @Enumerated(EnumType.STRING)
    private DisplayAttrType displayType;

    @Column(name = "sequence")
    private Short sequence;

    @ManyToOne
    @JoinColumn(name = "product_tmpl_id", referencedColumnName = "id")
    private ProductTemplate product;

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

    public ProductAttribute(Integer id) {
        this.id = id;
    }
}
