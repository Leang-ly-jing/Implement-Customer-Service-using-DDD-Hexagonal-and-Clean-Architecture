import {
  Users,
  RefreshCw,
  Eye,
  Edit2,
  Lock,
  Unlock,
  UserCheck,
  UserX,
  Trash2,
  ShieldCheck,
  KeyRound,
  Activity,
  History,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"

export function UserTable({
  users = [],
  loading = false,
  onViewDetails,
  onEdit,
  onAssignRoles,
  onAssignPermissions,
  onViewSessions,
  onViewHistory,
  onToggleActivate,
  onToggleLock,
  onDelete,
}) {
  const { t } = useTranslation()

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-left text-sm">
        <thead className="bg-muted/50 text-xs uppercase text-muted-foreground border-y">
          <tr>
            <th className="px-4 py-3">{t("usersPage.table.user")}</th>
            <th className="px-4 py-3">{t("usersPage.table.email")}</th>
            <th className="px-4 py-3">{t("usersPage.table.status")}</th>
            <th className="px-4 py-3">{t("usersPage.table.security")}</th>
            <th className="px-4 py-3 text-right">{t("usersPage.table.actions")}</th>
          </tr>
        </thead>
        <tbody className="divide-y">
          {loading ? (
            <tr>
              <td colSpan="5" className="px-4 py-12 text-center text-muted-foreground">
                <RefreshCw className="mx-auto size-6 animate-spin mb-2" />
                {t("usersPage.loadingUsers")}
              </td>
            </tr>
          ) : users.length === 0 ? (
            <tr>
              <td colSpan="5" className="px-4 py-12 text-center text-muted-foreground">
                <Users className="mx-auto size-8 mb-2 opacity-40" />
                {t("usersPage.noUsersFound")}
              </td>
            </tr>
          ) : (
            users.map((u) => {
              const isLocked = u.accountNonLocked === false
              return (
                <tr key={u.uuid} className="hover:bg-muted/30 transition-colors">
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-3">
                      <div className="flex size-9 items-center justify-center rounded-full bg-primary/10 text-primary font-semibold text-xs uppercase">
                        {(u.username || "U").slice(0, 2)}
                      </div>
                      <div>
                        <div className="font-medium text-foreground">{u.name || u.username}</div>
                        <div className="text-xs text-muted-foreground">@{u.username}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-3 text-muted-foreground">{u.email || "-"}</td>
                  <td className="px-4 py-3">
                    {u.enabled ? (
                      <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-xs">
                        <span className="mr-1 size-1.5 rounded-full bg-emerald-500 inline-block" />
                        {t("common.active")}
                      </Badge>
                    ) : (
                      <Badge variant="outline" className="border-rose-200 bg-rose-50 text-rose-700 text-xs">
                        <span className="mr-1 size-1.5 rounded-full bg-rose-500 inline-block" />
                        {t("common.deactivated")}
                      </Badge>
                    )}
                  </td>
                  <td className="px-4 py-3">
                    {isLocked ? (
                      <Badge variant="outline" className="border-amber-200 bg-amber-50 text-amber-700 text-xs">
                        <Lock className="mr-1 size-3 inline" />
                        {t("common.locked")}
                      </Badge>
                    ) : (
                      <Badge variant="outline" className="border-slate-200 bg-slate-50 text-slate-600 text-xs">
                        <Unlock className="mr-1 size-3 inline" />
                        {t("common.unlocked")}
                      </Badge>
                    )}
                  </td>
                  <td className="px-4 py-3 text-right">
                    <div className="flex items-center justify-end gap-1">
                      {/* View Details */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.viewDetails")}
                        className="text-foreground/70 hover:text-foreground hover:bg-muted"
                        onClick={() => onViewDetails?.(u)}
                      >
                        <Eye className="size-3.5" />
                      </Button>

                      {/* Edit Profile */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.editProfile")}
                        className="text-foreground/70 hover:text-foreground hover:bg-muted"
                        onClick={() => onEdit?.(u)}
                      >
                        <Edit2 className="size-3.5" />
                      </Button>

                      {/* Roles Assignment */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.manageRoles")}
                        className="text-purple-600 hover:text-purple-700 hover:bg-purple-50"
                        onClick={() => onAssignRoles?.(u)}
                      >
                        <ShieldCheck className="size-3.5" />
                      </Button>

                      {/* Permissions Assignment & Inspection */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.managePermissions")}
                        className="text-amber-600 hover:text-amber-700 hover:bg-amber-50"
                        onClick={() => onAssignPermissions?.(u)}
                      >
                        <KeyRound className="size-3.5" />
                      </Button>

                      {/* Sessions */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.manageSessions")}
                        className="text-cyan-600 hover:text-cyan-700 hover:bg-cyan-50"
                        onClick={() => onViewSessions?.(u)}
                      >
                        <Activity className="size-3.5" />
                      </Button>

                      {/* Login History */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.loginHistory")}
                        className="text-indigo-600 hover:text-indigo-700 hover:bg-indigo-50"
                        onClick={() => onViewHistory?.(u)}
                      >
                        <History className="size-3.5" />
                      </Button>

                      {/* Activate / Deactivate Toggle */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={u.enabled ? t("common.deactivateAccount") : t("common.activateAccount")}
                        className={u.enabled ? "text-amber-600 hover:text-amber-700 hover:bg-amber-50" : "text-emerald-600 hover:text-emerald-700 hover:bg-emerald-50"}
                        onClick={() => onToggleActivate?.(u)}
                      >
                        {u.enabled ? <UserX className="size-3.5" /> : <UserCheck className="size-3.5" />}
                      </Button>

                      {/* Lock / Unlock Toggle */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={isLocked ? t("common.unlockAccount") : t("common.lockAccount")}
                        className={isLocked ? "text-blue-600 hover:text-blue-700 hover:bg-blue-50" : "text-orange-600 hover:text-orange-700 hover:bg-orange-50"}
                        onClick={() => onToggleLock?.(u)}
                      >
                        {isLocked ? <Unlock className="size-3.5" /> : <Lock className="size-3.5" />}
                      </Button>

                      {/* Delete */}
                      <Button
                        variant="ghost"
                        size="icon-xs"
                        title={t("common.deleteUser")}
                        className="text-rose-600 hover:text-rose-700 hover:bg-rose-50"
                        onClick={() => onDelete?.(u)}
                      >
                        <Trash2 className="size-3.5" />
                      </Button>
                    </div>
                  </td>
                </tr>
              )
            })
          )}
        </tbody>
      </table>
    </div>
  )
}

export default UserTable
