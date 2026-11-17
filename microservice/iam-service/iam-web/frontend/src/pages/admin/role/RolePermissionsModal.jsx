import { useState, useMemo } from "react"
import {
  Key,
  Plus,
  Search,
  RefreshCw,
  X,
  Trash2,
  ShieldCheck,
  Check,
  CheckCheck,
} from "lucide-react"
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

export function RolePermissionsModal({
  isOpen,
  onClose,
  role,
  rolePermissions = [],
  allPermissions = [],
  loading = false,
  onAssignPermissions,
  onRevokePermission,
  assignSubmitting = false,
}) {
  const { t } = useTranslation()
  const [tab, setTab] = useState("assigned")
  const [assignedSearch, setAssignedSearch] = useState("")
  const [search, setSearch] = useState("")
  const [selectedToAdd, setSelectedToAdd] = useState([])

  // Deduplicate assigned permissions by uuid or name
  const uniqueAssigned = useMemo(() => {
    const seen = new Set()
    const list = []
    for (const p of rolePermissions || []) {
      const key = p.uuid || p.name
      if (key && !seen.has(key)) {
        seen.add(key)
        list.push(p)
      }
    }
    return list
  }, [rolePermissions])

  const assignedUuids = useMemo(
    () => new Set(uniqueAssigned.map((p) => p.uuid)),
    [uniqueAssigned]
  )

  const unassigned = useMemo(
    () => allPermissions.filter((p) => !assignedUuids.has(p.uuid)),
    [allPermissions, assignedUuids]
  )

  const filteredAssigned = useMemo(() => {
    if (!assignedSearch.trim()) return uniqueAssigned
    const q = assignedSearch.toLowerCase().trim()
    return uniqueAssigned.filter(
      (p) =>
        p.name?.toLowerCase().includes(q) ||
        p.resource?.toLowerCase().includes(q) ||
        p.action?.toLowerCase().includes(q) ||
        p.description?.toLowerCase().includes(q)
    )
  }, [uniqueAssigned, assignedSearch])

  const filteredUnassigned = useMemo(() => {
    if (!search.trim()) return unassigned
    const q = search.toLowerCase().trim()
    return unassigned.filter(
      (p) =>
        p.name?.toLowerCase().includes(q) ||
        p.resource?.toLowerCase().includes(q) ||
        p.action?.toLowerCase().includes(q) ||
        p.description?.toLowerCase().includes(q)
    )
  }, [unassigned, search])

  if (!isOpen || !role) return null

  const handleAssignClick = async () => {
    if (selectedToAdd.length === 0) return
    await onAssignPermissions(selectedToAdd)
    setSelectedToAdd([])
    setTab("assigned")
  }

  const handleSelectAllFiltered = () => {
    const ids = filteredUnassigned.map((p) => p.uuid)
    const combined = Array.from(new Set([...selectedToAdd, ...ids]))
    setSelectedToAdd(combined)
  }

  const handleClearSelection = () => {
    setSelectedToAdd([])
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
      <DialogContent className="sm:max-w-2xl w-full max-h-[85vh] flex flex-col p-6 gap-4">
        <DialogHeader className="space-y-1">
          <div className="flex items-center gap-2">
            <div className="flex size-9 items-center justify-center rounded-lg bg-primary/10 text-primary">
              <Key className="size-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-semibold">
                {t("rolePermissionsModal.title", { roleName: role.name })}
              </DialogTitle>
              <DialogDescription className="text-xs text-muted-foreground">
                {role.description || t("rolePermissionsModal.description")}
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        {/* Tab Switcher */}
        <div className="grid grid-cols-2 p-1 bg-muted/80 rounded-lg text-xs font-medium">
          <button
            type="button"
            onClick={() => setTab("assigned")}
            className={`flex items-center justify-center gap-2 py-2 rounded-md transition-all ${
              tab === "assigned"
                ? "bg-background text-foreground shadow-xs font-semibold"
                : "text-muted-foreground hover:text-foreground"
            }`}
          >
            <ShieldCheck className="size-4 text-emerald-600" />
            <span>{t("rolePermissionsModal.assignedTab")}</span>
            <span className="ml-1 rounded-full bg-muted px-2 py-0.5 text-[10px] font-mono font-bold">
              {uniqueAssigned.length}
            </span>
          </button>
          <button
            type="button"
            onClick={() => setTab("add")}
            className={`flex items-center justify-center gap-2 py-2 rounded-md transition-all ${
              tab === "add"
                ? "bg-background text-foreground shadow-xs font-semibold"
                : "text-muted-foreground hover:text-foreground"
            }`}
          >
            <Plus className="size-4 text-primary" />
            <span>{t("rolePermissionsModal.addTab")}</span>
            {unassigned.length > 0 && (
              <span className="ml-1 rounded-full bg-primary/15 text-primary px-2 py-0.5 text-[10px] font-mono font-bold">
                +{unassigned.length}
              </span>
            )}
          </button>
        </div>

        {/* Tab Content */}
        <div className="flex-1 overflow-y-auto pr-1">
          {loading ? (
            <div className="py-16 text-center text-muted-foreground">
              <RefreshCw className="size-7 animate-spin mx-auto mb-2 opacity-60" />
              <p className="text-xs font-medium">
                {t("rolePermissionsModal.loading")}
              </p>
            </div>
          ) : tab === "assigned" ? (
            <div className="space-y-3">
              {/* Search Bar for Assigned */}
              {uniqueAssigned.length > 0 && (
                <div className="relative">
                  <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-3.5 text-muted-foreground" />
                  <Input
                    value={assignedSearch}
                    onChange={(e) => setAssignedSearch(e.target.value)}
                    placeholder={t("rolePermissionsModal.filterPlaceholder")}
                    className="pl-8 pr-8 h-8.5 text-xs"
                  />
                  {assignedSearch && (
                    <button
                      type="button"
                      onClick={() => setAssignedSearch("")}
                      className="absolute right-2.5 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground"
                    >
                      <X className="size-3.5" />
                    </button>
                  )}
                </div>
              )}

              {uniqueAssigned.length === 0 ? (
                <div className="py-16 text-center text-muted-foreground border rounded-xl bg-muted/10">
                  <Key className="size-9 mx-auto mb-2.5 opacity-30 text-primary" />
                  <p className="text-sm font-medium text-foreground">
                    {t("rolePermissionsModal.noAssignedTitle")}
                  </p>
                  <p className="text-xs text-muted-foreground mt-1 max-w-xs mx-auto">
                    {t("rolePermissionsModal.noAssignedDesc")}
                  </p>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setTab("add")}
                    className="mt-4 gap-1.5 text-xs"
                  >
                    <Plus className="size-3.5" />
                    <span>{t("rolePermissionsModal.assignNow")}</span>
                  </Button>
                </div>
              ) : filteredAssigned.length === 0 ? (
                <div className="py-12 text-center text-muted-foreground border rounded-xl">
                  <Search className="size-7 mx-auto mb-2 opacity-30" />
                  <p className="text-xs">
                    {t("rolePermissionsModal.noMatching")}
                  </p>
                </div>
              ) : (
                <div className="border rounded-xl divide-y divide-border/60 overflow-hidden bg-card">
                  {filteredAssigned.map((p) => (
                    <div
                      key={p.uuid}
                      className="flex items-center justify-between p-3 hover:bg-muted/30 transition-colors gap-3"
                    >
                      <div className="min-w-0 flex-1">
                        <div className="flex flex-wrap items-center gap-2">
                          <span className="font-mono text-xs font-bold text-foreground">
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
                          <p className="text-xs text-muted-foreground mt-1 line-clamp-2">
                            {p.description}
                          </p>
                        )}
                      </div>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onRevokePermission(p.uuid)}
                        className="text-rose-600 hover:text-rose-700 hover:bg-rose-50 border-rose-200/60 h-8 px-2.5 text-xs font-medium gap-1.5 transition-colors shrink-0"
                        title={t("rolePermissionsModal.revokeTitle")}
                      >
                        <Trash2 className="size-3.5" />
                        <span>{t("rolePermissionsModal.revoke")}</span>
                      </Button>
                    </div>
                  ))}
                </div>
              )}
            </div>
          ) : (
            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <div className="relative flex-1">
                  <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-3.5 text-muted-foreground" />
                  <Input
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder={t("rolePermissionsModal.searchPlaceholder")}
                    className="pl-8 pr-8 h-8.5 text-xs"
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

                {filteredUnassigned.length > 0 && (
                  <div className="flex items-center gap-1.5 shrink-0">
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      onClick={handleSelectAllFiltered}
                      className="h-8.5 text-xs text-muted-foreground hover:text-foreground gap-1 px-2"
                    >
                      <CheckCheck className="size-3.5" />
                      <span>{t("rolePermissionsModal.selectAll")}</span>
                    </Button>
                    {selectedToAdd.length > 0 && (
                      <Button
                        type="button"
                        variant="ghost"
                        size="sm"
                        onClick={handleClearSelection}
                        className="h-8.5 text-xs text-rose-600 hover:text-rose-700 hover:bg-rose-50 px-2"
                      >
                        {t("rolePermissionsModal.clear")}
                      </Button>
                    )}
                  </div>
                )}
              </div>

              <div className="max-h-72 overflow-y-auto border rounded-xl divide-y divide-border/60 bg-card">
                {filteredUnassigned.length === 0 ? (
                  <div className="p-8 text-center text-muted-foreground">
                    <Key className="size-7 mx-auto mb-2 opacity-30 text-primary" />
                    <p className="text-xs font-medium">
                      {t("rolePermissionsModal.noUnassigned")}
                    </p>
                  </div>
                ) : (
                  filteredUnassigned.map((p) => {
                    const isSelected = selectedToAdd.includes(p.uuid)
                    return (
                      <label
                        key={p.uuid}
                        className={`flex items-center justify-between p-3 cursor-pointer transition-colors text-xs gap-3 ${
                          isSelected ? "bg-primary/5 hover:bg-primary/10" : "hover:bg-muted/40"
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
                            <p className="text-xs text-muted-foreground mt-1 line-clamp-2">
                              {p.description}
                            </p>
                          )}
                        </div>
                        <input
                          type="checkbox"
                          checked={isSelected}
                          onChange={(e) => {
                            if (e.target.checked) {
                              setSelectedToAdd([...selectedToAdd, p.uuid])
                            } else {
                              setSelectedToAdd(selectedToAdd.filter((id) => id !== p.uuid))
                            }
                          }}
                          className="size-4 rounded border-border text-primary focus:ring-primary shrink-0 cursor-pointer"
                        />
                      </label>
                    )
                  })
                )}
              </div>

              {/* Assignment Action Bar */}
              <div className="flex items-center justify-between pt-1">
                <span className="text-xs text-muted-foreground font-medium">
                  {selectedToAdd.length} {t("rolePermissionsModal.selected")}
                </span>
                <Button
                  size="sm"
                  onClick={handleAssignClick}
                  disabled={selectedToAdd.length === 0 || assignSubmitting}
                  className="gap-1.5 text-xs font-semibold shadow-xs"
                >
                  {assignSubmitting ? (
                    <RefreshCw className="size-3.5 animate-spin" />
                  ) : (
                    <Check className="size-3.5" />
                  )}
                  <span>
                    {t("rolePermissionsModal.assignButton", { count: selectedToAdd.length })}
                  </span>
                </Button>
              </div>
            </div>
          )}
        </div>

        <DialogFooter className="border-t pt-3 flex items-center justify-end">
          <Button variant="outline" size="sm" onClick={onClose} className="text-xs">
            {t("common.close")}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

export default RolePermissionsModal
