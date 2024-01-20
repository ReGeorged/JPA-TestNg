package proxy;

import org.testng.annotations.Test;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;
import r.entities.UsersEntity;
import r.repositories.UserRepository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RepositoryProxyTest {

    @Test
    public void testProxy() {
        var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        RepositoryProxy<UserRepository, UsersEntity, Long> proxyFactory = new RepositoryProxy<>(entityManager);
        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);
        var userInDb = userRepository.findById(1L).orElse(null);
        assertNotNull(userInDb);
        System.out.printf("User in db: %s%n", userInDb.getUsername());
    }
}

