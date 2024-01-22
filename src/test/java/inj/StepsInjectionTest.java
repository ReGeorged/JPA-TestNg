package inj;

import impl.steps.RagacSteps;
import impl.steps.UserSteps;
import org.regeorged.dev.inj.ApplicationContext;
import org.testng.annotations.Test;


public class StepsInjectionTest {


    @Test
    void testStepInjectionInTestNg() {

        try {
            ApplicationContext applicationContext = new ApplicationContext();
            UserSteps userServices = applicationContext.getInstance(UserSteps.class);
            var ragacSteps = applicationContext.getInstance(RagacSteps.class);
            System.out.println(ragacSteps.getRagac());
            System.out.println(userServices.getFirstUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
