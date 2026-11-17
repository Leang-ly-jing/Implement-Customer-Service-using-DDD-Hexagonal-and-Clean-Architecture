import { useState, useEffect } from "react"
import { Shield, Plus, Search, RefreshCw } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Card, CardHeader, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"
import { RolesTable } from "./role/RolesTable"
import { RoleCreateModal } from "./role/RoleCreateModal"
import { RoleEditModal } from "./role/RoleEditModal"
import { RoleDeleteDialog } from "./role/RoleDeleteDialog"
import { RolePermissionsModal } from "./role/RolePermissionsModal"

export default function RolesPage() {
  const [roles, setRoles] = useState([])
  const [pageMeta, setPageMeta] = useState({ number: 0, size: 10, totalElements: 0, totalPages: 0 })
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState("")
  const [statusFilter, setStatusFilter] = useState("ALL")
  const [currentPage, setCurrentPage] = useState(0)

  const [allPermissions, setAllPermissions] = useState([])

  const [createOpen, setCreateOpen] = useState(false)
  const [createSubmitting, setCreateSubmitting] = useState(false)

  const [editingRole, setEditingRole] = useState(null)
  const [editSubmitting, setEditSubmitting] = useState(false)

  const [deletingRole, setDeletingRole] = useState(null)
  const [deleteSubmitting, setDeleteSubmitting] = useState(false)

  const [permRole, setPermRole] = useState(null)
  const [rolePermissions, setRolePermissions] = useState([])
  const [permLoading, setPermLoading] = useState(false)
  const [assignSubmitting, setAssignSubmitting] = useState(false)

  const { i18n } = useTranslation()
  const rawLang = i18n?.language || localStorage.getItem("lang") || "kh"
  const isKhmer = rawLang.startsWith("kh") || rawLang.startsWith("km")
  const lang = isKhmer ? "km" : "en"

  const fetchRoles = async (page = 0) => {
    setLoading(true)
    try {
      let url = `/roles?page=${page}&size=10&sort=createdAt,desc`
      if (search.trim()) {
        url += `&search=${encodeURIComponent(search.trim())}`
      }
      if (statusFilter === "ACTIVE") {
        url += `&enabled=true`
      } else if (statusFilter === "INACTIVE") {
        url += `&enabled=false`
      }
      const res = await axiosClient.get(url)
      setRoles(res.data?.content || [])
      setPageMeta(res.data?.page || { number: 0, size: 10, totalElements: 0, totalPages: 0 })
      setCurrentPage(page)
    } catch (err) {
      console.error("Failed to fetch roles:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Could not retrieve roles list."
      )
    } finally {
      setLoading(false)
    }
  }

  const fetchAllPermissions = async () => {
    try {
      const res = await axiosClient.get("/permissions?page=0&size=100&sort=name,asc")
      setAllPermissions(res.data?.content || [])
    } catch (err) {
      console.error("Failed to load permissions catalog:", err)
    }
  }

  useEffect(() => {
    fetchRoles(0)
    fetchAllPermissions()
  }, [statusFilter])

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    fetchRoles(0)
  }

  const handleCreateSubmit = async (form) => {
    if (!form.name.trim()) {
      toast.error(
        lang === "km" ? "កំហុស" : "Validation",
        lang === "km" ? "សូមបញ្ចូលឈ្មោះ Role" : "Role name is required."
      )
      return
    }
    setCreateSubmitting(true)
    try {
      await axiosClient.post("/roles", {
        name: form.name.trim().toUpperCase(),
        description: form.description.trim() || undefined,
        permissionUuids: form.permissionUuids,
      })
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Created",
        lang === "km" ? "បានបង្កើត Role ថ្មីដោយជោគជ័យ" : "Role created successfully."
      )
      setCreateOpen(false)
      fetchRoles(0)
    } catch (err) {
      console.error("Create role error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to create role."
      )
    } finally {
      setCreateSubmitting(false)
    }
  }

  const handleEditSubmit = async (form) => {
    if (!editingRole) return
    setEditSubmitting(true)
    try {
      await axiosClient.patch(`/roles/${editingRole.uuid}`, {
        name: form.name.trim().toUpperCase(),
        description: form.description.trim() || undefined,
      })
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Updated",
        lang === "km" ? "បានកែប្រែព័ត៌មាន Role រួចរាល់" : "Role updated successfully."
      )
      setEditingRole(null)
      fetchRoles(currentPage)
    } catch (err) {
      console.error("Update role error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to update role."
      )
    } finally {
      setEditSubmitting(false)
    }
  }

  const handleDeleteConfirm = async () => {
    if (!deletingRole) return
    setDeleteSubmitting(true)
    try {
      await axiosClient.delete(`/roles/${deletingRole.uuid}`)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Deleted",
        lang === "km" ? "បានលុប Role ដោយជោគជ័យ" : "Role deleted successfully."
      )
      setDeletingRole(null)
      fetchRoles(currentPage)
    } catch (err) {
      console.error("Delete role error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to delete role."
      )
    } finally {
      setDeleteSubmitting(false)
    }
  }

  const handleToggleStatus = async (role) => {
    try {
      const endpoint = role.active ? `/roles/${role.uuid}/deactivate` : `/roles/${role.uuid}/activate`
      await axiosClient.post(endpoint)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Updated",
        role.active
          ? (lang === "km" ? "បានបិទដំណើរការ Role" : "Role deactivated.")
          : (lang === "km" ? "បានបើកដំណើរការ Role" : "Role activated.")
      )
      fetchRoles(currentPage)
    } catch (err) {
      console.error("Status toggle error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to change role status."
      )
    }
  }

  const openPermissionsModal = async (role) => {
    setPermRole(role)
    setPermLoading(true)
    try {
      const res = await axiosClient.get(`/roles/${role.uuid}`)
      setRolePermissions(res.data?.permissions || [])
    } catch (err) {
      console.error("Failed to load role permissions:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Could not load role details."
      )
    } finally {
      setPermLoading(false)
    }
  }

  const handleRevokePermission = async (permissionUuid) => {
    if (!permRole) return
    try {
      await axiosClient.delete(`/roles/${permRole.uuid}/permissions/${permissionUuid}?reason=REVOKED`)
      setRolePermissions((prev) => prev.filter((p) => p.uuid !== permissionUuid))
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Revoked",
        lang === "km" ? "បានដកសិទ្ធិចេញពី Role រួចរាល់" : "Permission revoked successfully."
      )
    } catch (err) {
      console.error("Revoke permission error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to revoke permission."
      )
    }
  }

  const handleAssignPermissions = async (selectedUuids) => {
    if (!permRole || selectedUuids.length === 0) return
    setAssignSubmitting(true)
    try {
      await axiosClient.post(`/roles/${permRole.uuid}/permissions`, {
        permissionUuids: selectedUuids,
        reason: "ADMIN_ASSIGNMENT",
      })
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Assigned",
        lang === "km" ? "បានផ្ដល់សិទ្ធិបន្ថែមទៅ Role រួចរាល់" : "Permissions assigned successfully."
      )
      const res = await axiosClient.get(`/roles/${permRole.uuid}`)
      setRolePermissions(res.data?.permissions || [])
    } catch (err) {
      console.error("Assign permissions error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to assign permissions."
      )
    } finally {
      setAssignSubmitting(false)
    }
  }

  return (
    <div className="space-y-6">

      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={lang === "km" ? "ការគ្រប់គ្រង Roles" : "Role Management"}
          description={
            lang === "km"
              ? "គ្រប់គ្រងតួនាទី (Roles) នៅក្នុងប្រព័ន្ធ បង្កើត កែប្រែ លុប និងកំណត់ Permissions សម្រាប់ Role នីមួយៗ"
              : "Create, view, update, and delete roles, and assign or remove permission scopes."
          }
        />
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm" onClick={() => fetchRoles(currentPage)} disabled={loading}>
            <RefreshCw className={`size-3.5 ${loading ? "animate-spin" : ""}`} />
            <span className="hidden sm:inline">{lang === "km" ? "ផ្ទុកឡើងវិញ" : "Refresh"}</span>
          </Button>
          <Button size="sm" onClick={() => setCreateOpen(true)} className="gap-1.5">
            <Plus className="size-4" />
            <span>{lang === "km" ? "បង្កើត Role ថ្មី" : "Create Role"}</span>
          </Button>
        </div>
      </div>

      <Card className="shadow-xs border">
        <CardHeader className="py-3 px-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <form onSubmit={handleSearchSubmit} className="relative flex-1 max-w-sm">
              <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-4 text-muted-foreground" />
              <Input
                placeholder={lang === "km" ? "ស្វែងរកតាមឈ្មោះ Role..." : "Search roles..."}
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-8 h-9 text-xs"
              />
            </form>

            <div className="flex items-center gap-1.5 overflow-x-auto pb-1 sm:pb-0">
              {["ALL", "ACTIVE", "INACTIVE"].map((f) => (
                <Button
                  key={f}
                  type="button"
                  variant={statusFilter === f ? "default" : "outline"}
                  size="xs"
                  onClick={() => setStatusFilter(f)}
                  className="rounded-full text-xs h-7"
                >
                  {f === "ALL" && (lang === "km" ? "ទាំងអស់" : "All")}
                  {f === "ACTIVE" && (lang === "km" ? "សកម្ម" : "Active")}
                  {f === "INACTIVE" && (lang === "km" ? "អសកម្ម" : "Inactive")}
                </Button>
              ))}
            </div>
          </div>
        </CardHeader>
      </Card>

      <RolesTable
        roles={roles}
        pageMeta={pageMeta}
        loading={loading}
        currentPage={currentPage}
        onPageChange={fetchRoles}
        onOpenPermissions={openPermissionsModal}
        onOpenEdit={(r) => setEditingRole(r)}
        onToggleStatus={handleToggleStatus}
        onOpenDelete={(r) => setDeletingRole(r)}
        lang={lang}
      />

      <RoleCreateModal
        isOpen={createOpen}
        onClose={() => setCreateOpen(false)}
        onSubmit={handleCreateSubmit}
        allPermissions={allPermissions}
        submitting={createSubmitting}
        lang={lang}
      />

      <RoleEditModal
        isOpen={Boolean(editingRole)}
        onClose={() => setEditingRole(null)}
        role={editingRole}
        onSubmit={handleEditSubmit}
        submitting={editSubmitting}
        lang={lang}
      />

      <RoleDeleteDialog
        isOpen={Boolean(deletingRole)}
        onClose={() => setDeletingRole(null)}
        role={deletingRole}
        onConfirm={handleDeleteConfirm}
        submitting={deleteSubmitting}
        lang={lang}
      />

      <RolePermissionsModal
        isOpen={Boolean(permRole)}
        onClose={() => setPermRole(null)}
        role={permRole}
        rolePermissions={rolePermissions}
        allPermissions={allPermissions}
        loading={permLoading}
        onAssignPermissions={handleAssignPermissions}
        onRevokePermission={handleRevokePermission}
        assignSubmitting={assignSubmitting}
        lang={lang}
      />
    </div>
  )
}
