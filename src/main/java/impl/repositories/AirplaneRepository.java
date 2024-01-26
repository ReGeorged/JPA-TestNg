package impl.repositories;

import impl.entities.AirplanesEntity;
import org.regeorged.dev.repository.annotations.Sqlfile;
import org.regeorged.dev.repository.TRepository;

import java.util.List;

public interface AirplaneRepository extends TRepository<AirplanesEntity,Integer> {
    @Sqlfile(value = "src/test/resources/sqlfiles/getAllAirplanes.sql",paramKeys = {":airplaneId"})
    List<AirplanesEntity> allPlanesWithSqlFile(String airplaneId);
}
