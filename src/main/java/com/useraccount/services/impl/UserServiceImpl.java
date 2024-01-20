package com.useraccount.services.impl;

import com.useraccount.services.UserService;
import org.di.framework.annotations.Component;

@Component
public class UserServiceImpl implements UserService {

	@Override
	public String getUserName() {
		return "username";
	}
}
