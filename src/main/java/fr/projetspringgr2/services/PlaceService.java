package fr.projetspringgr2.services;


import com.fasterxml.jackson.databind.JsonNode;
import fr.projetspringgr2.models.Country;
import fr.projetspringgr2.models.Place;
import fr.projetspringgr2.repositories.CountryRepository;
import fr.projetspringgr2.repositories.PlaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;


/**
 * Service class for managing Place entities.
 */
@Service
public class PlaceService {

    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Value("${file.path}")
    private String filePath;

    public PlaceService(PlaceRepository placeRepository, CountryService countryService, CountryRepository countryRepository){
        this.placeRepository = placeRepository;
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    /**
     * Retrieve allPlaces
     * @return List of all places
     */
    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();
        placeRepository.findAll().forEach(places::add);

        return places;
    }


    /**
     * Retrieve a place by ID.
     *
     * @param id The ID of the place.
     * @return The Place object if found, null otherwise
     */
    public Place getById(Long id) {

        if (placeRepository.existsById(id)) {
            return placeRepository.findById(id).get();
        }

        return null;
    }


    /**
     * Retrieve getState of the place
     * @param state The state of the place
     * @return ResponseEntity containing the Place details or a not found message
     */
    public ResponseEntity<String> getPlaceByState(String state) {
        Optional<Place> place = placeRepository.findByState(state);
        if (place.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(place.get().toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune place trouvée avec l'état: " + state);
    }


    /**
     * Retrieve getCity of the place
     * @param city The city of the place
     * @return ResponseEntity containing the Place details or a not found message
     */
    public ResponseEntity<String> getPlaceByCity(String city) {
        Optional<Place> place = placeRepository.findByCity(city);
        if (place.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(place.get().toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune place trouvée avec la ville: " + city);
    }


    /**
     * Retrieve getAddress of the place
     * @param address The address of the place
     * @return ResponseEntity containing the Place details or a not found message
     */
    public ResponseEntity<String> getPlaceByAddress(String address) {
        Optional<Place> place = placeRepository.findByAddress(address);
        if (place.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(place.get().toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune place trouvée avec l'adresse: " + address);
    }



    /**
     * Adding a new Place
     * @param newPlace The Place object to be added
     * @return ResponseEntity with creation status or conflict if already exists
     */
    public ResponseEntity<String> savePlace(Place newPlace) {
        // Verif if country isExist
        if (newPlace.getCountry() != null && newPlace.getCountry().getName() != null) {
            String countryName = newPlace.getCountry().getName();
            Optional<Country> existingCountry = countryRepository.findByName(countryName);

            if (existingCountry.isPresent()) {
                newPlace.setCountry(existingCountry.get());
            } else {
                Country newCountry = new Country();
                newCountry.setName(countryName);
                ResponseEntity<String> countryResponse = countryService.saveCountry(newCountry);

                if (countryResponse.getStatusCode() == HttpStatus.CREATED) {
                    newPlace.setCountry(newCountry);
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Failed to create Country: " + countryResponse.getBody());
                }
            }
        }
        placeRepository.save(newPlace);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Place insérée avec succès");
    }



    /**
     * Edit a place with id
     * @param id The ID of the place to be updated
     * @param updatedPlace The Place object with updated data
     * @return ResponseEntity with update status or conflict if not found
     */
    public ResponseEntity<String> updatePlace(Integer id, Place updatedPlace) {

        Optional<Place> existingPlaceOpt = placeRepository.findById((long) id);

        if (existingPlaceOpt.isPresent()) {
            Place existingPlace = existingPlaceOpt.get();

            // Verif if country isExist
            if (updatedPlace.getCountry() != null && updatedPlace.getCountry().getName() != null) {
                String countryName = updatedPlace.getCountry().getName();
                Optional<Country> existingCountry = countryRepository.findByName(countryName);

                if (existingCountry.isPresent()) {
                    updatedPlace.setCountry(existingCountry.get());
                } else {

                    Country newCountry = new Country();
                    newCountry.setName(countryName);
                    ResponseEntity<String> countryResponse = countryService.saveCountry(newCountry);

                    if (countryResponse.getStatusCode() == HttpStatus.CREATED) {
                        updatedPlace.setCountry(newCountry);
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Failed to create Country: " + countryResponse.getBody());
                    }

                }
            }

            existingPlace.setState(updatedPlace.getState());
            existingPlace.setCity(updatedPlace.getCity());
            existingPlace.setAddress(updatedPlace.getAddress());
            existingPlace.setCountry(updatedPlace.getCountry());


            placeRepository.save(existingPlace);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Place modifiée avec succès");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Place avec cet id n'existe pas.");
    }



    /**
     * Delete a place by id
     * @param id The ID of the place to be deleted
     * @return ResponseEntity with deletion status or conflict if not found
     */
    public ResponseEntity<String> deletePlace(Long id){
        Place place = getById(id);
        if(place != null){
            placeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Place supprimée avec succès");

        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Place avec cet id n'existe pas.");
    }


    /**
     * Import a Place from a JSON node
     * @param lieuTournageNode > JsonNode lieuTournageNode = movieNode.get("lieuTournage");
     * @return The imported or existing Place object, or null if not valid
     */
    public Place importPlaceFromJsonNode(JsonNode lieuTournageNode) {
        if (lieuTournageNode != null) {
            String cityAddress = lieuTournageNode.get("ville").asText().trim().strip();
            String state = lieuTournageNode.get("etatDept").asText().trim().strip();
            String country = lieuTournageNode.get("pays").asText().trim().strip();

            // Split City and Address
            String[] cityAndAddress = extractCityAndAddress(cityAddress);
            String city = cityAndAddress[0];
            String address = cityAndAddress[1];


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

                placeToSave = placeRepository.save(placeToSave);
                logger.info("New place saved: {}", placeToSave);
            } else {
                logger.info("Existing place found, skipping save: {}", placeToSave);
            }

            return placeToSave;
        }

        return null;
    }



    /**
     * Extract city and address from a combined city-address string.
     *
     * @param cityAddress The combined city and address string.
     * @return An array with separated city and address values.
     */
    public String[] extractCityAndAddress(String cityAddress) {
        if (cityAddress == null || !cityAddress.contains("-")) {
            return new String[]{cityAddress != null ? cityAddress.trim() : null, null};
        }
        String[] parts = cityAddress.split("-", 2);

        // City
        parts[0] = parts[0].trim();

        // Address
        parts[1] = parts[1].trim();

        if (parts[1].isEmpty() || parts[1].equalsIgnoreCase("NA")) {
            parts[1] = null;
        }

        return parts;
    }


    /**
     * Imports a place from a JSON node containing birthplace information
     * @param naissanceNode => JsonNode naissanceNode = movieNode.get("naissance");
     * @return The imported or existing Place object, or null if the "lieuNaissance" node is null or empty
     */
    public Place importPlaceFromBirthJsonNode(JsonNode naissanceNode) {
        if (naissanceNode != null) {
            JsonNode lieuNaissanceNode = naissanceNode.get("lieuNaissance");

            if (lieuNaissanceNode != null && !lieuNaissanceNode.asText().trim().isEmpty()) {
                String birthPlace = lieuNaissanceNode.asText();

                // Extract address, city, state, country from birthPlace
                String[] locationbirthPlace = extractBirthDatePlace(birthPlace);
                String address = locationbirthPlace[0];
                String city = locationbirthPlace[1];
                String state = locationbirthPlace[2];
                String country = locationbirthPlace[3].trim().strip();

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
                    // Create a new Place
                    Place newPlace = new Place(state, city, address);
                    newPlace.setCountry(countryEntity);


                    placeRepository.save(newPlace);
                    logger.info("New place : {}", newPlace);
                    return newPlace;
                } else {
                    logger.info("Place exists : {}", existingPlace.get());
                    return existingPlace.get();
                }
            }
        }

        return null;
    }



    /**
     * Splits a location string into address, city, state, and country.
     * @param location The location string to be parsed, which may contain address, city, state and country separated by commas.
     * @return An array of strings where:
     *          - index 0 contains the address (if available),
     *          - index 1 contains the city (if available),
     *          - index 2 contains the state (if available),
     *          - index 3 contains the country (if available).
     */
    public String[] extractBirthDatePlace(String location) {
        if (location == null || location.trim().isEmpty()) {
            return new String[]{null, null, null, null};
        }
        String[] parts = location.split(",");
        int length = parts.length;

        String country = null;
        String state = null;
        String city = null;
        String address = null;

        switch (length) {

        	case 1:
        		country = parts[0].trim();

        		break;
        	case 2:
                city = parts[0].trim();
                country = parts[1].trim();

                break;
            case 3:
                city = parts[0].trim();
                state = parts[1].trim();
                country = parts[2].trim();

                break;
            default:
                address = String.join(", ", Arrays.copyOfRange(parts, 0, length - 3)).trim();
                city = parts[length - 3].trim();
                state = parts[length - 2].trim();
                country = parts[length - 1].trim();
                break;
        }

        return new String[]{address, city, state, country};

    }

}
