export function apiFetch(path, opts = {}) {
  const token = localStorage.getItem("token");
  const headers = { "Content-Type": "application/json", ...(opts.headers || {}) };
  if (token) headers.Authorization = `Bearer ${token}`;

  return fetch(`http://localhost:8080${path}`, { ...opts, headers })
    .then(async (res) => {
      const isJson = (res.headers.get("content-type") || "").includes("application/json");
      const body = isJson ? await res.json().catch(() => null) : null;

      if (res.status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login";
        return;
      }

      if (!res.ok) {
        const msg = body?.message || `Request failed (${res.status})`;
        throw new Error(msg);
      }
      return body;
    });
}
