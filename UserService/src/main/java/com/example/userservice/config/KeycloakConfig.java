package com.example.userservice.config;

                                import io.github.cdimascio.dotenv.Dotenv;
                                import lombok.Getter;
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
                                    private String serverUrl;
                                    private String realm;
                                    @Getter
                                    private String clientId;
                                    private String username;
                                    private String password;

                                    @PostConstruct
                                    public void init() {
                                        dotenv = Dotenv.load();
                                        serverUrl = dotenv.get("KEYCLOAK_SERVER_URL");
                                        realm = dotenv.get("KEYCLOAK_REALM");
                                        clientId = dotenv.get("KEYCLOAK_CLIENT_ID");
                                        username = dotenv.get("KEYCLOAK_USERNAME");
                                        password = dotenv.get("KEYCLOAK_PASSWORD");
                                        System.out.println("Client found: " + clientId + " with realm: " + realm + " and server URL: " + serverUrl + " and username: " + username);
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
                                        System.out.println("Querying realm: " + realm + " for clientId: " + clientId);
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