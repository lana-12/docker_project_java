
package fr.projetspringgr2.controllers;

import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.services.MovieService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
//
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;


    /**
     * This method imports data into database from json file.
     *
     * @return the message "import success" if there are no problems with import
     */
    @GetMapping("/json")
    public String importMovies() {
        movieService.importMoviesFromJsonFile();
        return "Import success!";
    }


    @GetMapping("/all")
    public List<String> getAllMovies() {
        return movieService.getAllMovieNames();
    }


    /**
     * get all movies between two years
     * @param  year1 first year
     * @param  year1 second year
     * @return Movie List
     * Link : /yearbetween
     */
    @GetMapping("/yearbetween/{year1}/{year2}")
    public HashSet<Movie> getMoviesByYear(@PathVariable int year1, @PathVariable int year2) {
        return movieService.getMoviesByYearBetween(year1,year2);
    }

    /**
     * get all movies for an actor
     * @param  name the actors name
     * @param  surname the actors surname
     * @return Movie List
     * link :/actor
     */
    @GetMapping("/actor/{name}/{surname}")
    public HashSet<Movie> getMoviesByActor(@PathVariable String name, @PathVariable String surname) {
        return movieService.getMoviesByActor(name,surname);
    }

    /**
     * get all movies that contains a Role
     * @param  personnage Role name
     * @return Movie List
     * link : /role
     */
    @GetMapping("/role/{personnage}")
    public HashSet<Movie> getMoviesByRole(@PathVariable String personnage) {
        return movieService.getMoviesByRole(personnage);
    }

    /**
     * get all movies between two years for an actor
     * @param  yearMin first year
     * @param  yearMax second year
     * @param name the actors name
     * @param surname the actors surname
     * @return Movie List
     * link : /actorandyearbetween
     */
    @GetMapping("/actorandyearbetween/{yearMin}/{yearMax}/{name}/{surname}")
    public HashSet<Movie> getMoviesByYearBetweenAndActor(@PathVariable int yearMin, @PathVariable int yearMax, @PathVariable String name, @PathVariable String surname) {
        return movieService.getMoviesByYearBetweenAndActor(yearMin,yearMax,name,surname);
    }

    /**
     * get all movies between two years for a director
     * @param  yearMin first year
     * @param  yearMax second year
     * @param idPerson the directors id
     * @return Movie List
     * link : /directorandyearbetween
     */
    @GetMapping("/directorandyearbetween/{yearMin}/{yearMax}/{idPerson}")
    public HashSet<Movie> getMoviesByYearBetweenAndDirector(@PathVariable int yearMin, @PathVariable int yearMax, @PathVariable String idPerson) {
        return movieService.getMoviesByYearBetweenAndDirector(yearMin,yearMax,idPerson);
    }

    /**
     * get all movies shared by two actors
     * @param  id_actor1 first actors id
     * @param  id_actor2 second actors id
     * @return Movie List
     * link : /bytwoactors
     */
    @GetMapping("/bytwoactors/{id_actor1}/{id_actor2}")
    public HashSet<Movie> getMoviesByTwoActors(@PathVariable String id_actor1, @PathVariable String id_actor2) {
        return movieService.getMoviesByTwoActors(id_actor1,id_actor2);
    }

    /**
     * save movie in Database
     * @param movie Movie to be saved
     * @return responseEntity
     */
    @PostMapping
    public ResponseEntity<String> createMovie(@RequestBody Movie movie) {
        if(movieService.saveMovie(movie)) {
            return new ResponseEntity<String>("Succès ! ",HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("This movie alredy exists !",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * update the movie at id  with data from movie
     * @param  id id of the movie to be updated
     * @param movie Movie for update
     * @return responseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        if(movieService.updateMovie(id,movie)) {
            return new ResponseEntity<String>("Succès ! ",HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("This movie alredy exists !",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * delete the movie at id
     * @param  id id of the movie to be deleted
     * @return responseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id) {
        if (movieService.deleteMovie(id)) {
            return new ResponseEntity<String>("Succès !",HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("La supression a échouée !",HttpStatus.BAD_REQUEST);
        }
    }

}
