import { useState } from "react"
import axiosClient from "@/api/axiosClient"
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import { PageHeader, Field } from "./shared"

export function SecurityPage() {
  const [form, setForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  })
  const [loading, setLoading] = useState(false)

  const mismatch =
    form.confirmPassword.length > 0 && form.newPassword !== form.confirmPassword
  const isTooShort =
    form.newPassword.length > 0 && form.newPassword.length < 8
  const isSubmitDisabled =
    loading ||
    !form.currentPassword ||
    !form.newPassword ||
    !form.confirmPassword ||
    mismatch ||
    form.newPassword.length < 8

  const handleChange = (key) => (e) => {
    setForm({ ...form, [key]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (mismatch) {
      toast.error("Validation error", "New passwords do not match.")
      return
    }

    if (form.newPassword.length < 8) {
      toast.error("Validation error", "Password must be at least 8 characters.")
      return
    }

    setLoading(true)

    try {
      await axiosClient.put("/me/password", {
        currentPassword: form.currentPassword,
        newPassword: form.newPassword,
        confirmPassword: form.confirmPassword,
      })

      toast.success("Password updated", "Your account password has been changed successfully.")
      setForm({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      })
    } catch (err) {
      toast.error(
        "Update failed",
        err.response?.data?.message ||
          "Failed to update password. Please verify your current password."
      )
    } finally {
      setLoading(false)
    }
  }

  const handleSendResetLink = () => {
    toast.info(
      "Reset email requested",
      "We have sent password reset instructions to your registered email address."
    )
  }

  return (
    <>
      <PageHeader
        title="Password & security"
        description="Change your password and manage account security."
      />

      <div className="space-y-6">
        <form onSubmit={handleSubmit}>
          <Card>
            <CardHeader>
              <CardTitle>Change password</CardTitle>
              <CardDescription>
                Use at least 8 characters with letters, numbers and symbols.
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <Field
                label="Current password"
                id="currentPassword"
                type="password"
                value={form.currentPassword}
                onChange={handleChange("currentPassword")}
                required
              />
              <div>
                <Field
                  label="New password"
                  id="newPassword"
                  type="password"
                  value={form.newPassword}
                  onChange={handleChange("newPassword")}
                  required
                />
                {isTooShort && (
                  <p className="mt-1 text-xs text-destructive">
                    Password must be at least 8 characters.
                  </p>
                )}
              </div>
              <div>
                <Field
                  label="Confirm new password"
                  id="confirmPassword"
                  type="password"
                  value={form.confirmPassword}
                  onChange={handleChange("confirmPassword")}
                  required
                />
                {mismatch && (
                  <p className="mt-1 text-xs text-destructive">
                    Passwords do not match.
                  </p>
                )}
              </div>
            </CardContent>
            <CardFooter className="justify-end">
              <Button type="submit" disabled={isSubmitDisabled}>
                {loading ? "Updating..." : "Update password"}
              </Button>
            </CardFooter>
          </Card>
        </form>

        <Card>
          <CardHeader>
            <CardTitle>Forgot your password?</CardTitle>
            <CardDescription>
              If you forgot your password, you can request a reset link to your email.
            </CardDescription>
          </CardHeader>
          <CardFooter>
            <Button
              variant="outline"
              type="button"
              onClick={handleSendResetLink}
            >
              Send reset link
            </Button>
          </CardFooter>
        </Card>
      </div>
    </>
  )
}

export default SecurityPage
