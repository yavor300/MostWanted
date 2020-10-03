package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.common.Constants;
import mostwanted.domain.dtos.RacerImportDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.Racer;
import mostwanted.domain.entities.Town;
import mostwanted.repository.RacerRepository;
import mostwanted.repository.TownRepository;
import mostwanted.util.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class RacerServiceImpl implements RacerService {

    private final static String RACERS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/racers.json";

    private final RacerRepository racerRepository;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final TownRepository townRepository;

    @Autowired
    public RacerServiceImpl(RacerRepository racerRepository, FileUtil fileUtil, Gson gson, ModelMapper modelMapper, TownRepository townRepository) {
        this.racerRepository = racerRepository;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.townRepository = townRepository;
    }

    @Override
    public Boolean racersAreImported() {
        return this.racerRepository.count() > 0;
    }

    @Override
    public String readRacersJsonFile() throws IOException {
        return this.fileUtil.readFile(RACERS_JSON_FILE_PATH);
    }

    @Override
    public String importRacers(String racersFileContent) {
        StringBuilder sb = new StringBuilder();

        RacerImportDto[] dtos = this.gson.fromJson(racersFileContent, RacerImportDto[].class);

        for (RacerImportDto dto : dtos) {
            if (this.racerRepository.findByName(dto.getName()) != null) {
                sb.append(Constants.DUPLICATE_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            if (dto.getName() == null) {
                sb.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            Town town = this.townRepository.findByName(dto.getHomeTown());
            if (town == null) {
                sb.append(Constants.INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                continue;
            }

            Racer racer = this.modelMapper.map(dto, Racer.class);
            racer.setHomeTown(town);

            this.racerRepository.saveAndFlush(racer);
            sb.append(String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                    racer.getClass().getSimpleName(),
                    racer.getName()))
                    .append(System.lineSeparator());

        }

        return sb.toString().trim();
    }

    @Override
    public String exportRacingCars() {
        StringBuilder sb = new StringBuilder();

        List<Racer> racers = this.racerRepository.findAllByCarsIsNotNullOrderByCarsDescNameAsc();
        for (Racer racer : racers) {
            if (racer.getAge() == null) {
                continue;
            }

            sb.append(String.format("Name: %s%nCars:%n", racer.getName()));
            Set<Car> cars = racer.getCars();
            for (Car car : cars) {
                sb.append(String.format(" %s %s %d%n", car.getBrand(), car.getModel(), car.getYearOfProduction()));
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
