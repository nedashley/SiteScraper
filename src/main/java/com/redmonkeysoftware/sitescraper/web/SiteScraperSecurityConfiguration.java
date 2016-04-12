package com.redmonkeysoftware.sitescraper.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SiteScraperSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;
    //@Autowired
    //@Qualifier("siteScraperAuthenticationProvider")
    //private AuthenticationProvider authenticationProvider;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new Resource[1];
        resources[0] = new ClassPathResource("/com/redmonkeysoftware/sitescraper/app/defaults.properties");
        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        return pspc;
    }

    @Bean
    public BasicAuthenticationEntryPoint entryPoint() {
        BasicAuthenticationEntryPoint basicAuthEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthEntryPoint.setRealmName("Site Scraper");
        return basicAuthEntryPoint;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(env.getProperty("user.username", "sitescraper"))
                .password(env.getProperty("user.password", "testing"))
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.headers().xssProtection().disable()
                //.and().headers().frameOptions().disable()
                //.csrf().disable()
                .csrf().csrfTokenRepository(csrfTokenRepository())
                //.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true).and().and()
                /*.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/dashboard/**", "POST"))
                 .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/case/**", "POST"))
                 .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/dashboard/**", "PUT"))
                 .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/case/**", "PUT"))
                 .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/dashboard/**", "DELETE"))
                 .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/case/**", "DELETE"))*/
                .and()
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/logout*").permitAll()
                .antMatchers("/scrapes/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/scrapes/", true).permitAll()
                .and().logout().logoutUrl("/logout.htm").logoutSuccessUrl("/login.htm")
                .and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
        if (Boolean.TRUE.equals(env.getProperty("ssl.enabled", Boolean.class, Boolean.FALSE))) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
