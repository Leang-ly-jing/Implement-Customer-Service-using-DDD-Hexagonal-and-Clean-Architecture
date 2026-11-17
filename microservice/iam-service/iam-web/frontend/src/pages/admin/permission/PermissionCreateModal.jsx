import { useState } from "react"
import { KeyRound, Sparkles, RefreshCw } from "lucide-react"
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

const INITIAL_FORM = { resource: "", action: "", description: "" }

export function PermissionCreateModal({
  isOpen,
  onClose,
  onSubmit,
  submitting = false,
  lang = "km",
}) {
  const [form, setForm] = useState(INITIAL_FORM)

  if (!isOpen) return null

  const generatedPreviewName =
    form.resource.trim() && form.action.trim()
      ? `${form.resource.trim().toLowerCase()}:${form.action.trim().toLowerCase()}`
      : "resource:action"

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
    setForm(INITIAL_FORM)
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <KeyRound className="size-5 text-primary" />
            <span>{lang === "km" ? "បង្កើត Permission ថ្មី" : "Create Permission"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km"
              ? "កំណត់ Resource និង Action ដើម្បីបង្កើត Permission ចូលប្រើប្រាស់ថ្មី"
              : "Define the resource and allowed action (e.g. document:read, order:export)."}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4 pt-2">

          <div className="p-3 rounded-lg border bg-muted/30 flex items-center justify-between">
            <div>
              <span className="text-[11px] font-medium text-muted-foreground uppercase block">
                {lang === "km" ? "ទម្រង់ឈ្មោះ Permission" : "Generated Permission"}
              </span>
              <span className="font-mono text-sm font-bold text-primary">
                {generatedPreviewName}
              </span>
            </div>
            <Sparkles className="size-4 text-primary opacity-60" />
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "Resource Name *" : "Resource Name *"}
            </label>
            <Input
              value={form.resource}
              onChange={(e) => setForm({ ...form, resource: e.target.value.toLowerCase().trim() })}
              placeholder="e.g. order, product, billing, report"
              className="font-mono text-sm lowercase"
              required
            />

            <div className="flex items-center gap-1.5 mt-1.5 flex-wrap">
              <span className="text-[11px] text-muted-foreground">
                {lang === "km" ? "ឧទាហរណ៍:" : "Examples:"}
              </span>
              {["order", "report", "billing", "document", "notification"].map((ex) => (
                <button
                  key={ex}
                  type="button"
                  onClick={() => setForm({ ...form, resource: ex })}
                  className="text-[11px] font-mono px-1.5 py-0.5 rounded bg-muted hover:bg-muted/80 text-muted-foreground hover:text-foreground"
                >
                  {ex}
                </button>
              ))}
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "Action *" : "Action *"}
            </label>
            <Input
              value={form.action}
              onChange={(e) => setForm({ ...form, action: e.target.value.toLowerCase().trim() })}
              placeholder="e.g. read, create, update, delete, export"
              className="font-mono text-sm lowercase"
              required
            />

            <div className="flex items-center gap-1.5 mt-1.5 flex-wrap">
              <span className="text-[11px] text-muted-foreground">
                {lang === "km" ? "Actions ទូទៅ:" : "Common:"}
              </span>
              {["create", "read", "update", "delete", "export", "approve"].map((act) => (
                <button
                  key={act}
                  type="button"
                  onClick={() => setForm({ ...form, action: act })}
                  className="text-[11px] font-mono px-1.5 py-0.5 rounded bg-muted hover:bg-muted/80 text-muted-foreground hover:text-foreground"
                >
                  {act}
                </button>
              ))}
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "ការពិពណ៌នា" : "Description"}
            </label>
            <Input
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              placeholder="e.g. Allow viewing and exporting customer billing records"
            />
          </div>

          <DialogFooter className="pt-2">
            <Button type="button" variant="outline" onClick={onClose} disabled={submitting}>
              {lang === "km" ? "បោះបង់" : "Cancel"}
            </Button>
            <Button type="submit" disabled={submitting} className="gap-1.5">
              {submitting && <RefreshCw className="size-3.5 animate-spin" />}
              <span>{lang === "km" ? "បង្កើត Permission" : "Create Permission"}</span>
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default PermissionCreateModal
