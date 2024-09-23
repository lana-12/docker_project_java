package fr.projetspringgr2.repositories;


import fr.projetspringgr2.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {


        @Query("SELECT m.name FROM Movie m")
        List<String> findAllMovieNames();

        public Optional<Movie> findByName(String name);

        public HashSet<Movie> findByGenres(Genre genres);
        public HashSet<Movie> findByCountry(Country country);
        public HashSet<Movie> findByYear(int year);
        public HashSet<Movie> findByYearBetween(int yearMin, int yearMax);
        public HashSet<Movie> findByRating(double rating);
        //public HashSet<Movie> findByRatingGreaterThan(double rating);
        public HashSet<Movie> findByMovieLanguage(MovieLanguage movieLanguage);
        //public HashSet<Movie> findByRoles(Role role);
        //public HashSet<Movie> findByPersons(Person persons);
        public Movie findByNameAndYearAndRating(String name, int year, double rating);

        //public HashSet<Movie> findByPersons(int idPerson);
        public HashSet<Movie> findByRolesActorNameAndRolesActorSurname(String name,String surname);
        public HashSet<Movie> findByRolesPersonnage(String personnage);

        public HashSet<Movie> findByYearBetweenAndRolesActorNameAndRolesActorSurname(int yearMin, int yearMax, String name, String surname);
        public HashSet<Movie> findByYearBetweenAndPersonsIdPerson(int yearMin, int yearMax, String idPerson);

        @Query("SELECT m FROM Movie m " +
                "JOIN m.roles a1 " +
                "JOIN m.roles a2 " +
                "WHERE a1.actor.idPerson = :id_actor1 " +
                "AND a2.actor.idPerson = :id_actor2")
        public HashSet<Movie> findMoviesByTwoActors(@Param("id_actor1") String id_actor1,
                                          @Param("id_actor2") String id_actor2);




}
