package fr.projetspringgr2.services;

import com.fasterxml.jackson.databind.JsonNode;
import fr.projetspringgr2.models.MovieLanguage;
import fr.projetspringgr2.repositories.MovieLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Service class for managing MovieLanguage entities.
 */
@Service
public class MovieLanguageService {

    @Autowired
    private MovieLanguageRepository movieLanguageRepository;

    @Value("${genreService.importGenresFromJsonFile}")
    private String filePath;

    /**
     * Retrieve all MovieLanguage
     * @return List of all MovieLanguages
     */
    public List<MovieLanguage> getAllMovieLanguage(){
        List<MovieLanguage> movieLanguages = new ArrayList<>();
        movieLanguageRepository.findAll().forEach(movieLanguages::add);

        return movieLanguages;
    }


    /**
     * Retrieve MovieLanguage by id
     * @param id MovieLanguage
     * @return MovieLanguage
     */
    public MovieLanguage getById(Long id){
        if (movieLanguageRepository.existsById(id)) {
            return movieLanguageRepository.findById(id).get();
        }
        return null;

    }


    /**
     * Retrieve movieLanguage by name
     * @param name The name of the MovieLanguage.
     * @return ResponseEntity with the MovieLanguage details
     */
    public ResponseEntity<String> getMovieLanguageByName(String name){
        Optional<MovieLanguage> movieLanguage = movieLanguageRepository.findByName(name);
        if(movieLanguage.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(movieLanguage.get().toString());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune Langue trouvée avec : " + name);
    }


    /**
     * Adding a new MovieLanguage
     * @param movieLanguage
     * @return ResponseEntity with creation status or conflict if already exists
     */
    public ResponseEntity<String> saveMovieLanguage(MovieLanguage movieLanguage){
        if(movieLanguageRepository.findByName(movieLanguage.getName()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette Langue de Film existe déjà.");
        }
        movieLanguageRepository.save(movieLanguage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movieLanguage.toString());

    }


    /**
     * Edit a movieLanguage
     * @param id MovieLanguage to be updated
     * @param updateMovieLanguage the new object
     * @return ResponseEntity with update status or conflict if not found
     */
    public ResponseEntity<String> updateMovieLanguage(Integer id, MovieLanguage updateMovieLanguage){

        Optional<MovieLanguage> existingMovieLanguageOpt = movieLanguageRepository.findById((long) id);

        if (existingMovieLanguageOpt.isPresent()) {
            MovieLanguage existingMovieLanguage = existingMovieLanguageOpt.get();
            existingMovieLanguage.setName(updateMovieLanguage.getName());

            movieLanguageRepository.save(existingMovieLanguage);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Langue de film modifiée avec succès");

        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Langue de film avec cet id n'existe pas.");
    }


    /**
     * Delete MovieLanguage by id
     * @param id MovieLanguage
     * @return ResponseEntity with deletion status or conflict if not found
     */
    public ResponseEntity<String> deleteMovieLanguage(Long id){
        Optional<MovieLanguage> movieLanguage = movieLanguageRepository.findById(id);
        if(movieLanguage.isPresent()){
            movieLanguageRepository.delete(movieLanguage.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Langue de film supprimé avec succès");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Langue de film avec cet id n'existe pas.");
    }

    /**
     * Import a MovieLanguage from a JSON node.
     *
     * @param langueNode The JsonNode containing the language information.
     * @return The imported or existing MovieLanguage object, or null if not valid.
     */
    public MovieLanguage importMovieLanguageFromJsonNode(JsonNode langueNode) {
        if (langueNode != null && !langueNode.asText().trim().isEmpty()) {
            String languageName = langueNode.asText().trim();

            Optional<MovieLanguage> movieLanguageOpt = movieLanguageRepository.findByName(languageName);

            if (movieLanguageOpt.isPresent()) {
                MovieLanguage existingMovieLanguage = movieLanguageOpt.get();
                return existingMovieLanguage;
            } else {

                MovieLanguage newMovieLanguage = new MovieLanguage();
                newMovieLanguage.setName(languageName);
                newMovieLanguage = movieLanguageRepository.save(newMovieLanguage);

                return newMovieLanguage;
            }
        }
        return null;

    }


}
