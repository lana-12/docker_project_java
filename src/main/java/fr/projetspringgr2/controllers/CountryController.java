package fr.projetspringgr2.controllers;

import fr.projetspringgr2.models.Country;
import fr.projetspringgr2.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    private final CountryService countryService;

    /**
     * Constructeur pour injecter le service `CountryService`.
     *
     * @param countryService le service pour gérer les opérations liées aux pays
     */
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Crée un nouveau pays.
     *
     * @param country l'objet `Country` à créer
     * @return une réponse HTTP indiquant le succès ou l'échec de la création
     */
    @PostMapping
    public ResponseEntity<String> createCountry(@RequestBody Country country) {
        return countryService.saveCountry(country);
    }

    /**
     * Récupère tous les pays.
     *
     * @return une liste de tous les pays disponibles
     */
    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    /**
     * Récupère un pays par son ID.
     *
     * @param id l'ID du pays à récupérer
     * @return l'objet `Country` correspondant à l'ID, ou null s'il n'existe pas
     */
    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Integer id) {
        return countryService.getCountryById(id);
    }

    /**
     * Met à jour un pays existant par son ID.
     *
     * @param id              l'ID du pays à mettre à jour
     * @param countryDetails  l'objet `Country` contenant les nouvelles informations
     * @return une réponse HTTP indiquant le succès ou l'échec de la mise à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCountry(@PathVariable Integer id, @RequestBody Country countryDetails) {
        return countryService.updateCountry(id, countryDetails);
    }

    /**
     * Supprime un pays par son ID.
     *
     * @param id l'ID du pays à supprimer
     * @return une réponse HTTP indiquant le succès ou l'échec de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable Integer id) {
        return countryService.deleteCountry(id);
    }
}
