package fr.projetspringgr2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.models.Person;
import fr.projetspringgr2.models.Role;
import fr.projetspringgr2.repositories.RoleRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

import org.json.*;

/** The `RoleService` class provides services for managing roles in a movie, including extracting, saving, updating,
 * and deleting roles. It interacts with the `RoleRepository` to perform database operations and the `PersonService` to manage associated actors
 **/
@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PersonService personService;

	/**Method to extract a role from the given JSON data and associates it with the specified movie.
	 * If the role already exists in the database, it is returned; otherwise, a new role is created and saved.
	 * @param roleJSON the JSON data representing the role.
	 * @param movie the movie to associate the role with.
	 * @return the extracted or saved `Role` object.
	 **/
	public Role extractRole(JsonNode roleJSON, Movie movie) {
		Person actor = personService.extractPerson(roleJSON.get("acteur"));
		Role role = new Role();
		Optional<Role> optRole = roleRepository.findByActorAndMovie(actor, movie);
		if (optRole.isPresent()) {
			role = optRole.get();
		}
		else {
			role.setMovie(movie);
			role.setActor(actor);
		}
		role.setPersonnage(roleJSON.get("characterName").asText());
		return roleRepository.save(role);
	}

	/**  Method to extract a principal role (main role) from the given JSON data and associates it with the specified movie.
	 * If the role already exists, it is returned; otherwise, a new role is created, marked as principal, and saved.
	 * @param castingPrincipalJSON the JSON data representing the principal role.
	 * @param movie the movie to associate the role with.
	 * @return the extracted or saved `Role` object.
	 **/
	public Role extractCastingPrincipal(JsonNode castingPrincipalJSON, Movie movie) {
		Person actor = personService.extractPerson(castingPrincipalJSON);
		Role role = new Role();
		Optional<Role> optRole = roleRepository.findByActorAndMovie(actor, movie);
		if (optRole.isPresent()) {
			role = optRole.get();
		}
		else {
			role.setMovie(movie);
			role.setActor(actor);
		}
		role.setPrincipal(true);
		saveRole(role);
		return roleRepository.save(role);
	}

	/**Get all roles from the database. If no roles are found, returns null.
	 * @return a list of all roles, or null if no roles are found.
	 **/
	public List<Role> extractRoles() {
		try {
			return roleRepository.findBy();
		}
		catch (NoResultException e) {
			return null;
		}
	}

	/**Get a role by its ID. If the role does not exist, returns null.
	 * @param id the ID of the role.
	 * @return the `Role` object, or null if the role does not exist.
	 **/
	public Role extractRole(int id) {
		if (roleRepository.existsById(id)) {
			return roleRepository.findById(id).get();
		}
		return null;
	}

	/** Get a role by the associated actor and movie. If the role does not exist, returns null.
	 * @param actor the actor associated with the role.
	 * @param movie the movie associated with the role.
	 * @return the `Role` object, or null if the role does not exist.
	 **/
	public Role extractRoleByActorAndMovie(Person actor, Movie movie) {
		Optional<Role> role = roleRepository.findByActorAndMovie(actor, movie);
		if (role.isPresent()) {
			return role.get();
		}
		return null;
	}

	/** Saves a role to the database. If the role already exists, it returns a bad request response.
	 * If the role does not exist, it saves the role and returns a success response.
	 * @param role the role to be saved.
	 * @return a `ResponseEntity` indicating success or failure.
	 **/
	@Transactional
	public ResponseEntity<String> saveRole(Role role) {
		if (roleRepository.findByActorAndMovie(role.getActor(),role.getMovie()).isPresent()) {
			if (!roleRepository.existsById(role.getId())) {
				personService.savePerson(role.getActor());
				roleRepository.save(role);
				return ResponseEntity.ok("Success !");
			}
			return ResponseEntity.badRequest().body("A role whith that id already exists !");
		}
		return ResponseEntity.badRequest().body("That role already exists !");
	}

	/**Updates an existing role with new details. If the role does not exist, returns a bad request response.
	 * @param id the ID of the role to be updated.
	 * @param roleUpdated the updated role details.
	 * @return a `ResponseEntity` indicating success or failure.
	 **/
	@Transactional
	public ResponseEntity<String> updateRole(int id, Role roleUpdated) {
		if (roleRepository.existsById(id)) {
			Role role = roleRepository.findById(id).get();
			if (personService.getPersonById(roleUpdated.getActor().getIdPerson()) == null) {
				role.setActor(personService.savePerson(roleUpdated.getActor()));
			}
			else {
				personService.updatePerson(roleUpdated.getActor().getIdPerson(), roleUpdated.getActor());
				role.setActor(personService.getPersonById(roleUpdated.getActor().getIdPerson()));
			}
			
			role.setPersonnage(roleUpdated.getPersonnage());
			role.setPrincipal(roleUpdated.getPrincipal());
			roleRepository.save(role);
			return ResponseEntity.ok("Success !");
		}
		return ResponseEntity.badRequest().body("This role does not exists !");
	}
/**Deletes a role from the database by its ID. If the role does
 * **/
	@Transactional
	public ResponseEntity<String> deleteRole(int id) {
		if (roleRepository.existsById(id)) {
			roleRepository.deleteById(id);
			return ResponseEntity.ok("Success !");
		}
		return ResponseEntity.badRequest().body("This role does not exists !");
	}

	/**
	 * Retrieves a list of principal roles (main roles) associated with a given movie.
	 * A principal role is identified by the `principal` flag being set to `true`.
	 * @param movie the `Movie` object for which to retrieve the principal roles.
	 * @return a list of `Role` objects that are marked as principal and associated with the specified movie.
	 **/
	public List<Role> getCastingPrincipalByMovie(Movie movie) {
		return roleRepository.findByPrincipalAndMovie(true, movie);
	}

	/**
	 * Retrieves a list of all roles associated with a given movie, regardless of whether they are principal roles.
	 * @param movie the `Movie` object for which to retrieve the roles.
	 * @return a list of `Role` objects associated with the specified movie.
	 */
	public List<Role> getRoleByMovie(Movie movie) {
		return roleRepository.findByMovie(movie);
	}
}
