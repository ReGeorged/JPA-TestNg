package r.demo;

import com.useraccount.UserAccountApplication;
import com.useraccount.UserAccountClientComponent;
import org.di.framework.Injector;
import r.di.NewRepoInjector;
import r.steps.UserSteps;

public class DemoApp {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        NewRepoInjector.startApplication(UserAccountApplication.class);
        var userName = NewRepoInjector.getService(UserSteps.class).getFirstUserName();
        assert userName.equals("nika");
        long endTime = System.currentTimeMillis();
    }

}
