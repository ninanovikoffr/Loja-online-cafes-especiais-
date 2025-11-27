package com.engenhariadesoftware.e_comercecafe.Infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                    // Permitir acesso público ao Swagger
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                    // Endpoints públicos (autenticação sem token)
                    .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()  // Login e registro

                    // Endpoints ADMIN (apenas admins podem acessar)
                    .requestMatchers(HttpMethod.GET, "/usuario/all").hasRole("ADMIN")  // Listar todos os usuários
                    .requestMatchers(HttpMethod.GET, "/carrinhos/all").hasRole("ADMIN") // Listar todos os carrinhos
                    .requestMatchers(HttpMethod.GET, "/enderecos").hasRole("ADMIN") // Listar todos os endereços
                    .requestMatchers(HttpMethod.GET, "/pedidos").hasRole("ADMIN")  // Listar todos os pedidos
                    .requestMatchers(HttpMethod.GET, "/pedido-itens").hasRole("ADMIN") // Listar itens de pedidos
                    .requestMatchers(HttpMethod.GET, "/produtos/**").hasRole("ADMIN") // Listar todos os produtos
                    .requestMatchers(HttpMethod.GET, "/carrinho-itens").hasRole("ADMIN") // Listar todos os itens do carrinho
                    .requestMatchers(HttpMethod.GET, "/usuario/{id}").hasRole("ADMIN") // Buscar usuário por ID
                    .requestMatchers(HttpMethod.PUT, "/produtos/{id}").hasRole("ADMIN") // Atualizar produto
                    .requestMatchers(HttpMethod.PATCH, "/usuario/update/{id}").hasRole("ADMIN") // Atualizar dados do usuário
                    .requestMatchers(HttpMethod.DELETE, "/usuario/{id}").hasRole("ADMIN")  // Deletar usuário
                    .requestMatchers(HttpMethod.DELETE, "/produtos/{id}").hasRole("ADMIN") // Deletar produto
                    .requestMatchers(HttpMethod.PATCH, "/enderecos/tornar-padrao/{id}").hasRole("ADMIN") // Tornar endereço padrão
                    .requestMatchers(HttpMethod.DELETE, "/carrinho-itens/{id}").hasRole("ADMIN") // Deletar item do carrinho
                    .requestMatchers(HttpMethod.DELETE, "/pedidos/{id}").hasRole("ADMIN") // Deletar pedido
                    .requestMatchers(HttpMethod.DELETE, "/carrinhos/{id}").hasRole("ADMIN") // Deletar carrinho
                    .requestMatchers(HttpMethod.DELETE, "/usuario/{id}").hasRole("ADMIN") // Deletar usuário
                    .requestMatchers(HttpMethod.POST, "/produto/criar").hasRole("ADMIN")  // Criar produto
                    .requestMatchers(HttpMethod.PATCH, "/produto/{id}").hasRole("ADMIN")  // Atualizar parcialmente produto

                    // Endpoints para usuário autenticado (qualquer usuário, desde que logado)
                    .requestMatchers("/usuario/me", "/usuario/me/update").authenticated()
                    .requestMatchers("/carrinhos/criar").authenticated()  // Criar carrinho
                    .requestMatchers("/pedido-itens").authenticated() // Criar item de pedido
                    .requestMatchers("/pedidos").authenticated() // Criar pedido
                    .anyRequest().authenticated()  // Qualquer outra rota precisa ser autenticada
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
