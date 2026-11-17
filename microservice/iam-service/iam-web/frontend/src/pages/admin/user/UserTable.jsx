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
  MoreVertical,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

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
            <th className="px-4 py-3">{t("username")}</th>
            <th className="px-4 py-3">{t("email")}</th>
            <th className="px-4 py-3">{t("status")}</th>
            <th className="px-4 py-3">{t("security")}</th>
            <th className="px-4 py-3 text-right">{t("actions")}</th>
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
                      {/* View Details Quick Action */}
                      <Button
                        variant="ghost"
                        size="icon"
                        className="size-8 text-muted-foreground hover:text-foreground"
                        title={t("common.viewDetails")}
                        onClick={() => onViewDetails?.(u)}
                      >
                        <Eye className="size-4" />
                      </Button>

                      {/* Dropdown Menu for other actions */}
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button
                            variant="ghost"
                            size="icon"
                            className="size-8 text-muted-foreground hover:text-foreground"
                          >
                            <MoreVertical className="size-4" />
                            <span className="sr-only">Actions</span>
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end" className="w-52">
                          <DropdownMenuItem
                            onClick={() => onEdit?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            <Edit2 className="size-3.5 text-muted-foreground" />
                            <span>{t("common.editProfile")}</span>
                          </DropdownMenuItem>

                          <DropdownMenuSeparator />

                          <DropdownMenuItem
                            onClick={() => onAssignRoles?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            <ShieldCheck className="size-3.5 text-purple-600" />
                            <span>{t("common.manageRoles")}</span>
                          </DropdownMenuItem>

                          <DropdownMenuItem
                            onClick={() => onAssignPermissions?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            <KeyRound className="size-3.5 text-amber-600" />
                            <span>{t("common.managePermissions")}</span>
                          </DropdownMenuItem>

                          <DropdownMenuItem
                            onClick={() => onViewSessions?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            <Activity className="size-3.5 text-cyan-600" />
                            <span>{t("common.manageSessions")}</span>
                          </DropdownMenuItem>

                          <DropdownMenuItem
                            onClick={() => onViewHistory?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            <History className="size-3.5 text-indigo-600" />
                            <span>{t("common.loginHistory")}</span>
                          </DropdownMenuItem>

                          <DropdownMenuSeparator />

                          <DropdownMenuItem
                            onClick={() => onToggleActivate?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            {u.enabled ? (
                              <>
                                <UserX className="size-3.5 text-amber-600" />
                                <span>{t("common.deactivateAccount")}</span>
                              </>
                            ) : (
                              <>
                                <UserCheck className="size-3.5 text-emerald-600" />
                                <span>{t("common.activateAccount")}</span>
                              </>
                            )}
                          </DropdownMenuItem>

                          <DropdownMenuItem
                            onClick={() => onToggleLock?.(u)}
                            className="gap-2 cursor-pointer text-xs"
                          >
                            {isLocked ? (
                              <>
                                <Unlock className="size-3.5 text-blue-600" />
                                <span>{t("common.unlockAccount")}</span>
                              </>
                            ) : (
                              <>
                                <Lock className="size-3.5 text-orange-600" />
                                <span>{t("common.lockAccount")}</span>
                              </>
                            )}
                          </DropdownMenuItem>

                          <DropdownMenuSeparator />

                          <DropdownMenuItem
                            onClick={() => onDelete?.(u)}
                            className="gap-2 cursor-pointer text-xs text-rose-600 focus:text-rose-600 focus:bg-rose-50"
                          >
                            <Trash2 className="size-3.5" />
                            <span>{t("common.deleteUser")}</span>
                          </DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
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
