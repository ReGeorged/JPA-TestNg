package impl.steps.chained;

import impl.entities.UsersEntity;
import impl.repositories.UserRepository;
import impl.steps.RagacSteps;
import impl.steps.UserSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.inj.annotations.Steps;
import org.regeorged.dev.repository.annotations.Repository;

import java.util.List;

@Steps
public class ChainedSteps {
    @Inject
    private RagacSteps ragacSteps;
    @Inject
    private UserSteps userSteps;

    @Repository("postgres")
    private UserRepository userRepository;

    public String getUserAndRagac() {
        return ragacSteps.getRagac() + userSteps.getFirstUserName();
    }

    public List<UsersEntity> getAll() {
        return userRepository.findAll();
    }
}
