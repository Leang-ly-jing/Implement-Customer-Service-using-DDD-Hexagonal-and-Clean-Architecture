import { useState, useEffect, useMemo } from "react"
import { Plus, RefreshCw, Search } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Card, CardHeader, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"
import { UserTable } from "./user/UserTable"
import { UserCreateModal } from "./user/UserCreateModal"
import { UserEditModal } from "./user/UserEditModal"
import { UserDetailModal } from "./user/UserDetailModal"
import { UserDeleteModal } from "./user/UserDeleteModal"
import { UserRolesModal } from "./user/UserRolesModal"
import { UserPermissionsModal } from "./user/UserPermissionsModal"
import { UserSessionsModal } from "./user/UserSessionsModal"
import { UserLoginHistoryModal } from "./user/UserLoginHistoryModal"

export function UsersPage() {
  const { t } = useTranslation()
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState("")
  const [statusFilter, setStatusFilter] = useState("ALL")

  const [createModalOpen, setCreateModalOpen] = useState(false)
  const [editingUser, setEditingUser] = useState(null)
  const [detailUser, setDetailUser] = useState(null)
  const [userToDelete, setUserToDelete] = useState(null)
  const [roleTargetUser, setRoleTargetUser] = useState(null)
  const [permTargetUser, setPermTargetUser] = useState(null)
  const [sessionTargetUser, setSessionTargetUser] = useState(null)
  const [historyTargetUser, setHistoryTargetUser] = useState(null)

  const fetchUsers = async () => {
    setLoading(true)
    try {
      const res = await axiosClient.get("/users?page=0&size=50&sort=createdAt,desc")
      setUsers(res.data?.content || [])
    } catch (err) {
      console.error("Failed to load users:", err)
      toast.error("Error", "Could not load user accounts.")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchUsers()
  }, [])

  const filteredUsers = useMemo(() => {
    return users.filter((u) => {
      const matchesSearch =
        search === "" ||
        u.username?.toLowerCase().includes(search.toLowerCase()) ||
        u.email?.toLowerCase().includes(search.toLowerCase()) ||
        u.name?.toLowerCase().includes(search.toLowerCase())

      if (!matchesSearch) return false

      if (statusFilter === "ACTIVE") return u.enabled && u.accountNonLocked !== false
      if (statusFilter === "DISABLED") return !u.enabled
      if (statusFilter === "LOCKED") return u.accountNonLocked === false
      return true
    })
  }, [users, search, statusFilter])

  const handleToggleActivate = async (u) => {
    const isActivating = !u.enabled
    try {
      if (isActivating) {
        await axiosClient.post(`/users/${u.uuid}/enable`)
        toast.success("Account Activated", `User ${u.username} has been enabled.`)
      } else {
        await axiosClient.post(`/users/${u.uuid}/disable`)
        toast.success("Account Disabled", `User ${u.username} has been disabled.`)
      }
      setUsers((prev) =>
        prev.map((item) => (item.uuid === u.uuid ? { ...item, enabled: isActivating } : item))
      )
    } catch (err) {
      toast.error("Action Failed", err.response?.data?.detail || "Could not change active status.")
    }
  }

  const handleToggleLock = async (u) => {
    const isCurrentlyLocked = u.accountNonLocked === false
    try {
      if (isCurrentlyLocked) {
        await axiosClient.post(`/users/${u.uuid}/unlock`)
        toast.success("Account Unlocked", `User ${u.username} has been unlocked.`)
        setUsers((prev) =>
          prev.map((item) => (item.uuid === u.uuid ? { ...item, accountNonLocked: true } : item))
        )
      } else {
        await axiosClient.post(`/users/${u.uuid}/lock`)
        toast.success("Account Locked", `User ${u.username} has been locked.`)
        setUsers((prev) =>
          prev.map((item) => (item.uuid === u.uuid ? { ...item, accountNonLocked: false } : item))
        )
      }
    } catch (err) {
      toast.error("Action Failed", err.response?.data?.detail || "Could not change lock status.")
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={t("usersPage.title")}
          description={t("usersPage.subtitle")}
        />
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm" onClick={fetchUsers} disabled={loading}>
            <RefreshCw className={`size-4 ${loading ? "animate-spin" : ""}`} />
            <span className="hidden sm:inline">{t("common.refresh")}</span>
          </Button>
          <Button size="sm" onClick={() => setCreateModalOpen(true)} className="gap-1.5">
            <Plus className="size-4" />
            <span>{t("usersPage.addUser")}</span>
          </Button>
        </div>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-2.5 top-2.5 size-4 text-muted-foreground" />
              <Input
                placeholder={t("usersPage.searchUsers")}
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-8"
              />
            </div>

            <div className="flex items-center gap-1.5 overflow-x-auto pb-1 md:pb-0">
              {["ALL", "ACTIVE", "DISABLED", "LOCKED"].map((f) => (
                <Button
                  key={f}
                  variant={statusFilter === f ? "default" : "outline"}
                  size="xs"
                  onClick={() => setStatusFilter(f)}
                  className="rounded-full text-xs"
                >
                  {f === "ALL" && t("common.all")}
                  {f === "ACTIVE" && t("common.active")}
                  {f === "DISABLED" && t("common.disabled")}
                  {f === "LOCKED" && t("common.locked")}
                </Button>
              ))}
            </div>
          </div>
        </CardHeader>

        <CardContent className="p-0">
          <UserTable
            users={filteredUsers}
            loading={loading}
            onViewDetails={(u) => setDetailUser(u)}
            onEdit={(u) => setEditingUser(u)}
            onAssignRoles={(u) => setRoleTargetUser(u)}
            onAssignPermissions={(u) => setPermTargetUser(u)}
            onViewSessions={(u) => setSessionTargetUser(u)}
            onViewHistory={(u) => setHistoryTargetUser(u)}
            onToggleActivate={handleToggleActivate}
            onToggleLock={handleToggleLock}
            onDelete={(u) => setUserToDelete(u)}
          />
        </CardContent>
      </Card>

      <UserCreateModal
        isOpen={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        onUserCreated={fetchUsers}
      />

      <UserEditModal
        isOpen={!!editingUser}
        user={editingUser}
        onClose={() => setEditingUser(null)}
        onUserUpdated={fetchUsers}
      />

      <UserDetailModal
        isOpen={!!detailUser}
        user={detailUser}
        onClose={() => setDetailUser(null)}
      />

      <UserDeleteModal
        isOpen={!!userToDelete}
        user={userToDelete}
        onClose={() => setUserToDelete(null)}
        onUserDeleted={fetchUsers}
      />

      <UserRolesModal
        isOpen={!!roleTargetUser}
        user={roleTargetUser}
        onClose={() => setRoleTargetUser(null)}
        onRolesUpdated={fetchUsers}
      />

      <UserPermissionsModal
        isOpen={!!permTargetUser}
        user={permTargetUser}
        onClose={() => setPermTargetUser(null)}
        onPermissionsUpdated={fetchUsers}
      />

      <UserSessionsModal
        isOpen={!!sessionTargetUser}
        user={sessionTargetUser}
        onClose={() => setSessionTargetUser(null)}
      />

      <UserLoginHistoryModal
        isOpen={!!historyTargetUser}
        user={historyTargetUser}
        onClose={() => setHistoryTargetUser(null)}
      />
    </div>
  )
}

export default UsersPage
