package com.ashtana.backend.Entity;

import com.ashtana.backend.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders") // "order" is reserved in SQL
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne
    private Influencer influencer; // if coupon applied

    @Column(nullable = false)
    private Double totalAmount;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.orderNumber == null) {
            this.orderNumber = UUID.randomUUID().toString();
        }
    }

}
