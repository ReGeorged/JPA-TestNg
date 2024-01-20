package dev.usage.services;

import dev.annotations.Component;
import dev.usage.iservices.UserService;


@Component
public class UserServiceImpl implements UserService {

    @Override
    public String getUserName() {
        return "username";
    }
}
