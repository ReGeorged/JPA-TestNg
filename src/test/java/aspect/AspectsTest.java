package aspect;

import impl.steps.AirplaneSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.testng.listeners.StepsListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(StepsListener.class)
public class AspectsTest {

    @Inject
    private AirplaneSteps airplaneSteps;
    @Test
    void testAspect(){
        Assert.assertNotNull(airplaneSteps.getByAspectInj());
    }

    @Test
    void testAirplane(){
        airplaneSteps.getFirsPlane().get();
    }
}
