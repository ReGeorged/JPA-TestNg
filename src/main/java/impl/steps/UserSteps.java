package impl.steps;

import org.regeorged.dev.repository.annotations.Repository;
import org.regeorged.dev.inj.annotations.Steps;
import impl.repositories.UserRepository;

@Steps
public class UserSteps {

    @Repository("postgres")
    private UserRepository usersRepository;

    public String getFirstUserName() {
        return usersRepository.findById(1L).get().getUsername();
    }


}
