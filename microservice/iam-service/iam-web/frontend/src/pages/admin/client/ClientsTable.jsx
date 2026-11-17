import {
  AppWindow,
  Edit2,
  Trash2,
  KeyRound,
  RefreshCw,
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

export function ClientsTable({
  clients = [],
  pageMeta = { number: 0, size: 10, totalElements: 0, totalPages: 0 },
  loading = false,
  currentPage = 0,
  onPageChange,
  onOpenEdit,
  onOpenRegenSecret,
  onOpenDelete,
  lang = "km",
}) {
  const isSystemClient = (clientId) => {
    return clientId === "oidc-client" || clientId === "web-client"
  }

  return (
    <Card className="shadow-xs overflow-hidden border">
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm border-collapse">
            <thead>
              <tr className="bg-muted/40 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground border-b">
                <th className="px-5 py-3.5">{lang === "km" ? "Client" : "Client"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Grant Types" : "Grant Types"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Auth Methods" : "Auth Methods"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Scopes" : "Scopes"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Redirects" : "Redirect URIs"}</th>
                <th className="px-4 py-3.5">{lang === "km" ? "Security" : "Security"}</th>
                <th className="px-5 py-3.5 text-right">{lang === "km" ? "សកម្មភាព" : "Actions"}</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border/60">
              {loading ? (
                <tr>
                  <td colSpan="7" className="px-4 py-16 text-center text-muted-foreground">
                    <RefreshCw className="mx-auto size-6 animate-spin mb-2.5 opacity-60" />
                    <p className="text-sm font-medium">{lang === "km" ? "កំពុងទាញយក Clients..." : "Loading clients..."}</p>
                  </td>
                </tr>
              ) : clients.length === 0 ? (
                <tr>
                  <td colSpan="7" className="px-4 py-16 text-center text-muted-foreground">
                    <AppWindow className="mx-auto size-9 mb-2.5 opacity-30" />
                    <p className="text-sm font-medium">{lang === "km" ? "រកមិនឃើញ Client ណាមួយឡើយ" : "No OAuth2 clients found."}</p>
                  </td>
                </tr>
              ) : (
                clients.map((c) => {
                  const isSystem = isSystemClient(c.clientId)
                  return (
                    <tr key={c.id} className="hover:bg-muted/40 transition-colors">

                      <td className="px-5 py-3.5">
                        <div className="flex items-center gap-3">
                          <div className="flex size-8 shrink-0 items-center justify-center rounded-lg bg-primary/10 text-primary font-semibold text-xs border border-primary/20">
                            <AppWindow className="size-4" />
                          </div>
                          <div className="min-w-0">
                            <div className="flex items-center gap-1.5">
                              <span className="font-semibold text-foreground text-sm block truncate">
                                {c.clientName}
                              </span>
                              {isSystem && (
                                <Badge variant="outline" className="text-[10px] px-1 py-0 border-indigo-300 bg-indigo-50 text-indigo-700">
                                  System
                                </Badge>
                              )}
                            </div>
                            <span className="font-mono text-xs text-muted-foreground block truncate">
                              {c.clientId}
                            </span>
                          </div>
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        <div className="flex flex-wrap gap-1 max-w-[180px]">
                          {Array.from(c.authorizationGrantTypes || []).map((g) => (
                            <Badge
                              key={g}
                              variant="outline"
                              className="text-[10px] font-mono px-1.5 py-0 border-blue-200 bg-blue-50 text-blue-700"
                            >
                              {g.replace("_", " ")}
                            </Badge>
                          ))}
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        <div className="flex flex-wrap gap-1 max-w-[160px]">
                          {Array.from(c.clientAuthenticationMethods || []).map((m) => (
                            <Badge
                              key={m}
                              variant="outline"
                              className="text-[10px] font-mono px-1.5 py-0 border-border bg-muted/40 text-muted-foreground"
                            >
                              {m}
                            </Badge>
                          ))}
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        <div className="flex flex-wrap gap-1 max-w-[160px]">
                          {Array.from(c.scopes || []).map((s) => (
                            <Badge
                              key={s}
                              variant="outline"
                              className="text-[10px] font-mono px-1.5 py-0 border-emerald-200 bg-emerald-50 text-emerald-700"
                            >
                              {s}
                            </Badge>
                          ))}
                        </div>
                      </td>

                      <td className="px-4 py-3.5">
                        <span className="text-xs text-muted-foreground font-mono">
                          {c.redirectUrisCount} {lang === "km" ? "URIs" : "URIs"}
                        </span>
                      </td>

                      <td className="px-4 py-3.5">
                        <div className="space-y-1">
                          {c.requireProofKey ? (
                            <Badge variant="outline" className="text-[10px] px-1.5 py-0 border-amber-300 bg-amber-50 text-amber-700 block w-fit">
                              PKCE Enabled
                            </Badge>
                          ) : (
                            <span className="text-[11px] text-muted-foreground/70 block">No PKCE</span>
                          )}
                          {c.requireAuthorizationConsent && (
                            <Badge variant="outline" className="text-[10px] px-1.5 py-0 border-purple-200 bg-purple-50 text-purple-700 block w-fit">
                              Consent
                            </Badge>
                          )}
                        </div>
                      </td>

                      <td className="px-5 py-3.5 text-right">
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" size="icon" className="size-8">
                              <MoreVertical className="size-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end" className="w-48">
                            <DropdownMenuItem
                              onClick={() => onOpenEdit(c)}
                              className="gap-2 cursor-pointer text-xs"
                            >
                              <Edit2 className="size-3.5" />
                              <span>{lang === "km" ? "កែប្រែព័ត៌មាន" : "Edit Client"}</span>
                            </DropdownMenuItem>

                            <DropdownMenuItem
                              onClick={() => onOpenRegenSecret(c)}
                              className="gap-2 cursor-pointer text-xs"
                            >
                              <KeyRound className="size-3.5 text-amber-600" />
                              <span>{lang === "km" ? "បង្កើត Secret ថ្មី" : "Regenerate Secret"}</span>
                            </DropdownMenuItem>

                            <DropdownMenuSeparator />

                            <DropdownMenuItem
                              onClick={() => onOpenDelete(c)}
                              disabled={isSystem}
                              className="gap-2 cursor-pointer text-xs text-rose-600 focus:text-rose-600"
                            >
                              <Trash2 className="size-3.5" />
                              <span>{lang === "km" ? "លុប Client" : "Delete Client"}</span>
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

export default ClientsTable
