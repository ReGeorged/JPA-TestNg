package r.repositories;

import r.dev.annotations.Query;
import r.dev.irepository.ITRepository;
import r.entities.UsersEntity;

public interface UserRepository extends ITRepository<UsersEntity, Long> {

    @Query("SELECT u FROM UsersEntity u WHERE u.username = :username")
    UsersEntity findByUsername(String username);
}
