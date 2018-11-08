package com.miguelo.io.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/*
 * Extiende del la configuracion del servidor de autorizaciones
 * */

@Configuration
@EnableAuthorizationServer
public class AuthorizacionServerConfiguration extends AuthorizationServerConfigurerAdapter{

	@Autowired 
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager; // inyeccion del admin de autentificacion
	  
	@Autowired
	private TokenStore tokenStore; // inyeccion del almacen de token

	 
	// sobreescribe configure del server de autorizacion
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
			    .withClient("cliente")
	            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
	            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT","USER")
	            .scopes("read","write")
	            .autoApprove(true)	        
	            .secret(passwordEncoder().encode("password"));          

	}
	
	// method encargado del encode/decode del pass con alg de encrip Bcrypt	
	private BCryptPasswordEncoder passwordEncoder() {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	    endpoints
	            .authenticationManager(authenticationManager)	        
	            .tokenStore(tokenStore);
	}
	 
	@Bean
	public TokenStore tokenStore() {
      return new InMemoryTokenStore();
	}
	  
}
