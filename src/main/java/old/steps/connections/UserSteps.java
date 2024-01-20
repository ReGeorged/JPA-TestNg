package old.steps.connections;

import old.dev.annotations.Connection;
import entities.UsersEntity;
import jakarta.persistence.EntityManager;

public class UserSteps {

    @Connection("postgres")
    private EntityManager em;
    public  UsersEntity getUser(){
        return (UsersEntity) em.createNativeQuery("Select * from Users limit 1 ", UsersEntity.class).getSingleResult();
    }

}
