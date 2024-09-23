package fr.projetspringgr2.extractData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetspringgr2.models.Country;
import fr.projetspringgr2.models.Place;
import fr.projetspringgr2.repositories.CountryRepository;
import fr.projetspringgr2.repositories.PlaceRepository;
import fr.projetspringgr2.services.CountryService;
import fr.projetspringgr2.services.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for importing places from a JSON file.
 */
@Service
public class TestExtractJsonPlace {

    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${file.path}")
    private String filePath;

    /**
     * Constructor for TestExtractJsonPlace.
     *
     * @param placeRepository Repository for managing Place entities.
     * @param countryService Service for handling Country operations.
     * @param countryRepository Repository for managing Country entities.
     */
    public TestExtractJsonPlace(PlaceRepository placeRepository, CountryService countryService, CountryRepository countryRepository) {
        this.placeRepository = placeRepository;
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    /**
     * Imports filming locations (lieuTournage) from a specified JSON file.
     *
     * @param filePath The path to the JSON file containing place data.
     */
    public void importTestPlacesFromJsonFileForLieuTournage(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            for (JsonNode movieNode : rootNode) {
                JsonNode lieuTournageNode = movieNode.get("lieuTournage");
                if (lieuTournageNode != null) {
                    String cityAddress = lieuTournageNode.get("ville").asText();
                    String state = lieuTournageNode.get("etatDept").asText();
                    String country = lieuTournageNode.get("pays").asText();

                    // Split City and Address
                    String[] cityAndAddress = placeService.extractCityAndAddress(cityAddress);
                    String city = cityAndAddress[0];
                    String address = cityAndAddress[1];

                    // Find existing places by state and city
                    List<Place> existingPlaces = placeRepository.findAllByStateAndCity(state, city);
                    Place placeToSave = null;

                    for (Place place : existingPlaces) {
                        if ((address == null && place.getAddress() == null) ||
                                (address != null && address.equals(place.getAddress()))) {
                            placeToSave = place;
                            break;
                        }
                    }

                    if (placeToSave == null) {
                        // Create a new Place
                        placeToSave = new Place(state, city, address);

                        if (country != null) {
                            Optional<Country> countryOb = countryRepository.findByName(country);
                            if (countryOb.isPresent()) {
                                placeToSave.setCountry(countryOb.get());
                            } else {
                                Country newCountry = new Country();
                                newCountry.setName(country);
                                countryService.saveCountry(newCountry);
                                placeToSave.setCountry(newCountry);
                            }
                        }

                        placeRepository.save(placeToSave);
                        logger.info("New place : {}", placeToSave);
                    } else {
                        logger.info("Place Existing: {} ", placeToSave);
                    }
                }
            }

        } catch (IOException e) {
            logger.error("Error ", e);
        }
    }

    /**
     * Imports birthplaces (lieuNaissance) from a specified JSON file.
     *
     * @param filePath The path to the JSON file containing place data.
     */
    public void importTestPlacesFromJsonFileForLieuNaissance(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Place> places = new ArrayList<>();
        try {

            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            for (JsonNode movieNode : rootNode) {
                JsonNode lieuNaissanceNode = movieNode.get("naissance");

                if (lieuNaissanceNode != null) {
                    String birthPlace = lieuNaissanceNode.get("lieuNaissance").asText();

                    if (birthPlace != null && !birthPlace.trim().isEmpty()) {

                        // Extract birth place details
                        String[] locationbirthPlace = placeService.extractBirthDatePlace(birthPlace);
                        String address = locationbirthPlace[0];
                        String city = locationbirthPlace[1];
                        String state = locationbirthPlace[2];
                        String country = locationbirthPlace[3];

                        // Verify if country exists
                        Optional<Country> countryOpt = country != null ? countryRepository.findByName(country) : Optional.empty();
                        Country countryEntity = null;

                        if (countryOpt.isPresent()) {
                            countryEntity = countryOpt.get();
                        } else if (country != null) {
                            countryEntity = new Country();
                            countryEntity.setName(country);
                            countryService.saveCountry(countryEntity);
                        }

                        Optional<Place> existingPlace = placeRepository.findByAddressAndStateAndCityAndCountry(address, state, city, countryEntity);
                        if (existingPlace.isEmpty()) {

                            Place newPlace = new Place(state, city, address);
                            newPlace.setCountry(countryEntity);

                            places.add(newPlace);
                            logger.info("New place: {}", newPlace);
                        } else {
                            logger.info("Place  exists: {}", existingPlace.get());
                        }
                    }
                }
            }

            if (!places.isEmpty()) {
                placeRepository.saveAll(places);
                logger.info("All new places saved.");
            } else {
                logger.info("No places to save.");
            }

        } catch (IOException e) {
            logger.error("Error ", e);
        }
    }
}
