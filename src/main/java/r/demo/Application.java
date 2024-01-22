package r.demo;

public class Application {
    public static void main(String[] args) {
         try {
            ApplicationContext applicationContext = new ApplicationContext(Application.class);
            var userSteps = applicationContext.getInstance(UserSteps.class);
            System.out.println(userSteps.getUserName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
