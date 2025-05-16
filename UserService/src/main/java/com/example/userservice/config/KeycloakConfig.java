package com.example.userservice.config;

                import io.github.cdimascio.dotenv.Dotenv;
                import org.keycloak.OAuth2Constants;
                import org.keycloak.admin.client.Keycloak;
                import org.keycloak.admin.client.KeycloakBuilder;
                import org.keycloak.admin.client.resource.RealmResource;
                import org.keycloak.admin.client.resource.UsersResource;

                import javax.annotation.PostConstruct;

                import org.springframework.context.annotation.Configuration;

                @Configuration
                public class KeycloakConfig {

                    private Keycloak keycloak;
                    private Dotenv dotenv;
                    private final String serverUrl = dotenv.get("KEYCLOAK_SERVER_URL");
                    private final String realm = dotenv.get("KEYCLOAK_REALM");
                    private final String clientId = dotenv.get("KEYCLOAK_CLIENT_ID");
                    private final String username = dotenv.get("KEYCLOAK_USERNAME");
                    private final String password = dotenv.get("KEYCLOAK_PASSWORD");

                    @PostConstruct
                    public void init() {
                        keycloak = KeycloakBuilder.builder()
                                .serverUrl(serverUrl)
                                .realm(realm)
                                .clientId(clientId)
                                .username(username)
                                .password(password)
                                .grantType(OAuth2Constants.PASSWORD)
                                .build();

                        testConnection();
                    }

                    public RealmResource getRealmResource() {
                        return keycloak.realm(realm);
                    }

                    public UsersResource getUsersResource() {
                        return getRealmResource().users();
                    }

                    private void testConnection() {
                        try {
                            keycloak.tokenManager().getAccessToken();
                            System.out.println("Connected to Keycloak successfully.");
                        } catch (Exception e) {
                            System.err.println("Failed to connect to Keycloak: " + e.getMessage());
                        }
                    }
                }