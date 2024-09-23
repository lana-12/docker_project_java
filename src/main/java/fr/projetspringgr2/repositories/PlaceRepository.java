package fr.projetspringgr2.repositories;

import fr.projetspringgr2.models.Country;
import fr.projetspringgr2.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByState(String state);

    Optional<Place> findByCity(String city);

    Optional<Place> findByAddress(String address);

    List<Place> findAllByStateAndCity(String state, String city);

    Optional<Place> findByAddressAndStateAndCityAndCountry(String address, String state, String city, Country country);

}
