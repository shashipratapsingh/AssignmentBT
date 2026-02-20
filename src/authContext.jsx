import React, { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token"));

  const value = useMemo(() => ({
    token,
    setToken: (t) => {
      setToken(t);
      if (t) localStorage.setItem("token", t);
      else localStorage.removeItem("token");
    },
    logout: () => {
      setToken(null);
      localStorage.removeItem("token");
      window.location.href = "/login";
    }
  }), [token]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
