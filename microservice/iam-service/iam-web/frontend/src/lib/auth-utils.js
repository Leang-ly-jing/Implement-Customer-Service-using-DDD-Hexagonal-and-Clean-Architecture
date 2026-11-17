export function parseJwt(token) {
  try {
    if (!token || typeof token !== "string") return null
    const parts = token.split(".")
    if (parts.length < 2) return null
    const base64Url = parts[1]
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/")
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    )
    return JSON.parse(jsonPayload)
  } catch {
    return null
  }
}

export function getCurrentUserRoles() {
  const roles = new Set()

  try {
    const storedUser = JSON.parse(localStorage.getItem("user") || "{}")
    if (Array.isArray(storedUser.roles)) {
      storedUser.roles.forEach((r) => {
        const name = typeof r === "string" ? r : r?.name
        if (name) roles.add(name.toUpperCase())
      })
    }
  } catch {

  }

  const token = localStorage.getItem("access_token")
  if (token) {
    const decoded = parseJwt(token)
    if (decoded) {
      if (Array.isArray(decoded.roles)) {
        decoded.roles.forEach((r) => roles.add(String(r).toUpperCase()))
      }
      if (Array.isArray(decoded.authorities)) {
        decoded.authorities.forEach((a) => {
          const str = String(a).toUpperCase()
          if (str.startsWith("ROLE_") || str === "ADMIN" || str === "USER") {
            roles.add(str)
          }
        })
      }
    }
  }

  return Array.from(roles)
}

export function hasAdminRole(roleList) {
  const roles = Array.isArray(roleList) ? roleList : getCurrentUserRoles()
  return roles.some((r) => {
    const upper = typeof r === "string" ? r.toUpperCase() : (r?.name || "").toUpperCase()
    return (
      upper === "ADMIN" ||
      upper === "ROLE_ADMIN" ||
      upper === "SUPER_ADMIN" ||
      upper === "ROLE_SUPER_ADMIN"
    )
  })
}
