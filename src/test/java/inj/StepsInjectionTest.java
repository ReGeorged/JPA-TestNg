package inj;

import org.testng.Assert;
import org.testng.annotations.Test;
import r.demo.ApplicationContext;
import r.steps.RagacSteps;
import r.steps.UserSteps;



public class StepsInjectionTest {


    @Test
    void testStepInjectionInTestNg(){

        try {
            ApplicationContext applicationContext = new ApplicationContext();
            UserSteps userServices =  applicationContext.getInstance(UserSteps.class);
            var ragacSteps = applicationContext.getInstance(RagacSteps.class);
            System.out.println(ragacSteps.getRagac());
            System.out.println(userServices.getFirstUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
