package inj;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import r.dev.annotations.Inject;
import r.dev.annotations.Repository;
import r.dev.listeners.StepsListener;
import r.repositories.UserRepository;
import r.steps.RagacSteps;
import r.steps.UserSteps;

import static org.testng.AssertJUnit.assertNotNull;


@Listeners(StepsListener.class)
public class StepsAutowiringTest {
    @Inject
    private  UserSteps userSteps;
    @Inject
    private RagacSteps ragacSteps;

    @Repository("postgres")
    private UserRepository userRepository;

    @Test
    void testRepositoryInjection(){
        var userInDb = userSteps.getFirstUserName();
        assertNotNull(userInDb);
    }

    @Test
    void testRagacSteps(){
        assertNotNull(ragacSteps.getRagac());

    }

}