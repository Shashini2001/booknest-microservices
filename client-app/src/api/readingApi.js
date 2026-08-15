import axiosClient from "./axiosClient";

export const getForUser = (userId) => axiosClient.get(`/reading/${userId}`);
export const getFavorites = (userId) => axiosClient.get(`/reading/${userId}/favorites`);
export const getStats = (userId) => axiosClient.get(`/reading/${userId}/stats`);
export const addReadingEntry = (entry) => axiosClient.post("/reading", entry);
export const updateReadingEntry = (id, updates) => axiosClient.put(`/reading/entry/${id}`, updates);
export const deleteReadingEntry = (id) => axiosClient.delete(`/reading/entry/${id}`);
