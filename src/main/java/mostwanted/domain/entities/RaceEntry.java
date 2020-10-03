package mostwanted.domain.entities;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Table(name = "race_entries")
public class RaceEntry extends BaseEntity{

    private Boolean hasFinished;
    private Double finishTime;
    private Car car;
    private Racer racer;
    private Race race;

    public RaceEntry() {
    }


    @Column(name = "has_finished", nullable = false)
    public Boolean getHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(Boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    @Column(name = "finish_time")
    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    @ManyToOne
    @JoinColumn(
            name = "car_id",
            referencedColumnName = "id"
    )
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @ManyToOne
    @JoinColumn(
            name = "racer_id",
            referencedColumnName = "id"
    )
    public Racer getRacer() {
        return racer;
    }

    public void setRacer(Racer racer) {
        this.racer = racer;
    }

    @ManyToOne
    @JoinColumn(
            name = "race_id",
            referencedColumnName = "id"
    )
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
