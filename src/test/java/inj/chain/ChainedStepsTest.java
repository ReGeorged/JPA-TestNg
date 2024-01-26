package inj.chain;


import impl.steps.chained.ChainedSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.testng.listeners.StepsListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(StepsListener.class)
public class ChainedStepsTest {
    @Inject
    private ChainedSteps chainedSteps;

    @Test
    void testStepChaining() {
        Assert.assertNotNull(chainedSteps.getUserAndRagac());
    }
    @Test
    void testRepoInjInChaindeSteps() {
        var all = chainedSteps.getAll();
        System.out.println(all);
        Assert.assertNotNull(all);
    }
}
