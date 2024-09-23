package fr.projetspringgr2.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.projetspringgr2.models.*;
import fr.projetspringgr2.repositories.MovieRepository;
import jakarta.persistence.NoResultException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing a Movie (Movie)
 */
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CountryService countryService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private MovieLanguageService movieLanguageService;

    @Autowired
    private PersonService personService;
    @Autowired
    private RoleService roleService;

    @Value("${file.path}")
    private String filePath;

    /**
     * Constructor with parameter : movieRepository
     */
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }



    public List<String> getAllMovieNames() {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);

        // Utilisation des streams pour extraire les noms
        return movies.stream()
                .map(Movie::getName)  // Extraction du champ "name" de chaque Movie
                .collect(Collectors.toList());
    }

    /**
     * read and extract movie data from films.json
     */
    public void importMoviesFromJsonFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Movie> movies = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(filePath));


            for (JsonNode movieNode : rootNode) {

                String id = movieNode.get("id").asText();
                String name = movieNode.get("nom").asText();
                String rating = movieNode.get("rating").asText();
                String synopsis = movieNode.get("plot").asText();
                String year = movieNode.get("anneeSortie").asText();

                Movie movie = new Movie();
                movie.setIdMovie(id);
                movie.setName(name);
                movie.setRating(extractRating(rating));
                movie.setSynopsis(synopsis);
                movie.setYear( extractYear(year));


                movie.setCountry(countryService.importCountryFromJson(movieNode.get("pays")));
                movie.setMovieLanguage(movieLanguageService.importMovieLanguageFromJsonNode(movieNode.get("langue")));
                movie.setPlace(placeService.importPlaceFromJsonNode(movieNode.get("lieuTournage")));

                HashSet<Genre> genres= new HashSet<Genre>();
                for(JsonNode o : movieNode.get("genres") ){
                    if(!genres.contains(o.asText()));
                    {
                        genres.add(genreService.importGenresFromJson(o));
                    }
                }
                movie.setGenres(genres);

                HashSet<Person> persons = new HashSet<Person>();
                for(JsonNode o : movieNode.get("realisateurs") ){
                    persons.add(personService.extractPerson(o));
                }
                movie.setPersons(persons);

                movie = movieRepository.save(movie);

                for(JsonNode o : movieNode.get("roles") ){
                	roleService.extractRole(o, movie);
                }
                for(JsonNode o : movieNode.get("castingPrincipal") ){
                	roleService.extractCastingPrincipal(o, movie);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve rating (double) from rating (String)
     * @param rating String extraction of rating
     * @return double
     */
    public double extractRating(String rating) {
        if(Objects.equals(rating, "")){
            return -1;
        }
        if(rating.contains(",")){
            rating = rating.replace(",",".");
        }
        return Double.parseDouble(rating);
    }

    /**
     * Retrieve year (int) from year (String)
     * @param year String extraction of year
     * @return year
     */
    public int extractYear(String year) {
        if(year.contains("–")){
            String[] years = year.split("–" , 2);
            return Integer.parseInt(years[1]);
        }
        else{
            return Integer.parseInt(year);
        }
    }

    /**
     * save movie in Database
     * @param movie Movie to be saved
     * @return true if created, false if not
     */
    public boolean saveMovie(Movie movie) {
        Movie result = null;

        try {
            result = movieRepository.findByNameAndYearAndRating(movie.getName(),movie.getYear(),movie.getRating());
        } catch (NoSuchElementException nsee) {
        }

        if(movie.getCountry() != null){
            countryService.saveCountry(movie.getCountry());
        }
        if(movie.getPlace() != null)
        {
            placeService.savePlace(movie.getPlace());
        }
        if(movie.getMovieLanguage() != null)
        {
            movieLanguageService.saveMovieLanguage(movie.getMovieLanguage());
        }
        if(movie.getGenres() != null)
        {
            for(Genre g : movie.getGenres()){
                genreService.saveGenre(g);
            }
        }
        if(movie.getPersons() != null)
        {
            for(Person p : movie.getPersons()){
                personService.savePerson(p);
            }
        }

        if(movie.getRoles() != null)
        {
            for(Role a : movie.getRoles()){
                roleService.saveRole(a);
            }
        }

        if (result != null) {
            return false;
        } else {
            movieRepository.save(movie);
            return true;
        }
    }

    /**
     * update the movie at id  with data from movie
     * @param  id id of the movie to be updated
     * @param movie Movie for update
     * @return true if update succesfull, false if not
     */
    public boolean updateMovie(int id,Movie movie) {
        Movie result = null;
        try {
            result = movieRepository.findById(id).get();
            result.setName(movie.getName());
            result.setRating(movie.getRating());
            result.setYear(movie.getYear());
            result.setSynopsis(movie.getSynopsis());
            result.setIdMovie(movie.getIdMovie());


            if (movie.getMovieLanguage() != null)
            {
                movieLanguageService.saveMovieLanguage(movie.getMovieLanguage());
                result.setMovieLanguage(movie.getMovieLanguage());
            }

            if(movie.getPlace() != null)
            {
                placeService.savePlace(movie.getPlace());
                result.setPlace(movie.getPlace());
            }

            if(movie.getCountry() != null)
            {
                countryService.saveCountry(movie.getCountry());
                result.setCountry(movie.getCountry());
            }

            if(movie.getGenres() != null)
            {
                for(Genre g : movie.getGenres()){
                    genreService.saveGenre(g);
                }
                result.setGenres(movie.getGenres());
            }


            if(movie.getRoles() != null)
            {
                for(Role r : movie.getRoles()){
                    roleService.saveRole(r);
                }
                result.setRoles(movie.getRoles());
            }

            if(movie.getPersons() != null)
            {
                for(Person p : movie.getPersons()){
                    personService.savePerson(p);
                }
                result.setPersons(movie.getPersons());
            }

            movieRepository.save(result);
            return true;
        } catch (NoResultException nre) {
            return false;
        }

    }

    /**
     * delete the movie at  id
     * @param  id id of the movie to be deleted
     * @return true if delete succesfull, false if not
     */
    public boolean deleteMovie(int id) {
        Optional<Movie> result = null;
        try {
            result = movieRepository.findById(id);
        } catch (NoSuchElementException nsee) {
            return false;
        }
        movieRepository.deleteById(id);
        return true;
    }

    /**
     * get all movies between two years
     * @param  year1 first year
     * @param  year1 second year
     * @return Movie List
     */
    public HashSet<Movie> getMoviesByYearBetween(int year1, int year2) {
        HashSet<Movie> result = new HashSet<>();
        try {
            result = movieRepository.findByYearBetween(year1,year2);
        } catch (NoSuchElementException nsee) {
        }
        return result;
    }

    /**
     * get all movies for an actor
     * @param  name the actors name
     * @param  surname the actors surname
     * @return Movie List
     */
    public HashSet<Movie> getMoviesByActor(String name, String surname) {
        HashSet<Movie> result = new HashSet<>();
        try {
            result = movieRepository .findByRolesActorNameAndRolesActorSurname(name,surname);
        } catch (NoSuchElementException nsee) {
        }
        return result;
    }

    /**
     * get all movies that contains a Role
     * @param  personnage Role name
     * @return Movie List
     */
    public HashSet<Movie> getMoviesByRole(String personnage) {
        HashSet<Movie> result = new HashSet<>();
        try {
            result = movieRepository .findByRolesPersonnage(personnage);
        } catch (NoSuchElementException nsee) {
        }
        return result;
    }

    /**
     * get all movies between two years for an actor
     * @param  yearMin first year
     * @param  yearMax second year
     * @param name the actors name
     * @param surname the actors surname
     * @return Movie List
     */
    public HashSet<Movie> getMoviesByYearBetweenAndActor(int yearMin, int yearMax, String name, String surname) {
        HashSet<Movie> result = new HashSet<>();
        try {
            result = movieRepository .findByYearBetweenAndRolesActorNameAndRolesActorSurname(yearMin,yearMax,name,surname);
        } catch (NoSuchElementException nsee) {
        }
        return result;
    }

    /**
     * get all movies between two years for a director
     * @param  yearMin first year
     * @param  yearMax second year
     * @param idPerson the directors id
     * @return Movie List
     */
    public HashSet<Movie> getMoviesByYearBetweenAndDirector(int yearMin, int yearMax, String idPerson) {
        HashSet<Movie> result = new HashSet<>();
        try {
            result = movieRepository .findByYearBetweenAndPersonsIdPerson(yearMin,yearMax,idPerson);
        } catch (NoSuchElementException nsee) {
        }
        return result;
    }

    /**
     * get all movies shared by two actors
     * @param  id_actor1 first actors id
     * @param  id_actor2 second actors id
     * @return Movie List
     */
    public HashSet<Movie> getMoviesByTwoActors(String id_actor1, String id_actor2) {
        HashSet<Movie> result = new HashSet<>();
        try {
            result = movieRepository .findMoviesByTwoActors(id_actor1,id_actor2);
        } catch (NoSuchElementException nsee) {
        }
        return result;
    }

}
