import { Navigate, Outlet, useLocation } from "react-router-dom"
import { getCurrentUserRoles, hasAdminRole } from "@/lib/auth-utils"

export default function ProtectedRoute({ allowedRoles }) {
  const location = useLocation()
  const searchParams = new URLSearchParams(location.search)
  const queryToken = searchParams.get("token")

  if (queryToken) {
    localStorage.setItem("access_token", queryToken)
    const queryUsername = searchParams.get("username")
    if (queryUsername) {
      localStorage.setItem("user", JSON.stringify({ username: queryUsername, roles: [] }))
    }
    window.history.replaceState({}, document.title, location.pathname)
  }

  const token = localStorage.getItem("access_token")

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location }} />
  }

  if (allowedRoles && allowedRoles.length > 0) {
    const userRoles = getCurrentUserRoles()
    const isAdminAllowed = allowedRoles.some(
      (r) => r.toUpperCase() === "ADMIN" || r.toUpperCase() === "ROLE_ADMIN"
    )

    const isAuthorized = isAdminAllowed
      ? hasAdminRole(userRoles)
      : allowedRoles.some((r) => userRoles.includes(r.toUpperCase()))

    if (!isAuthorized) {
      return <Navigate to="/account/profile" replace />
    }
  }

  return <Outlet />
}
