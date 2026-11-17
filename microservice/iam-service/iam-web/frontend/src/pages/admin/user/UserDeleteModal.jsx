import { useState } from "react"
import { AlertTriangle } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserDeleteModal({ isOpen, onClose, user, onUserDeleted }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [loading, setLoading] = useState(false)

  if (!isOpen || !user) return null

  const handleDelete = async () => {
    setLoading(true)
    try {
      await axiosClient.delete(`/users/${user.uuid}`)
      toast.success(
        isKm ? "លុបជោគជ័យ" : "User Deleted",
        isKm
          ? `អ្នកប្រើប្រាស់ @${user.username} ត្រូវបានលុបចេញពីប្រព័ន្ធ។`
          : `User @${user.username} deleted successfully.`
      )
      onUserDeleted?.()
      onClose()
    } catch (err) {
      toast.error("Delete Failed", err.response?.data?.detail || "Could not delete user account.")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-md rounded-xl bg-background border p-6 shadow-xl animate-in fade-in-0 zoom-in-95">
        <div className="flex items-center gap-3 text-rose-600 mb-3">
          <AlertTriangle className="size-6 shrink-0" />
          <h3 className="text-lg font-semibold text-foreground">
            {isKm ? "តើអ្នកពិតជាចង់លុបគណនីនេះមែនទេ?" : "Delete User Account?"}
          </h3>
        </div>

        <p className="text-sm text-muted-foreground mb-4">
          {isKm
            ? `អ្នកប្រើប្រាស់ @${user.username} នឹងត្រូវលុបចេញពីប្រព័ន្ធ។ សកម្មភាពនេះមិនអាចត្រឡប់វិញបានទេ។`
            : `User account @${user.username} will be marked as deleted. This action cannot be undone.`}
        </p>

        <div className="flex justify-end gap-2 pt-3 border-t">
          <Button
            type="button"
            variant="outline"
            disabled={loading}
            onClick={onClose}
          >
            {t("common.cancel")}
          </Button>
          <Button
            type="button"
            variant="destructive"
            disabled={loading}
            onClick={handleDelete}
          >
            {loading ? t("common.loading") : t("common.delete")}
          </Button>
        </div>
      </div>
    </div>
  )
}

export default UserDeleteModal
