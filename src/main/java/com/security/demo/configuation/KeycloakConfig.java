package com.security.demo.configuation;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }

    /* 
        KeyCloakWebSecurity 에서 제공하는 http 인증 설정
        WebSecurity 를 생속받아서 http 로 들어온 신호에 대해서 Filter 처리
        전체에 대해서 인증이 필요하도록 처리할 수 도 있고
        특정 신호에 대해서만 인증처리할 수 도 있음
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/app*").permitAll()
                .anyRequest().authenticated();
    }


    // KeyCloak Configuration 설정
    // KeyCloak 설정을 가져오는 부분 담당
    // 현재는 사용하지않음 -> resourceAsStream 이 문제가 있어서 설정파일을 정상적으로 가져올 수 없음
    // 또한 keycloakspringconfigReoslver 와 충돌이 나서 serivce가 cycling 되는 현상 발생 (특정 버전문제인지는 확인 못함)
    // 따라서 해당 Config에서는 지우고, 별도의 Class 파일을 생성하여 keycloakspringconfigresolver를 처리 -> application.properties 파일에 설정
    @Bean
    public KeycloakSpringBootConfigResolver keycloakSpringBootConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
    /* public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakConfigResolver() {
            private KeycloakDeployment keycloakDeployment;

            @Override
            public KeycloakDeployment resolve(HttpFacade.Request facade) {
                if (keycloakDeployment != null) {
                    return keycloakDeployment;
                }
                InputStream configInputStream = getClass().getResourceAsStream("/keycloak.json");
                return KeycloakDeploymentBuilder.build(configInputStream);
            }
        };
    } */
}
