package fr.projetspringgr2.repositories;

import fr.projetspringgr2.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    /**
     * Trouve un pays par son nom.
     * @param name Le nom du pays à rechercher.
     * @return Un Optional contenant le pays si trouvé, sinon un Optional vide.
     */
    Optional<Country> findByName(String name);
}
