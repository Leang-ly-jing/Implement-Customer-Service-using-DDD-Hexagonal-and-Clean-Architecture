import * as React from "react"
import { Toast } from "@base-ui/react/toast"
import { cn } from "cn"
import { CheckCircle2, AlertCircle, Info, X } from "lucide-react"

export const toast = Toast.createToastManager()

toast.success = (title, description) =>
  toast.add({
    title,
    description,
    type: "success",
  })

toast.error = (title, description) =>
  toast.add({
    title,
    description,
    type: "error",
  })

toast.info = (title, description) =>
  toast.add({
    title,
    description,
    type: "info",
  })

export function Toaster() {
  return (
    <Toast.Provider toastManager={toast} timeout={4000}>
      <ToastList />
    </Toast.Provider>
  )
}

function ToastList() {
  const { toasts } = Toast.useToastManager()

  return (
    <Toast.Portal>
      <Toast.Viewport className="fixed top-4 right-4 z-50 flex flex-col gap-2 outline-none">
        {toasts.map((t) => (
          <Toast.Root
            key={t.id}
            toast={t}
            className={cn(
              "pointer-events-auto flex w-full max-w-sm items-start gap-3 rounded-lg border p-4 shadow-md backdrop-blur-sm transition-all",
              t.type === "error" && "border-destructive/30 bg-destructive/10 text-destructive dark:bg-destructive/20",
              t.type === "success" && "border-emerald-500/30 bg-emerald-50 text-emerald-900 dark:bg-emerald-950/50 dark:text-emerald-200 dark:border-emerald-500/30",
              (!t.type || t.type === "info") && "border-border bg-background text-foreground"
            )}
          >
            <div className="mt-0.5 shrink-0">
              {t.type === "success" && <CheckCircle2 className="size-4 text-emerald-600 dark:text-emerald-400" />}
              {t.type === "error" && <AlertCircle className="size-4 text-destructive" />}
              {(!t.type || t.type === "info") && <Info className="size-4 text-blue-500" />}
            </div>

            <Toast.Content className="flex-1 space-y-1">
              {t.title && (
                <Toast.Title className="text-sm font-semibold leading-none">
                  {t.title}
                </Toast.Title>
              )}
              {t.description && (
                <Toast.Description className="text-xs opacity-90">
                  {t.description}
                </Toast.Description>
              )}
            </Toast.Content>

            <Toast.Close className="rounded-md p-0.5 opacity-60 hover:opacity-100 transition-opacity">
              <X className="size-4" />
            </Toast.Close>
          </Toast.Root>
        ))}
      </Toast.Viewport>
    </Toast.Portal>
  )
}
