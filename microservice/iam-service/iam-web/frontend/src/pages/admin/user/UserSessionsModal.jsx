import { useState, useEffect } from "react"
import {
  Activity,
  X,
  RefreshCw,
  LogOut,
  Monitor,
  Smartphone,
  Laptop,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserSessionsModal({ isOpen, onClose, user }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [sessions, setSessions] = useState([])
  const [loading, setLoading] = useState(false)
  const [actionId, setActionId] = useState(null)
  const [terminatingAll, setTerminatingAll] = useState(false)

  const fetchSessions = async () => {
    if (!user?.uuid) return
    setLoading(true)
    setSessions([])
    try {
      const res = await axiosClient.get(`/users/${user.uuid}/sessions`)
      setSessions(res.data || [])
    } catch (err) {
      toast.error(isKm ? "កំហុស" : "Error", err.response?.data?.detail || "Could not load sessions.")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (isOpen && user?.uuid) {
      fetchSessions()
    }
  }, [isOpen, user?.uuid])

  if (!isOpen || !user) return null

  const handleTerminateSession = async (sessionId) => {
    setActionId(sessionId)
    try {
      await axiosClient.delete(`/users/${user.uuid}/sessions/${sessionId}`)
      toast.success(
        isKm ? "បានបញ្ឈប់" : "Session Terminated",
        isKm ? "Session ត្រូវបានបញ្ឈប់ដោយជោគជ័យ។" : "The session has been terminated."
      )
      setSessions((prev) => prev.filter((s) => s.id !== sessionId))
    } catch (err) {
      toast.error(isKm ? "កំហុស" : "Error", err.response?.data?.detail || "Could not terminate session.")
    } finally {
      setActionId(null)
    }
  }

  const handleTerminateAll = async () => {
    setTerminatingAll(true)
    try {
      await axiosClient.delete(`/users/${user.uuid}/sessions`)
      toast.success(
        isKm ? "បានបញ្ឈប់ទាំងអស់" : "All Sessions Terminated",
        isKm
          ? `Sessions ទាំងអស់របស់ ${user.username} ត្រូវបានបញ្ឈប់។`
          : `All sessions for ${user.username} have been terminated.`
      )
      setSessions([])
    } catch (err) {
      toast.error(isKm ? "កំហុស" : "Error", err.response?.data?.detail || "Could not terminate all sessions.")
    } finally {
      setTerminatingAll(false)
    }
  }

  const formatSessionTime = (iso) => {
    if (!iso) return "-"
    return new Date(iso).toLocaleString(undefined, {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    })
  }

  const getDeviceIcon = (deviceType) => {
    if (!deviceType) return <Monitor className="size-4" />
    const d = deviceType.toLowerCase()
    if (d.includes("mobile") || d.includes("phone")) return <Smartphone className="size-4" />
    if (d.includes("tablet")) return <Laptop className="size-4" />
    return <Monitor className="size-4" />
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-2xl rounded-xl bg-background border shadow-xl animate-in fade-in-0 zoom-in-95 flex flex-col max-h-[90vh]">

        <div className="flex items-center justify-between px-6 py-4 border-b shrink-0">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <Activity className="size-5 text-cyan-600" />
            {isKm ? "Sessions របស់អ្នកប្រើ" : "User Sessions"}
          </h3>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        <div className="px-6 py-3 border-b bg-muted/30 shrink-0">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="flex size-10 items-center justify-center rounded-full bg-cyan-100 text-cyan-700 font-bold text-sm uppercase">
                {(user.username || "U").slice(0, 2)}
              </div>
              <div>
                <div className="font-semibold text-sm">{user.name || user.username}</div>
                <div className="text-xs text-muted-foreground">@{user.username}</div>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <Badge variant="outline" className="border-cyan-200 bg-cyan-50 text-cyan-700 text-xs">
                {sessions.length} {isKm ? "Session សកម្ម" : "Active Sessions"}
              </Badge>
              {sessions.length > 0 && (
                <Button
                  variant="outline"
                  size="xs"
                  disabled={terminatingAll}
                  className="text-rose-600 border-rose-200 hover:bg-rose-50 text-xs gap-1"
                  onClick={handleTerminateAll}
                >
                  {terminatingAll ? (
                    <RefreshCw className="size-3 animate-spin" />
                  ) : (
                    <LogOut className="size-3" />
                  )}
                  {isKm ? "បញ្ឈប់ទាំងអស់" : "Terminate All"}
                </Button>
              )}
            </div>
          </div>
        </div>

        <div className="overflow-y-auto flex-1 px-6 py-4">
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <RefreshCw className="size-5 animate-spin text-muted-foreground" />
            </div>
          ) : sessions.length === 0 ? (
            <div className="flex h-40 flex-col items-center justify-center gap-2 text-muted-foreground">
              <Activity className="size-8 opacity-30" />
              <p className="text-sm">{isKm ? "មិនមាន Session សកម្មទេ" : "No active sessions found."}</p>
            </div>
          ) : (
            <div className="space-y-3">
              {sessions.map((session) => (
                <div
                  key={session.id}
                  className="flex items-start justify-between p-4 rounded-lg border bg-card hover:bg-muted/20 transition-colors gap-3"
                >
                  <div className="flex items-start gap-3 min-w-0">
                    <div className="mt-0.5 shrink-0 p-2 rounded-lg bg-cyan-50 text-cyan-600">
                      {getDeviceIcon(session.deviceType)}
                    </div>
                    <div className="min-w-0 space-y-1">
                      <div className="flex flex-wrap items-center gap-1.5">
                        <span className="font-medium text-sm text-foreground">
                          {session.browser || (isKm ? "កម្មវិធីរុករក" : "Unknown Browser")}
                        </span>
                        {session.deviceType && (
                          <Badge variant="outline" className="text-[10px] py-0 px-1.5 font-normal">
                            {session.deviceType}
                          </Badge>
                        )}
                      </div>
                      <div className="flex flex-wrap items-center gap-3 text-xs text-muted-foreground">
                        {session.ipAddress && (
                          <span className="font-mono">{session.ipAddress}</span>
                        )}
                        {session.location && (
                          <span>{session.location}</span>
                        )}
                      </div>
                      <div className="flex flex-wrap items-center gap-3 text-xs text-muted-foreground">
                        <span>
                          <span className="font-medium text-foreground/70">
                            {isKm ? "ចុងក្រោយ:" : "Last active:"}
                          </span>{" "}
                          {formatSessionTime(session.lastActiveAt)}
                        </span>
                        <span>
                          <span className="font-medium text-foreground/70">
                            {isKm ? "ចាប់ផ្ដើម:" : "Started:"}
                          </span>{" "}
                          {formatSessionTime(session.createdAt)}
                        </span>
                      </div>
                    </div>
                  </div>

                  <Button
                    variant="outline"
                    size="xs"
                    disabled={actionId === session.id}
                    className="shrink-0 text-rose-600 border-rose-200 hover:bg-rose-50 hover:text-rose-700 text-xs gap-1"
                    onClick={() => handleTerminateSession(session.id)}
                  >
                    {actionId === session.id ? (
                      <RefreshCw className="size-3 animate-spin" />
                    ) : (
                      <LogOut className="size-3" />
                    )}
                    {isKm ? "បញ្ឈប់" : "Terminate"}
                  </Button>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="flex justify-end px-6 py-4 border-t shrink-0">
          <Button variant="outline" onClick={onClose}>
            {t("common.close")}
          </Button>
        </div>
      </div>
    </div>
  )
}

export default UserSessionsModal
