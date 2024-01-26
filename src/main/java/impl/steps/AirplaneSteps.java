package impl.steps;

import impl.entities.AirplanesEntity;
import impl.repositories.AirplaneRepository;
import org.regeorged.dev.inj.annotations.Steps;
import org.regeorged.dev.repository.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Steps
public class AirplaneSteps {
    @Repository("postgres")
    private AirplaneRepository airplaneRepository;

    public Optional<AirplanesEntity> getFirsPlane(){
        return airplaneRepository.findById(1);
    }

    public List<AirplanesEntity> getByAspectInj(){
        return airplaneRepository.allPlanesWithSqlFile("1");
    }
}
