package r.demo;

import r.steps.UserSteps;

public class Application {
    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = new ApplicationContext();
            UserSteps userServices = (UserSteps) applicationContext.getInstance();
            System.out.println(userServices.getFirstUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
