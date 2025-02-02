package fr.fms.dao;

import fr.fms.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> id(Long id);
}
