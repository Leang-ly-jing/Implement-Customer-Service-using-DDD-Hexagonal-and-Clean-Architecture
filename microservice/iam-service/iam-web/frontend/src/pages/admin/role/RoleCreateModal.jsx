import { useState, useMemo } from "react"
import { Shield, RefreshCw, Search, X, CheckCheck } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"

export function RoleCreateModal({
  isOpen,
  onClose,
  onSubmit,
  allPermissions = [],
  submitting = false,
}) {
  const { t } = useTranslation()
  const [form, setForm] = useState({ name: "", description: "", permissionUuids: [] })
  const [search, setSearch] = useState("")

  const filteredPermissions = useMemo(() => {
    if (!search.trim()) return allPermissions
    const q = search.toLowerCase().trim()
    return allPermissions.filter(
      (p) =>
        p.name?.toLowerCase().includes(q) ||
        p.resource?.toLowerCase().includes(q) ||
        p.action?.toLowerCase().includes(q) ||
        p.description?.toLowerCase().includes(q)
    )
  }, [allPermissions, search])

  if (!isOpen) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
    setForm({ name: "", description: "", permissionUuids: [] })
    setSearch("")
  }

  const handleSelectAllFiltered = () => {
    const filteredIds = filteredPermissions.map((p) => p.uuid)
    const merged = Array.from(new Set([...form.permissionUuids, ...filteredIds]))
    setForm({ ...form, permissionUuids: merged })
  }

  const handleClearSelection = () => {
    setForm({ ...form, permissionUuids: [] })
  }

  const getActionBadgeClass = (action) => {
    switch ((action || "").toLowerCase()) {
      case "create":
      case "write":
        return "border-emerald-200 bg-emerald-50 text-emerald-700"
      case "read":
      case "view":
        return "border-blue-200 bg-blue-50 text-blue-700"
      case "update":
      case "edit":
        return "border-amber-200 bg-amber-50 text-amber-700"
      case "delete":
      case "remove":
        return "border-rose-200 bg-rose-50 text-rose-700"
      default:
        return "border-zinc-200 bg-zinc-50 text-zinc-700"
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-2xl w-full max-h-[90vh] flex flex-col p-6 gap-4">
        <DialogHeader className="space-y-1">
          <div className="flex items-center gap-2">
            <div className="flex size-9 items-center justify-center rounded-lg bg-primary/10 text-primary">
              <Shield className="size-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-semibold">
                {t("rolesPage.createModalTitle")}
              </DialogTitle>
              <DialogDescription className="text-xs text-muted-foreground">
                {t("rolesPage.createModalSubtitle")}
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="flex-1 flex flex-col gap-4 overflow-y-auto pr-1">
          {/* Role Name */}
          <div className="space-y-1">
            <label className="text-xs font-semibold text-foreground block">
              {t("rolesPage.roleName")} <span className="text-rose-500">*</span>
            </label>
            <Input
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value.toUpperCase() })}
              placeholder="E.G. MANAGER, STAFF, AUDITOR"
              className="uppercase font-mono font-semibold tracking-wider h-9 text-xs"
              required
            />
            <span className="text-[11px] text-muted-foreground block">
              {t("rolesPage.roleNameHint")}
            </span>
          </div>

          {/* Role Description */}
          <div className="space-y-1">
            <label className="text-xs font-semibold text-foreground block">
              {t("rolesPage.description")}
            </label>
            <Input
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              placeholder="e.g. Operations manager with user update capabilities"
              className="h-9 text-xs"
            />
          </div>

          {/* Initial Permissions Assignment */}
          <div className="space-y-2 flex-1">
            <div className="flex items-center justify-between">
              <label className="text-xs font-semibold text-foreground">
                {t("rolesPage.initialPermissions")}
              </label>
              <span className="text-[11px] text-muted-foreground font-mono font-medium">
                {form.permissionUuids.length} {t("rolePermissionsModal.selected")}
              </span>
            </div>

            {/* Filter Search Bar */}
            <div className="flex items-center gap-2">
              <div className="relative flex-1">
                <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-3.5 text-muted-foreground" />
                <Input
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  placeholder={t("rolePermissionsModal.searchPlaceholder")}
                  className="pl-8 pr-8 h-8 text-xs"
                />
                {search && (
                  <button
                    type="button"
                    onClick={() => setSearch("")}
                    className="absolute right-2.5 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                  >
                    <X className="size-3.5" />
                  </button>
                )}
              </div>

              {filteredPermissions.length > 0 && (
                <div className="flex items-center gap-1.5 shrink-0">
                  <Button
                    type="button"
                    variant="ghost"
                    size="sm"
                    onClick={handleSelectAllFiltered}
                    className="h-8 text-xs text-muted-foreground hover:text-foreground gap-1 px-2"
                  >
                    <CheckCheck className="size-3.5" />
                    <span>{t("rolePermissionsModal.selectAll")}</span>
                  </Button>
                  {form.permissionUuids.length > 0 && (
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      onClick={handleClearSelection}
                      className="h-8 text-xs text-rose-600 hover:text-rose-700 hover:bg-rose-50 px-2"
                    >
                      {t("rolePermissionsModal.clear")}
                    </Button>
                  )}
                </div>
              )}
            </div>

            {/* Permissions Scrollable List */}
            <div className="max-h-60 overflow-y-auto border rounded-xl divide-y divide-border/60 bg-card">
              {filteredPermissions.length === 0 ? (
                <p className="text-xs text-muted-foreground p-6 text-center">
                  {t("rolePermissionsModal.noMatching")}
                </p>
              ) : (
                filteredPermissions.map((p) => {
                  const isChecked = form.permissionUuids.includes(p.uuid)
                  return (
                    <label
                      key={p.uuid}
                      className={`flex items-center justify-between p-3 cursor-pointer transition-colors text-xs gap-3 ${
                        isChecked ? "bg-primary/5 hover:bg-primary/10" : "hover:bg-muted/40"
                      }`}
                    >
                      <div className="min-w-0 flex-1">
                        <div className="flex flex-wrap items-center gap-2">
                          <span className="font-mono text-xs font-semibold text-foreground">
                            {p.name}
                          </span>
                          {p.resource && (
                            <span className="px-1.5 py-0.5 rounded text-[10px] font-mono uppercase bg-muted text-muted-foreground border">
                              {p.resource}
                            </span>
                          )}
                          {p.action && (
                            <Badge
                              variant="outline"
                              className={`text-[10px] uppercase font-mono px-1.5 py-0 ${getActionBadgeClass(
                                p.action
                              )}`}
                            >
                              {p.action}
                            </Badge>
                          )}
                        </div>
                        {p.description && (
                          <p className="text-[11px] text-muted-foreground mt-1 line-clamp-2">
                            {p.description}
                          </p>
                        )}
                      </div>
                      <input
                        type="checkbox"
                        checked={isChecked}
                        onChange={(e) => {
                          if (e.target.checked) {
                            setForm({
                              ...form,
                              permissionUuids: [...form.permissionUuids, p.uuid],
                            })
                          } else {
                            setForm({
                              ...form,
                              permissionUuids: form.permissionUuids.filter((id) => id !== p.uuid),
                            })
                          }
                        }}
                        className="size-4 rounded border-border text-primary focus:ring-primary shrink-0 cursor-pointer"
                      />
                    </label>
                  )
                })
              )}
            </div>
          </div>

          <DialogFooter className="border-t pt-3 flex items-center justify-end gap-2">
            <Button type="button" variant="outline" size="sm" onClick={onClose} disabled={submitting}>
              {t("common.cancel")}
            </Button>
            <Button type="submit" size="sm" disabled={submitting} className="gap-1.5 font-semibold">
              {submitting && <RefreshCw className="size-3.5 animate-spin" />}
              <span>{t("rolesPage.addRole")}</span>
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default RoleCreateModal
