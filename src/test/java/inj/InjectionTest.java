package inj;

import org.testng.annotations.Test;
import r.repositories.UserRepository;

import static org.testng.AssertJUnit.assertNotNull;

public class InjectionTest {

//    @Repository("postgres")
    public UserRepository userRepository;

    @Test
    void testRepositoryInjection(){
        var userInDb = userRepository.findByUsernameCustomQuery("nika");
        assertNotNull(userInDb);
        userInDb.stream().forEach(n -> System.out.println(n.getUsername()));

    }
}
