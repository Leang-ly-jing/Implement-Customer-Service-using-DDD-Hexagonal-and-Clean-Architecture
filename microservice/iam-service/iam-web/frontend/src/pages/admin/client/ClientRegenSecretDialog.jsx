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

export function ClientRegenSecretDialog({
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
          <AlertDialogTitle className="flex items-center gap-2 text-amber-600">
            <AlertTriangle className="size-5" />
            <span>{lang === "km" ? "បង្កើត Secret ថ្មី?" : "Regenerate Client Secret?"}</span>
          </AlertDialogTitle>
          <AlertDialogDescription>
            {lang === "km"
              ? `តើអ្នកប្រាកដថាចង់ Reset Secret សម្រាប់ Client "${client.clientId}" មែនទេ? កម្មវិធីដែលកំពុងប្រើ Secret ចាស់នឹងលែងដំណើរការរហូតដល់ត្រូវបាន Update Secret ថ្មី។`
              : `Are you sure you want to regenerate the secret for client "${client.clientId}"? Applications currently using the existing secret will stop authenticating until updated.`}
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel disabled={submitting}>
            {lang === "km" ? "បោះបង់" : "Cancel"}
          </AlertDialogCancel>
          <AlertDialogAction
            onClick={onConfirm}
            disabled={submitting}
            className="bg-amber-600 hover:bg-amber-700 text-white"
          >
            {submitting
              ? lang === "km" ? "កំពុងដំណើរការ..." : "Regenerating..."
              : lang === "km" ? "បង្កើត Secret ថ្មី" : "Regenerate"}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}

export default ClientRegenSecretDialog
