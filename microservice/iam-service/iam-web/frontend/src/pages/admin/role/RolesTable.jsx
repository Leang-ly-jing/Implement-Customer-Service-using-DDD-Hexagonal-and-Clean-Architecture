import {
  Shield,
  Key,
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

export function RolesTable({
  roles = [],
  pageMeta = { number: 0, size: 10, totalElements: 0, totalPages: 0 },
  loading = false,
  currentPage = 0,
  onPageChange,
  onOpenPermissions,
  onOpenEdit,
  onToggleStatus,
  onOpenDelete,
  lang = "km",
}) {
  return (
    <Card className="shadow-xs overflow-hidden border">
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm border-collapse">
            <thead>
              <tr className="bg-muted/40 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground border-b">
                <th className="px-5 py-3.5">{lang === "km" ? "Role Name" : "Role Name"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "ការពិពណ៌នា" : "Description"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "ប្រភេទ" : "Type"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "ស្ថានភាព" : "Status"}</th>
                <th className="px-5 py-3.5 text-right">{lang === "km" ? "សកម្មភាព" : "Actions"}</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/60">
              {loading ? (
                <tr>
                  <td colSpan="5" className="px-4 py-16 text-center text-muted-foreground">
                    <RefreshCw className="mx-auto size-6 animate-spin mb-2.5 opacity-60" />
                    <p className="text-sm font-medium">{lang === "km" ? "កំពុងទាញយក Roles..." : "Loading roles..."}</p>
                  </td>
                </tr>
              ) : roles.length === 0 ? (
                <tr>
                  <td colSpan="5" className="px-4 py-16 text-center text-muted-foreground">
                    <Shield className="mx-auto size-9 mb-2.5 opacity-30" />
                    <p className="text-sm font-medium">{lang === "km" ? "រកមិនឃើញ Role ណាមួយឡើយ" : "No roles found."}</p>
                  </td>
                </tr>
              ) : (
                roles.map((r) => (
                  <tr key={r.uuid} className="hover:bg-muted/40 transition-colors">

                    <td className="px-5 py-3.5">
                      <div className="flex items-center gap-3">
                        <div
                          className={`flex size-8 shrink-0 items-center justify-center rounded-lg font-semibold text-xs ${
                            r.system
                              ? "bg-primary/10 text-primary border border-primary/20"
                              : "bg-muted text-foreground border border-border"
                          }`}
                        >
                          <Shield className="size-4" />
                        </div>
                        <div>
                          <span className="font-semibold text-foreground text-sm block">
                            {r.name}
                          </span>
                        </div>
                      </div>
                    </td>

                    <td className="px-4 py-3.5">
                      <p className="text-xs text-muted-foreground max-w-sm truncate">
                        {r.description || (lang === "km" ? "គ្មានការពិពណ៌នា" : "No description")}
                      </p>
                    </td>

                    <td className="px-4 py-3.5">
                      {r.system ? (
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
                      {r.active ? (
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
                      <div className="flex items-center justify-end gap-1.5">

                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => onOpenPermissions(r)}
                          className="h-8 gap-1 text-xs"
                          title={lang === "km" ? "កំណត់ Permissions" : "Manage Permissions"}
                        >
                          <Key className="size-3.5 text-primary" />
                          <span className="hidden sm:inline">
                            {lang === "km" ? "Permissions" : "Permissions"}
                          </span>
                        </Button>

                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon" className="size-8">
                              <MoreVertical className="size-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end" className="w-44">
                            <DropdownMenuItem
                              onClick={() => onOpenEdit(r)}
                              disabled={r.system}
                              className="gap-2 cursor-pointer text-xs"
                            >
                              <Edit2 className="size-3.5" />
                              <span>{lang === "km" ? "កែប្រែព័ត៌មាន" : "Edit Role"}</span>
                            </DropdownMenuItem>

                            <DropdownMenuItem
                              onClick={() => onToggleStatus(r)}
                              disabled={r.system}
                              className="gap-2 cursor-pointer text-xs"
                            >
                              {r.active ? (
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
                              onClick={() => onOpenDelete(r)}
                              disabled={r.system}
                              className="gap-2 cursor-pointer text-xs text-rose-600 focus:text-rose-600"
                            >
                              <Trash2 className="size-3.5" />
                              <span>{lang === "km" ? "លុប Role" : "Delete Role"}</span>
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </div>
                    </td>
                  </tr>
                ))
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

export default RolesTable
