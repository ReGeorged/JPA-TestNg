import listeners.ConnectionListener;
import listeners.RepositoryListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import steps.UserSteps;
import steps.nestedSteps.NestedUserStep;

@Listeners({ConnectionListener.class})
public class UsersTest {
    private final UserSteps userSteps = new UserSteps();
    private final NestedUserStep nestedUserStep = new NestedUserStep();

    @Test
    void testUser() {
        Assert.assertNotNull(userSteps.getUser());
    }

    @Test
    void testRepoInjection(){
//        Assert.assertNotNull(userSteps.getRandomUser());
    }

    void nestedStepTest(){

    }
}
