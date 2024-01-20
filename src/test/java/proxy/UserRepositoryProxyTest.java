package proxy;

import org.testng.annotations.Test;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.UserRepositoryProxy;
import r.dev.repository.TRepository;
import r.entities.UsersEntity;
import r.repositories.UserRepository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class UserRepositoryProxyTest {

    @Test
    public void testProxy() {

       var entityManager  = PersistenceProviderFactory.getInstance("postgres").createEntityManager();
        UserRepositoryProxy proxyFactory = new UserRepositoryProxy(entityManager);

        UserRepository userRepository = proxyFactory.createProxy(UserRepository.class, UsersEntity.class);


        // Use the proxy to perform operations and verify that it works as expected
        var userInDb = userRepository.findById(1L).orElse(null);

        assertNotNull(userInDb);
    }
}

