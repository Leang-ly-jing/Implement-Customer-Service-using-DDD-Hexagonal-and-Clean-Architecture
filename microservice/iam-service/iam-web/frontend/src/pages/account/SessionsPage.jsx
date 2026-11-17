import { useState, useEffect } from "react"
import { LogOut, Laptop, Smartphone } from "lucide-react"
import axiosClient from "@/api/axiosClient"
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import { PageHeader, Confirm } from "./shared"

export function SessionsPage() {
  const [sessions, setSessions] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let isMounted = true

    const loadSessions = async () => {
      try {
        const res = await axiosClient.get("/me/sessions")
        if (isMounted) {
          setSessions(res.data || [])
        }
      } catch {
        if (isMounted) {
          toast.error("Failed to load sessions", "Could not retrieve your active sessions.")
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    loadSessions()

    return () => {
      isMounted = false
    }
  }, [])

  const handleTerminate = async (sessionId) => {
    try {
      await axiosClient.delete(`/me/sessions/${sessionId}`)
      setSessions((prev) => prev.filter((s) => s.id !== sessionId))
      toast.success("Session terminated", "The selected session has been logged out.")
    } catch (err) {
      toast.error(
        "Action failed",
        err.response?.data?.message || "Unable to terminate session."
      )
    }
  }

  const handleLogOutAll = async () => {
    try {
      await axiosClient.delete("/me/sessions")
      setSessions((prev) => prev.filter((s) => s.current))
      toast.success(
        "All other sessions revoked",
        "You have been logged out from all other devices."
      )
    } catch (err) {
      toast.error(
        "Action failed",
        err.response?.data?.message || "Unable to log out other devices."
      )
    }
  }

  if (loading) {
    return (
      <div className="flex h-48 items-center justify-center">
        <p className="text-sm text-muted-foreground">Loading active sessions...</p>
      </div>
    )
  }

  return (
    <>
      <PageHeader
        title="Active sessions"
        description="Devices currently signed in to your account."
      />

      <Card>
        <CardHeader className="flex-row items-center justify-between space-y-0">
          <CardTitle className="text-base">{sessions.length} active sessions</CardTitle>
          {sessions.length > 1 && (
            <Confirm
              trigger={
                <Button variant="outline" size="sm" className="gap-2">
                  <LogOut className="size-4" /> Log out all devices
                </Button>
              }
              title="Log out from all other devices?"
              description="All other sessions will be terminated immediately. You will stay signed in on this current device."
              action="Log out all"
              destructive
              onConfirm={handleLogOutAll}
            />
          )}
        </CardHeader>

        <CardContent className="divide-y p-0">
          {sessions.length === 0 ? (
            <div className="p-6 text-center text-sm text-muted-foreground">
              No active sessions found.
            </div>
          ) : (
            sessions.map((s) => {
              const Icon = s.deviceType === "mobile" ? Smartphone : Laptop
              return (
                <div key={s.id} className="flex items-center gap-4 p-5">
                  <div className="flex size-10 shrink-0 items-center justify-center rounded-lg bg-muted">
                    <Icon className="size-5 text-muted-foreground" />
                  </div>

                  <div className="min-w-0 flex-1 space-y-0.5">
                    <div className="flex flex-wrap items-center gap-2 font-medium">
                      <span>{s.browser}</span>
                      {s.current && (
                        <Badge variant="secondary" className="text-xs font-normal">
                          This device
                        </Badge>
                      )}
                    </div>
                    <p className="text-sm text-muted-foreground">
                      {s.ipAddress} {s.location ? `· ${s.location}` : ""}
                    </p>
                    <p className="text-xs text-muted-foreground">
                      {s.lastActiveAt ? new Date(s.lastActiveAt).toLocaleString() : ""}
                    </p>
                  </div>

                  {!s.current && (
                    <Confirm
                      trigger={
                        <Button variant="outline" size="sm">
                          Terminate
                        </Button>
                      }
                      title="Terminate this session?"
                      description="This device will be logged out and will need to sign in again to access the account."
                      action="Terminate"
                      destructive
                      onConfirm={() => handleTerminate(s.id)}
                    />
                  )}
                </div>
              )
            })
          )}
        </CardContent>
      </Card>
    </>
  )
}

export default SessionsPage