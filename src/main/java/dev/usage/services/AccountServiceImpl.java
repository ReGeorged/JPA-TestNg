package dev.usage.services;

import dev.annotations.Component;
import dev.usage.iservices.AccountService;

@Component
public class AccountServiceImpl implements AccountService {

    @Override
    public Long getAccountNumber(String userName) {
        return 12345689L;
    }
}
