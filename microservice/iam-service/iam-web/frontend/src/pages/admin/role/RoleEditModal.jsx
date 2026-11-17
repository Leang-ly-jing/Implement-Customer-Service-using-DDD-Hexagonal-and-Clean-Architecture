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

export function RoleEditModal({
  isOpen,
  onClose,
  role,
  onSubmit,
  submitting = false,
  lang = "km",
}) {
  const [form, setForm] = useState({ name: "", description: "" })

  useEffect(() => {
    if (role) {
      setForm({ name: role.name || "", description: role.description || "" })
    }
  }, [role])

  if (!isOpen || !role) return null

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
            <span>{lang === "km" ? "កែប្រែ Role" : "Edit Role"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km" ? "កែប្រែឈ្មោះ និងការពិពណ៌នារបស់ Role" : "Update role name and description."}
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
              required
              className="uppercase font-semibold tracking-wider"
            />
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "ការពិពណ៌នា" : "Description"}
            </label>
            <Input
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
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

export default RoleEditModal
