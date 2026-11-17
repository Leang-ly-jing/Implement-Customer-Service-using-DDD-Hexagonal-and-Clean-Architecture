import { useState, useEffect } from "react"
import { Eye, X, RefreshCw, Shield, KeyRound, CheckCircle2 } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserDetailModal({ isOpen, onClose, user }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [detail, setDetail] = useState(user)
  const [authorities, setAuthorities] = useState({ roles: [], directPermissions: [], effectivePermissions: [] })
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (isOpen && user?.uuid) {
      setDetail(user)
      const fetchDetail = async () => {
        setLoading(true)
        try {
          const [userRes, authRes] = await Promise.all([
            axiosClient.get(`/users/${user.uuid}`),
            axiosClient.get(`/users/${user.uuid}/authorities`),
          ])
          setDetail(userRes.data)
          setAuthorities({
            roles: authRes.data?.roles || [],
            directPermissions: authRes.data?.directPermissions || [],
            effectivePermissions: authRes.data?.effectivePermissions || [],
          })
        } catch {
          toast.error("Detail Error", "Could not load full user profile.")
        } finally {
          setLoading(false)
        }
      }
      fetchDetail()
    }
  }, [isOpen, user])

  if (!isOpen || !user) return null

  const displayUser = detail || user
  const effectivePerms = authorities.effectivePermissions || []
  const assignedRoles = authorities.roles?.length > 0 ? authorities.roles : (displayUser.roles || [])

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-lg rounded-xl bg-background border p-6 shadow-xl animate-in fade-in-0 zoom-in-95">
        <div className="flex items-center justify-between pb-3 border-b">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <Eye className="size-5 text-primary" />
            {isKm ? "ព័ត៌មានលម្អិតអ្នកប្រើប្រាស់" : "User Account Details"}
          </h3>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        {loading ? (
          <div className="py-12 text-center text-muted-foreground">
            <RefreshCw className="mx-auto size-5 animate-spin mb-2" />
            {isKm ? "កំពុងទាញយកព័ត៌មាន..." : "Loading profile..."}
          </div>
        ) : (
          <div className="space-y-4 pt-4">
            <div className="flex items-center gap-3 p-3 rounded-lg bg-muted/40">
              <div className="flex size-12 items-center justify-center rounded-full bg-primary/10 text-primary font-bold text-base uppercase">
                {(displayUser.username || "U").slice(0, 2)}
              </div>
              <div>
                <h4 className="font-semibold text-foreground text-base">
                  {displayUser.name || displayUser.username}
                </h4>
                <div className="text-xs text-muted-foreground">@{displayUser.username}</div>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3 text-sm">
              <div className="p-3 rounded-lg border bg-card">
                <div className="text-xs text-muted-foreground mb-1">{t("common.email")}</div>
                <div className="font-medium truncate">{displayUser.email || "-"}</div>
              </div>
              <div className="p-3 rounded-lg border bg-card">
                <div className="text-xs text-muted-foreground mb-1">{isKm ? "ភាសា (Locale)" : "Locale"}</div>
                <div className="font-medium uppercase">{displayUser.locale || "km"}</div>
              </div>
              <div className="p-3 rounded-lg border bg-card">
                <div className="text-xs text-muted-foreground mb-1">{t("common.status")}</div>
                <div className="font-medium">
                  {displayUser.enabled ? (
                    <span className="text-emerald-600 font-semibold">{t("common.active")}</span>
                  ) : (
                    <span className="text-rose-600 font-semibold">{t("common.deactivated")}</span>
                  )}
                </div>
              </div>
              <div className="p-3 rounded-lg border bg-card">
                <div className="text-xs text-muted-foreground mb-1">{t("common.security")}</div>
                <div className="font-medium">
                  {displayUser.accountNonLocked === false ? (
                    <span className="text-amber-600 font-semibold">{t("common.locked")}</span>
                  ) : (
                    <span className="text-slate-600 font-semibold">{t("common.unlocked")}</span>
                  )}
                </div>
              </div>
            </div>

            {/* Assigned Roles */}
            <div className="p-3 rounded-lg border bg-card">
              <div className="text-xs text-muted-foreground mb-1.5 flex items-center gap-1.5">
                <Shield className="size-3.5 text-purple-600" />
                <span>{isKm ? "តួនាទី (Roles)" : "Assigned Roles"}</span>
              </div>
              {assignedRoles.length > 0 ? (
                <div className="flex flex-wrap gap-1.5">
                  {assignedRoles.map((r) => (
                    <Badge key={r.uuid || r.name} variant="secondary" className="bg-purple-50 text-purple-700 border-purple-200">
                      {r.name}
                    </Badge>
                  ))}
                </div>
              ) : (
                <p className="text-xs text-muted-foreground italic">
                  {isKm ? "មិនទាន់មានតួនាទីទេ" : "No roles assigned"}
                </p>
              )}
            </div>

            {/* Effective Permissions from Roles */}
            <div className="p-3 rounded-lg border bg-card">
              <div className="text-xs text-muted-foreground mb-1.5 flex items-center justify-between">
                <span className="flex items-center gap-1.5">
                  <KeyRound className="size-3.5 text-amber-600" />
                  <span>{isKm ? "សិទ្ធិទទួលបានពីតួនាទី (Effective Permissions)" : "Effective Permissions"}</span>
                </span>
                <Badge variant="outline" className="text-[10px] py-0 px-1.5">
                  {effectivePerms.length}
                </Badge>
              </div>
              {effectivePerms.length > 0 ? (
                <div className="flex flex-wrap gap-1 max-h-28 overflow-y-auto pt-1">
                  {effectivePerms.map((perm) => (
                    <Badge
                      key={perm}
                      variant="outline"
                      className="text-[10px] font-mono py-0.5 px-1.5 bg-emerald-50 text-emerald-800 border-emerald-200"
                    >
                      <CheckCircle2 className="size-2.5 mr-1 inline text-emerald-600" />
                      {perm}
                    </Badge>
                  ))}
                </div>
              ) : (
                <p className="text-xs text-muted-foreground italic">
                  {isKm ? "មិនទាន់មានសិទ្ធិទេ (កំណត់តាម Roles)" : "No effective permissions."}
                </p>
              )}
            </div>

            <div className="text-xs text-muted-foreground space-y-1 pt-1">
              <div>
                <span className="font-medium">UUID:</span> <span className="font-mono">{displayUser.uuid}</span>
              </div>
              {displayUser.createdAt && (
                <div>
                  <span className="font-medium">{isKm ? "កាលបរិច្ឆេទបង្កើត:" : "Created At:"}</span>{" "}
                  {new Date(displayUser.createdAt).toLocaleString()}
                </div>
              )}
            </div>

            <div className="flex justify-end pt-3 border-t">
              <Button variant="outline" onClick={onClose}>
                {t("common.close")}
              </Button>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default UserDetailModal
