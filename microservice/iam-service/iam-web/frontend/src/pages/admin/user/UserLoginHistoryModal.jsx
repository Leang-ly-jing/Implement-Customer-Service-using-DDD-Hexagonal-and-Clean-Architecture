import { useState, useEffect } from "react"
import {
  History,
  X,
  CheckCircle2,
  XCircle,
  AlertTriangle,
  RefreshCw,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserLoginHistoryModal({ isOpen, onClose, user }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (!isOpen || !user?.uuid) return
    const fetchHistory = async () => {
      setLoading(true)
      setHistory([])
      try {
        const res = await axiosClient.get(`/users/${user.uuid}/login-history?page=0&size=20`)
        setHistory(res.data?.content || [])
      } catch (err) {
        toast.error(isKm ? "កំហុស" : "Error", err.response?.data?.detail || "Could not load login history.")
      } finally {
        setLoading(false)
      }
    }
    fetchHistory()
  }, [isOpen, user?.uuid])

  if (!isOpen || !user) return null

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

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-2xl rounded-xl bg-background border shadow-xl animate-in fade-in-0 zoom-in-95 flex flex-col max-h-[90vh]">
        <div className="flex items-center justify-between px-6 py-4 border-b shrink-0">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <History className="size-5 text-indigo-600" />
            {isKm ? "ប្រវត្តិចូលប្រើប្រាស់របស់អ្នកប្រើ" : "User Login History"}
          </h3>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        <div className="px-6 py-3 border-b bg-muted/30 shrink-0">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="flex size-10 items-center justify-center rounded-full bg-indigo-100 text-indigo-700 font-bold text-sm uppercase">
                {(user.username || "U").slice(0, 2)}
              </div>
              <div>
                <div className="font-semibold text-sm">{user.name || user.username}</div>
                <div className="text-xs text-muted-foreground">@{user.username}</div>
              </div>
            </div>
            <Badge variant="outline" className="border-indigo-200 bg-indigo-50 text-indigo-700 text-xs">
              {history.length} {isKm ? "កំណត់ត្រាថ្មីៗ" : "Recent Records"}
            </Badge>
          </div>
        </div>

        <div className="overflow-y-auto flex-1 px-6 py-4">
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <RefreshCw className="size-5 animate-spin text-muted-foreground" />
            </div>
          ) : history.length === 0 ? (
            <div className="flex h-40 flex-col items-center justify-center gap-2 text-muted-foreground">
              <History className="size-8 opacity-30" />
              <p className="text-sm">{isKm ? "មិនទាន់មានកំណត់ត្រាចូលប្រើទេ" : "No login history recorded yet."}</p>
            </div>
          ) : (
            <div className="space-y-3">
              {history.map((h) => (
                <div
                  key={h.id}
                  className={`p-3.5 rounded-lg border bg-card transition-colors flex flex-col gap-1.5 ${
                    h.suspicious ? "border-amber-300 bg-amber-50/30" : ""
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      {h.status === "SUCCESS" ? (
                        <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-xs">
                          <CheckCircle2 className="size-3 mr-1 inline" />
                          {isKm ? "ជោគជ័យ" : "Success"}
                        </Badge>
                      ) : (
                        <Badge variant="outline" className="border-rose-200 bg-rose-50 text-rose-700 text-xs">
                          <XCircle className="size-3 mr-1 inline" />
                          {isKm ? "បរាជ័យ" : "Failed"}
                        </Badge>
                      )}
                      {h.failureReason && (
                        <span className="text-xs text-rose-600 font-medium">
                          {h.failureReason}
                        </span>
                      )}
                    </div>
                    <span className="text-xs text-muted-foreground">
                      {formatSessionTime(h.loginAt)}
                    </span>
                  </div>

                  {h.suspicious && (
                    <div className="flex items-center gap-1.5 text-xs text-amber-800 bg-amber-100/70 px-2 py-1 rounded">
                      <AlertTriangle className="size-3.5 text-amber-600 shrink-0" />
                      <span>{h.suspiciousReason || (isKm ? "សកម្មភាពគួរឱ្យសង្ស័យ" : "Suspicious activity detected")}</span>
                    </div>
                  )}

                  <div className="flex flex-wrap items-center gap-3 text-xs text-muted-foreground pt-0.5">
                    {h.ipAddress && (
                      <span className="font-mono text-foreground/80">IP: {h.ipAddress}</span>
                    )}
                    {h.device && (
                      <span className="truncate max-w-xs">{h.device}</span>
                    )}
                    {h.deviceType && (
                      <Badge variant="outline" className="text-[10px] py-0 px-1 font-normal">
                        {h.deviceType}
                      </Badge>
                    )}
                  </div>
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

export default UserLoginHistoryModal
