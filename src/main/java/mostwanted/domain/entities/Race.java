package mostwanted.domain.entities;

import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "races")
public class Race extends BaseEntity{

    private Integer laps;
    private District district;
    private Set<RaceEntry> entries;

    public Race() {
    }

    @Column(name = "laps", nullable = false, columnDefinition = "int(11) default 0")
    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    @ManyToOne
    @JoinColumn(
            name = "district_id",
            referencedColumnName = "id",
            nullable = false
    )
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @OneToMany(mappedBy = "race")
    public Set<RaceEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<RaceEntry> entries) {
        this.entries = entries;
    }
}
