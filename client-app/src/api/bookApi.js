import axiosClient from "./axiosClient";

export const getAllBooks = () => axiosClient.get("/books");
export const getBooksByCategory = (category) => axiosClient.get(`/books/category/${category}`);
export const getBookById = (id) => axiosClient.get(`/books/${id}`);
export const searchBooks = (keyword) => axiosClient.get("/books/search", { params: { keyword } });
export const createBook = (book) => axiosClient.post("/books", book);
export const updateBook = (id, book) => axiosClient.put(`/books/${id}`, book);
export const deleteBook = (id) => axiosClient.delete(`/books/${id}`);
