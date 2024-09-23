package fr.projetspringgr2.repositories;

import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.models.Person;
import fr.projetspringgr2.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    // Find a person by their name
    List<Person> findByName(String name);

    // Find persons by their surname
    List<Person> findBySurname(String surname);

    // Find persons by their birthdate
    List<Person> findByBirthDate(Date birthDate);

    // Find persons by their place
    List<Person> findByPlace(Place place);
    
    @Query("SELECT p FROM Person p " +
            "JOIN p.roles a1 " +
            "JOIN p.roles a2 " +
            "WHERE a1.movie.id = :id_movie1 " +
            "AND a2.movie.id = :id_movie2")
    public List<Person> findActorsByTwoMovies(@Param("id_movie1") int id_movie1,
                                      @Param("id_movie2") int id_movie2);

    @Query(value = "SELECT p.* FROM person p JOIN movie_person mp ON p.id_person = mp.id_person WHERE mp.id_movie = :movieId", nativeQuery = true)
    Set<Person> findDirectorsByMovieId(@Param("movieId") Integer movieId);
}

