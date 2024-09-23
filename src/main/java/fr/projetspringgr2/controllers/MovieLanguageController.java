package fr.projetspringgr2.controllers;

import fr.projetspringgr2.extractData.TestExtractJsonMovieLanguage;
import fr.projetspringgr2.models.MovieLanguage;
import fr.projetspringgr2.services.MovieLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing CRUD operations on MovieLanguage objects.
 */
@RestController
@RequestMapping("/movieLanguages")
public class MovieLanguageController {

    @Autowired
    private MovieLanguageService movieLanguageService;

    @Autowired
    private TestExtractJsonMovieLanguage testExtractJsonMovieLanguage;

    /**
     * Constructor for MovieLanguageController.
     *
     * @param movieLanguageService Service for handling movie language operations.
     * @param testExtractJsonMovieLanguage Service for importing movie languages from a JSON file.
     */
    public MovieLanguageController(MovieLanguageService movieLanguageService, TestExtractJsonMovieLanguage testExtractJsonMovieLanguage) {
        this.movieLanguageService = movieLanguageService;
        this.testExtractJsonMovieLanguage = testExtractJsonMovieLanguage;
    }

    /**
     * Get all movie languages.
     *
     * @return A list of all MovieLanguage objects.
     */
    @GetMapping
    public List<MovieLanguage> getMovieLanguage() {
        return movieLanguageService.getAllMovieLanguage();
    }

    /**
     * Get a movie language by its ID.
     *
     * @param id The ID of the movie language to retrieve.
     * @return The MovieLanguage object with the specified ID.
     */
    @GetMapping("/{id}")
    public MovieLanguage getMovieLanguage(@PathVariable Long id) {
        return movieLanguageService.getById(id);
    }

    /**
     * Create a new movie language.
     *
     * @param newMovieLanguage The MovieLanguage object to create.
     * @return A ResponseEntity with a success message if the creation is successful.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createMovieLanguage(@RequestBody MovieLanguage newMovieLanguage) {
        return movieLanguageService.saveMovieLanguage(newMovieLanguage);
    }

    /**
     * Update an existing movie language by its ID.
     *
     * @param id The ID of the movie language to update.
     * @param newMovieLanguage The updated MovieLanguage object.
     * @return A ResponseEntity with a success message if the update is successful.
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editPlace(@PathVariable Integer id, @RequestBody MovieLanguage newMovieLanguage){
        return movieLanguageService.updateMovieLanguage(id, newMovieLanguage);
    }

    /**
     * Delete a movie language by its ID.
     *
     * @param id The ID of the movie language to delete.
     * @return A ResponseEntity with a success message if the deletion is successful.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable Long id) {
        return movieLanguageService.deleteMovieLanguage(id);
    }

    /**
     * Import movie languages from a JSON file.
     *
     * @return A success message if the import is successful.
     */
    @GetMapping("/json")
    public String importMovieLanguage() {
        testExtractJsonMovieLanguage.importMovieLanguagesFromJsonFile("F:\\films.json");
        return "Import success!";
    }
}
