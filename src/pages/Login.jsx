import React, { useState } from "react";
import { apiFetch } from "../api";
import { useAuth } from "../authContext";
import  axios from 'axios';

export default function Login() {
  const { setToken } = useAuth();
  const [email, setEmail] = useState("alice@example.com");
  const [password, setPassword] = useState("Password@123");
  const [error, setError] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await axios.post('http://localhost:8081/api/auth/login', {
      email: email,
      password: password
    });
      setToken(res.data.token);
      window.location.href = "/";
    } catch (err) {
      setError(err.message || "Login failed");
    }
  };

  return (
    <div className="container" style={{ maxWidth: 420 }}>
      <h3 className="mb-3">Login</h3>
      {error ? <div className="alert alert-danger">{error}</div> : null}
      <form onSubmit={submit}>
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} />
        </div>
        <div className="mb-3">
          <label className="form-label">Password</label>
          <input className="form-control" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button className="btn btn-primary w-100" type="submit">Sign in</button>
      </form>
      <div className="text-muted mt-3" style={{ fontSize: 13 }}>
        Seeded users: alice@example.com / bob@example.com (Password@123)
      </div>
    </div>
  );
}
