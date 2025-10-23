package com.ashtana.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ColorName;

    @Column(nullable = false)
    private String ColorCode;

    @OneToMany(mappedBy = "color", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnoreProperties("color")
    private List<Product> products = new ArrayList<>();
}
