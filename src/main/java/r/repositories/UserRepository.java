package r.repositories;

import r.dev.annotations.Query;
import r.dev.irepository.ITRepository;
import r.entities.UsersEntity;

import java.util.List;

public interface UserRepository extends ITRepository<UsersEntity, Long> {

    @Query("select u from UsersEntity u where u.username = ?1")
    List<UsersEntity> findByUsername(String username);
}
