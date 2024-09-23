
package fr.projetspringgr2.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetspringgr2.models.Genre;
import fr.projetspringgr2.models.Role;
import fr.projetspringgr2.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;
    
    @Value("${genreService.importGenresFromJsonFile}")
    private String filePath;
    
    // Create
    public ResponseEntity<String> saveGenre(Genre newGenre) {
        Optional<Genre> existingGenre=genreRepository.findByName(newGenre.getName());
        if (existingGenre.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Genre already exists");
        }
        genreRepository.save(newGenre);

        return ResponseEntity.status(HttpStatus.CREATED).body("Genre created");
    }

    // Read all genres
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    // Read by ID
    public Genre getGenreById(int id) {
        if (genreRepository.existsById(id)){
            return genreRepository.findById(id).get();
        }
        return null;
    }

    // Update
    public ResponseEntity<String> updateGenre(int id, Genre genreDetails) {
        if (genreRepository.existsById(id)){
            Genre genre = genreRepository.findById(id).get();
            genre.setName(genreDetails.getName());
            genreRepository.save(genre);
            return ResponseEntity.status(HttpStatus.OK).body("Genre modified");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Genre not found");
    }

    // Delete
    public ResponseEntity<String> deleteGenre(int id) {
        if (genreRepository.existsById(id)){
            genreRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Genre deleted");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Genre not found");
    }

    public void saveRole(Role a) {
    }

    // Méthode pour lire un fichier JSON et ajouter les genres sans doublons
    public ResponseEntity<String> importGenresFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<String> genresToSave = new HashSet<>();  // Utiliser un Set pour éviter les doublons

        try {
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            for (JsonNode movieNode : rootNode) {
                JsonNode genresNode = movieNode.get("genres");
                if (genresNode != null) {
                    for (JsonNode genreNode : genresNode) {
                        String genreName = genreNode.asText();

                        // Ajouter le nom du genre au Set, ce qui évite automatiquement les doublons
                        genresToSave.add(genreName);
                    }
                }
            }

            // Pour chaque genre unique, vérifier s'il existe dans la base de données et l'ajouter s'il n'existe pas
            for (String genreName : genresToSave) {
                Optional<Genre> existingGenre = genreRepository.findByName(genreName);
                if (existingGenre.isEmpty()) {
                    Genre newGenre = new Genre();
                    newGenre.setName(genreName);
                    genreRepository.save(newGenre);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Genres imported successfully");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }
    
    public Genre importGenresFromJson(JsonNode genresNode) {
    	String genreName = genresNode.asText();
    	Optional<Genre> existingGenre = genreRepository.findByName(genreName);
        if (existingGenre.isEmpty()) {
            Genre newGenre = new Genre();
            newGenre.setName(genreName);
            return genreRepository.save(newGenre);
        }
        else {
        	return existingGenre.get();
        }
    }
}