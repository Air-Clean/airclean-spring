package aircleanprojectback.restapi.car.repository;

import aircleanprojectback.restapi.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, String> {
}