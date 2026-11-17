import { AlertTriangle } from "lucide-react"
import { useTranslation } from "react-i18next"
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

export function RoleDeleteDialog({
  isOpen,
  onClose,
  role,
  onConfirm,
  submitting = false,
}) {
  const { t } = useTranslation()

  if (!isOpen || !role) return null

  return (
    <AlertDialog open={isOpen} onOpenChange={onClose}>
      <AlertDialogContent className="sm:max-w-lg">
        <AlertDialogHeader>
          <AlertDialogTitle className="flex items-center gap-2 text-rose-600">
            <AlertTriangle className="size-5" />
            <span>{t("rolesPage.deleteConfirmTitle", "Delete Role?")}</span>
          </AlertDialogTitle>
          <AlertDialogDescription>
            {t("rolesPage.deleteConfirmDesc", `Are you sure you want to permanently delete role "${role.name}"? Users with this role may lose their access permissions.`)}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel disabled={submitting}>
            {t("common.cancel")}
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={onConfirm}
            disabled={submitting}
            className="bg-rose-600 hover:bg-rose-700 text-white"
          >
            {submitting ? t("common.deleting", "Deleting...") : t("common.delete")}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}

export default RoleDeleteDialog
