package com.ferdyfermadi.cafe.model.entity;

import com.ferdyfermadi.cafe.model.constants.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.ORDER_DETAIL)
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "menu_price", nullable = false, updatable = false)
    private Long menuPrice;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Column(name = "note")
    private String note;
}
