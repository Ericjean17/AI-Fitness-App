// import { StrictMode } from 'react'
// import { createRoot } from 'react-dom/client'
// import App from './App.jsx'

// createRoot(document.getElementById('root')).render(
//   <StrictMode>
//     <App />
//   </StrictMode>,
// )
import React from 'react'
import ReactDOM from 'react-dom/client'

import { Provider } from 'react-redux'
import { store } from './store/store'

import App from './App'
import { AuthProvider } from 'react-oauth2-code-pkce'
import { authConfig } from './authConfig'

// As of React 18
const root = ReactDOM.createRoot(document.getElementById('root'))
root.render(
  //  AuthProvider starts listening for OAuth tokens. After keycloak redirects back here with an auth code, 
  // calls Keycloak's tokenEndpoint and gets back JWT access token and refresh token exchanges code for token
  <AuthProvider authConfig={authConfig} loadingComponent={<div>Loading...</div>}>
    <Provider store={store}> {/*Initializes with authSlice*/}
      <App />
    </Provider>,
  </AuthProvider>
)