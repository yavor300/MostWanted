package mostwanted.domain.dtos.races;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "races")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceImportRootDto {

    @XmlElement(name = "race")
    private List<RaceImportDto> races;

    public RaceImportRootDto() {
    }

    public List<RaceImportDto> getRaces() {
        return races;
    }

    public void setRaces(List<RaceImportDto> races) {
        this.races = races;
    }
}
