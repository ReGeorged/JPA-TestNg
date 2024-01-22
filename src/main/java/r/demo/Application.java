package r.demo;

import r.steps.RagacSteps;
import r.steps.UserSteps;

public class Application {
    public static void main(String[] args) {
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
