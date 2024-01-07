package steps;

import annotations.Connection;
import annotations.Repository;
import entities.UsersEntity;
import jakarta.persistence.EntityManager;
import repository.TRepository;

public class UserSteps {



    @Connection("postgres")
    private EntityManager em;
    public  UsersEntity getUser(){
        return (UsersEntity) em.createNativeQuery("Select * from Users limit 1 ", UsersEntity.class).getSingleResult();
    }

}
