package proxy;

import jakarta.persistence.EntityManagerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;
import r.entities.UsersEntity;
import r.repositories.UserRepository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RepositoryProxyTest {
    public static final EntityManagerFactory emf =  PersistenceProviderFactory.getInstance("postgres");

    @Test
    public void testProxy() {
        var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        RepositoryProxy<UserRepository, UsersEntity, Long> proxyFactory = RepositoryProxy.getInstance(entityManager);
        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);

        var userInDb = userRepository.findById(1L).orElse(null);
        assertNotNull(userInDb);
        System.out.printf("User in db: %s%n", userInDb.getUsername());
    }

    @Test(invocationCount = 30,threadPoolSize = 12)
    public void threadedProxyTest() {
        var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        RepositoryProxy<UserRepository, UsersEntity, Long> proxyFactory = RepositoryProxy.getInstance(emf.createEntityManager());
        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);
        var userInDb = userRepository.findById(1L).orElse(null);
        assertNotNull(userInDb);
        System.out.printf("User in db: %s%n", userInDb.getUsername());
    }

    @Test
    void testQuery(){
        var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        RepositoryProxy<UserRepository, UsersEntity, Long> proxyFactory = RepositoryProxy.getInstance(entityManager);
        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);
        var userInDb = userRepository.findByUsernameCustomQuery("nika");
        assertNotNull(userInDb);
        userInDb.stream().forEach(n -> System.out.println(n.getUsername()));
    }

    @Test
    void testParametrizedQuery(){
        var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        RepositoryProxy<UserRepository, UsersEntity, Long> proxyFactory = RepositoryProxy.getInstance(entityManager);
        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);
        var userInDb = userRepository.findByUserNameParam("nika");
        assertNotNull(userInDb);
        userInDb.stream().forEach(n -> System.out.println(n.getUsername()));
    }

    @Test
    void testNativeQuery(){
        var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        RepositoryProxy<UserRepository, UsersEntity, Long> proxyFactory = RepositoryProxy.getInstance(entityManager);
        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);
        var userInDb = userRepository.findByUsernameWithNativeQuery("nika");
        assertNotNull(userInDb);
        userInDb.stream().forEach(n -> System.out.println(n.getUsername()));
    }
}

