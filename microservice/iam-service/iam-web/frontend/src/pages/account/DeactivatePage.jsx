import { useState } from "react"
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { authService } from "@/api/authService"
import { PageHeader, Confirm } from "./shared"

export function DeactivatePage() {
  const [reason, setReason] = useState("")
  const [submitting, setSubmitting] = useState(false)

  const handleDeactivate = async () => {
    setSubmitting(true)
    try {
      await axiosClient.post("/me/deactivate", { reason })
      toast.success("Account deactivated", "Your account has been deactivated successfully.")
      authService.logout()
    } catch (err) {
      toast.error(
        "Action failed",
        err.response?.data?.message || "Unable to deactivate account."
      )
      setSubmitting(false)
    }
  }

  return (
    <>
      <PageHeader
        title="Deactivation"
        description="Request deactivation of your account."
      />

      <Card className="border-destructive/50">
        <CardHeader>
          <CardTitle className="text-destructive">Deactivate account</CardTitle>
          <CardDescription>
            This cannot be undone without contacting support. All active sessions will be terminated immediately.
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-2">
          <Label htmlFor="reason">Reason (optional)</Label>
          <Textarea
            id="reason"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            placeholder="Tell us why you are leaving..."
            disabled={submitting}
          />
        </CardContent>
        <CardFooter className="justify-end">
          <Confirm
            trigger={
              <Button variant="destructive" disabled={submitting}>
                {submitting ? "Deactivating..." : "Request deactivation"}
              </Button>
            }
            title="Are you absolutely sure?"
            description="Your account will be disabled and all active sessions will be terminated immediately."
            action="Confirm"
            destructive
            onConfirm={handleDeactivate}
          />
        </CardFooter>
      </Card>
    </>
  )
}

export default DeactivatePage
