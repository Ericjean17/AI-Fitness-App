import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
// Create a global Redux store (holds all app state)
export const store = configureStore({
  reducer: {
    auth: authReducer
  }
})