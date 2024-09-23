/*
package fr.projetspringgr2.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;


import com.fasterxml.jackson.databind.JsonNode;
import fr.projetspringgr2.models.Actor;


import fr.projetspringgr2.repositories.ActorRepository;
import fr.projetspringgr2.repositories.RoleRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityExistsException;


//@Service
public class ActorService {
	
	
	@Autowired
	private PersonService personService;
	@Autowired
	private ActorRepository actorRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Transactional
	public Actor extractActor(JsonNode actorJSON) {
		Actor actor = extractActor(actorJSON.get("id").asText());
		if (actor == null) {
			actor = new Actor(personService.extractPerson(actorJSON));
			actor.setIdPerson(actorJSON.get("id").asText());
			if (actorJSON.has("height")) {
				String height = actorJSON.get("height").asText();
				Pattern stringPattern = Pattern.compile("[1-9][.,][1-9]+");
			    Matcher matcher = stringPattern.matcher(height);
			    boolean matchFound = matcher.find();
			    if(matchFound) {
			    	height = matcher.group(0);
					actor.setSize(Double.parseDouble(height.replace(",",".")));
			    }
			}
			if (personService.getPersonById(actorJSON.get("id").asText()) == null) {
				actor = actorRepository.save(actor);
			}
		}
		return actor;
	}
	
	public List<Actor> extractActors() {
		try {
			return actorRepository.findBy();
		}
		catch (NoResultException e) {
			return null;
		}
	}
	
	public Actor extractActor(String id) {
		try {
			return actorRepository.findById(id).get();
		}
		catch(NoSuchElementException e) {
			return null;
		}
	}
	
	@Transactional
	public boolean saveActor(Actor actor) {
		if (!actorRepository.existsById(actor.getIdPerson())) {
			personService.savePerson(actor);
			actorRepository.save(actor);
			return true;
		}
		return false;
	}
	
	@Transactional
	public boolean updateActor(String id, Actor actorUpdated) {
		if (actorRepository.existsById(id) && personService.updatePerson(id, actorUpdated)) {
			Actor actor = actorRepository.findById(id).get();
			actor.setSize(actorUpdated.getSize());
			actorRepository.save(actor);
			return true;
		}
		return false;
	}
	
	@Transactional
	public ResponseEntity<String> deleteActor(String id) {
		if (actorRepository.existsById(id)) {
			Actor actor = actorRepository.findById(id).get();
			if (roleRepository.findByActor(actor).size() > 0) {
				return ResponseEntity.badRequest().body("This actor has roles !");
			}
			actorRepository.delete(actor);
			return ResponseEntity.ok("Success !");
		}
		return ResponseEntity.badRequest().body("This actor already exists !");
	}

}
*/