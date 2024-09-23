package fr.projetspringgr2.services;

import com.fasterxml.jackson.databind.JsonNode;
import fr.projetspringgr2.models.Country;
import fr.projetspringgr2.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    /**
     * Ajoute un nouveau Country.
     *
     * @param newCountry Le nouvel objet Country à ajouter.
     * @return Une réponse HTTP contenant un message indiquant le succès ou l'échec de l'opération.
     */
    public ResponseEntity<String> saveCountry(Country newCountry) {
        Optional<Country> existingCountry = countryRepository.findByName(newCountry.getName());

        if (existingCountry.isPresent()) {
            // Retourne un conflit HTTP si le Country existe déjà
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A Country with this name already exists");
        }

        // Sauvegarde le nouveau Country et retourne une réponse de création
        countryRepository.save(newCountry);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Country successfully inserted");
    }

    /**
     * Récupère tous les pays.
     *
     * @return Une liste de tous les objets Country.
     */
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    /**
     * Récupère un pays par son ID.
     *
     * @param id L'ID du pays à rechercher.
     * @return L'objet Country correspondant, ou null si le pays n'existe pas.
     */
    public Country getCountryById(Integer id) {
        if (countryRepository.existsById(id)) {
            return countryRepository.findById(id).get();
        }
        return null;
    }

    /**
     * Met à jour un pays existant.
     *
     * @param id L'ID du pays à mettre à jour.
     * @param countryDetails Les détails du pays à mettre à jour.
     * @return Une réponse HTTP indiquant le succès ou l'échec de la mise à jour.
     */
    public ResponseEntity<String> updateCountry(Integer id, Country countryDetails) {
        if (countryRepository.existsById(id)) {
            Country country = countryRepository.findById(id).get();
            country.setName(countryDetails.getName());
            countryRepository.save(country);
            return ResponseEntity.status(HttpStatus.OK).body("Country modified");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Country not found");
    }

    /**
     * Supprime un pays par son ID.
     *
     * @param id L'ID du pays à supprimer.
     * @return Une réponse HTTP indiquant le succès ou l'échec de la suppression.
     */
    public ResponseEntity<String> deleteCountry(Integer id) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Country deleted");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Country not found");
    }

    /**
     * Méthode pour extraire les données du pays à partir d'un nœud JSON.
     *
     * @param countryNode Le nœud JSON contenant les informations du pays.
     * @return Un objet Country correspondant aux données extraites, ou null si les données sont invalides.
     */
    public Country importCountryFromJson(JsonNode countryNode) {
        if (countryNode != null && !countryNode.get("nom").asText().trim().isEmpty()) {
            String countryName = countryNode.get("nom").asText().trim().strip();

            Optional<Country> countryOpt = countryRepository.findByName(countryName);

            if (countryOpt.isPresent()) {
                return countryOpt.get();
            } else {
                Country newCountry;
                newCountry = new Country();
                newCountry.setName(countryName);
                newCountry = countryRepository.save(newCountry);

                return newCountry;
            }
        }
        return null;
    }
}
