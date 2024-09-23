
package fr.projetspringgr2.controllers;

import java.util.List;

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

import fr.projetspringgr2.models.Movie;
import fr.projetspringgr2.models.Role;
import fr.projetspringgr2.services.RoleService;

/**
 * REST controller for managing `Role` entities.
 * Provides endpoints for retrieving, creating, updating, and deleting roles, as well as retrieving roles by movie.
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * Retrieves a list of all roles.
	 * @return a list of `Role` objects.
	 **/
	@GetMapping
	public List<Role> getRoles(){
		return (List<Role>) roleService.extractRoles();
	}

	/**
	 * Retrieves a specific role by its ID.
	 * @param id the ID of the role to retrieve.
	 * @return the `Role` object corresponding to the specified ID.
	 **/
	@GetMapping(path = "/{id}")
	public Role getRole(@PathVariable int id){
		return roleService.extractRole(id);
	}

	/**
	 * Inserts a new role.
	 * @param role the `Role` object to insert.
	 * @return a `ResponseEntity` containing a success or failure message.
	 **/
	@PostMapping
	public ResponseEntity<String> insertRole(@RequestBody Role role){
		return roleService.saveRole(role);
	}

	/**
	 * Updates an existing role.
	 * @param id the ID of the role to update.
	 * @param role the updated `Role` object.
	 * @return a `ResponseEntity` containing a success or failure message.
	 */
	@PutMapping(path = "/{id}")
	public ResponseEntity<String> updateRole(@PathVariable int id, @RequestBody Role role){
		return roleService.updateRole(id, role);

	}

	/**
	 * Deletes a specific role by its ID.
	 * @param id the ID of the role to delete.
	 * @return a `ResponseEntity` containing a success or failure message.
	 */
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable int id){
		return roleService.deleteRole(id);

	}
	/**
	 * Retrieves a list of all roles associated with a specific movie.
	 * @param movie the `Movie` object for which to retrieve the roles.
	 * @return a list of `Role` objects associated with the specified movie.
	 **/
	@GetMapping(path = "/movie")
	public List<Role> getRoleByMovie(@RequestBody Movie movie){
		return roleService.getRoleByMovie(movie);
	}

	/**
	 * Retrieves a list of principal roles (main roles) associated with a specific movie.
	 * @param movie the `Movie` object for which to retrieve the principal roles.
	 * @return a list of `Role` objects that are marked as principal and associated with the specified movie.
	 **/
	@GetMapping(path = "/principal/movie")
	public List<Role> getCastingPrincipalByMovie(@RequestBody Movie movie){
		return roleService.getCastingPrincipalByMovie(movie);
	}
}
