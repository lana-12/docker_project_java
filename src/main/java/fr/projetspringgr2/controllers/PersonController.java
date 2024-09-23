
package fr.projetspringgr2.controllers;

import fr.projetspringgr2.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.projetspringgr2.services.PersonService;

import java.util.List;

/**
 * The `PersonController` class is a REST controller that handles HTTP requests related to `Person` entities.
 * This controller provides endpoints to perform CRUD operations and specific queries on `Person` objects,
 **/
 @RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    /** Get a list of all `Person` entities.
     * This endpoint handles HTTP GET requests to `/persons`.
     * @return a list of all persons, including actors, actresses, and directors.
     **/
    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    /** Get a `Person` entity by its unique ID.
     * This endpoint handles HTTP GET requests to `/persons/{id}`.
     * @param id the ID of the person to retrieve.
     * @return the `Person` entity with the specified ID, or `null` if not found.
     **/
    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable String id) {
        return personService.getPersonById(id);
    }

    /**Creates a new `Person` entity.
     * This endpoint handles HTTP POST requests to `/persons`.
     * @param person the `Person` entity to create, provided in the request body.
     * @return the created `Person` entity.
     **/
    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personService.savePerson(person);
    }

    /**Updates an existing `Person` entity.
     * This endpoint handles HTTP PUT requests to `/persons/{id}`.
     * @param id the ID of the person to update.
     * @param personDetails the updated details of the person, provided in the request body.
     * @return a `ResponseEntity` with status `200 OK` if the update was successful,
     *         or `404 Not Found` if the person does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePerson(@PathVariable String id, @RequestBody Person personDetails) {
        boolean updated = personService.updatePerson(id, personDetails);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**Retrieves a list of actors who acted in both specified movies.
     * This endpoint handles HTTP GET requests to `/persons/actors/movies/{idMovie1}/{idMovie2}`.
     * @param idMovie1 the ID of the first movie.
     * @param idMovie2 the ID of the second movie.
     * @return a list of `Person` entities who acted in both movies.
     **/
    @GetMapping("/actors/movies/{idMovie1}/{idMovie2}")
    public List<Person> getPersonById(@PathVariable int idMovie1, @PathVariable int idMovie2) {
        return personService.getActorsByMovies(idMovie1, idMovie2);
    }

}
