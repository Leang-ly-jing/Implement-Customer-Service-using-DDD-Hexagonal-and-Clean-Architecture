import { useState, useEffect } from "react"
import { Edit2, RefreshCw } from "lucide-react"
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

export function PermissionEditModal({
  isOpen,
  onClose,
  permission,
  onSubmit,
  submitting = false,
  lang = "km",
}) {
  const [form, setForm] = useState({ description: "" })

  useEffect(() => {
    if (permission) {
      setForm({ description: permission.description || "" })
    }
  }, [permission])

  if (!isOpen || !permission) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Edit2 className="size-5 text-primary" />
            <span>{lang === "km" ? "កែប្រែ Permission" : "Edit Permission"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km"
              ? `កែប្រែការពិពណ៌នារបស់ Permission "${permission.name}"`
              : `Update description for permission "${permission.name}".`}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 pt-2">
          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "Permission Name (មិនអាចកែប្រែបាន)" : "Permission Name"}
            </label>
            <Input
              value={permission.name || ""}
              disabled
              className="font-mono text-sm bg-muted/50 text-muted-foreground"
            />
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "ការពិពណ៌នា" : "Description"}
            </label>
            <Input
              value={form.description}
              onChange={(e) => setForm({ description: e.target.value })}
              placeholder="Description of what this permission permits"
            />
          </div>

          <DialogFooter className="pt-2">
            <Button type="button" variant="outline" onClick={onClose} disabled={submitting}>
              {lang === "km" ? "បោះបង់" : "Cancel"}
            </Button>
            <Button type="submit" disabled={submitting} className="gap-1.5">
              {submitting && <RefreshCw className="size-3.5 animate-spin" />}
              <span>{lang === "km" ? "រក្សាទុកការកែប្រែ" : "Save Changes"}</span>
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default PermissionEditModal
