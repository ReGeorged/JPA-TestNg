package j.di2;

public class Application {
    public static void main(String[] args) {
        try {
            ApplicationContext applicationContext = new ApplicationContext(Application.class);
            var userServices = applicationContext.getInstance(UserService.class);
            System.out.println(userServices.getUserName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
