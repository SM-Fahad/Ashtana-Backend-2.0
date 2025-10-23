package com.ashtana.backend.Repository;



import com.ashtana.backend.Entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepo extends JpaRepository<Color, Long> {
}
