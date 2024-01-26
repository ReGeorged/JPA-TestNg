package inj.chain;

import impl.steps.AirplaneSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.testng.listeners.StepsListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(StepsListener.class)
public class AirplaneTests {
    @Inject
    private AirplaneSteps airplaneSteps;

    @Test
    void testPlaneNotNull(){
        var firsPlane = airplaneSteps.getFirsPlane().get();
        System.out.println(firsPlane);
        Assert.assertNotNull(firsPlane);
    }
}
