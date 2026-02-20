import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom"; // <-- import this

export default function Register() {
  const navigate = useNavigate(); // <-- initialize navigation
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [accountType, setAccountType] = useState("SAVINGS");
  const [error, setError] = useState("");
  const [msg, setMsg] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    setMsg("");

    try {
      const res = await axios.post("http://localhost:8081/api/auth/register", {
        email,
        password,
        fullName,
        phone,
      });

      const token = res.data.token;
      if (!token) {
        throw new Error("Registration succeeded but token not returned");
      }

      // Create user account with selected account type
      await axios.post(
        "http://localhost:8082/api/accounts",
        { accountType },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      setMsg("Registration and account creation successful!");
      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.message ||
          "Registration or account creation failed"
      );
    }
  };

  return (
    <div className="container" style={{ maxWidth: 420 }}>
      <h3 className="mb-3">Register</h3>
      {msg && <div className="alert alert-success">{msg}</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={submit}>
        <div className="mb-3">
          <label className="form-label">Full Name</label>
          <input
            className="form-control"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Phone</label>
          <input
            className="form-control"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            required
            pattern="^\d{10}$"
            title="Enter a 10-digit phone number"
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Email</label>
          <input
            className="form-control"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            className="form-control"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={6}
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Account Type</label>
          <select
            className="form-select"
            value={accountType}
            onChange={(e) => setAccountType(e.target.value)}
          >
            <option value="SAVINGS">Savings</option>
            <option value="CURRENT">Current</option>
          </select>
        </div>
        <button className="btn btn-primary w-100" type="submit">
          Register
        </button>
      </form>
    </div>
  );
}