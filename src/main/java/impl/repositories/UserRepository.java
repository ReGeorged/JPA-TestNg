package impl.repositories;

import org.regeorged.dev.repository.annotations.Param;
import org.regeorged.dev.repository.annotations.Query;
import org.regeorged.dev.repository.TRepository;
import impl.entities.UsersEntity;

import java.util.List;

public interface UserRepository extends TRepository<UsersEntity, Long> {

    @Query("select u from UsersEntity u where u.username = ?1")
    List<UsersEntity> findByUsernameCustomQuery(String username);
    @Query("select u from UsersEntity u where u.username = :username")
    List<UsersEntity> findByUserNameParam(@Param("username") String username);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    List<UsersEntity> findByUsernameWithNativeQuery(String username);
}
