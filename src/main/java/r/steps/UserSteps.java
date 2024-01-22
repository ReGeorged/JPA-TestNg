package r.steps;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import r.dev.annotations.Repository;
import r.dev.annotations.Steps;
import r.repositories.UserRepository;

@Steps
public class UserSteps {

    @Repository("postgres")
    public UserRepository repository;

    public String getUserName(){
        return repository.findByUsernameCustomQuery("nika").get(0).getUsername();
    }
}