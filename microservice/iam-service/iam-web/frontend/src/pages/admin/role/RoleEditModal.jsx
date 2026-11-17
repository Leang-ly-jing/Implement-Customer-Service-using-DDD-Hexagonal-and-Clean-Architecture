import { useState, useEffect } from "react"
import { Edit2, RefreshCw } from "lucide-react"
import { useTranslation } from "react-i18next"
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
}) {
  const { t } = useTranslation()
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
      <DialogContent className="sm:max-w-lg w-full p-6 gap-4">
        <DialogHeader className="space-y-1">
          <div className="flex items-center gap-2">
            <div className="flex size-9 items-center justify-center rounded-lg bg-primary/10 text-primary">
              <Edit2 className="size-4.5" />
            </div>
            <div>
              <DialogTitle className="text-base font-semibold">
                {t("rolesPage.editRole", "Edit Role")}
              </DialogTitle>
              <DialogDescription className="text-xs text-muted-foreground">
                {t("rolesPage.editRoleSubtitle", "Update role name and description.")}
              </DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 pt-2">
          <div className="space-y-1">
            <label className="text-xs font-semibold text-foreground block">
              {t("rolesPage.roleName")} <span className="text-rose-500">*</span>
            </label>
            <Input
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value.toUpperCase() })}
              required
              className="uppercase font-mono font-semibold tracking-wider h-9 text-xs"
            />
          </div>

          <div className="space-y-1">
            <label className="text-xs font-semibold text-foreground block">
              {t("rolesPage.description")}
            </label>
            <Input
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              className="h-9 text-xs"
            />
          </div>

          <DialogFooter className="border-t pt-3 flex items-center justify-end gap-2">
            <Button type="button" variant="outline" size="sm" onClick={onClose} disabled={submitting}>
              {t("common.cancel")}
            </Button>
            <Button type="submit" size="sm" disabled={submitting} className="gap-1.5 font-semibold">
              {submitting && <RefreshCw className="size-3.5 animate-spin" />}
              <span>{t("common.save")}</span>
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default RoleEditModal
