package inj;

import impl.repositories.UserRepository;
import impl.steps.RagacSteps;
import impl.steps.UserSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.repository.annotations.Repository;
import org.regeorged.dev.testng.listeners.StepsListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertNotNull;


@Listeners(StepsListener.class)
public class StepsAutowiringTest {
    @Inject
    private UserSteps userSteps;
    @Inject
    private RagacSteps ragacSteps;

    @Repository("postgres")
    private UserRepository userRepository;

    @Test
    void testRepositoryInjection() {
        var userInDb = userSteps.getFirstUserName();
        assertNotNull(userInDb);
    }

    @Test
    void testRagacSteps() {
        assertNotNull(ragacSteps.getRagac());

    }

}