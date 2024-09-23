package fr.projetspringgr2.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.models.Person;
import fr.projetspringgr2.models.Role;
import fr.projetspringgr2.repositories.PersonRepository;
import fr.projetspringgr2.repositories.RoleRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;


/**
 * Service class for generating PDF documents for movies.
 */
@Service
public class PdfService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;


    /**
     * Generates a PDF document containing details about a movie.
     *
     * @param movie    the movie for which the PDF should be generated
     * @param filePath the path where the generated PDF will be saved
     */
    public void generateMoviePdf(Movie movie, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        try {
            // Initialize PdfWriter to write to the specified file path
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Define font for the PDF
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            // Title
            document.add(new Paragraph("Title: " + movie.getName(), font));

            // Year
            document.add(new Paragraph("Year: " + movie.getYear(), font));

            // Synopsis
            document.add(new Paragraph("Synopsis: " + movie.getSynopsis(), font));

            //Movielanguage
            document.add(new Paragraph("Language: " + movie.getMovieLanguage().getName(), font));


            // Genres
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                String genres = movie.getGenres().stream()
                        .map(genre -> genre.getName())
                        .reduce((g1, g2) -> g1 + ", " + g2)
                        .orElse("Unknown");
                document.add(new Paragraph("Genres: " + genres, font));
            } else {
                document.add(new Paragraph("Genres: Not available", font));
            }

            // Principal Cast
            Set<Role> principalRoles = roleRepository.findByMovieIdAndPrincipalTrue(movie.getId());
            String principalCast = (principalRoles != null) ? principalRoles.stream()
                    .filter(role -> role.getActor() != null)
                    .map(role -> " - "  + role.getPersonnage())
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("Unknown") : "Unknown";
            document.add(new Paragraph("Principal Cast:\n" + principalCast, font));



            // Director
            Set<Person> directors = personRepository.findDirectorsByMovieId(movie.getId());
            if (directors != null && !directors.isEmpty()) {
                Person director = directors.iterator().next();
                String directorName = director.getName();
                document.add(new Paragraph("Director: " + directorName, font));
            } else {
                document.add(new Paragraph("Director: Unknown", font));
            }



        } finally {
            document.close();
        }
    }



}
