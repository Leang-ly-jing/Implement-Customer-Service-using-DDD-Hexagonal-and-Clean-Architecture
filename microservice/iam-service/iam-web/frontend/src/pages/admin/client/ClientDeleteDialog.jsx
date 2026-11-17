import { AlertTriangle } from "lucide-react"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"

export function ClientDeleteDialog({
  isOpen,
  onClose,
  client,
  onConfirm,
  submitting = false,
  lang = "km",
}) {
  if (!isOpen || !client) return null

  return (
    <AlertDialog open={isOpen} onOpenChange={onClose}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle className="flex items-center gap-2 text-rose-600">
            <AlertTriangle className="size-5" />
            <span>{lang === "km" ? "លុប Client?" : "Delete Client?"}</span>
          </AlertDialogTitle>
          <AlertDialogDescription>
            {lang === "km"
              ? `តើអ្នកប្រាកដថាចង់លុប Client "${client.clientId}" មែនទេ? សកម្មភាពនេះមិនអាចត្រឡប់ថយក្រោយវិញបានទេ ហើយរាល់ tokens និង consents ទាំងអស់នឹងត្រូវបានលុបចេញ។`
              : `Are you sure you want to delete client "${client.clientId}"? All associated active tokens, grants, and consents will be permanently removed.`}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel disabled={submitting}>
            {lang === "km" ? "បោះបង់" : "Cancel"}
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={onConfirm}
            disabled={submitting}
            className="bg-rose-600 hover:bg-rose-700 text-white"
          >
            {submitting
              ? lang === "km" ? "កំពុងលុប..." : "Deleting..."
              : lang === "km" ? "លុបចេញ" : "Delete"}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}

export default ClientDeleteDialog
