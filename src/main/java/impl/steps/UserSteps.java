package impl.steps;

import impl.repositories.UserRepository;
import org.regeorged.dev.inj.annotations.Steps;
import org.regeorged.dev.repository.annotations.Repository;

@Steps
public class UserSteps {

    @Repository("postgres")
    private UserRepository usersRepository;

    public String getFirstUserName() {
        return usersRepository.findById(1L).get().getUsername();
    }


}
