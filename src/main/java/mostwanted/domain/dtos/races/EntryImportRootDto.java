package mostwanted.domain.dtos.races;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "entries")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntryImportRootDto {

    @XmlElement(name = "entry")
    private List<EntryImportDto> entries;

    public EntryImportRootDto() {
    }

    public List<EntryImportDto> getEntries() {
        return entries;
    }

    public void setEntries(List<EntryImportDto> entries) {
        this.entries = entries;
    }
}
