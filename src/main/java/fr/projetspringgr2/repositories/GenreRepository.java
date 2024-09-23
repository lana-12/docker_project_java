package fr.projetspringgr2.repositories;

import fr.projetspringgr2.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface {@code GenreRepository} qui étend {@code JpaRepository} pour l'entité {@code Genre}.
 * <p>
 * Cette interface fournit des méthodes pour effectuer des opérations CRUD (Create, Read, Update, Delete)
 * sur l'entité Genre en utilisant la persistance JPA (Java Persistence API).
 * </p>
 */
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    /**
     * Méthode pour rechercher un genre par son nom.
     * <p>
     * Cette méthode permet de récupérer un genre spécifique à partir de son nom en utilisant une requête personnalisée
     * générée automatiquement par Spring Data JPA.
     * </p>
     *
     * @param name le nom du genre à rechercher
     * @return un objet {@code Optional<Genre>} qui contient le genre trouvé ou est vide si aucun genre n'est trouvé
     */
    Optional<Genre> findByName(String name);
}
