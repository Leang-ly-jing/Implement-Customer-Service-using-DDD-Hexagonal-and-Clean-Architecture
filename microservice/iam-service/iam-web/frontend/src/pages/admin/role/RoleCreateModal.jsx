import { useState } from "react"
import { Shield, RefreshCw } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
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
  lang = "km",
}) {
  const [form, setForm] = useState({ name: "", description: "", permissionUuids: [] })

  if (!isOpen) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
    setForm({ name: "", description: "", permissionUuids: [] })
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Shield className="size-5 text-primary" />
            <span>{lang === "km" ? "បង្កើត Role ថ្មី" : "Create New Role"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km"
              ? "កំណត់ឈ្មោះ ការពិពណ៌នា និងសិទ្ធិដំបូងសម្រាប់ Role ថ្មី"
              : "Specify the role name, description, and assign initial permissions."}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 pt-2">
          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "ឈ្មោះ Role *" : "Role Name *"}
            </label>
            <Input
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value.toUpperCase() })}
              placeholder="e.g. MANAGER, STAFF, AUDITOR"
              className="uppercase font-semibold tracking-wider"
              required
            />
            <span className="text-[11px] text-muted-foreground mt-1 block">
              {lang === "km"
                ? "ឈ្មោះ Role នឹងត្រូវបំប្លែងជាអក្សរធំដោយស្វ័យប្រវត្តិ"
                : "Role name will be auto-capitalized (e.g. STAFF)."}
            </span>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "ការពិពណ៌នា" : "Description"}
            </label>
            <Input
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              placeholder="e.g. Operations manager with user update capabilities"
            />
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "កំណត់ Permissions ដំបូង (ជម្រើស)" : "Initial Permissions (Optional)"}
            </label>
            <div className="max-h-48 overflow-y-auto border rounded-lg p-2 divide-y divide-border/60 bg-muted/20">
              {allPermissions.length === 0 ? (
                <p className="text-xs text-muted-foreground p-2">Loading permissions...</p>
              ) : (
                allPermissions.map((p) => {
                  const isChecked = form.permissionUuids.includes(p.uuid)
                  return (
                    <label
                      key={p.uuid}
                      className="flex items-center justify-between py-1.5 px-2 hover:bg-muted/40 rounded cursor-pointer text-xs"
                    >
                      <div className="min-w-0 pr-2">
                        <span className="font-mono font-medium text-foreground block">{p.name}</span>
                        {p.description && (
                          <span className="text-[11px] text-muted-foreground block truncate">{p.description}</span>
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
                        className="size-4 rounded border-border text-primary focus:ring-primary"
                      />
                    </label>
                  )
                })
              )}
            </div>
          </div>

          <DialogFooter className="pt-2">
            <Button type="button" variant="outline" onClick={onClose} disabled={submitting}>
              {lang === "km" ? "បោះបង់" : "Cancel"}
            </Button>
            <Button type="submit" disabled={submitting} className="gap-1.5">
              {submitting && <RefreshCw className="size-3.5 animate-spin" />}
              <span>{lang === "km" ? "បង្កើត Role" : "Create Role"}</span>
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default RoleCreateModal
