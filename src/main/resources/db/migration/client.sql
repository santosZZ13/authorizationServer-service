drop table if exists client;
CREATE TABLE client (
                        id varchar(255) NOT NULL,
                        client_id varchar(255) NOT NULL,
                        client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        client_secret varchar(255) DEFAULT NULL,
                        client_secret_expires_at timestamp DEFAULT NULL,
                        client_name varchar(255) NOT NULL,
                        authorization_grant_types varchar(1000) NOT NULL,
                        client_authentication_methods varchar(1000) NOT NULL,
                        redirect_uris varchar(1000) DEFAULT NULL,
                        post_logout_redirect_uris varchar(1000) DEFAULT NULL,
                        scopes varchar(1000) NOT NULL,
                        client_settings varchar(2000) NOT NULL,
                        token_settings varchar(2000) NOT NULL,
                        PRIMARY KEY (id)
);

INSERT INTO `client`
(id, client_name, client_id, client_secret, client_id_issued_at, client_secret_expires_at,
 authorization_grant_types,client_authentication_methods, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES
    ('1', 'Client 1', 'client1', 'secret1', NOW(), DATE_ADD(NOW(), INTERVAL 1 HOUR), 'method1,method2', 'grantType1,grantType2', 'uri1,uri2', 'postLogoutUri1,postLogoutUri2', 'scope1,scope2', '{"setting1":"value1", "setting2":"value2"}', '{"setting1":"value1", "setting2":"value2"}'),
    ('2', 'Client 2', 'client2', 'secret2', NOW(), DATE_ADD(NOW(), INTERVAL 1 HOUR), 'method3,method4', 'grantType3,grantType4', 'uri3,uri4', 'postLogoutUri3,postLogoutUri4', 'scope3,scope4', '{"setting1":"value1", "setting2":"value2"}', '{"setting1":"value1", "setting2":"value2"}'),
    ('3', 'Client 3', 'client3', 'secret3', NOW(), DATE_ADD(NOW(), INTERVAL 1 HOUR), 'method5,method6', 'grantType5,grantType6', 'uri5,uri6', 'postLogoutUri5,postLogoutUri6', 'scope5,scope6', '{"setting1":"value1", "setting2":"value2"}', '{"setting1":"value1", "setting2":"value2"}'),
    ('4', 'Client 4', 'client4', 'secret4', NOW(), DATE_ADD(NOW(), INTERVAL 1 HOUR), 'method7,method8', 'grantType7,grantType8', 'uri7,uri8', 'postLogoutUri7,postLogoutUri8', 'scope7,scope8', '{"setting1":"value1", "setting2":"value2"}', '{"setting1":"value1", "setting2":"value2"}'),
    ('5', 'Client 5', 'client5', 'secret5', NOW(), DATE_ADD(NOW(), INTERVAL 1 HOUR), 'method9,method10', 'grantType9,grantType10', 'uri9,uri10', 'postLogoutUri9,postLogoutUri10', 'scope9,scope10', '{"setting1":"value1", "setting2":"value2"}', '{"setting1":"value1", "setting2":"value2"}');

# JDBC exception executing SQL [c1_0.,c1_0.redirect_uris,c1_0.scopes,c1_0.token_settings from `client` c1_0 where c1_0.client_id=?]


CREATE TABLE authorization (
                               id varchar(255) NOT NULL,
                               registeredClientId varchar(255) NOT NULL,
                               principalName varchar(255) NOT NULL,
                               authorizationGrantType varchar(255) NOT NULL,
                               authorizedScopes varchar(500) DEFAULT NULL,
                               attributes varchar(500) DEFAULT NULL,
                               state varchar(500) DEFAULT NULL,
                               authorizationCodeValue varchar(500) DEFAULT NULL,
                               authorizationCodeIssuedAt timestamp DEFAULT NULL,
                               authorizationCodeExpiresAt timestamp DEFAULT NULL,
                               authorizationCodeMetadata varchar(500) DEFAULT NULL,
                               accessTokenValue varchar(500) DEFAULT NULL,
                               accessTokenIssuedAt timestamp DEFAULT NULL,
                               accessTokenExpiresAt timestamp DEFAULT NULL,
                               accessTokenMetadata varchar(500) DEFAULT NULL,
                               accessTokenType varchar(255) DEFAULT NULL,
                               accessTokenScopes varchar(500) DEFAULT NULL,
                               refreshTokenValue varchar(500) DEFAULT NULL,
                               refreshTokenIssuedAt timestamp DEFAULT NULL,
                               refreshTokenExpiresAt timestamp DEFAULT NULL,
                               refreshTokenMetadata varchar(500) DEFAULT NULL,
                               oidcIdTokenValue varchar(500) DEFAULT NULL,
                               oidcIdTokenIssuedAt timestamp DEFAULT NULL,
                               oidcIdTokenExpiresAt timestamp DEFAULT NULL,
                               oidcIdTokenMetadata varchar(500) DEFAULT NULL,
                               oidcIdTokenClaims varchar(500) DEFAULT NULL,
                               userCodeValue varchar(500) DEFAULT NULL,
                               userCodeIssuedAt timestamp DEFAULT NULL,
                               userCodeExpiresAt timestamp DEFAULT NULL,
                               userCodeMetadata varchar(2000) DEFAULT NULL,
                               deviceCodeValue varchar(500) DEFAULT NULL,
                               deviceCodeIssuedAt timestamp DEFAULT NULL,
                               deviceCodeExpiresAt timestamp DEFAULT NULL,
                               deviceCodeMetadata varchar(2000) DEFAULT NULL,
                               PRIMARY KEY (id)
);


CREATE TABLE authorizationConsent (
                                      registeredClientId varchar(255) NOT NULL,
                                      principalName varchar(255) NOT NULL,
                                      authorities varchar(1000) NOT NULL,
                                      PRIMARY KEY (registeredClientId, principalName)
);