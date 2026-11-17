import { useState, useEffect } from "react"
import { Plus } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"
import { RolesTable } from "./role/RolesTable"
import { RoleCreateModal } from "./role/RoleCreateModal"
import { RoleEditModal } from "./role/RoleEditModal"
import { RoleDeleteDialog } from "./role/RoleDeleteDialog"
import { RolePermissionsModal } from "./role/RolePermissionsModal"

export default function RolesPage() {
  const { t } = useTranslation()
  const [roles, setRoles] = useState([])
  const [pageMeta, setPageMeta] = useState({ number: 0, size: 10, totalElements: 0, totalPages: 0 })
  const [loading, setLoading] = useState(true)
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

  const fetchRoles = async (page = 0) => {
    setLoading(true)
    try {
      const url = `/roles?page=${page}&size=10&sort=createdAt,desc`
      const res = await axiosClient.get(url)
      setRoles(res.data?.content || [])
      setPageMeta(res.data?.page || { number: 0, size: 10, totalElements: 0, totalPages: 0 })
      setCurrentPage(page)
    } catch (err) {
      console.error("Failed to fetch roles:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolesPage.fetchFailed")
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
  }, [])

  const handleCreateSubmit = async (form) => {
    if (!form.name.trim()) {
      toast.error(
        t("common.error"),
        t("rolesPage.roleName") + " is required."
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
        t("common.success"),
        t("rolesPage.createSuccess")
      )
      setCreateOpen(false)
      fetchRoles(0)
    } catch (err) {
      console.error("Create role error:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolesPage.statusChangeFailed")
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
        t("common.success"),
        t("rolesPage.editSuccess")
      )
      setEditingRole(null)
      fetchRoles(currentPage)
    } catch (err) {
      console.error("Update role error:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolesPage.statusChangeFailed")
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
        t("common.success"),
        t("rolesPage.deleteSuccess")
      )
      setDeletingRole(null)
      fetchRoles(currentPage)
    } catch (err) {
      console.error("Delete role error:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolesPage.systemRoleCannotDelete")
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
        t("common.success"),
        role.active ? t("rolesPage.roleDeactivated") : t("rolesPage.roleActivated")
      )
      fetchRoles(currentPage)
    } catch (err) {
      console.error("Status toggle error:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolesPage.statusChangeFailed")
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
        t("common.error"),
        err.response?.data?.detail || t("rolePermissionsModal.loadFailed")
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
        t("common.success"),
        t("rolePermissionsModal.revokeSuccess")
      )
    } catch (err) {
      console.error("Revoke permission error:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolePermissionsModal.revokeFailed")
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
        t("common.success"),
        t("rolePermissionsModal.assignSuccess")
      )
      const res = await axiosClient.get(`/roles/${permRole.uuid}`)
      setRolePermissions(res.data?.permissions || [])
    } catch (err) {
      console.error("Assign permissions error:", err)
      toast.error(
        t("common.error"),
        err.response?.data?.detail || t("rolePermissionsModal.assignFailed")
      )
    } finally {
      setAssignSubmitting(false)
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={t("rolesPage.title")}
          description={t("rolesPage.subtitle")}
        />
        <div className="flex items-center gap-2">
          <Button size="sm" onClick={() => setCreateOpen(true)} className="gap-1.5">
            <Plus className="size-4" />
            <span>{t("rolesPage.addRole")}</span>
          </Button>
        </div>
      </div>

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
      />

      <RoleCreateModal
        isOpen={createOpen}
        onClose={() => setCreateOpen(false)}
        onSubmit={handleCreateSubmit}
        allPermissions={allPermissions}
        submitting={createSubmitting}
      />

      <RoleEditModal
        isOpen={Boolean(editingRole)}
        onClose={() => setEditingRole(null)}
        role={editingRole}
        onSubmit={handleEditSubmit}
        submitting={editSubmitting}
      />

      <RoleDeleteDialog
        isOpen={Boolean(deletingRole)}
        onClose={() => setDeletingRole(null)}
        role={deletingRole}
        onConfirm={handleDeleteConfirm}
        submitting={deleteSubmitting}
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
      />
    </div>
  )
}
