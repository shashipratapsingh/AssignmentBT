import React from "react";
import { Link, useLocation } from "react-router-dom";
import { useAuth } from "../authContext";
export default function NavBar() {
  const { logout } = useAuth();
  const loc = useLocation();

  return (
    <nav className="navbar navbar-expand navbar-dark bg-dark mb-4">
      <div className="container">
        <Link className="navbar-brand" to="/">Banking App</Link>
        <div className="navbar-nav me-auto">
          <Link className={"nav-link" + (loc.pathname === "/" ? " active" : "")} to="/">Dashboard</Link>
          <Link className={"nav-link" + (loc.pathname === "/transfer" ? " active" : "")} to="/transfer">Transfer</Link>
          <Link className={"nav-link" + (loc.pathname === "/transactions" ? " active" : "")} to="/transactions">Transactions</Link>
        </div>
        <button className="btn btn-outline-light btn-sm" onClick={logout}>Logout</button>
      </div>
    </nav>
  );
}
