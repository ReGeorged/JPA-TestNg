package com.useraccount.services.impl;

import com.useraccount.services.AccountService;
import org.di.framework.annotations.Component;

@Component
public class AccountServiceImpl implements AccountService {

	@Override
	public Long getAccountNumber(String userName) {
		return 12345689L;
	}
}
