import { useState, useEffect } from "react"
import { useTranslation } from "react-i18next"
import {
  RefreshCw,
  AlertTriangle,
  CheckCircle2,
  XCircle,
  Monitor,
  Smartphone,
  Laptop,
  ChevronLeft,
  ChevronRight,
  ShieldAlert,
  ShieldCheck,
  History,
} from "lucide-react"
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"

export function AdminLoginHistoryPage() {
  const [data, setData] = useState({ content: [], page: { number: 0, size: 20, totalElements: 0, totalPages: 0 } })
  const [loading, setLoading] = useState(true)
  const [currentPage, setCurrentPage] = useState(0)

  const { i18n } = useTranslation()
  const rawLang = i18n.language || localStorage.getItem("lang") || "kh"
  const isKhmer = rawLang.startsWith("kh") || rawLang.startsWith("km")
  const lang = isKhmer ? "km" : "en"

  const fetchHistory = async (page = 0) => {
    setLoading(true)
    try {
      const res = await axiosClient.get(`/admin/login-history?page=${page}&size=20`)
      setData(res.data || { content: [], page: { number: 0, size: 20, totalElements: 0, totalPages: 0 } })
      setCurrentPage(page)
    } catch (err) {
      console.error("Failed to fetch login history:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Could not retrieve login history."
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchHistory(0)
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

  const getDeviceIcon = (deviceType) => {
    if (!deviceType) return <Monitor className="size-4" />
    const d = deviceType.toLowerCase()
    if (d.includes("mobile") || d.includes("phone")) return <Smartphone className="size-4" />
    if (d.includes("tablet")) return <Laptop className="size-4" />
    return <Monitor className="size-4" />
  }

  const formatReason = (reason) => {
    if (!reason) return null
    switch (reason) {
      case "BAD_CREDENTIALS":
        return lang === "km" ? "ពាក្យសម្ងាត់មិនត្រឹមត្រូវ" : "Invalid credentials"
      case "ACCOUNT_LOCKED":
        return lang === "km" ? "គណនីជាប់សោ" : "Account locked"
      case "ACCOUNT_DISABLED":
        return lang === "km" ? "គណនីត្រូវបានបិទ" : "Account disabled"
      case "USER_NOT_FOUND":
        return lang === "km" ? "រកមិនឃើញគណនី" : "User not found"
      default:
        return reason
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={lang === "km" ? "ប្រវត្តិចូលប្រើប្រាស់ប្រព័ន្ធ" : "Login History"}
          description={
            lang === "km"
              ? "តាមដានរាល់កំណត់ត្រានៃការចូលប្រើប្រាស់ ស្ថានភាពជោគជ័យ ឬបរាជ័យ និងសកម្មភាពសង្ស័យ"
              : "System-wide log of authentication attempts, successful logins, failures, and threat alerts."
          }
        />
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="px-2.5 py-1 text-xs font-normal border-muted-foreground/30">
            {data.page.totalElements} {lang === "km" ? "កំណត់ត្រា" : "records"}
          </Badge>
        </div>
      </div>

      <Card className="shadow-xs overflow-hidden border">
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm border-collapse">
              <thead>
                <tr className="bg-muted/40 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground border-b">
                  <th className="px-5 py-3.5">{lang === "km" ? "អ្នកប្រើប្រាស់" : "User"}</th>
                  <th className="px-4 py-3.5">{lang === "km" ? "ស្ថានភាព" : "Status"}</th>
                  <th className="px-4 py-3.5">{lang === "km" ? "សន្តិសុខ" : "Threat Alert"}</th>
                  <th className="px-4 py-3.5">{lang === "km" ? "IP Address" : "IP Address"}</th>
                  <th className="px-4 py-3.5">{lang === "km" ? "ឧបករណ៍ និង Browser" : "Device & Browser"}</th>
                  <th className="px-5 py-3.5 text-right">{lang === "km" ? "កាលបរិច្ឆេទ" : "Date & Time"}</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border/60">
                {loading ? (
                  <tr>
                    <td colSpan="6" className="px-4 py-16 text-center text-muted-foreground">
                      <RefreshCw className="mx-auto size-6 animate-spin mb-2.5 opacity-60" />
                      <p className="text-sm font-medium">{lang === "km" ? "កំពុងទាញយកទិន្នន័យ..." : "Loading records..."}</p>
                    </td>
                  </tr>
                ) : data.content.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="px-4 py-16 text-center text-muted-foreground">
                      <History className="mx-auto size-9 mb-2.5 opacity-30" />
                      <p className="text-sm font-medium">{lang === "km" ? "មិនទាន់មានកំណត់ត្រាចូលប្រើប្រាស់នៅឡើយទេ" : "No login history records found."}</p>
                    </td>
                  </tr>
                ) : (
                  data.content.map((item) => (
                    <tr
                      key={item.id}
                      className={`hover:bg-muted/40 transition-colors ${
                        item.suspicious ? "bg-amber-50/50 hover:bg-amber-50/70" : ""
                      }`}
                    >

                      <td className="px-5 py-3.5">
                        <div className="flex items-center gap-3">
                          <div className="flex size-8 shrink-0 items-center justify-center rounded-full bg-primary/10 text-primary font-semibold text-xs uppercase">
                            {(item.username || "U").slice(0, 2)}
                          </div>
                          <div className="min-w-0">
                            <span className="font-medium text-foreground text-sm block truncate">
                              @{item.username}
                            </span>
                          </div>
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        <div className="space-y-1">
                          {item.status === "SUCCESS" ? (
                            <Badge
                              variant="outline"
                              className="border-emerald-300 bg-emerald-50 text-emerald-700 text-xs font-medium py-0.5 px-2 inline-flex items-center gap-1"
                            >
                              <CheckCircle2 className="size-3" />
                              <span>{lang === "km" ? "ជោគជ័យ" : "Success"}</span>
                            </Badge>
                          ) : (
                            <Badge
                              variant="outline"
                              className="border-rose-300 bg-rose-50 text-rose-700 text-xs font-medium py-0.5 px-2 inline-flex items-center gap-1"
                            >
                              <XCircle className="size-3" />
                              <span>{lang === "km" ? "បរាជ័យ" : "Failed"}</span>
                            </Badge>
                          )}
                          {item.failureReason && (
                            <p className="text-[11px] text-muted-foreground truncate max-w-[160px]">
                              {formatReason(item.failureReason)}
                            </p>
                          )}
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        {item.suspicious ? (
                          <div className="space-y-0.5">
                            <Badge
                              variant="outline"
                              className="border-amber-400 bg-amber-100 text-amber-800 text-[11px] font-semibold py-0.5 px-2 inline-flex items-center gap-1"
                            >
                              <AlertTriangle className="size-3 text-amber-600" />
                              <span>{lang === "km" ? "សង្ស័យ" : "Suspicious"}</span>
                            </Badge>
                            {item.suspiciousReason && (
                              <p className="text-[11px] text-amber-800/90 truncate max-w-[200px]" title={item.suspiciousReason}>
                                {item.suspiciousReason}
                              </p>
                            )}
                          </div>
                        ) : (
                          <span className="inline-flex items-center gap-1 text-xs text-muted-foreground/70">
                            <ShieldCheck className="size-3.5 text-muted-foreground/40" />
                            <span>{lang === "km" ? "ធម្មតា" : "Normal"}</span>
                          </span>
                        )}
                      </td>

                      <td className="px-4 py-3.5">
                        <span className="font-mono text-xs text-foreground/80 bg-muted/60 px-2 py-1 rounded-md border border-border/40">
                          {item.ipAddress || "-"}
                        </span>
                      </td>

                      <td className="px-4 py-3.5">
                        <div className="flex items-center gap-2 text-xs text-muted-foreground">
                          <div className="p-1 rounded bg-muted/50 text-muted-foreground/80 shrink-0">
                            {getDeviceIcon(item.deviceType)}
                          </div>
                          <span className="truncate max-w-[190px]">{item.device || "Unknown Device"}</span>
                        </div>
                      </td>

                      <td className="px-5 py-3.5 text-right text-xs text-muted-foreground whitespace-nowrap">
                        {formatTimestamp(item.loginAt)}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {data.page.totalPages > 1 && (
            <div className="flex items-center justify-between px-5 py-3.5 border-t bg-muted/20">
              <div className="text-xs text-muted-foreground">
                {lang === "km" ? "ទំព័រ" : "Page"}{" "}
                <span className="font-semibold text-foreground">{data.page.number + 1}</span>{" "}
                {lang === "km" ? "នៃ" : "of"}{" "}
                <span className="font-semibold text-foreground">{data.page.totalPages}</span>{" "}
                <span className="text-muted-foreground/70">
                  ({data.page.totalElements} {lang === "km" ? "កំណត់ត្រាសរុប" : "total records"})
                </span>
              </div>
              <div className="flex items-center gap-1.5">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={data.page.number === 0 || loading}
                  onClick={() => fetchHistory(data.page.number - 1)}
                  className="h-8 gap-1 text-xs"
                >
                  <ChevronLeft className="size-3.5" />
                  <span>{lang === "km" ? "ថយក្រោយ" : "Previous"}</span>
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={data.page.number >= data.page.totalPages - 1 || loading}
                  onClick={() => fetchHistory(data.page.number + 1)}
                  className="h-8 gap-1 text-xs"
                >
                  <span>{lang === "km" ? "បន្ទាប់" : "Next"}</span>
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

export default AdminLoginHistoryPage
