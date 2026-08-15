import React from "react";

export default function LoadingState({ text = "Loading..." }) {
  return <p className="loading-text">{text}</p>;
}
