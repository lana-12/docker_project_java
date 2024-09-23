package fr.projetspringgr2.models;

import java.util.Set;
import jakarta.persistence.*;
import java.util.HashSet;

@Entity
@Table(name="country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", length = 100, nullable = false )
    private String name;

    // One-to-Many relationship with the Movie entity
    @OneToMany(mappedBy="country")
    private Set<Movie> movies;

    // One-to-Many relationship with the Place entity
    @OneToMany(mappedBy="country")
    private Set<Place> places;

    // Bloc d'initialisation pour configurer la collection places
    {
        places = new HashSet<>();
    }

    /**
     * Constructeur par défaut
     * Initialise une nouvelle instance de la classe Country sans définir d'attributs.
     */
    public Country() {}

    /**
     * Constructeur avec paramètre name
     * @param name Le nom du pays à initialiser pour l'objet Country.
     */
    public Country(String name) {
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
     * Convertit l'objet Country en une représentation textuelle.
     * @return Une chaîne de caractères représentant l'objet Country, incluant son identifiant et son nom.
     */
    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
