import { useState, useEffect } from "react"
import { Plus, Search, RefreshCw } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"
import { PermissionsTable } from "./permission/PermissionsTable"
import { PermissionCreateModal } from "./permission/PermissionCreateModal"
import { PermissionEditModal } from "./permission/PermissionEditModal"
import { PermissionDeleteDialog } from "./permission/PermissionDeleteDialog"

export default function PermissionsPage() {
  const [permissions, setPermissions] = useState([])
  const [distinctResources, setDistinctResources] = useState([])
  const [pageMeta, setPageMeta] = useState({ number: 0, size: 20, totalElements: 0, totalPages: 0 })
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState("")
  const [resourceFilter, setResourceFilter] = useState("ALL")
  const [statusFilter, setStatusFilter] = useState("ALL")
  const [currentPage, setCurrentPage] = useState(0)

  const [createOpen, setCreateOpen] = useState(false)
  const [createSubmitting, setCreateSubmitting] = useState(false)

  const [editingPerm, setEditingPerm] = useState(null)
  const [editSubmitting, setEditSubmitting] = useState(false)

  const [deletingPerm, setDeletingPerm] = useState(null)
  const [deleteSubmitting, setDeleteSubmitting] = useState(false)

  const { i18n } = useTranslation()
  const rawLang = i18n?.language || localStorage.getItem("lang") || "kh"
  const isKhmer = rawLang.startsWith("kh") || rawLang.startsWith("km")
  const lang = isKhmer ? "km" : "en"

  const fetchPermissions = async (page = 0) => {
    setLoading(true)
    try {
      let url = `/permissions?page=${page}&size=20&sort=createdAt,desc`
      if (search.trim()) {
        url += `&search=${encodeURIComponent(search.trim())}`
      }
      if (resourceFilter !== "ALL") {
        url += `&resource=${encodeURIComponent(resourceFilter)}`
      }
      if (statusFilter === "ACTIVE") {
        url += `&enabled=true`
      } else if (statusFilter === "INACTIVE") {
        url += `&enabled=false`
      }
      const res = await axiosClient.get(url)
      setPermissions(res.data?.content || [])
      setPageMeta(res.data?.page || { number: 0, size: 20, totalElements: 0, totalPages: 0 })
      setCurrentPage(page)
    } catch (err) {
      console.error("Failed to fetch permissions:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Could not retrieve permissions."
      )
    } finally {
      setLoading(false)
    }
  }

  const fetchResourcesCatalog = async () => {
    try {
      const res = await axiosClient.get("/permissions/resources")
      if (Array.isArray(res.data)) {
        setDistinctResources(res.data)
      }
    } catch {

      const set = new Set(permissions.map((p) => p.resource).filter(Boolean))
      setDistinctResources(Array.from(set))
    }
  }

  useEffect(() => {
    fetchPermissions(0)
  }, [resourceFilter, statusFilter])

  useEffect(() => {
    fetchResourcesCatalog()
  }, [])

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    fetchPermissions(0)
  }

  const handleCreateSubmit = async (form) => {
    if (!form.resource.trim() || !form.action.trim()) {
      toast.error(
        lang === "km" ? "កំហុស" : "Validation",
        lang === "km" ? "Resource និង Action ត្រូវតែបំពេញ" : "Resource and Action are required."
      )
      return
    }
    setCreateSubmitting(true)
    try {
      await axiosClient.post("/permissions", {
        resource: form.resource.trim().toLowerCase(),
        action: form.action.trim().toLowerCase(),
        description: form.description.trim() || undefined,
      })
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Created",
        lang === "km" ? "បានបង្កើត Permission ថ្មីដោយជោគជ័យ" : "Permission created successfully."
      )
      setCreateOpen(false)
      fetchPermissions(0)
      fetchResourcesCatalog()
    } catch (err) {
      console.error("Create permission error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to create permission."
      )
    } finally {
      setCreateSubmitting(false)
    }
  }

  const handleEditSubmit = async (form) => {
    if (!editingPerm) return
    setEditSubmitting(true)
    try {
      await axiosClient.patch(`/permissions/${editingPerm.uuid}`, {
        description: form.description.trim() || undefined,
      })
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Updated",
        lang === "km" ? "បានកែប្រែព័ត៌មាន Permission រួចរាល់" : "Permission updated successfully."
      )
      setEditingPerm(null)
      fetchPermissions(currentPage)
    } catch (err) {
      console.error("Update permission error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to update permission."
      )
    } finally {
      setEditSubmitting(false)
    }
  }

  const handleDeleteConfirm = async () => {
    if (!deletingPerm) return
    setDeleteSubmitting(true)
    try {
      await axiosClient.delete(`/permissions/${deletingPerm.uuid}`)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Deleted",
        lang === "km" ? "បានលុប Permission ដោយជោគជ័យ" : "Permission deleted successfully."
      )
      setDeletingPerm(null)
      fetchPermissions(currentPage)
    } catch (err) {
      console.error("Delete permission error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to delete permission."
      )
    } finally {
      setDeleteSubmitting(false)
    }
  }

  const handleToggleStatus = async (p) => {
    const isActive = p.isActive ?? p.active
    try {
      const endpoint = isActive ? `/permissions/${p.uuid}/deactivate` : `/permissions/${p.uuid}/activate`
      await axiosClient.post(endpoint)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Updated",
        isActive
          ? (lang === "km" ? "បានបិទដំណើរការ Permission" : "Permission deactivated.")
          : (lang === "km" ? "បានបើកដំណើរការ Permission" : "Permission activated.")
      )
      fetchPermissions(currentPage)
    } catch (err) {
      console.error("Status toggle error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to change permission status."
      )
    }
  }

  return (
    <div className="space-y-6">

      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={lang === "km" ? "ការគ្រប់គ្រង Permissions" : "Permission Management"}
          description={
            lang === "km"
              ? "គ្រប់គ្រងសិទ្ធិអនុញ្ញាត និងកំណត់សិទ្ធិចូលប្រើប្រាស់តាម Resources (Resource-Based Access Control)"
              : "Create, view, update, and delete granular resource-based access permissions."
          }
        />
        <div className="flex items-center gap-2">
          <Button
            onClick={() => setCreateOpen(true)}
            className="gap-1.5"
          >
            <Plus className="size-4" />
            <span>{lang === "km" ? "បង្កើត Permission ថ្មី" : "Create Permission"}</span>
          </Button>
          <Button
            variant="outline"
            size="icon"
            onClick={() => fetchPermissions(currentPage)}
            disabled={loading}
            title={lang === "km" ? "ផ្ទុកឡើងវិញ" : "Refresh"}
          >
            <RefreshCw className={`size-4 ${loading ? "animate-spin" : ""}`} />
          </Button>
        </div>
      </div>

      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <form onSubmit={handleSearchSubmit} className="relative flex-1 max-w-sm">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 size-4 text-muted-foreground" />
          <Input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder={lang === "km" ? "ស្វែងរកតាមឈ្មោះ, resource..." : "Search permissions, resource..."}
            className="pl-9 h-9 text-sm"
          />
        </form>

        <div className="flex items-center flex-wrap gap-2">

          {distinctResources.length > 0 && (
            <div className="flex items-center gap-1.5">
              <span className="text-xs text-muted-foreground font-medium hidden md:inline">
                {lang === "km" ? "Resource:" : "Resource:"}
              </span>
              <select
                value={resourceFilter}
                onChange={(e) => setResourceFilter(e.target.value)}
                className="h-8 rounded-lg border border-border bg-background px-2.5 text-xs font-medium text-foreground focus:outline-none focus:ring-1 focus:ring-primary"
              >
                <option value="ALL">{lang === "km" ? "Resources ទាំងអស់" : "All Resources"}</option>
                {distinctResources.map((res) => (
                  <option key={res} value={res}>
                    {res.toUpperCase()}
                  </option>
                ))}
              </select>
            </div>
          )}

          <div className="inline-flex rounded-lg border bg-muted/40 p-0.5 text-xs font-medium">
            <button
              type="button"
              onClick={() => setStatusFilter("ALL")}
              className={`px-3 py-1 rounded-md transition-all ${
                statusFilter === "ALL" ? "bg-background text-foreground shadow-xs font-semibold" : "text-muted-foreground hover:text-foreground"
              }`}
            >
              {lang === "km" ? "ទាំងអស់" : "All"}
            </button>
            <button
              type="button"
              onClick={() => setStatusFilter("ACTIVE")}
              className={`px-3 py-1 rounded-md transition-all ${
                statusFilter === "ACTIVE" ? "bg-background text-foreground shadow-xs font-semibold" : "text-muted-foreground hover:text-foreground"
              }`}
            >
              {lang === "km" ? "សកម្ម" : "Active"}
            </button>
            <button
              type="button"
              onClick={() => setStatusFilter("INACTIVE")}
              className={`px-3 py-1 rounded-md transition-all ${
                statusFilter === "INACTIVE" ? "bg-background text-foreground shadow-xs font-semibold" : "text-muted-foreground hover:text-foreground"
              }`}
            >
              {lang === "km" ? "អសកម្ម" : "Inactive"}
            </button>
          </div>

          <Badge variant="outline" className="px-2.5 py-1 text-xs font-normal border-muted-foreground/30">
            {pageMeta.totalElements} {lang === "km" ? "Permissions" : "Permissions"}
          </Badge>
        </div>
      </div>

      <PermissionsTable
        permissions={permissions}
        pageMeta={pageMeta}
        loading={loading}
        currentPage={currentPage}
        onPageChange={fetchPermissions}
        onOpenEdit={(p) => setEditingPerm(p)}
        onToggleStatus={handleToggleStatus}
        onOpenDelete={(p) => setDeletingPerm(p)}
        lang={lang}
      />

      <PermissionCreateModal
        isOpen={createOpen}
        onClose={() => setCreateOpen(false)}
        onSubmit={handleCreateSubmit}
        submitting={createSubmitting}
        lang={lang}
      />

      <PermissionEditModal
        isOpen={Boolean(editingPerm)}
        onClose={() => setEditingPerm(null)}
        permission={editingPerm}
        onSubmit={handleEditSubmit}
        submitting={editSubmitting}
        lang={lang}
      />

      <PermissionDeleteDialog
        isOpen={Boolean(deletingPerm)}
        onClose={() => setDeletingPerm(null)}
        permission={deletingPerm}
        onConfirm={handleDeleteConfirm}
        submitting={deleteSubmitting}
        lang={lang}
      />
    </div>
  )
}
