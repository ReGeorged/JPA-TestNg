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
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xsi:schemaLocation="
        http://xmlns.jcp.org/xml/ns/persistence
        http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">

    <persistence-unit name="postgres" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <properties>
            <property name="hibernate.connection.driver_class" value="org.postgresql.Driver"/>
            <property name="hibernate.connection.url" value="jdbc:postgresql://localhost:5432/postgres"/>
            <property name="hibernate.connection.username" value="postgres"/>
            <property name="hibernate.connection.password" value="root"/>
            <!-- Hibernate properties -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="none"/>
            <property name="hibernate.connection.provider_class" value="org.hibernate.hikaricp.internal.HikariCPConnectionProvider" />
<!--            Set the minimumIdle amount of connections to the amount of threads you will be running the test in-->
            <property name="hibernate.hikari.minimumIdle" value="6" />
            <property name="hibernate.hikari.maximumPoolSize" value="50" />
            <property name="hibernate.hikari.idleTimeout" value="30000" />
        </properties>
    </persistence-unit>
</persistence>
```