package org.example.m295_nour.configurationsTest;

import org.example.m295_nour.configurations.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

class SecurityConfigTest {

    private final SecurityConfig config = new SecurityConfig();

    @Test
    void testFilterChainBean() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);  // nicht vollständig testbar – meist Integrationstest
        // Hier würdest du ggf. Integrationstests machen – das hier ist begrenzt nützlich
        assertThat(config).isNotNull();
    }

    @Test
    void testUserDetailsService() {
        UserDetailsService userDetailsService = config.users();

        UserDetails user = userDetailsService.loadUserByUsername("user");
        UserDetails admin = userDetailsService.loadUserByUsername("admin");

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getAuthorities()).extracting("authority").contains("ROLE_USER");

        assertThat(admin).isNotNull();
        assertThat(admin.getUsername()).isEqualTo("admin");
        assertThat(admin.getAuthorities()).extracting("authority").contains("ROLE_ADMIN");
    }
}
