import React, { createContext, useContext, useState, useEffect } from "react";

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const [items, setItems] = useState(() => {
    const saved = localStorage.getItem("cart");
    return saved ? JSON.parse(saved) : [];
  });

  useEffect(() => {
    localStorage.setItem("cart", JSON.stringify(items));
  }, [items]);

  const addToCart = (book) => {
    setItems((prev) => {
      const existing = prev.find((i) => i.bookId === book.id);
      if (existing) {
        return prev.map((i) => (i.bookId === book.id ? { ...i, quantity: i.quantity + 1 } : i));
      }
      return [...prev, { bookId: book.id, title: book.title, coverUrl: book.coverUrl, unitPrice: book.price, quantity: 1 }];
    });
  };

  const updateQuantity = (bookId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(bookId);
      return;
    }
    setItems((prev) => prev.map((i) => (i.bookId === bookId ? { ...i, quantity } : i)));
  };

  const removeFromCart = (bookId) => {
    setItems((prev) => prev.filter((i) => i.bookId !== bookId));
  };

  const clearCart = () => setItems([]);

  const subtotal = items.reduce((sum, i) => sum + i.unitPrice * i.quantity, 0);

  return (
    <CartContext.Provider value={{ items, addToCart, updateQuantity, removeFromCart, clearCart, subtotal }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  return useContext(CartContext);
}
