package inj;

import org.testng.Assert;
import org.testng.annotations.Test;
import r.steps.UserSteps;

public class StepsInjectionTest {
    @Autowired
    private UserSteps userSteps;

    @Test
    void testStepInjectionInTestNg(){
        Assert.assertNotNull(userSteps.getFirstUserName(),"uusername or user object or userSteps is null");
    }
}
