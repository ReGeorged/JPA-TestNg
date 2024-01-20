package r.repositories;

import r.dev.annotations.Param;
import r.dev.annotations.Query;
import r.dev.irepository.ITRepository;
import r.entities.UsersEntity;

import java.util.List;

public interface UserRepository extends ITRepository<UsersEntity, Long> {

    @Query("select u from UsersEntity u where u.username = ?1")
    List<UsersEntity> findByUsernameCustomQuery(String username);
    @Query("select u from UsersEntity u where u.username = :username")
    List<UsersEntity> findByUserNameParam(@Param("username") String username);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    List<UsersEntity> findByUsernameWithNativeQuery(String username);
}
