package hotel;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hotel.security.LoginSuccessHandler;

@EnableSalespoint
public class Hotel {

	private static final String LOGIN_ROUTE = "/login";

	public static void main(String[] args) {
		SpringApplication.run(Hotel.class, args);
	}

	@Configuration
	static class HotelConfiguration implements WebMvcConfigurer {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController(LOGIN_ROUTE).setViewName("member/login");
			registry.addViewController("/").setViewName("member/index");
		}
	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		@Autowired
		private LoginSuccessHandler loginSuccessHandler;

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().formLogin().loginPage(LOGIN_ROUTE)
					.loginProcessingUrl(LOGIN_ROUTE).successHandler(loginSuccessHandler).and().logout()
					.logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}
