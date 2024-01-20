package j.di2;


import r.dev.annotations.Repository;
import r.dev.annotations.Steps;
import r.repositories.UserRepository;

@Steps
public class UserService {

    @Repository("postgres")
    private UserRepository repository;

    public String getUserName(){
        return repository.findByUsernameCustomQuery("nika").get(0).getUsername();
    }
}