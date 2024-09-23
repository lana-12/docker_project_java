package fr.projetspringgr2.models;


import jakarta.persistence.*;

/** The `Role` class represents a role that a person (an actor or actress) plays in a movie.
 * It maps to the `role` table in the database and contains information about the character,
 * It contains fields that map to the columns of the `role` table and relationships to the `Person` and `Movie` entities using the `@ManyToOne` annotation.
 **/
@Entity
@Table(name="role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String personnage;

	/** An indicator of whether this role is a principal (main) role **/
	private boolean principal;

	/**This is a many-to-one relationship with the `Person` entity,
	 * It is mapped to the `id_person` column in the `role` table.
	 **/
	@ManyToOne 
	@JoinColumn(name="id_person") 
	private Person actor;

	/**The movie in which this role appears.
	 * It is a many-to-one relationship with the `Movie` entity,and is mapped to the `id_movie` column in the `role` table.
	 **/
	@ManyToOne 
	@JoinColumn(name="id_movie") 
	private Movie movie;

	/**No-argument constructor required by JPA.**/
	public Role() {}

	/** GETTERS and SETTERS**/
	public int getId() {
		return id;
	}

	public String getPersonnage() {
		return personnage;
	}

	public void setPersonnage(String personnage) {
		this.personnage = personnage;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public boolean getPrincipal() {
		return this.principal;
	}

	public Person getActor() {
		return actor;
	}

	public void setActor(Person actor) {
		this.actor = actor;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

}

