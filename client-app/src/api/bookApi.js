import axiosClient from "./axiosClient";

// These call the Gateway, which forwards /api/books/** to the Book Catalog Service
export const getAllBooks = () => axiosClient.get("/books");
export const getBooksByCategory = (category) => axiosClient.get(`/books/category/${category}`);
export const getBookById = (id) => axiosClient.get(`/books/${id}`);
export const searchBooks = (keyword) => axiosClient.get(`/books/search`, { params: { keyword } });
