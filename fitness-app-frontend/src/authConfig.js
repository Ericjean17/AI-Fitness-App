export const authConfig = {
  clientId: 'oauth2-pkce-client', // keycloak client id
  //where to login  
  authorizationEndpoint: 'http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth',
  
  //where to get tokens (AuthProvider exchanges code for JWT access token and refresh token)
  tokenEndpoint: 'http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/token',
  
  // where keycloak sends user back with an auth code
  redirectUri: 'http://localhost:5173',
  scope: 'openid profile email offline_access', // what permissions to request
  onRefreshTokenExpire: (event) => event.logIn(),
}