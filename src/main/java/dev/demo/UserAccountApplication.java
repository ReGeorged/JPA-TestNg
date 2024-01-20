package dev.demo;

import dev.components.UserAccountClientComponent;
import dev.inj.Injector;

public class UserAccountApplication {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Injector.startApplication(UserAccountApplication.class);
        Injector.getService(UserAccountClientComponent.class).displayUserAccount();
        long endime = System.currentTimeMillis();
    }
}
