package mostwanted.repository;

import mostwanted.domain.entities.Racer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RacerRepository extends JpaRepository<Racer, Integer> {
    Racer findByName(String name);

    @Query(value = "SELECT r FROM Racer r WHERE r.cars.size > 0 ORDER BY r.cars.size DESC, r.name ASC")
    List<Racer> findAllByCarsIsNotNullOrderByCarsDescNameAsc();
}
