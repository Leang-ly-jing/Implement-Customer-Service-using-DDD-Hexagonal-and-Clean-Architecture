import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom"
import LoginPage from "./pages/auth/LoginPage"
import RegisterPage from "./pages/auth/RegisterPage"
import AccountLayout from "./layouts/account-layout"
import OverviewPage from "./pages/account/OverviewPage"
import ProfilePage from "./pages/account/ProfilePage"
import SecurityPage from "./pages/account/SecurityPage"
import LinkedAccountsPage from "./pages/account/LinkedAccountsPage"
import SessionsPage from "./pages/account/SessionsPage"
import LoginHistoryPage from "./pages/account/LoginHistoryPage"
import ActivityPage from "./pages/account/ActivityPage"
import DeactivatePage from "./pages/account/DeactivatePage"
import AdminOverviewPage from "./pages/admin/AdminOverviewPage"
import UsersPage from "./pages/admin/UsersPage"
import RolesPage from "./pages/admin/RolesPage"
import PermissionsPage from "./pages/admin/PermissionsPage"
import ClientsPage from "./pages/admin/ClientsPage"
import AdminLoginHistoryPage from "./pages/admin/AdminLoginHistoryPage"
import { AdminAuditLogPage } from "./pages/admin/AdminAuditLogPage"
import ProtectedRoute from "./components/ProtectedRoute"
import { Toaster } from "@/components/ui/toast"

export default function App() {
  return (
    <BrowserRouter>
      <Toaster />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/account" element={<AccountLayout />}>
            <Route index element={<OverviewPage />} />
            <Route path="profile" element={<ProfilePage />} />
            <Route path="security" element={<SecurityPage />} />
            <Route path="linked" element={<LinkedAccountsPage />} />
            <Route path="sessions" element={<SessionsPage />} />
            <Route path="login-history" element={<LoginHistoryPage />} />
            <Route path="activity" element={<ActivityPage />} />
            <Route path="deactivate" element={<DeactivatePage />} />
          </Route>
        </Route>

        <Route element={<ProtectedRoute allowedRoles={["ROLE_ADMIN", "ADMIN"]} />}>
          <Route path="/admin" element={<AccountLayout />}>
            <Route index element={<AdminOverviewPage />} />
            <Route path="users" element={<UsersPage />} />
            <Route path="roles" element={<RolesPage />} />
            <Route path="permissions" element={<PermissionsPage />} />
            <Route path="clients" element={<ClientsPage />} />
            <Route path="applications" element={<ClientsPage />} />
            <Route path="login-history" element={<AdminLoginHistoryPage />} />
            <Route path="audit-log" element={<AdminAuditLogPage />} />
          </Route>
        </Route>

        <Route path="/" element={<Navigate to="/account" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
