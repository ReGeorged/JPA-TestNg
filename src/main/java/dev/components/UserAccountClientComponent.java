package dev.components;

import dev.annotations.Autowired;
import dev.annotations.Component;
import dev.annotations.Qualifier;
import dev.usage.iservices.AccountService;
import dev.usage.iservices.UserService;

@Component
public class UserAccountClientComponent {

    @Autowired
    private UserService userService;

//    @Autowired
//    @Qualifier(value = "accountServiceImpl")
//    private AccountService accountService;

    public void displayUserAccount() {
        String username = userService.getUserName();
//        Long accountNumber = accountService.getAccountNumber(username);
        System.out.println("\n\tUser Name: " + username + "\n\tAccount Number: " );
    }
}