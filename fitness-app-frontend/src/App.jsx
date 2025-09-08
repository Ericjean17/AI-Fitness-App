import { Button, Box, Typography } from "@mui/material";
import{ useState, useEffect, useContext } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router"
import { setCredentials } from "./store/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail";

const ActivitiesPage = () => {
    return (<Box sx={{ p: 2, border: '1px dashed grey' }}>
        <ActivityForm onActivityAdded={() => window.location.reload()}/>
        <ActivityList />
      </Box>
    )
}

function App() {
  //App receives tokens in AuthContext (token(raw JWT string) & tokenData(sub, email, etc)
  const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext);
  const dispatch = useDispatch(); // let components use global state
  const [authReady, setAuthReady] = useState(false);

  // When token exists, save to redux
  useEffect(() => {
    if (token) {
      // authSlice saves user info (tokenData), token, and userId in user.sub, saves to localStorage
      dispatch(setCredentials({token, user: tokenData})); // asves to redux store
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch])

  return (
    <>
      <Router>
        {!token ? (
          <Box
            sx={{
              height: "100vh",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              justifyContent: "center",
              textAlign: "center"
            }}
          >
            <Typography variant="h4" gutterBottom>
              Welcome to the Fitness Tracker App
            </Typography>
            <Typography variant="subtitle1" sx={{ mb: 3 }}>
              Please login to access your activities
            </Typography>
            <Button variant="contained" 
                color="primary"
                size="large"
                onClick={() => { // redirects to keycloak login page from AuthContext
                  logIn();
                }}
                > LOGIN </Button>
          </Box>
        ) : (
          <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
            <Button variant="contained" color="secondary" onClick={logOut}>Logout</Button>
            <Routes>
              <Route path="/activities" element={<ActivitiesPage/>} />
              <Route path="/activities/:id" element={<ActivityDetail />} />
              <Route path="/" element={token ? <Navigate to="/activities" replace /> : <div>Welcome! Please Login.</div>} />
            </Routes>
          </Box>
        )}
      </Router>
    </>
  )
}

export default App
