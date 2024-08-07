package aircleanprojectback.restapi.car.repository;

import aircleanprojectback.restapi.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {


    Car findByCarNumber(String selectedCar);


    List<Car> findAllByCarNumberIn(List<String> selectedCars);
}
