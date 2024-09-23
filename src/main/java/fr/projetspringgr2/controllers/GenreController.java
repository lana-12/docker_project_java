package fr.projetspringgr2.controllers;

import fr.projetspringgr2.models.Genre;
import fr.projetspringgr2.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * La classe {@code GenreController} est un contrôleur REST pour gérer les opérations liées aux genres de films.
 *
 * <p>Ce contrôleur permet de créer, lire, mettre à jour et supprimer des genres,
 * ainsi que de récupérer tous les genres disponibles dans le système.</p>
 *
 * <p>Il est mappé au chemin "/genre" et répond aux différentes requêtes HTTP pour manipuler les données de genre.</p>

 *
 * Auteur : Votre Nom
 */
@RestController
@RequestMapping("/genre")
public class GenreController {

    /**
     * Le service de gestion des genres qui contient la logique métier.
     */
    @Autowired
    private final GenreService genreService;

    /**
     * Constructeur pour injecter le service de genre.
     *
     * @param genreService le service de genre à injecter
     */
    public GenreController(GenreService genreService){
        this.genreService = genreService;
    }

    /**
     * Crée un nouveau genre.
     *
     * @param genre l'objet {@code Genre} à créer
     * @return une réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @PostMapping
    public ResponseEntity<String> createGenre(@RequestBody Genre genre) {
        return genreService.saveGenre(genre);
    }

    /**
     * Récupère tous les genres.
     *
     * @return une liste de tous les genres
     */
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    /**
     * Récupère un genre par son identifiant.
     *
     * @param id l'identifiant du genre à récupérer
     * @return l'objet {@code Genre} correspondant à l'identifiant, ou {@code null} si non trouvé
     */
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genreService.getGenreById(id);
    }

    /**
     * Met à jour un genre existant par son identifiant.
     *
     * @param id l'identifiant du genre à mettre à jour
     * @param genreDetails les détails mis à jour du genre
     * @return une réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateGenre(@PathVariable int id, @RequestBody Genre genreDetails) {
        return genreService.updateGenre(id, genreDetails);
    }

    /**
     * Supprime un genre par son identifiant.
     *
     * @param id l'identifiant du genre à supprimer
     * @return une réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable int id) {
        return genreService.deleteGenre(id);
    }

    /*
    // Méthode pour importer des genres depuis un fichier JSON
    @GetMapping("/json")
    public String importGenres(){
        genreService.importGenresFromJsonFile("C:\\Users\\lenovo\\films.json");
        return "success";
    }
    */
}
