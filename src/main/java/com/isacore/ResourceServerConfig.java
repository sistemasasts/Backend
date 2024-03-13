package com.isacore;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {

	@Autowired
    private ResourceServerTokenServices tokenServices;


    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceIds).tokenServices(tokenServices);
    }
/*
 * En este métod definiremos las URL de los servicios que necesitas agregar seguridad con tokens
 * .exceptionHandling().authenticationEntryPoint(new AuthException()) para enviar exepción del acceso denegado
 * */
    @Override
    public void configure(HttpSecurity http) throws Exception {
                http
                .exceptionHandling().authenticationEntryPoint(new AuthException())
                .and()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/qualityQR/**" ).authenticated()
                .antMatchers("/security/**" ).authenticated()
                .antMatchers("/menus/**" ).authenticated()
                .antMatchers("/propertylists/**" ).authenticated()
                .antMatchers("/providers/**" ).authenticated()
                .antMatchers("/userimptek/**" ).authenticated()
                .antMatchers("/exitmaterials/**" ).authenticated()
                .antMatchers("/pncs/**" ).authenticated()
                .antMatchers("/product_properties/**" ).authenticated()
                .antMatchers("/product_providers/**" ).authenticated()
                .antMatchers("/products/**" ).authenticated()
                .antMatchers("/informationAditional/**" ).authenticated()
                .antMatchers("/informationAditionalFile/**" ).authenticated()
                .antMatchers("/laboratoryNorms/**" ).authenticated()
                .antMatchers("/configurationsFormEntry/**" ).authenticated()
                .antMatchers("/configuracionesse/**" ).authenticated()
                .antMatchers("/solicitudDocumentos/**" ).authenticated()
                .antMatchers("/solicitudesEnsayo/**" ).authenticated()
                .antMatchers("/solicitudHistorial/**" ).authenticated()
                .antMatchers("/solicitudesPruebasProceso/**" ).authenticated()
                .antMatchers("/solicitudPPInforme/**" ).authenticated()
                .antMatchers("/notificacionesCorreo/**" ).permitAll()
                .antMatchers("/tests/**" ).authenticated()
                .antMatchers("/aprobacionAdicional/**" ).permitAll()
                .antMatchers("/tokens/**" ).permitAll();


    }


}
