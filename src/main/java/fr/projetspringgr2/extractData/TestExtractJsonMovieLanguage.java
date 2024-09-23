package fr.projetspringgr2.extractData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetspringgr2.models.MovieLanguage;
import fr.projetspringgr2.repositories.MovieLanguageRepository;
import fr.projetspringgr2.services.MovieLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for importing movie languages from a JSON file.
 */
@Service
public class TestExtractJsonMovieLanguage {

    @Autowired
    private MovieLanguageRepository movieLanguageRepository;

    @Autowired
    private MovieLanguageService movieLanguageService;

    @Value("${file.path}")
    private String filePath;

    /**
     * Constructor for TestExtractJsonMovieLanguage.
     *
     * @param movieLanguageRepository Repository for managing MovieLanguage entities.
     * @param movieLanguageService Service for handling MovieLanguage operations.
     */
    public TestExtractJsonMovieLanguage(MovieLanguageRepository movieLanguageRepository, MovieLanguageService movieLanguageService) {
        this.movieLanguageRepository = movieLanguageRepository;
        this.movieLanguageService = movieLanguageService;
    }

    /**
     * Imports movie languages from a specified JSON file.
     *
     * @param filePath The path to the JSON file containing movie language data.
     */
    public void importMovieLanguagesFromJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<MovieLanguage> movieLanguages = new ArrayList<>();
        try {

            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            for (JsonNode movieNode : rootNode) {
                JsonNode languagesNode = movieNode.get("langue");
                if (languagesNode != null) {
                    String languageName = languagesNode.asText().trim();


                    Optional<MovieLanguage> movieLanguageOb = movieLanguageRepository.findByName(languageName);
                    if (movieLanguageOb.isEmpty()) {
                        MovieLanguage movieLanguage = new MovieLanguage();
                        movieLanguage.setName(languageName);
                        movieLanguageRepository.save(movieLanguage);
                        movieLanguages.add(movieLanguage);
                    }
                }
            }

            movieLanguageRepository.saveAll(movieLanguages);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
