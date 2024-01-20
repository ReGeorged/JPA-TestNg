package old;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import r.dev.listeners.RepositoryListener;


@Listeners({RepositoryListener.class})
public class RepositoryInjectionTests {

    @Test
    void testRepoInjection() {
//        Assert.assertNotNull(repoSteps.getUser());

    }
}
