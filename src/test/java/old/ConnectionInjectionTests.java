package old;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//@Listeners({ConnectionListener.class})
public class ConnectionInjectionTests {
//    private final UserSteps userSteps = new UserSteps();
//    private final NestedUserStep nestedUserStep = new NestedUserStep();

    @Test
    void testUser() {
//        Assert.assertNotNull(userSteps.getUser());
    }

    @Test(invocationCount = 120, threadPoolSize = 12)
    void testThreadedConnectionInjection() {
//        Assert.assertNotNull(userSteps.getUser());
    }

    @Test
    void testRepoInjection() {
//        Assert.assertNotNull(userSteps.getRandomUser());
    }

    @Test
    void nestedStepTest() {
//        Assert.assertNotNull(nestedUserStep.getUserName());
    }
}