package old;

import old.dev.listeners.RepositoryListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import old.steps.repositories.RepositorySteps;


@Listeners({RepositoryListener.class})
public class RepositoryInjectionTests {
    private final RepositorySteps repoSteps = new RepositorySteps();
    @Test
    void testRepoInjection() {
        Assert.assertNotNull(repoSteps.getUser());

    }
}
