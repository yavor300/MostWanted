package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.common.Constants;
import mostwanted.domain.dtos.TownImportDto;
import mostwanted.domain.entities.Town;
import mostwanted.repository.TownRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Transactional
@Service
public class TownServiceImpl implements TownService{

    private final static String TOWNS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/towns.json";
    private final TownRepository townRepository;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, FileUtil fileUtil, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileUtil.readFile(TOWNS_JSON_FILE_PATH);
    }

    @Override
    public String importTowns(String townsFileContent) {
        StringBuilder sb = new StringBuilder();

        TownImportDto[] dtos = this.gson.fromJson(townsFileContent, TownImportDto[].class);

        for (TownImportDto dto : dtos) {
            if (this.townRepository.findByName(dto.getName()) != null) {
                sb.append(Constants.DUPLICATE_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            if (dto.getName() == null) {
                sb.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            Town town = this.modelMapper.map(dto, Town.class);
            this.townRepository.saveAndFlush(town);
            sb.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                    town.getClass().getName(),
                    town.getName()))
                    .append(System.lineSeparator());

        }

        return sb.toString().trim();
    }

    @Override
    public String exportRacingTowns() {
        StringBuilder sb = new StringBuilder();

        List<Town> towns = this.townRepository.findAllByRacersIsNotNullOrderByRacersDescNameAsc();
        for (Town town : towns) {
            sb.append(String.format("Name: %s%nRacers: %d%n%n", town.getName(), town.getRacers().size()));
        }

        return sb.toString().trim();
    }
}
