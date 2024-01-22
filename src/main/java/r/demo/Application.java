package r.demo;

import r.steps.UserSteps;

public class Application {
    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = new ApplicationContext();
            var userServices = applicationContext.getInstance(UserSteps.class);
            System.out.println(userServices.getFirstUserName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
