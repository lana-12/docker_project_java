package fr.projetspringgr2.models;



import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import org.springframework.beans.MutablePropertyValues;


import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="movie")
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( nullable = false)
    private String idMovie;

    @Column(name="name", length = 100, nullable = false )
    private String name;

    @Column(name="year" )
    private int year;

    @Column(name="rating", nullable = false )
    private double rating;


    @Column(name="synopsis", nullable = false,columnDefinition = "TEXT" )
    private String synopsis;


    @ManyToOne
    @JoinColumn(name = "id_country")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "id_place")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "id_movieLanguage")
    private MovieLanguage movieLanguage;

    @OneToMany(mappedBy = "movie")
    private Set<Role> roles = new HashSet<>();


    @ManyToMany
    @JoinTable(name="movie_genre",joinColumns = @JoinColumn(name = "id_movie", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "id_genre",referencedColumnName = "id")

    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name="movie_person",joinColumns = @JoinColumn(name = "id_movie", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "id_person",referencedColumnName = "id_person")

    )
    private Set<Person> persons = new HashSet<>();




    /**
     * Constructor by default
     */
    public Movie() {}

    /**
     * Constructor by parameters
     * @param name movie name
     * @param year year of movie
     * @param rating movie rating
     * @param synopsis movie synopsis
     */
    public Movie(Integer id, String name, int year, double rating, String synopsis) {

        this.name = name;
        this.year = year;
        this.rating = rating;
        this.synopsis = synopsis;
    }


    /**GETTERS and SETTERS*/

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public MovieLanguage getMovieLanguage() {
        return movieLanguage;
    }

    public void setMovieLanguage(MovieLanguage movieLanguage) {
        this.movieLanguage = movieLanguage;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
/*
    public void setId(Integer id) {
        this.id = id;
    }*/

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }



    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +

                ", year=" + year +

                ", rating=" + rating +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }


}
