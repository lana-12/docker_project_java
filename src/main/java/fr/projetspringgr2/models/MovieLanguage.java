package fr.projetspringgr2.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="movie_language")
public class MovieLanguage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", length = 100, nullable = false )
    private String name;

    @OneToMany(mappedBy = "movieLanguage")
    private List<Movie> movies;

    /**
     * Constructor by default
     */
    public MovieLanguage() {}

    public MovieLanguage(String name) {
        this.name = name;
    }

    /**
     * Getters and Setters
     *
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "Langue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
