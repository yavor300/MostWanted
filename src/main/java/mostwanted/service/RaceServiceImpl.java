package mostwanted.service;

import mostwanted.common.Constants;
import mostwanted.domain.dtos.races.EntryImportDto;
import mostwanted.domain.dtos.races.RaceImportDto;
import mostwanted.domain.dtos.races.RaceImportRootDto;
import mostwanted.domain.entities.*;
import mostwanted.repository.*;
import mostwanted.util.FileUtil;
import mostwanted.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class RaceServiceImpl implements RaceService {

    private final static String RACES_XML_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/races.xml";

    private final RaceRepository raceRepository;
    private final RaceEntryRepository raceEntryRepository;
    private final DistrictRepository districtRepository;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    @Autowired
    public RaceServiceImpl(RaceRepository raceRepository, RaceEntryRepository raceEntryRepository, DistrictRepository districtRepository, FileUtil fileUtil, XmlParser xmlParser, ModelMapper modelMapper) {
        this.raceRepository = raceRepository;
        this.raceEntryRepository = raceEntryRepository;
        this.districtRepository = districtRepository;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }


    @Override
    public Boolean racesAreImported() {
        return this.raceRepository.count() > 0;
    }

    @Override
    public String readRacesXmlFile() throws IOException {
        return this.fileUtil.readFile(RACES_XML_FILE_PATH);
    }

    @Override
    public String importRaces() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        RaceImportRootDto dtos = this.xmlParser.parseXml(RaceImportRootDto.class, RACES_XML_FILE_PATH);

        RaceImportDto:
        for (RaceImportDto dto : dtos.getRaces()) {
            if (dto.getLaps() == null) {
                sb.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            District district = this.districtRepository.findByName(dto.getDistrictName());
            if (district == null) {
                sb.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            Race race = this.modelMapper.map(dto, Race.class);
            race.setDistrict(district);

            Set<RaceEntry> raceEntries = new LinkedHashSet<>();
            for (EntryImportDto entry : dto.getEntries().getEntries()) {
                RaceEntry raceEntry = this.raceEntryRepository.findById(entry.getIdentifier()).orElse(null);
                if (raceEntry == null) {
                    sb.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                    continue RaceImportDto;
                }
                raceEntries.add(raceEntry);

                this.raceRepository.save(race);
                raceEntry.setRace(race);
                this.raceEntryRepository.saveAndFlush(raceEntry);
            }
            race.setEntries(raceEntries);

            this.raceRepository.saveAndFlush(race);
            sb.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                    race.getClass().getSimpleName(),
                    race.getId() + ""))
                    .append(System.lineSeparator());

        }

        return sb.toString().trim();
    }
}