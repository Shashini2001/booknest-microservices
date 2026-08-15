import axiosClient from "./axiosClient";

export const getDeliveryByOrderId = (orderId) => axiosClient.get(`/deliveries/${orderId}`);
export const getAllDeliveries = () => axiosClient.get("/deliveries");
