import {
  KeyRound,
  FolderTree,
  Edit2,
  Trash2,
  CheckCircle2,
  XCircle,
  RefreshCw,
  Lock,
  ChevronLeft,
  ChevronRight,
  MoreVertical,
} from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

export function PermissionsTable({
  permissions = [],
  pageMeta = { number: 0, size: 20, totalElements: 0, totalPages: 0 },
  loading = false,
  currentPage = 0,
  onPageChange,
  onOpenEdit,
  onToggleStatus,
  onOpenDelete,
  lang = "km",
}) {
  const getActionBadge = (action) => {
    const act = (action || "").toLowerCase()
    switch (act) {
      case "create":
        return "border-emerald-300 bg-emerald-50 text-emerald-700"
      case "read":
        return "border-blue-300 bg-blue-50 text-blue-700"
      case "update":
        return "border-amber-300 bg-amber-50 text-amber-700"
      case "delete":
        return "border-rose-300 bg-rose-50 text-rose-700"
      default:
        return "border-purple-300 bg-purple-50 text-purple-700"
    }
  }

  return (
    <Card className="shadow-xs overflow-hidden border">
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm border-collapse">
            <thead>
              <tr className="bg-muted/40 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground border-b">
                <th className="px-5 py-3.5">{lang === "km" ? "Permission Name" : "Permission"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Resource" : "Resource"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Action" : "Action"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "ការពិពណ៌នា" : "Description"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "ប្រភេទ" : "Type"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "ស្ថានភាព" : "Status"}</th>
                <th className="px-5 py-3.5 text-right">{lang === "km" ? "សកម្មភាព" : "Actions"}</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/60">
              {loading ? (
                <tr>
                  <td colSpan="7" className="px-4 py-16 text-center text-muted-foreground">
                    <RefreshCw className="mx-auto size-6 animate-spin mb-2.5 opacity-60" />
                    <p className="text-sm font-medium">{lang === "km" ? "កំពុងទាញយក Permissions..." : "Loading permissions..."}</p>
                  </td>
                </tr>
              ) : permissions.length === 0 ? (
                <tr>
                  <td colSpan="7" className="px-4 py-16 text-center text-muted-foreground">
                    <KeyRound className="mx-auto size-9 mb-2.5 opacity-30" />
                    <p className="text-sm font-medium">{lang === "km" ? "រកមិនឃើញ Permission ណាមួយឡើយ" : "No permissions found."}</p>
                  </td>
                </tr>
              ) : (
                permissions.map((p) => {
                  const isSystem = p.isSystem ?? p.system
                  const isActive = p.isActive ?? p.active
                  return (
                    <tr key={p.uuid} className="hover:bg-muted/40 transition-colors">

                      <td className="px-5 py-3.5">
                        <div className="flex items-center gap-2.5">
                          <div className="flex size-7 shrink-0 items-center justify-center rounded bg-primary/10 text-primary">
                            <KeyRound className="size-3.5" />
                          </div>
                          <span className="font-mono font-semibold text-foreground text-xs">
                            {p.name}
                          </span>
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        <Badge
                          variant="outline"
                          className="text-xs font-mono font-normal border-border bg-muted/30 capitalize"
                        >
                          <FolderTree className="size-3 mr-1 opacity-70" />
                          <span>{p.resource}</span>
                        </Badge>
                      </td>

                      <td className="px-4 py-3.5">
                        <Badge
                          variant="outline"
                          className={`text-xs font-medium py-0.5 px-2 capitalize ${getActionBadge(p.action)}`}
                        >
                          {p.action}
                        </Badge>
                      </td>

                      <td className="px-4 py-3.5">
                        <p className="text-xs text-muted-foreground max-w-xs truncate">
                          {p.description || (lang === "km" ? "គ្មានការពិពណ៌នា" : "No description")}
                        </p>
                      </td>

                      <td className="px-4 py-3.5">
                        {isSystem ? (
                          <Badge
                            variant="outline"
                            className="border-indigo-300 bg-indigo-50 text-indigo-700 text-xs font-medium py-0.5 px-2 inline-flex items-center gap-1"
                          >
                            <Lock className="size-3" />
                            <span>System</span>
                          </Badge>
                        ) : (
                          <Badge
                            variant="outline"
                            className="border-zinc-300 bg-zinc-50 text-zinc-700 text-xs font-medium py-0.5 px-2"
                          >
                            Custom
                          </Badge>
                        )}
                      </td>

                      <td className="px-4 py-3.5">
                        {isActive ? (
                          <Badge
                            variant="outline"
                            className="border-emerald-300 bg-emerald-50 text-emerald-700 text-xs font-medium py-0.5 px-2 inline-flex items-center gap-1"
                          >
                            <CheckCircle2 className="size-3" />
                            <span>{lang === "km" ? "សកម្ម" : "Active"}</span>
                          </Badge>
                        ) : (
                          <Badge
                            variant="outline"
                            className="border-zinc-300 bg-zinc-100 text-zinc-600 text-xs font-medium py-0.5 px-2 inline-flex items-center gap-1"
                          >
                            <XCircle className="size-3" />
                            <span>{lang === "km" ? "អសកម្ម" : "Inactive"}</span>
                          </Badge>
                        )}
                      </td>

                      <td className="px-5 py-3.5 text-right">
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon" className="size-8">
                              <MoreVertical className="size-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end" className="w-44">
                            <DropdownMenuItem
                              onClick={() => onOpenEdit(p)}
                              disabled={isSystem}
                              className="gap-2 cursor-pointer text-xs"
                            >
                              <Edit2 className="size-3.5" />
                              <span>{lang === "km" ? "កែប្រែព័ត៌មាន" : "Edit Permission"}</span>
                            </DropdownMenuItem>

                            <DropdownMenuItem
                              onClick={() => onToggleStatus(p)}
                              disabled={isSystem}
                              className="gap-2 cursor-pointer text-xs"
                            >
                              {isActive ? (
                                <>
                                  <XCircle className="size-3.5 text-amber-600" />
                                  <span>{lang === "km" ? "បិទដំណើរការ" : "Deactivate"}</span>
                                </>
                              ) : (
                                <>
                                  <CheckCircle2 className="size-3.5 text-emerald-600" />
                                  <span>{lang === "km" ? "បើកដំណើរការ" : "Activate"}</span>
                                </>
                              )}
                            </DropdownMenuItem>

                            <DropdownMenuSeparator />

                            <DropdownMenuItem
                              onClick={() => onOpenDelete(p)}
                              disabled={isSystem}
                              className="gap-2 cursor-pointer text-xs text-rose-600 focus:text-rose-600"
                            >
                              <Trash2 className="size-3.5" />
                              <span>{lang === "km" ? "លុប Permission" : "Delete"}</span>
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </td>
                    </tr>
                  )
                })
              )}
            </tbody>
          </table>
        </div>

        {pageMeta.totalPages > 1 && (
          <div className="flex items-center justify-between px-5 py-3.5 border-t bg-muted/20">
            <span className="text-xs text-muted-foreground">
              {lang === "km" ? "ទំព័រ" : "Page"} {pageMeta.number + 1} / {pageMeta.totalPages} ({pageMeta.totalElements} {lang === "km" ? "សរុប" : "total"})
            </span>
            <div className="flex items-center gap-2">
              <Button
                variant="outline"
                size="sm"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage <= 0 || loading}
                className="gap-1 text-xs"
              >
                <ChevronLeft className="size-3.5" />
                <span>{lang === "km" ? "ថយក្រោយ" : "Previous"}</span>
              </Button>
              <Button
                variant="outline"
                size="sm"
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage >= pageMeta.totalPages - 1 || loading}
                className="gap-1 text-xs"
              >
                <span>{lang === "km" ? "បន្ទាប់" : "Next"}</span>
                <ChevronRight className="size-3.5" />
              </Button>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  )
}

export default PermissionsTable
