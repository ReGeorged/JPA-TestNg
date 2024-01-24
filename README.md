# This Framework is developed to make QATA's files easier by providing ways to inject JPA* repositories in TestNg Tests



# Usage example

### create JPA Entity then create a repository interface and extend TRepository
```java
public interface UserRepository extends TRepository<UsersEntity, Long> {
}
```
### you can define qustom queries in the repository interface like this
```java
public interface UserRepository extends TRepository<UsersEntity, Long> {

    @Query("select u from UsersEntity u where u.username = ?1")
    List<UsersEntity> findByUsernameCustomQuery(String username);

    @Query("select u from UsersEntity u where u.username = :username")
    List<UsersEntity> findByUserNameParam(@Param("username") String username);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    List<UsersEntity> findByUsernameWithNativeQuery(String username);
}
```

### Then create a Class and annotate it with @Steps, Annotate the created interface in previous step with @Repository and provide persistence-unit-name 
```java
@Steps
public class UserSteps {
    @Repository("postgres")
    private UserRepository usersRepository;
    
    public String getFirstUserName() {
        return usersRepository.findById(1L).get().getUsername();
    }
}
```
### you can also inject other steps in @Steps class
```java
@Steps
public class ChainedSteps {
    @Inject
    private RagacSteps ragacSteps;
    @Inject
    private UserSteps userSteps;

    @Repository("postgres")
    private UserRepository userRepository;

    public String getUserAndRagac() {
        return ragacSteps.getRagac() + userSteps.getFirstUserName();
    }
    public List<UsersEntity> getAll() {
        return userRepository.findAll();
    }
}
```
### It is that simple, now to use the Steps in the tests annotate the TestNG Test Class with @StepsListener.class and inject the @Steps classes with @Inject
```java
import impl.steps.chained.ChainedSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.testng.listeners.StepsListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(StepsListener.class)
public class ChainedStepsTest {
    @Inject
    private ChainedSteps chainedSteps;

    @Test
    void testStepChaining() {
        Assert.assertNotNull(chainedSteps.getUserAndRagac());
    }
}
```

#### Enjoy the benefits of DI, The Connection pools and connections are created and destroyed automatically so we don't need to worry about cleaning them up or creating them.

### To configure the Database connections create persistence.xml in src/main/resources/META-INF/ package
```xml

```