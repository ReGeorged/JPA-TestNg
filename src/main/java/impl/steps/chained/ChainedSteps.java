package impl.steps.chained;

import impl.steps.RagacSteps;
import impl.steps.UserSteps;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.inj.annotations.Steps;

@Steps
public class ChainedSteps {
    @Inject
    private RagacSteps ragacSteps;
    @Inject
    private UserSteps userSteps;

    public String getUserAndRagac(){
        return ragacSteps.getRagac() + userSteps.getFirstUserName();
    }
}
