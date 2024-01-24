package inj;

import impl.steps.RagacSteps;
import impl.steps.UserSteps;
import org.regeorged.dev.inj.annotations.Inject;
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