package fr.projetspringgr2.controllers;

import com.itextpdf.text.DocumentException;
import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.repositories.MovieRepository;
import fr.projetspringgr2.services.MovieService;
import fr.projetspringgr2.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class for handling PDF generation requests related to movies.
 */
@RestController
public class PdfController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;


    /**
     * Endpoint to generate a PDF for a specific movie.
     *
     * @param id the ID of the movie for which the PDF should be generated
     * @return a message indicating the success or failure of PDF generation
     * It generates it at the root of the project.
     */
    @GetMapping("/generateMoviePdf/{id}")
    public String generatePdf(@PathVariable Integer id) {
        String filePath = "movie_" + id + ".pdf";

        try {
            Optional<Movie> movieOpt = movieRepository.findById(id);

            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();
                pdfService.generateMoviePdf(movie, filePath);
                return "PDF généré avec succès : " + filePath;
            } else {
                return "Film avec l'ID " + id + " non trouvé.";
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return "Error  génération du PDF : " + e.getMessage();
        }
    }
}
