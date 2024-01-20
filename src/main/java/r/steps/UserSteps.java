package r.steps;

import r.dev.annotations.Repository;
import r.dev.annotations.Steps;
import r.dev.inj.RepositoryInjector;
import r.repositories.UserRepository;

@Steps
public class UserSteps {

    @Repository("postgres")
    private UserRepository usersRepository;

    public String getFirstUserName() {
        return usersRepository.findById(1L).get().getUsername();
    }


}