import { useState } from "react"
import { Plus, X } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

const INITIAL_FORM = {
  userName: "",
  email: "",
  password: "",
  confirmPassword: "",
  givenName: "",
  familyName: "",
  phoneNumber: "",
  gender: "MALE",
  locate: "km",
}

export function UserCreateModal({ isOpen, onClose, onUserCreated }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [form, setForm] = useState(INITIAL_FORM)
  const [loading, setLoading] = useState(false)

  if (!isOpen) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (form.password !== form.confirmPassword) {
      toast.error(isKm ? "កំហុសផ្ទៀងផ្ទាត់" : "Validation Error", isKm ? "ពាក្យសម្ងាត់មិនដូចគ្នាទេ។" : "Passwords do not match.")
      return
    }
    if (form.password.length < 8) {
      toast.error(isKm ? "កំហុសផ្ទៀងផ្ទាត់" : "Validation Error", isKm ? "ពាក្យសម្ងាត់ត្រូវមានយ៉ាងតិច ៨ តួអក្សរ។" : "Password must be at least 8 characters.")
      return
    }

    setLoading(true)
    try {
      await axiosClient.post("/users", {
        userName: form.userName.trim(),
        email: form.email.trim(),
        password: form.password,
        confirmPassword: form.confirmPassword,
        givenName: form.givenName.trim(),
        familyName: form.familyName.trim(),
        phoneNumber: form.phoneNumber.trim() || null,
        gender: form.gender,
        locate: form.locate,
      })
      toast.success(
        isKm ? "បង្កើតជោគជ័យ" : "User Created",
        isKm
          ? `អ្នកប្រើប្រាស់ ${form.userName} ត្រូវបានបង្កើតដោយជោគជ័យ។`
          : `User ${form.userName} has been created successfully.`
      )
      setForm(INITIAL_FORM)
      onUserCreated?.()
      onClose()
    } catch (err) {
      const msg = err.response?.data?.detail || err.response?.data?.message || (isKm ? "ការបង្កើតអ្នកប្រើប្រាស់បានបរាជ័យ" : "Failed to create user.")
      toast.error(isKm ? "បរាជ័យ" : "Create User Failed", msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-lg rounded-xl bg-background border p-6 shadow-xl animate-in fade-in-0 zoom-in-95">
        <div className="flex items-center justify-between pb-3 border-b">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <Plus className="size-5 text-primary" />
            {isKm ? "បង្កើតអ្នកប្រើប្រាស់ថ្មី" : "Create New User"}
          </h3>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4 pt-4">
          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "នាមត្រកូល (Family Name) *" : "Family Name *"}
              </label>
              <Input
                required
                value={form.familyName}
                onChange={(e) => setForm({ ...form, familyName: e.target.value })}
                placeholder="e.g. Chan"
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "នាមខ្លួន (Given Name) *" : "Given Name *"}
              </label>
              <Input
                required
                value={form.givenName}
                onChange={(e) => setForm({ ...form, givenName: e.target.value })}
                placeholder="e.g. Dara"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "ឈ្មោះគណនី (Username) *" : "Username *"}
              </label>
              <Input
                required
                minLength={3}
                maxLength={50}
                value={form.userName}
                onChange={(e) => setForm({ ...form, userName: e.target.value })}
                placeholder="e.g. chandara"
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "អ៊ីមែល (Email) *" : "Email *"}
              </label>
              <Input
                required
                type="email"
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
                placeholder="dara@example.com"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "ពាក្យសម្ងាត់ (Password) *" : "Password *"}
              </label>
              <Input
                required
                type="password"
                minLength={8}
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                placeholder="Min. 8 characters"
              />
            </div>
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "បញ្ជាក់ពាក្យសម្ងាត់ *" : "Confirm Password *"}
              </label>
              <Input
                required
                type="password"
                minLength={8}
                value={form.confirmPassword}
                onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })}
                placeholder="Re-enter password"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="space-y-1">
              <label className="text-xs font-medium text-foreground">
                {isKm ? "លេខទូរស័ព្ទ (Phone)" : "Phone Number"}
              </label>
              <Input
                value={form.phoneNumber}
                onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
                placeholder="012345678"
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
                ? (isKm ? "កំពុងបង្កើត..." : "Creating...")
                : t("common.create")}
            </Button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default UserCreateModal
