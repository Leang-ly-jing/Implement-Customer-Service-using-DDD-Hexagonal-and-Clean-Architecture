import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import { Users, UserCheck, ShieldCheck, KeyRound, AppWindow, ArrowRight, Clock, Activity } from "lucide-react"
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"

const ACTION_LABELS = {
  PASSWORD_CHANGED: "Password changed",
  PROFILE_UPDATED: "Profile updated",
  IDENTITY_UNLINKED: "Social identity unlinked",
  SESSION_TERMINATED: "Session terminated",
  ALL_SESSIONS_REVOKED: "All other sessions revoked",
  ACCOUNT_DEACTIVATED: "Account deactivated",
}

export function AdminOverviewPage() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const lang = localStorage.getItem("lang") || "km"

  useEffect(() => {
    let isMounted = true

    const fetchOverview = async () => {
      try {
        const res = await axiosClient.get("/admin/overview")
        if (isMounted) {
          setData(res.data)
        }
      } catch (err) {
        console.error("Overview error:", err.response?.data || err)
        if (isMounted) {
          const message = err.response?.data?.detail || "Could not retrieve admin statistics."
          toast.error("Failed to load overview", message)
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    fetchOverview()

    return () => {
      isMounted = false
    }
  }, [])

  const formatTimestamp = (isoString) => {
    if (!isoString) return "-"
    const date = new Date(isoString)
    return date.toLocaleString(undefined, {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    })
  }

  const statCards = [
    {
      title: lang === "km" ? "អ្នកប្រើប្រាស់សរុប" : "Total Users",
      value: data?.totalUsers ?? 0,
      icon: Users,
      color: "text-blue-600 bg-blue-50 border-blue-200",
      link: "/admin/users",
    },
    {
      title: lang === "km" ? "អ្នកប្រើប្រាស់សកម្ម" : "Active Users",
      value: data?.activeUsers ?? 0,
      icon: UserCheck,
      color: "text-emerald-600 bg-emerald-50 border-emerald-200",
      link: "/admin/users",
    },
    {
      title: lang === "km" ? "តួនាទីសរុប" : "Total Roles",
      value: data?.totalRoles ?? 0,
      icon: ShieldCheck,
      color: "text-purple-600 bg-purple-50 border-purple-200",
      link: "/admin/roles",
    },
    {
      title: lang === "km" ? "សិទ្ធិអនុញ្ញាត" : "Permissions",
      value: data?.totalPermissions ?? 0,
      icon: KeyRound,
      color: "text-amber-600 bg-amber-50 border-amber-200",
      link: "/admin/permissions",
    },
    {
      title: lang === "km" ? "កម្មវិធី (Clients)" : "Applications",
      value: data?.totalClients ?? 0,
      icon: AppWindow,
      color: "text-indigo-600 bg-indigo-50 border-indigo-200",
      link: "/admin/applications",
    },
  ]

  return (
    <>
      <PageHeader
        title={lang === "km" ? "ទិដ្ឋភាពទូទៅនៃប្រព័ន្ធ" : "Admin Overview"}
        description={
          lang === "km"
            ? "ស្ថិតិ និងសកម្មភាពថ្មីៗនៃគណនី តួនាទី សិទ្ធិ និងកម្មវិធីទាំងអស់"
            : "System-wide statistics and recent activities across users, roles, permissions, and clients."
        }
      />

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {statCards.map((stat) => (
          <Card key={stat.title} className="relative overflow-hidden transition hover:shadow-md">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-xs font-medium text-muted-foreground uppercase tracking-wider">
                {stat.title}
              </CardTitle>
              <div className={`p-2 rounded-xl border ${stat.color}`}>
                <stat.icon className="size-4" />
              </div>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold tracking-tight">
                {loading ? "..." : stat.value}
              </div>
              <div className="mt-3">
                <Button variant="ghost" size="sm" className="h-7 -ml-2 text-xs text-muted-foreground hover:text-foreground" render={<Link to={stat.link} />}>
                  <span>{lang === "km" ? "មើលព័ត៌មានលម្អិត" : "View details"}</span>
                  <ArrowRight className="size-3 ml-1" />
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="mt-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between">
            <div className="flex items-center gap-2">
              <Activity className="size-4 text-blue-600" />
              <CardTitle className="text-base font-semibold">
                {lang === "km" ? "សកម្មភាពថ្មីៗក្នុងប្រព័ន្ធ" : "Recent System Activities"}
              </CardTitle>
            </div>
            <Badge variant="outline" className="font-normal text-xs">
              Top 10
            </Badge>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="flex h-36 items-center justify-center">
                <p className="text-sm text-muted-foreground">Loading recent activities...</p>
              </div>
            ) : !data?.recentActivities || data.recentActivities.length === 0 ? (
              <div className="flex h-36 items-center justify-center">
                <p className="text-sm text-muted-foreground">No recent activity recorded.</p>
              </div>
            ) : (
              <ol className="relative space-y-4 border-l pl-5">
                {data.recentActivities.map((act) => (
                  <li key={act.id} className="relative">
                    <span className="absolute -left-[27px] mt-1 size-2.5 rounded-full border bg-blue-600" />
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-1">
                      <p className="font-medium text-sm text-foreground">
                        {ACTION_LABELS[act.action] || act.action}
                      </p>
                      <div className="flex items-center gap-1.5 text-xs text-muted-foreground">
                        <Clock className="size-3" />
                        <span>{formatTimestamp(act.createdAt)}</span>
                      </div>
                    </div>
                    {act.metadata && (
                      <p className="text-xs text-muted-foreground mt-0.5">{act.metadata}</p>
                    )}
                    {(act.device || act.ipAddress) && (
                      <div className="mt-1 flex flex-wrap items-center gap-2 text-xs">
                        {act.device && (
                          <Badge variant="outline" className="font-normal text-[11px] py-0 px-1.5">
                            {act.device}
                          </Badge>
                        )}
                        {act.ipAddress && (
                          <span className="font-mono text-[11px] text-muted-foreground">
                            {act.ipAddress}
                          </span>
                        )}
                      </div>
                    )}
                  </li>
                ))}
              </ol>
            )}
          </CardContent>
        </Card>
      </div>
    </>
  )
}

export default AdminOverviewPage
