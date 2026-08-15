import axiosClient from "./axiosClient";

export const checkout = (order) => axiosClient.post("/orders/checkout", order);
export const getOrders = (userId) => axiosClient.get("/orders", { params: userId ? { userId } : {} });
export const getOrderById = (id) => axiosClient.get(`/orders/${id}`);
export const updateOrderStatus = (id, status) => axiosClient.put(`/orders/${id}/status`, { status });
export const cancelOrder = (id) => axiosClient.delete(`/orders/${id}`);
