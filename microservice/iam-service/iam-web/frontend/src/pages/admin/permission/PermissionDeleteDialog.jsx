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

export function PermissionDeleteDialog({
  isOpen,
  onClose,
  permission,
  onConfirm,
  submitting = false,
  lang = "km",
}) {
  if (!isOpen || !permission) return null

  return (
    <AlertDialog open={isOpen} onOpenChange={onClose}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle className="flex items-center gap-2 text-rose-600">
            <AlertTriangle className="size-5" />
            <span>{lang === "km" ? "លុប Permission?" : "Delete Permission?"}</span>
          </AlertDialogTitle>
          <AlertDialogDescription>
            {lang === "km"
              ? `តើអ្នកប្រាកដថាចង់លុប Permission "${permission.name}" មែនទេ? Role ណាដែលកាន់ Permission នេះនឹងបាត់បង់សិទ្ធិនោះជាអចិន្ត្រៃយ៍។`
              : `Are you sure you want to permanently delete permission "${permission.name}"? Roles holding this permission will lose associated access rights.`}
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

export default PermissionDeleteDialog
