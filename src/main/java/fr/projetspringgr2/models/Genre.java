package fr.projetspringgr2.models;

import jakarta.persistence.*;
import java.util.Set;

/**
 * La classe {@code Genre} représente un genre de film dans le système.
 * Chaque genre a un identifiant unique et un nom, et il est associé à plusieurs films.
 *
 * <p>Cette entité est mappée à la table "genre" dans la base de données
 * et est utilisée pour catégoriser les films par genre (ex : Action, Comédie, Drame).</p>
 *
 * <p>La classe {@code Genre} est reliée à la classe {@code Movie} par une relation de type
 * many-to-many.</p>
 *
 * Exemple d'utilisation :
 * <pre>
 *     Genre genre = new Genre("Action");
 *     genre.setName("Comédie");
 * </pre>
 *
 * Auteur : Votre Nom
 */
@Entity
@Table(name = "genre")
public class Genre {

    /**
     * L'identifiant unique pour chaque genre.
     * Cette valeur est générée automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Le nom du genre (par exemple : Action, Comédie).
     * Ce champ est obligatoire et a une longueur maximale de 100 caractères.
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * Le jeu de films associés à ce genre.
     * Cette relation est bidirectionnelle, avec la classe {@code Movie} étant le côté propriétaire.
     */
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;

    /**
     * Constructeur par défaut pour la classe {@code Genre}.
     * Requis par JPA.
     */
    public Genre() {}

    /**
     * Construit un nouveau {@code Genre} avec le nom spécifié.
     *
     * @param name le nom du genre
     */
    public Genre(String name) {
        this.name = name;
    }

    // GETTERS et SETTERS


    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    /**
     * Fournit une représentation textuelle du genre.
     * Utile pour le débogage et les journaux.
     *
     * @return une représentation sous forme de chaîne de caractères de l'objet genre
     */
    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
