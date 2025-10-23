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
@Table(name = "size")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String SizeName;


    @OneToMany(mappedBy = "size", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnoreProperties("size")
    private List<Product> products = new ArrayList<>();
}
