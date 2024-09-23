package fr.projetspringgr2.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.models.Person;
import fr.projetspringgr2.models.Role;

public interface RoleRepository  extends JpaRepository<Role, Integer>{
	
	List<Role> findBy();
	
	Optional<Role> findById(int id);
	
	List<Role> findByActor(Person actor);
	
	List<Role> findByMovie(Movie movie);

	Optional<Role> findByActorAndMovie(Person actor, Movie movie);
	
	List<Role> findByPrincipalAndMovie(boolean principal, Movie movie);

	Set<Role> findByMovieIdAndPrincipalTrue(Integer movieId);
}
