package mostwanted.domain.dtos.races;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "race")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceImportDto {

    @XmlElement
    private Integer laps;

    @XmlElement(name = "district-name")
    private String districtName;

    @XmlElement
    private EntryImportRootDto entries;

    public RaceImportDto() {
    }

    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public EntryImportRootDto getEntries() {
        return entries;
    }

    public void setEntries(EntryImportRootDto entries) {
        this.entries = entries;
    }
}
