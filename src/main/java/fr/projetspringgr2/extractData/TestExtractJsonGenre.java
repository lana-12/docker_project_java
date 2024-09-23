package fr.projetspringgr2.extractData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetspringgr2.models.Genre;
import fr.projetspringgr2.repositories.GenreRepository;
import fr.projetspringgr2.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for importing genres from a JSON file.
 */
@Service
public class TestExtractJsonGenre {

    /**
     * Repository pour les opérations CRUD sur l'entité Genre.
     */
    @Autowired
    private GenreRepository genreRepository;

    /**
     * Service pour gérer la logique métier liée aux genres.
     */
    @Autowired
    private GenreService genreService;

    /**
     * Chemin du fichier JSON, récupéré à partir des propriétés de l'application.
     */
    @Value("${file.path}")
    private String filePath;

    /**
     * Constructor for TestExtractJsonGenre.
     *
     * @param genreRepository Repository for managing Genre entities.
     * @param genreService Service for handling Genre operations.
     */
    public TestExtractJsonGenre(GenreRepository genreRepository, GenreService genreService) {
        this.genreRepository = genreRepository;
        this.genreService = genreService;
    }

    /**
     * Imports genres from a specified JSON file.
     *
     * @param filePath The path to the JSON file containing genre data.
     */
    public void importGenresFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();  // Utilisé pour lire et convertir les données JSON
        List<Genre> genres = new ArrayList<>();  // Liste pour stocker les genres extraits

        try {
            // Lecture du fichier JSON et conversion en un arbre d'objets JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Parcours de chaque nœud de film dans le fichier JSON
            for (JsonNode movieNode : rootNode) {

                // Récupération du nœud "genres" pour chaque film
                JsonNode genresNode = movieNode.get("genres");
                if (genresNode != null) {

                    // Extraction du nom du genre sous forme de texte
                    String genreName = genresNode.asText().trim();

                    // Vérification si le genre existe déjà dans la base de données
                    Optional<Genre> genreOb = genreRepository.findByName(genreName);
                    if (genreOb.isEmpty()) {
                        // Si le genre n'existe pas, création d'un nouvel objet Genre et sauvegarde dans la base de données
                        Genre genre = new Genre();
                        genre.setName(genreName);
                        genreRepository.save(genre);
                        genres.add(genre);  // Ajout du genre à la liste pour un traitement ultérieur
                    }
                }
            }


            // Sauvegarde de tous les genres extraits dans la base de données
            genreRepository.saveAll(genres);

        } catch (IOException e) {
            e.printStackTrace();  // Gestion des exceptions en cas d'erreur lors de la lecture du fichier JSON
        }
    }
}
