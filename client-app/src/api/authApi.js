import axiosClient from "./axiosClient";

// These call the Gateway, which forwards /api/auth/** to the Auth Service (Student 1)
export const login = (email, password) => axiosClient.post("/auth/login", { email, password });
export const register = (fullName, email, password) =>
  axiosClient.post("/auth/register", { fullName, email, password });
