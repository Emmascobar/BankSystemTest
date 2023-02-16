package com.ironhack.security;
import com.ironhack.filters.CustomAuthenticationFilter;
import com.ironhack.filters.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Esta es la clase mas editable del security model
@Configuration
public class SecurityConfiguration {
    // UserDetailsService is an interface provided by Spring Security that defines a way to retrieve user information
    // Implementation is in CustomUserDetailsService
    @Autowired
    private UserDetailsService userDetailsService; // CON ESTA LINEA SE ENLAZAN LOS USUARIOS DE LA BD A SPRINGSECURITY. SE COPIA Y PEGA.

    // Autowired instance of the AuthenticationManagerBuilder
    @Autowired
    private AuthenticationManagerBuilder authManagerBuilder; // NOS AYUDA CONSTRUIR LAS AUTENTIFICACIONES. COPIAR Y PEGAR.

    @Bean // METODO DE JAVA QUE EXTIENDE MUCHAS OTRAS FUNCIONES. COPIAR Y PEGAR.
    public PasswordEncoder passwordEncoder() { // Este metodo encripta la contraseña para que no sea visible a simple vista en una BD)
        return new BCryptPasswordEncoder(); // Las contraseñas nunca se devuelven en las peticiones de HTTP - Idealmente crearse un DTO.

    }

    @Bean // ESTE BEAN AYUDA A REALIZAR TODAS LAS AUTENTIFICACIONES NECESARIAS. COPIAR Y PEGAR.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean // ESTE ES EL MAS IMPORTANTE. ES EL METODO DE PASOS Y FILTROS QUE SE EJECUTARA EN LA RUTA DE AUTENTIFICACION. COPIAR Y PEGAR COMPLETO.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CustomAuthenticationFilter instance created. // FUNCIONA COMO UN FILTRO EN LA URL DE LOGIN.
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManagerBuilder.getOrBuild());

        // set the URL that the filter should process. // AQUI DEFINE LA RUTA EN LA QUE NOS AUTENTIFICAREMOS.
        customAuthenticationFilter.setFilterProcessesUrl("/login");

        // disable CSRF protection. //
        http.csrf().disable();

        // set the session creation policy to stateless // CON ESTO DEFINIMOS QUE LA SESION NO VA A GUARDAR INFORMACION
        //DE SESIONES DENTRO DEL SERVIDOR, QUE VA A SER STATELESS, PORQUE VAMOS A USAR TOKENS. COPIAR Y PEGAR.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // set up authorization for different request matchers and user roles. //AQUI SETEAMOS LAS RUTAS A PROTEGER.
        http.authorizeHttpRequests() //DEFINIMOS LA MANERA DE LA QUE LA VAMOS A PROTEGER.

                .anyRequest().permitAll(); //Esto dice que el resto de las rutas de la API serian públicas.

        http.addFilter(customAuthenticationFilter);
        // Add the custom authorization filter before the standard authentication filter.
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        // Build the security filter chain to be returned.
        return http.build();

    }
}

// EN EL SECURITY CONFIGURATION LO UNICO QUE DEBEMOS CAMBIAR
// Y/O MODIFICAR SON LAS RUTAS A PROTEGER EN http.authorizeHttpRequests()