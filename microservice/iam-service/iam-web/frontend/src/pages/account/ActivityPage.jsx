import { useState, useEffect } from "react"
import { Key, User, Unlink, Laptop, ShieldCheck, Clock } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "./shared"

const ACTION_I18N = {
  km: {
    PASSWORD_CHANGED: {
      title: "បានប្តូរពាក្យសម្ងាត់",
      desc: "ពាក្យសម្ងាត់គណនីត្រូវបានផ្លាស់ប្តូរដោយជោគជ័យ",
    },
    PROFILE_UPDATED: {
      title: "បានកែប្រែព័ត៌មានផ្ទាល់ខ្លួន",
      desc: "ព័ត៌មាន Profile ត្រូវបានធ្វើបច្ចុប្បន្នភាព",
    },
    IDENTITY_UNLINKED: {
      title: "បានផ្តាច់គណនី Social",
      desc: "គណនី Social ត្រូវបានផ្តាច់ចេញពីគណនី",
    },
    SESSION_TERMINATED: {
      title: "បានបិទ Session ឧបករណ៍",
      desc: "ឧបករណ៍ដែលបានជ្រើសរើសត្រូវបាន Log out",
    },
    ALL_SESSIONS_REVOKED: {
      title: "បាន Log out គ្រប់ឧបករណ៍ផ្សេងទៀត",
      desc: "ឧបករណ៍ផ្សេងៗទាំងអស់ត្រូវបានកាត់ផ្តាច់",
    },
    ACCOUNT_DEACTIVATED: {
      title: "បានស្នើសុំបិទគណនី",
      desc: "គណនីត្រូវបានបិទដំណើរការ",
    },
  },
  en: {
    PASSWORD_CHANGED: {
      title: "Password changed",
      desc: "Your account password was updated successfully",
    },
    PROFILE_UPDATED: {
      title: "Profile updated",
      desc: "Personal profile details were updated",
    },
    IDENTITY_UNLINKED: {
      title: "Social account unlinked",
      desc: "A connected social profile was unlinked",
    },
    SESSION_TERMINATED: {
      title: "Session terminated",
      desc: "Selected device session was terminated",
    },
    ALL_SESSIONS_REVOKED: {
      title: "All other sessions revoked",
      desc: "Logged out from all other active devices",
    },
    ACCOUNT_DEACTIVATED: {
      title: "Account deactivated",
      desc: "Account was deactivated upon user request",
    },
  },
}

ACTION_I18N.kh = ACTION_I18N.km

export function ActivityPage() {
  const [activities, setActivities] = useState([])
  const [loading, setLoading] = useState(true)
  const rawLang = localStorage.getItem("lang") || "kh"
  const isKhmer = rawLang.startsWith("kh") || rawLang.startsWith("km")
  const lang = isKhmer ? "kh" : "en"
  const i18n = isKhmer ? ACTION_I18N.km : ACTION_I18N.en

  useEffect(() => {
    let isMounted = true

    const fetchActivities = async () => {
      try {
        const res = await axiosClient.get("/me/activities")
        if (isMounted) {
          setActivities(res.data || [])
        }
      } catch {
        if (isMounted) {
          toast.error("Failed to load activities", "Could not retrieve account activity.")
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    fetchActivities()

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

  const renderIcon = (action) => {
    switch (action) {
      case "PASSWORD_CHANGED":
        return <Key className="size-4 text-amber-600" />
      case "PROFILE_UPDATED":
        return <User className="size-4 text-blue-600" />
      case "IDENTITY_UNLINKED":
        return <Unlink className="size-4 text-rose-600" />
      case "SESSION_TERMINATED":
      case "ALL_SESSIONS_REVOKED":
        return <Laptop className="size-4 text-purple-600" />
      default:
        return <ShieldCheck className="size-4 text-emerald-600" />
    }
  }

  return (
    <>
      <PageHeader
        title={isKhmer ? "សកម្មភាពគណនី" : "Account activity"}
        description={
          isKhmer
            ? "សកម្មភាពពាក់ព័ន្ធនឹងសុវត្ថិភាពថ្មីៗនៅលើគណនីរបស់អ្នក"
            : "Recent security-related activity on your account."
        }
      />

      <Card>
        <CardContent className="pt-6">
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p className="text-sm text-muted-foreground">Loading activity history...</p>
            </div>
          ) : activities.length === 0 ? (
            <div className="flex h-40 items-center justify-center">
              <p className="text-sm text-muted-foreground">
                {isKhmer ? "មិនទាន់មានសកម្មភាពគណនីនៅឡើយទេ" : "No account activity recorded yet."}
              </p>
            </div>
          ) : (
            <ol className="relative space-y-6 border-l pl-6">
              {activities.map((a) => {
                const actionInfo = i18n[a.action] || {
                  title: a.action,
                  desc: a.metadata || "",
                }

                return (
                  <li key={a.id} className="relative group">
                    <span className="absolute -left-9 mt-0.5 flex size-6 items-center justify-center rounded-full border bg-background shadow-xs">
                      {renderIcon(a.action)}
                    </span>
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-1">
                      <p className="font-medium text-sm text-foreground">{actionInfo.title}</p>
                      <div className="flex items-center gap-1.5 text-xs text-muted-foreground">
                        <Clock className="size-3" />
                        <span>{formatTimestamp(a.createdAt)}</span>
                      </div>
                    </div>
                    {actionInfo.desc && (
                      <p className="mt-0.5 text-xs text-muted-foreground">{actionInfo.desc}</p>
                    )}
                    {(a.device || a.ipAddress) && (
                      <div className="mt-1.5 flex flex-wrap items-center gap-2 text-xs">
                        {a.device && (
                          <Badge variant="outline" className="font-normal text-[11px] py-0 px-1.5">
                            {a.device}
                          </Badge>
                        )}
                        {a.ipAddress && (
                          <span className="font-mono text-[11px] text-muted-foreground">
                            {a.ipAddress}
                          </span>
                        )}
                      </div>
                    )}
                  </li>
                )
              })}
            </ol>
          )}
        </CardContent>
      </Card>
    </>
  )
}

export default ActivityPage
