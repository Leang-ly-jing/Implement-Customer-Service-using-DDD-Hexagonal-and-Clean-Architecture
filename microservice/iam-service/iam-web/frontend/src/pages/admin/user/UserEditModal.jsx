import { useState, useEffect } from "react"
import { Edit2, X } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserEditModal({ isOpen, onClose, user, onUserUpdated }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")

  const [form, setForm] = useState({
    givenName: "",
    familyName: "",
    nickname: "",
    phoneNumber: "",
    gender: "MALE",
    locale: "km",
  })
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (user) {
      const nameParts = (user.name || "").split(" ")
      setForm({
        givenName: nameParts[0] || "",
        familyName: nameParts.slice(1).join(" ") || "",
        nickname: user.nickname || "",
        phoneNumber: user.phoneNumber || "",
        gender: user.gender || "MALE",
        locale: user.locale || "km",
      })
    }
  }, [user])

  if (!isOpen || !user) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await axiosClient.patch(`/users/${user.uuid}`, {
        givenName: form.givenName.trim(),
        familyName: form.familyName.trim(),
        nickname: form.nickname.trim() || null,
        phoneNumber: form.phoneNumber.trim() || null,
        gender: form.gender,
        locale: form.locale,
      })
      toast.success(
        isKm ? "កែប្រែជោគជ័យ" : "User Updated",
        isKm
          ? `ព័ត៌មានអ្នកប្រើប្រាស់ ${user.username} ត្រូវបានកែប្រែដោយជោគជ័យ។`
          : `User ${user.username} updated successfully.`
      )
      onUserUpdated?.()
      onClose()
    } catch (err) {
      const msg = err.response?.data?.detail || err.response?.data?.message || (isKm ? "ការកែប្រែបានបរាជ័យ" : "Failed to update user.")
      toast.error(isKm ? "បរាជ័យ" : "Update Failed", msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-md rounded-xl bg-background border p-6 shadow-xl animate-in fade-in-0 zoom-in-95">
        <div className="flex items-center justify-between pb-3 border-b">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <Edit2 className="size-5 text-primary" />
            {isKm ? "កែប្រែព័ត៌មានអ្នកប្រើប្រាស់" : "Update User Profile"}
          </h3>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4 pt-4">
          <div className="text-xs text-muted-foreground pb-1">
            {isKm ? "កែប្រែគណនី:" : "Updating account:"}{" "}
            <span className="font-semibold text-foreground">@{user.username}</span>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "នាមត្រកូល (Family Name)" : "Family Name"}
              </label>
              <Input
                value={form.familyName}
                onChange={(e) => setForm({ ...form, familyName: e.target.value })}
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "នាមខ្លួន (Given Name)" : "Given Name"}
              </label>
              <Input
                value={form.givenName}
                onChange={(e) => setForm({ ...form, givenName: e.target.value })}
              />
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-medium text-foreground">
              {isKm ? "ឈ្មោះហៅក្រៅ (Nickname)" : "Nickname"}
            </label>
            <Input
              value={form.nickname}
              onChange={(e) => setForm({ ...form, nickname: e.target.value })}
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "លេខទូរស័ព្ទ (Phone)" : "Phone"}
              </label>
              <Input
                value={form.phoneNumber}
                onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "ភេទ (Gender)" : "Gender"}
              </label>
              <select
                className="w-full rounded-md border border-input bg-background px-3 py-2 text-sm shadow-xs focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring"
                value={form.gender}
                onChange={(e) => setForm({ ...form, gender: e.target.value })}
              >
                <option value="MALE">{isKm ? "ប្រុស (Male)" : "Male"}</option>
                <option value="FEMALE">{isKm ? "ស្រី (Female)" : "Female"}</option>
                <option value="OTHER">{isKm ? "ផ្សេងទៀត (Other)" : "Other"}</option>
              </select>
            </div>
          </div>

          <div className="flex justify-end gap-2 pt-3 border-t">
            <Button type="button" variant="outline" onClick={onClose}>
              {t("common.cancel")}
            </Button>
            <Button type="submit" disabled={loading}>
              {loading
                ? (isKm ? "កំពុងរក្សាទុក..." : "Saving...")
                : t("common.save")}
            </Button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default UserEditModal
