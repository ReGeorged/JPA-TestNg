package old.steps.repositories;

import old.dev.annotations.Repository;
import old.dev.repository.TRepository;
import entities.UsersEntity;

import java.util.Optional;

public class RepositorySteps {
    @Repository("postgres")
    private TRepository<UsersEntity,Long> usersRepository;

    public Optional<UsersEntity> getUser(){
        return usersRepository.findById(1L);
    }
}
