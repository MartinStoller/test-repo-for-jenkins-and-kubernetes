package de.example.haegertime.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncodeTest();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/user").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers(HttpMethod.POST,"/customer/create").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/customer/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/customer/update").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/customer/addproject/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/customer/delete/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/customer").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/projects").hasAnyAuthority("EMPLOYEE","ADMIN","BOOKKEEPER")
                .antMatchers("/api/projects/{id}").hasAnyAuthority("BOOKKEEPER","ADMIN","EMPLOYEE")
                .antMatchers(HttpMethod.PATCH, "/api/projects/update/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/user/all").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers(HttpMethod.POST, "/api/user/create").hasAnyAuthority("ADMIN")
                .antMatchers("/api/user/myacc").hasAnyAuthority("ADMIN","EMPLOYEE","BOOKKEEPER")
                .antMatchers("/api/user/myacc/update").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll().and().httpBasic()
                .and().csrf().disable();
                //TODO die Methode erweitern nach Wunsch

    }
}