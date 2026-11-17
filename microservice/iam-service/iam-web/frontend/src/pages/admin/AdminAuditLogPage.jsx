import { useState, useEffect } from "react"
import {
  FileText,
  RefreshCw,
  Search,
  ChevronLeft,
  ChevronRight,
  Shield,
  User,
  Key,
  Lock,
  Unlock,
  AlertTriangle,
  CheckCircle,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Card, CardHeader, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"

export function AdminAuditLogPage() {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [data, setData] = useState({ content: [], page: { number: 0, size: 20, totalElements: 0, totalPages: 0 } })
  const [loading, setLoading] = useState(true)
  const [currentPage, setCurrentPage] = useState(0)
  const [search, setSearch] = useState("")

  const fetchAuditLogs = async (page = 0) => {
    setLoading(true)
    try {
      const res = await axiosClient.get(`/admin/audit-logs?page=${page}&size=20`)
      setData(res.data || { content: [], page: { number: 0, size: 20, totalElements: 0, totalPages: 0 } })
      setCurrentPage(page)
    } catch (err) {
      console.error("Failed to load audit logs:", err)
      toast.error(
        isKm ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || (isKm ? "មិនអាចទាញយកកំណត់ហេតុសវនកម្មបានទេ។" : "Could not retrieve audit logs.")
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchAuditLogs(0)
  }, [])

  const formatTimestamp = (iso) => {
    if (!iso) return "-"
    return new Date(iso).toLocaleString(undefined, {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    })
  }

  const getActionBadge = (action) => {
    switch (action) {
      case "USER_CREATED":
        return (
          <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-xs">
            <User className="mr-1 size-3 inline" /> {isKm ? "បង្កើតអ្នកប្រើ" : "User Created"}
          </Badge>
        )
      case "USER_UPDATED":
        return (
          <Badge variant="outline" className="border-blue-200 bg-blue-50 text-blue-700 text-xs">
            <User className="mr-1 size-3 inline" /> {isKm ? "កែប្រែអ្នកប្រើ" : "User Updated"}
          </Badge>
        )
      case "USER_DELETED":
        return (
          <Badge variant="outline" className="border-rose-200 bg-rose-50 text-rose-700 text-xs">
            <User className="mr-1 size-3 inline" /> {isKm ? "លុបអ្នកប្រើ" : "User Deleted"}
          </Badge>
        )
      case "ROLE_ASSIGNED":
        return (
          <Badge variant="outline" className="border-purple-200 bg-purple-50 text-purple-700 text-xs">
            <Shield className="mr-1 size-3 inline" /> {isKm ? "កំណត់ Role" : "Role Assigned"}
          </Badge>
        )
      case "ROLE_REVOKED":
        return (
          <Badge variant="outline" className="border-amber-200 bg-amber-50 text-amber-700 text-xs">
            <Shield className="mr-1 size-3 inline" /> {isKm ? "ដក Role" : "Role Revoked"}
          </Badge>
        )
      case "PERMISSION_UPDATED":
        return (
          <Badge variant="outline" className="border-cyan-200 bg-cyan-50 text-cyan-700 text-xs">
            <Key className="mr-1 size-3 inline" /> {isKm ? "កែប្រែសិទ្ធិ" : "Permission Updated"}
          </Badge>
        )
      case "PASSWORD_RESET":
        return (
          <Badge variant="outline" className="border-indigo-200 bg-indigo-50 text-indigo-700 text-xs">
            <Key className="mr-1 size-3 inline" /> {isKm ? "កំណត់ពាក្យសម្ងាត់ថ្មី" : "Password Reset"}
          </Badge>
        )
      case "ACCOUNT_LOCKED":
        return (
          <Badge variant="outline" className="border-rose-200 bg-rose-50 text-rose-700 text-xs">
            <Lock className="mr-1 size-3 inline" /> {isKm ? "ចាក់សោគណនី" : "Account Locked"}
          </Badge>
        )
      case "ACCOUNT_UNLOCKED":
        return (
          <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-xs">
            <Unlock className="mr-1 size-3 inline" /> {isKm ? "ដោះសោគណនី" : "Account Unlocked"}
          </Badge>
        )
      case "ACCOUNT_ENABLED":
        return (
          <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-xs">
            <CheckCircle className="mr-1 size-3 inline" /> {isKm ? "បើកដំណើរការ" : "Account Enabled"}
          </Badge>
        )
      case "ACCOUNT_DISABLED":
        return (
          <Badge variant="outline" className="border-amber-200 bg-amber-50 text-amber-700 text-xs">
            <AlertTriangle className="mr-1 size-3 inline" /> {isKm ? "បិទដំណើរការ" : "Account Disabled"}
          </Badge>
        )
      case "CONFIG_UPDATED":
        return (
          <Badge variant="outline" className="border-slate-300 bg-slate-100 text-slate-700 text-xs">
            {isKm ? "កែប្រែ Config" : "Config Updated"}
          </Badge>
        )
      default:
        return (
          <Badge variant="outline" className="text-xs">
            {action || "-"}
          </Badge>
        )
    }
  }

  const filteredLogs = data.content.filter((item) => {
    if (!search.trim()) return true
    const q = search.toLowerCase()
    return (
      item.actor?.toLowerCase().includes(q) ||
      item.details?.toLowerCase().includes(q) ||
      item.targetId?.toLowerCase().includes(q) ||
      item.targetType?.toLowerCase().includes(q) ||
      item.action?.toLowerCase().includes(q)
    )
  })

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={isKm ? "កំណត់ហេតុសវនកម្ម (Audit Log)" : "Audit Log"}
          description={
            isKm
              ? "តាមដាន និងត្រួតពិនិត្យរាល់សកម្មភាពរដ្ឋបាល ការកែប្រែទិន្នន័យ និងព្រឹត្តិការណ៍សុវត្ថិភាពសំខាន់ៗ"
              : "System-wide audit trail of administrative actions, resource modifications, and security events."
          }
        />
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="px-2.5 py-1 text-xs font-normal border-muted-foreground/30">
            {data.page.totalElements} {isKm ? "កំណត់ត្រា" : "records"}
          </Badge>
        </div>
      </div>

      <Card>
        <CardHeader className="pb-3">
          <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
            <div className="relative flex-1 max-w-sm">
              <Search className="absolute left-2.5 top-2.5 size-4 text-muted-foreground" />
              <Input
                placeholder={isKm ? "ស្វែងរកតាម Actor, Action, Details..." : "Search logs by actor, action, details..."}
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-8 text-xs"
              />
            </div>
          </div>
        </CardHeader>

        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-muted/50 text-xs uppercase text-muted-foreground border-y">
                <tr>
                  <th className="px-4 py-3">{isKm ? "កាលបរិច្ឆេទ" : "Timestamp"}</th>
                  <th className="px-4 py-3">{isKm ? "អ្នកធ្វើសកម្មភាព (Actor)" : "Actor"}</th>
                  <th className="px-4 py-3">{isKm ? "សកម្មភាព (Action)" : "Action"}</th>
                  <th className="px-4 py-3">{isKm ? "គោលដៅ (Target)" : "Target"}</th>
                  <th className="px-4 py-3">{isKm ? "ព័ត៌មានលម្អិត (Details)" : "Details"}</th>
                  <th className="px-4 py-3">{isKm ? "IP Address" : "IP"}</th>
                </tr>
              </thead>
              <tbody className="divide-y">
                {loading ? (
                  <tr>
                    <td colSpan="6" className="px-4 py-12 text-center text-muted-foreground">
                      <RefreshCw className="mx-auto size-6 animate-spin mb-2" />
                      {isKm ? "កំពុងទាញយកកំណត់ហេតុ..." : "Loading audit logs..."}
                    </td>
                  </tr>
                ) : filteredLogs.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="px-4 py-12 text-center text-muted-foreground">
                      <FileText className="mx-auto size-8 mb-2 opacity-40" />
                      {isKm ? "មិនមានកំណត់ត្រាសវនកម្មទេ" : "No audit logs found."}
                    </td>
                  </tr>
                ) : (
                  filteredLogs.map((log) => (
                    <tr key={log.id} className="hover:bg-muted/30 transition-colors text-xs">
                      <td className="px-4 py-3 whitespace-nowrap text-muted-foreground font-mono">
                        {formatTimestamp(log.createdAt)}
                      </td>
                      <td className="px-4 py-3 font-medium">
                        <span className="rounded bg-muted px-1.5 py-0.5 font-mono">
                          {log.actor || "SYSTEM"}
                        </span>
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap">
                        {getActionBadge(log.action)}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap">
                        {log.targetType ? (
                          <span className="font-semibold text-muted-foreground">
                            {log.targetType}:{" "}
                            <span className="font-mono font-normal">
                              {log.targetId ? log.targetId.slice(0, 8) + "..." : "-"}
                            </span>
                          </span>
                        ) : (
                          "-"
                        )}
                      </td>
                      <td className="px-4 py-3 text-muted-foreground max-w-xs truncate" title={log.details}>
                        {log.details || "-"}
                      </td>
                      <td className="px-4 py-3 whitespace-nowrap font-mono text-muted-foreground">
                        {log.ipAddress || "-"}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {data.page.totalPages > 1 && (
            <div className="flex items-center justify-between px-4 py-3 border-t bg-muted/20">
              <span className="text-xs text-muted-foreground">
                {isKm ? "ទំព័រ" : "Page"} {data.page.number + 1} / {data.page.totalPages} ({data.page.totalElements}{" "}
                {isKm ? "សរុប" : "total"})
              </span>
              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="xs"
                  onClick={() => fetchAuditLogs(currentPage - 1)}
                  disabled={currentPage <= 0 || loading}
                  className="gap-1 text-xs"
                >
                  <ChevronLeft className="size-3.5" />
                  <span>{isKm ? "ថយក្រោយ" : "Previous"}</span>
                </Button>
                <Button
                  variant="outline"
                  size="xs"
                  onClick={() => fetchAuditLogs(currentPage + 1)}
                  disabled={currentPage >= data.page.totalPages - 1 || loading}
                  className="gap-1 text-xs"
                >
                  <span>{isKm ? "បន្ទាប់" : "Next"}</span>
                  <ChevronRight className="size-3.5" />
                </Button>
              </div>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

export default AdminAuditLogPage
