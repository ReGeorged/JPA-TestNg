package steps.nestedSteps;

import steps.UserSteps;

public class NestedUserStep {

    public String getUserName(){
        var steps = new UserSteps();
        return steps.getUser().getUsername();
    }
}
