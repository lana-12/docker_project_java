package fr.projetspringgr2.controllers;

import fr.projetspringgr2.extractData.TestExtractJsonPlace;
import fr.projetspringgr2.models.Place;
import fr.projetspringgr2.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing CRUD operations on Place objects.
 */
@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private final PlaceService placeService;

    @Autowired
    private final TestExtractJsonPlace testExtractJsonPlace;

    /**
     * Constructor for PlaceController.
     *
     * @param placeService Service for handling place operations.
     * @param testExtractJsonPlace Service for importing places from a JSON file.
     */
    public PlaceController(PlaceService placeService, TestExtractJsonPlace testExtractJsonPlace) {
        this.placeService = placeService;
        this.testExtractJsonPlace = testExtractJsonPlace;
    }

    /**
     * Get all places.
     *
     * @return A list of all Place objects.
     */
    @GetMapping
    public List<Place> getPlaces() {
        return placeService.getAllPlaces();
    }

    /**
     * Get a place by its ID.
     *
     * @param id The ID of the place to retrieve.
     * @return The Place object with the specified ID.
     */
    @GetMapping("/{id}")
    public Place getPlace(@PathVariable Long id) {
        return placeService.getById(id);
    }

    /**
     * Find places by state.
     *
     * @param state The state to search for places.
     * @return A ResponseEntity with a JSON string containing places found in the specified state.
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<String> findPlaceByState(@PathVariable String state) {
        return placeService.getPlaceByState(state);
    }

    /**
     * Find places by city.
     *
     * @param city The city to search for places.
     * @return A ResponseEntity with a JSON string containing places found in the specified city.
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<String> findPlaceByCity(@PathVariable String city) {
        return placeService.getPlaceByCity(city);
    }

    /**
     * Find places by address.
     *
     * @param address The address to search for places.
     * @return A ResponseEntity with a JSON string containing places found at the specified address.
     */
    @GetMapping("/address/{address}")
    public ResponseEntity<String> findPlaceByAddress(@PathVariable String address) {
        return placeService.getPlaceByAddress(address);
    }

    /**
     * Create a new place.
     *
     * @param newPlace The Place object to create.
     * @return A ResponseEntity with a success message if the creation is successful.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createPlace(@RequestBody Place newPlace) {
        return placeService.savePlace(newPlace);
    }

    /**
     * Update an existing place by its ID.
     *
     * @param id The ID of the place to update.
     * @param newPlace The updated Place object.
     * @return A ResponseEntity with a success message if the update is successful.
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editPlace(@PathVariable Integer id, @RequestBody Place newPlace) {
        return placeService.updatePlace(id, newPlace);
    }

    /**
     * Delete a place by its ID.
     *
     * @param id The ID of the place to delete.
     * @return A ResponseEntity with a success message if the deletion is successful.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable Long id) {
        return placeService.deletePlace(id);
    }

    /**
     * Import places from a JSON file.
     *
     * @return A success message if the import is successful.
     */
    @GetMapping("/json")
    public String importPlaces() {
        testExtractJsonPlace.importTestPlacesFromJsonFileForLieuTournage("F:\\films.json");
        testExtractJsonPlace.importTestPlacesFromJsonFileForLieuNaissance("F:\\films.json");
        return "Import success!";
    }
}
