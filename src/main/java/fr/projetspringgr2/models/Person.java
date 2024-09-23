package fr.projetspringgr2.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

  /***
   * * This class is associated with other entities like `Place`, `Movie`, and `Role`.
  */

@Entity
public class Person {

    /**
     * * This ID is stored in the `id_person` column in the table.
     */

    @Id
    @Column(name = "id_person", length = 10)
    private String idPerson;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "surname", length = 50, nullable = true)
    private String surname;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;
    
    @Column(name = "size")
    private double size;

    /**
     * The person's place of residence.
     * This field is a Many-to-One association with the `Place` entity,and is stored in the `id_place` column.
     */
	@ManyToOne
    @JoinColumn(name = "id_place", nullable = true)
    private Place place;

    /**
     * This field is a Many-to-Many relationship with the `Movie` entity.
     * The management of this relationship is handled by the `Movie` entity.
     */
    @ManyToMany(mappedBy = "persons")
    private Set<Movie> movies;

    /**
            * Set of roles the person has played.
     * This field is a One-to-Many relationship with the `Role` entity.
     * The management of this relationship is handled by the `Role` entity.
     **/
    @OneToMany(mappedBy="actor")
  	private Set<Role> roles;


    /**
     * Default constructor for the `Person` class.
     **/
    public Person(){}


      /** getters and setters **/

    public Person(Person person) {
		this.idPerson = person.getIdPerson();
		this.birthDate = person.getBirthDate();
		this.name = person.getName();
		this.surname = person.getSurname();
		this.place = person.getPlace();
	}

	public String getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate localDate) {
        this.birthDate = localDate;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
    
    public double getSize() {
		return size;
	}


	public void setSize(double size) {
		this.size = size;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

    public CharSequence getMovies() {return null;}
}
