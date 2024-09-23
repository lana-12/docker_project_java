package fr.projetspringgr2.models;



import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="state", length = 150, nullable = true )
    private String state;

    @Column(name="city", length = 100, nullable = true )
    private String city;

    @Column(name="address", nullable = true )
    private String address;

    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;

    @OneToMany(mappedBy = "place")
    private Set<Person> persons;


    @OneToMany(mappedBy = "place")
    private Set<Movie> movies;

    {
        movies = new HashSet<>();
        persons = new HashSet<>();
    }

    /**
     * Constructor by default
     */

    public Place() {}

    public Place(String state, String city, String address) {
        this.state = state;
        this.city = city;
        this.address = address;

    }


    /**
     * Getters and Setters
     *
     */

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

