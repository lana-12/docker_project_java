package fr.projetspringgr2.services;

import fr.projetspringgr2.models.Person;
import fr.projetspringgr2.models.Place;
import fr.projetspringgr2.repositories.PersonRepository;
import jakarta.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The `PersonService` class provides services related to managing `Person` entities.
 * This class handles operations such as extracting person details from JSON data,retrieving, saving, updating, and deleting `Person` objects in the database.
 * */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PlaceService placeService;

	/**Extracts a `Person` entity from a given JSON node.
	 *  If the person already exists in the database,
     * the existing entity is returned. Otherwise, a new `Person` entity is created, populated with the data from the JSON, and saved to the database.
     * @param personJSON the JSON node containing person data.
     * @return the extracted and saved `Person` entity.
     */
	public Person extractPerson(JsonNode personJSON) {
		Person person = getPersonById(personJSON.get("id").asText());
		if (person == null) {
			person = new Person();
			person.setIdPerson(personJSON.get("id").asText());
			String[] identite = personJSON.get("identite").asText().split(" ");
			if (identite.length == 1) {
				person.setName(identite[0]);
			}
			else if (identite.length == 2) {
				person.setName(identite[0]);
				person.setSurname(identite[1]);
			}
			else if (identite.length == 3 && identite[2].equals("Jr.")) {
				person.setName(identite[0] + " " + identite[2]);
				person.setSurname(identite[1]);
			}
			else if (identite.length == 3 && identite[2].equals("De")) {
				person.setName(identite[0]);
				person.setSurname(identite[1] + " " + identite[2]);
			}
			else {
				person.setName(identite[0] + " " + identite[1]);
				person.setSurname(identite[2]);
			}
			
			if (personJSON.has("height")) {
				String height = personJSON.get("height").asText();
				Pattern stringPattern = Pattern.compile("[1-9][.,][1-9]+");
			    Matcher matcher = stringPattern.matcher(height);
			    boolean matchFound = matcher.find();
			    if(matchFound) {
			    	height = matcher.group(0);
					person.setSize(Double.parseDouble(height.replace(",",".")));
			    }
			}
			
			JsonNode naissance = personJSON.get("naissance");
			if (!naissance.get("dateNaissance").asText().equals("")) {
				String date = naissance.get("dateNaissance").asText();
				if (date.split(" ").length == 3) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy ", Locale.ENGLISH);
					LocalDate localDate;
					try {
						localDate = LocalDate.parse(date, formatter);
					}
					catch(DateTimeParseException e) {
						formatter = DateTimeFormatter.ofPattern("d MMMM yyyy ", Locale.FRANCE);
						localDate = LocalDate.parse(date, formatter);
					}
					person.setBirthDate(localDate);
				}
				
			}
			if (!naissance.get("lieuNaissance").asText().equals("")) {
				
				Place place = placeService.importPlaceFromBirthJsonNode(naissance);
				person.setPlace(place);
			}
			person = personRepository.save(person);
		}
		return person;
	}


	/** Method to get all persons (actors/actresses and directors)
	 **/

	public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

	/** Method to insert a new person (actor/actress or director) into the database.
	 * If the person already exists,the existing entity is returned.
	 * @param person the `Person` entity to save.
	 * @return the saved or existing `Person` entity.
	 **/
	public Person savePerson(Person person) {
    	Optional<Person> optionalPerson = personRepository.findById(person.getIdPerson());

        if (optionalPerson.isPresent()) {
        	 return optionalPerson.get();
        }
        return personRepository.save(person);
    }

	/** Method to update an existing person (actor/actress or director)
	 * @param id the identifier of the person to update.
	 * @param personDetails the new details for the person.
	 * @return `true` if the update was successful, `false` otherwise.
	 */
	public boolean updatePerson(String id, Person personDetails) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setName(personDetails.getName());
            person.setSurname(personDetails.getSurname());
            person.setBirthDate(personDetails.getBirthDate());
            person.setPlace(personDetails.getPlace());
            person.setSize(personDetails.getSize());
            personRepository.save(person);
            return true;
        } else {
            return false;
        }
    }

	/**Method to get a person by their unique identifier.
	 * @param id the identifier of the person to retrieve.
	 * @return the `Person` entity, or `null` if no person with the given ID exists.
	 **/
    public Person getPersonById(String id) {
    	if (personRepository.existsById(id)) {
    		return personRepository.findById(id).get();
    	}
    	return null;
    }

	/** Deletes a person from the database based on their unique identifier.
	 * This operation is transactional to ensure database integrity.
	 * @param id the identifier of the person to delete.
	 * @return a `ResponseEntity` indicating success or failure of the operation.
	 **/
    @Transactional
	public ResponseEntity<String> deletePerson(String id) {
		if (personRepository.existsById(id)) {
			Person person = personRepository.findById(id).get();
			
			personRepository.delete(person);
			return ResponseEntity.ok("Success !");
		}
		return ResponseEntity.badRequest().body("This actor already exists !");
	}

	/** Method to get  a list of actors who are common between two specified movies.
	 * @param idMovie1 the identifier of the first movie.
	 * @param idMovie2 the identifier of the second movie.
	 * @return a list of `Person` entities who acted in both movies.
	 **/
    public List<Person> getActorsByMovies(int idMovie1, int idMovie2) {
    	return personRepository.findActorsByTwoMovies(idMovie1, idMovie2);
    }
}