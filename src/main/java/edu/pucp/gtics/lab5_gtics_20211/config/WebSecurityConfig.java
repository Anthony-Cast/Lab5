package edu.pucp.gtics.lab5_gtics_20211.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/user/signIn").loginProcessingUrl("/processLogin")
                .usernameParameter("correo").defaultSuccessUrl("/user/signInRedirect",true);

                http.authorizeRequests()
                        .antMatchers("/gameshop3/plataformas/**","/gameshop3/juegos/nuevo","/gameshop3/juegos/editar","/gameshop3/juegos/guardar","/gameshop3/juegos/borrar").hasAuthority("ADMIN")
                        .antMatchers("/gameshop3/juegos/lista").hasAuthority("USER")
                        .anyRequest().permitAll();
                http.logout()
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true);

    }
    @Autowired
    DataSource datasource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(datasource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select correo,password,enabled " +
                        "from usuarios where correo=?")
                .authoritiesByUsernameQuery("select correo,autorizacion " +
                        "from usuarios where correo=? and enabled=1");
    }
}