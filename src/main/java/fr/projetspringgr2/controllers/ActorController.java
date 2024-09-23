/*
package fr.projetspringgr2.controllers;

import fr.projetspringgr2.models.Actor;
import fr.projetspringgr2.services.ActorService;

import java.util.List;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/actors")
public class ActorController {

	//@Autowired
	//private ActorService actorService;

	@GetMapping
	public List<Actor> getActors(){
		return (List<Actor>) actorService.extractActors();
	}

	@GetMapping(path = "/{id}")
	public Actor getActor(@PathVariable String id){
		return actorService.extractActor(id);
	}

	
	
	

	@PostMapping
	public ResponseEntity<String> insertActor(@RequestBody Actor actor){
		if (actorService.saveActor(actor)) {
			return ResponseEntity.ok("Success !");
		}
		return ResponseEntity.badRequest().body("This actor does not exists !");
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<String> updateActor(@PathVariable String id, @RequestBody Actor actor){
		if (actorService.updateActor(id, actor)) {
			return ResponseEntity.ok("Success !");
		}
		return ResponseEntity.badRequest().body("This actor does not exists !");
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteActor(@PathVariable String id){
		return actorService.deleteActor(id);

	}
}
*/