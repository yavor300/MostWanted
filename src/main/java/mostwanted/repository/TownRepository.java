package mostwanted.repository;

import mostwanted.domain.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {
    Town findByName(String name);

    @Query(value = "SELECT t FROM Town t WHERE t.racers.size > 0 order by t.racers.size DESC, t.name")
    List<Town> findAllByRacersIsNotNullOrderByRacersDescNameAsc();
}
