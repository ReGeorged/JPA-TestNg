package old.steps.connections.nestedSteps;

import old.dev.listeners.ConnectionListener;
import old.steps.connections.UserSteps;
import org.testng.annotations.Listeners;


@Listeners({ConnectionListener.class})
public class NestedUserStep {

    public String getUserName(){
        var steps = new UserSteps();
        return steps.getUser().getUsername();
    }
}
