import { createSlice } from '@reduxjs/toolkit'

// Create an auth "slice" (section of global state) that manages authentication state
const authSlice = createSlice({
  name: 'auth', // name of slice
  initialState: { // initial state when app loads
    user: JSON.parse(localStorage.getItem('user')) || null, // load saved user data, token, & userId
    token: localStorage.getItem('token') || null,
    userId: localStorage.getItem('userId') || null
  },
  reducers: { // functions that modify the state
    setCredentials: (state, action) => { // save login data to state & local storage
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.userId = action.payload.user.sub; // sub contains userId

      localStorage.setItem("token", action.payload.token);
      localStorage.setItem("user", JSON.stringify(action.payload.user));
      localStorage.setItem("userId", action.payload.user.sub);
    },
    logout: (state) => { // clear redux state & local storage
      state.user = null;
      state.token = null;
      state.userId = null;
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      localStorage.removeItem("userId");
    },
  },
});

export const { setCredentials, logout } = authSlice.actions
export default authSlice.reducer