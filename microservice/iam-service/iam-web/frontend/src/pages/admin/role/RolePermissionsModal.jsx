import { useState, useMemo } from "react"
import {
  Key,
  Plus,
  Search,
  RefreshCw,
  X,
} from "lucide-react"
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
  lang = "km",
}) {
  const [tab, setTab] = useState("assigned")
  const [search, setSearch] = useState("")
  const [selectedToAdd, setSelectedToAdd] = useState([])

  const assignedUuids = useMemo(
    () => new Set(rolePermissions.map((p) => p.uuid)),
    [rolePermissions]
  )

  const unassigned = useMemo(
    () => allPermissions.filter((p) => !assignedUuids.has(p.uuid)),
    [allPermissions, assignedUuids]
  )

  const filteredUnassigned = useMemo(() => {
    if (!search.trim()) return unassigned
    const q = search.toLowerCase()
    return unassigned.filter(
      (p) =>
        p.name?.toLowerCase().includes(q) ||
        p.resource?.toLowerCase().includes(q) ||
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

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[85vh] flex flex-col">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Key className="size-5 text-primary" />
            <span>
              {lang === "km"
                ? `គ្រប់គ្រង Permissions: ${role.name}`
                : `Manage Permissions: ${role.name}`}
            </span>
          </DialogTitle>
          <DialogDescription>
            {role.description ||
              (lang === "km"
                ? "ផ្ដល់ ឬដកសិទ្ធិអនុញ្ញាតសម្រាប់ Role នេះ"
                : "Assign or revoke permission scopes for this role.")}
          </DialogDescription>
        </DialogHeader>

        <div className="flex items-center gap-2 border-b pb-2 pt-1">
          <button
            type="button"
            onClick={() => setTab("assigned")}
            className={`px-3 py-1.5 text-xs font-semibold rounded-md transition-colors ${
              tab === "assigned"
                ? "bg-primary text-primary-foreground"
                : "text-muted-foreground hover:bg-muted"
            }`}
          >
            {lang === "km" ? "Permissions ដែលមាន" : "Assigned Permissions"} ({rolePermissions.length})
          </button>
          <button
            type="button"
            onClick={() => setTab("add")}
            className={`px-3 py-1.5 text-xs font-semibold rounded-md transition-colors ${
              tab === "add"
                ? "bg-primary text-primary-foreground"
                : "text-muted-foreground hover:bg-muted"
            }`}
          >
            {lang === "km" ? "+ បន្ថែម Permission ថ្មី" : "+ Assign More Permissions"}
          </button>
        </div>

        <div className="flex-1 overflow-y-auto py-2">
          {loading ? (
            <div className="py-12 text-center text-muted-foreground">
              <RefreshCw className="size-6 animate-spin mx-auto mb-2 opacity-60" />
              <p className="text-xs">
                {lang === "km" ? "កំពុងទាញយក Permissions..." : "Loading permissions..."}
              </p>
            </div>
          ) : tab === "assigned" ? (

            rolePermissions.length === 0 ? (
              <div className="py-12 text-center text-muted-foreground">
                <Key className="size-8 mx-auto mb-2 opacity-30" />
                <p className="text-xs font-medium">
                  {lang === "km"
                    ? "Role នេះមិនទាន់មាន Permission ណាមួយនៅឡើយទេ"
                    : "No permissions assigned to this role yet."}
                </p>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setTab("add")}
                  className="mt-3 gap-1 text-xs"
                >
                  <Plus className="size-3.5" />
                  <span>{lang === "km" ? "ផ្ដល់ Permission ឥឡូវនេះ" : "Assign Permissions Now"}</span>
                </Button>
              </div>
            ) : (
              <div className="divide-y divide-border/60 border rounded-lg overflow-hidden">
                {rolePermissions.map((p) => (
                  <div key={p.uuid} className="flex items-center justify-between p-3 hover:bg-muted/30">
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-mono text-xs font-semibold text-foreground">{p.name}</span>
                        <Badge variant="outline" className="text-[10px] px-1.5 py-0 border-muted-foreground/30">
                          {p.resource} : {p.action}
                        </Badge>
                      </div>
                      {p.description && (
                        <p className="text-[11px] text-muted-foreground mt-0.5">{p.description}</p>
                      )}
                    </div>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => onRevokePermission(p.uuid)}
                      className="text-rose-600 hover:text-rose-700 hover:bg-rose-50 h-7 px-2 text-xs gap-1"
                      title={lang === "km" ? "ដកសិទ្ធិនេះចេញ" : "Revoke this permission"}
                    >
                      <X className="size-3.5" />
                      <span>{lang === "km" ? "ដកចេញ" : "Revoke"}</span>
                    </Button>
                  </div>
                ))}
              </div>
            )
          ) : (

            <div className="space-y-3">
              <div className="relative">
                <Search className="absolute left-2.5 top-1/2 -translate-y-1/2 size-3.5 text-muted-foreground" />
                <Input
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  placeholder={lang === "km" ? "ស្វែងរក permission តាមឈ្មោះ ឬ resource..." : "Search permissions..."}
                  className="pl-8 h-8 text-xs"
                />
              </div>

              <div className="max-h-64 overflow-y-auto border rounded-lg divide-y divide-border/60">
                {filteredUnassigned.length === 0 ? (
                  <p className="text-xs text-muted-foreground p-6 text-center">
                    {lang === "km" ? "គ្មាន Permission ណាដែលទំនេរសម្រាប់ផ្ដល់ទៀតទេ" : "No unassigned permissions available."}
                  </p>
                ) : (
                  filteredUnassigned.map((p) => {
                    const isSelected = selectedToAdd.includes(p.uuid)
                    return (
                      <label
                        key={p.uuid}
                        className="flex items-center justify-between p-2.5 hover:bg-muted/40 cursor-pointer text-xs"
                      >
                        <div>
                          <div className="flex items-center gap-2">
                            <span className="font-mono font-medium text-foreground">{p.name}</span>
                            <Badge variant="outline" className="text-[10px] px-1.5 py-0 border-muted-foreground/30">
                              {p.resource} : {p.action}
                            </Badge>
                          </div>
                          {p.description && (
                            <p className="text-[11px] text-muted-foreground mt-0.5">{p.description}</p>
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
                          className="size-4 rounded border-border text-primary focus:ring-primary ml-2"
                        />
                      </label>
                    )
                  })
                )}
              </div>

              <div className="flex items-center justify-between pt-2">
                <span className="text-xs text-muted-foreground">
                  {selectedToAdd.length} {lang === "km" ? "បានជ្រើសរើស" : "selected"}
                </span>
                <Button
                  size="sm"
                  onClick={handleAssignClick}
                  disabled={selectedToAdd.length === 0 || assignSubmitting}
                  className="gap-1.5 text-xs"
                >
                  {assignSubmitting && <RefreshCw className="size-3.5 animate-spin" />}
                  <span>
                    {lang === "km"
                      ? `ផ្ដល់ ${selectedToAdd.length} Permissions ទៅ Role`
                      : `Assign ${selectedToAdd.length} Permissions`}
                  </span>
                </Button>
              </div>
            </div>
          )}
        </div>

        <DialogFooter className="border-t pt-3">
          <Button variant="outline" onClick={onClose}>
            {lang === "km" ? "បិទ" : "Close"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

export default RolePermissionsModal
