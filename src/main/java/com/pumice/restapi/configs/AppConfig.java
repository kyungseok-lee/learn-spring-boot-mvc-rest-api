package com.pumice.restapi.configs;

import static java.util.Set.of;

import com.pumice.restapi.accounts.Account;
import com.pumice.restapi.accounts.AccountRole;
import com.pumice.restapi.accounts.AccountService;
import com.pumice.restapi.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {

			@Autowired
			AccountService accountService;

			@Autowired
			AppProperties appProperties;

			@Override
			public void run(ApplicationArguments args) throws Exception {
				Account admin = Account.builder()
					.email(appProperties.getAdminUsername())
					.password(appProperties.getAdminPassword())
					.roles(of(AccountRole.ADMIN, AccountRole.USER))
					.build();
				accountService.saveAccount(admin);

				Account user = Account.builder()
					.email(appProperties.getUserUsername())
					.password(appProperties.getUserPassword())
					.roles(of(AccountRole.USER))
					.build();
				accountService.saveAccount(user);
			}
		};
	}

}
