package fr.projetspringgr2.repositories;

import fr.projetspringgr2.models.MovieLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieLanguageRepository extends JpaRepository<MovieLanguage, Long> {

    Optional<MovieLanguage> findByName(String name);
}
