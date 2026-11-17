import { useState, useEffect, useMemo } from "react"
import {
  KeyRound,
  X,
  Search,
  Plus,
  Trash2,
  RefreshCw,
  ShieldCheck,
  CheckCircle2,
  SlidersHorizontal,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserPermissionsModal({ isOpen, onClose, user, onPermissionsUpdated }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [activeTab, setActiveTab] = useState("effective") // "effective" | "direct" | "grant"
  const [systemPermissions, setSystemPermissions] = useState([])
  const [directPermissions, setDirectPermissions] = useState([])
  const [effectivePermissions, setEffectivePermissions] = useState([])
  const [assignedRoles, setAssignedRoles] = useState([])
  const [loading, setLoading] = useState(false)
  const [actionId, setActionId] = useState(null)
  const [search, setSearch] = useState("")
  const [effect, setEffect] = useState("GRANT")

  const loadData = async () => {
    if (!user?.uuid) return
    setLoading(true)
    setSearch("")
    setEffect("GRANT")
    try {
      const [permsRes, userAuthRes] = await Promise.all([
        axiosClient.get("/permissions?page=0&size=100"),
        axiosClient.get(`/users/${user.uuid}/authorities`),
      ])
      setSystemPermissions(permsRes.data?.content || [])
      setDirectPermissions(userAuthRes.data?.directPermissions || [])
      setEffectivePermissions(userAuthRes.data?.effectivePermissions || [])
      setAssignedRoles(userAuthRes.data?.roles || [])
    } catch (err) {
      console.error("Failed to load permissions:", err)
      toast.error(
        isKm ? "កំហុស" : "Error",
        isKm ? "មិនអាចទាញយកសិទ្ធិរបស់អ្នកប្រើប្រាស់បានទេ។" : "Could not load permissions for this user."
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (isOpen && user?.uuid) {
      loadData()
    }
  }, [isOpen, user?.uuid])

  if (!isOpen || !user) return null

  const refreshPermissions = async () => {
    try {
      const res = await axiosClient.get(`/users/${user.uuid}/authorities`)
      setDirectPermissions(res.data?.directPermissions || [])
      setEffectivePermissions(res.data?.effectivePermissions || [])
      setAssignedRoles(res.data?.roles || [])
      onPermissionsUpdated?.()
    } catch (err) {
      console.error("Error refreshing user permissions:", err)
    }
  }

  const handleGrantPermission = async (permUuid, permName) => {
    setActionId(permUuid)
    try {
      await axiosClient.post(`/users/${user.uuid}/permissions`, {
        permissionUuids: [permUuid],
        effect: effect,
        reason: "ADMIN_DIRECT_GRANT",
      })
      toast.success(
        isKm ? "បានផ្ដល់សិទ្ធិ" : "Permission Granted",
        isKm
          ? `បានផ្ដល់សិទ្ធិ ${permName} (${effect}) ដោយជោគជ័យ។`
          : `Permission ${permName} granted.`
      )
      await refreshPermissions()
    } catch (err) {
      toast.error(
        isKm ? "បរាជ័យ" : "Grant Failed",
        err.response?.data?.detail || "Could not grant permission."
      )
    } finally {
      setActionId(null)
    }
  }

  const handleRevokePermission = async (permUuid, permName) => {
    setActionId(permUuid)
    try {
      await axiosClient.delete(`/users/${user.uuid}/permissions/${permUuid}?reason=REVOKED`)
      toast.success(
        isKm ? "បានដកសិទ្ធិ" : "Permission Revoked",
        isKm ? `បានដកសិទ្ធិ ${permName} ដោយជោគជ័យ។` : `Permission ${permName} revoked.`
      )
      await refreshPermissions()
    } catch (err) {
      toast.error(
        isKm ? "បរាជ័យ" : "Revocation Failed",
        err.response?.data?.detail || "Could not revoke permission."
      )
    } finally {
      setActionId(null)
    }
  }

  const filteredEffective = useMemo(() => {
    if (!search.trim()) return effectivePermissions
    const q = search.toLowerCase()
    return effectivePermissions.filter((p) => p.toLowerCase().includes(q))
  }, [effectivePermissions, search])

  const filteredSystemPermissions = useMemo(() => {
    if (!search.trim()) return systemPermissions
    const q = search.toLowerCase()
    return systemPermissions.filter(
      (p) => p.name?.toLowerCase().includes(q) || p.resource?.toLowerCase().includes(q)
    )
  }, [systemPermissions, search])

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-xl rounded-xl bg-background border p-6 shadow-xl animate-in fade-in-0 zoom-in-95">
        <div className="flex items-center justify-between pb-3 border-b">
          <div className="flex items-center gap-2">
            <KeyRound className="size-5 text-amber-600" />
            <div>
              <h3 className="text-base font-semibold">
                {isKm ? "សិទ្ធិរបស់អ្នកប្រើប្រាស់ (User Permissions)" : "User Permissions"}
              </h3>
              <p className="text-xs text-muted-foreground">
                {isKm
                  ? "ពិនិត្យសិទ្ធិជាក់ស្ដែងដែលទទួលបានពី Roles និងកំណត់សិទ្ធិផ្ទាល់"
                  : "View effective permissions inherited from roles and manage direct overrides."}
              </p>
            </div>
          </div>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        <div className="space-y-4 pt-4">
          {/* User Info Header */}
          <div className="flex items-center justify-between p-3 rounded-lg bg-muted/40">
            <div className="flex items-center gap-3">
              <div className="flex size-10 items-center justify-center rounded-full bg-amber-100 text-amber-700 font-bold text-sm uppercase">
                {(user.username || "U").slice(0, 2)}
              </div>
              <div>
                <h4 className="font-semibold text-foreground text-sm">{user.name || user.username}</h4>
                <div className="text-xs text-muted-foreground flex items-center gap-2">
                  <span>@{user.username}</span>
                  {assignedRoles.length > 0 && (
                    <span className="text-purple-600 font-medium">
                      ({assignedRoles.map((r) => r.name).join(", ")})
                    </span>
                  )}
                </div>
              </div>
            </div>
            <div className="flex items-center gap-1.5">
              <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-xs">
                {effectivePermissions.length} {isKm ? "សិទ្ធិសរុប" : "Effective"}
              </Badge>
              {directPermissions.length > 0 && (
                <Badge variant="outline" className="border-amber-200 bg-amber-50 text-amber-700 text-xs">
                  {directPermissions.length} {isKm ? "ផ្ទាល់" : "Direct"}
                </Badge>
              )}
            </div>
          </div>

          {/* Navigation Tabs */}
          <div className="flex border-b text-xs font-medium">
            <button
              onClick={() => {
                setActiveTab("effective")
                setSearch("")
              }}
              className={`pb-2.5 px-3 border-b-2 transition-colors flex items-center gap-1.5 ${
                activeTab === "effective"
                  ? "border-primary text-primary font-semibold"
                  : "border-transparent text-muted-foreground hover:text-foreground"
              }`}
            >
              <CheckCircle2 className="size-3.5" />
              <span>{isKm ? "សិទ្ធិជាក់ស្ដែង (Effective)" : "Effective Permissions"}</span>
              <span className="ml-1 rounded-full bg-muted px-1.5 py-0.2 text-[10px]">
                {effectivePermissions.length}
              </span>
            </button>

            <button
              onClick={() => {
                setActiveTab("direct")
                setSearch("")
              }}
              className={`pb-2.5 px-3 border-b-2 transition-colors flex items-center gap-1.5 ${
                activeTab === "direct"
                  ? "border-primary text-primary font-semibold"
                  : "border-transparent text-muted-foreground hover:text-foreground"
              }`}
            >
              <SlidersHorizontal className="size-3.5" />
              <span>{isKm ? "សិទ្ធិកំណត់ផ្ទាល់ (Direct)" : "Direct Overrides"}</span>
              <span className="ml-1 rounded-full bg-muted px-1.5 py-0.2 text-[10px]">
                {directPermissions.length}
              </span>
            </button>

            <button
              onClick={() => {
                setActiveTab("grant")
                setSearch("")
              }}
              className={`pb-2.5 px-3 border-b-2 transition-colors flex items-center gap-1.5 ${
                activeTab === "grant"
                  ? "border-primary text-primary font-semibold"
                  : "border-transparent text-muted-foreground hover:text-foreground"
              }`}
            >
              <Plus className="size-3.5" />
              <span>{isKm ? "ផ្ដល់សិទ្ធិថ្មី (Grant)" : "Grant New"}</span>
            </button>
          </div>

          {/* Tab 1: Effective Permissions (Inherited from Roles) */}
          {activeTab === "effective" && (
            <div className="space-y-3">
              <div className="p-2.5 rounded-lg border bg-blue-50/50 dark:bg-blue-950/20 text-xs text-blue-700 dark:text-blue-300 flex items-start gap-2">
                <ShieldCheck className="size-4 shrink-0 mt-0.5 text-blue-600" />
                <div>
                  <span className="font-semibold">
                    {isKm ? "សិទ្ធិទទួលបានតាមរយៈ Roles:" : "Permissions inherited from Roles:"}
                  </span>{" "}
                  {isKm
                    ? "អ្នកប្រើប្រាស់នេះទទួលបានសិទ្ធិខាងក្រោមដោយស្វ័យប្រវត្តិតាមរយៈតួនាទី (Roles) ដែលបានកំណត់។"
                    : "The user inherits these permissions automatically through their assigned roles."}
                </div>
              </div>

              <div className="relative">
                <Search className="absolute left-2.5 top-2.5 size-3.5 text-muted-foreground" />
                <Input
                  placeholder={isKm ? "ស្វែងរកសិទ្ធិ (ឧ. user:read)..." : "Filter effective permissions..."}
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  className="pl-8 text-xs h-8"
                />
              </div>

              {loading ? (
                <div className="py-8 text-center text-muted-foreground">
                  <RefreshCw className="mx-auto size-5 animate-spin mb-2" />
                  {isKm ? "កំពុងទាញយកទិន្នន័យ..." : "Loading permissions..."}
                </div>
              ) : filteredEffective.length === 0 ? (
                <div className="p-4 text-center rounded-lg border border-dashed text-muted-foreground text-xs">
                  {search
                    ? isKm
                      ? "មិនមានសិទ្ធិដែលត្រូវនឹងការស្វែងរកទេ"
                      : "No permissions match your filter."
                    : isKm
                    ? "អ្នកប្រើប្រាស់នេះមិនទាន់មានសិទ្ធិណាមួយឡើយ (សូមកំណត់ Role ជូនគាត់ជាមុន)។"
                    : "No permissions assigned. Assign a role to grant permissions."}
                </div>
              ) : (
                <div className="flex flex-wrap gap-1.5 max-h-52 overflow-y-auto p-1 border rounded-lg bg-card">
                  {filteredEffective.map((perm) => (
                    <Badge
                      key={perm}
                      variant="secondary"
                      className="text-xs font-mono py-1 px-2.5 bg-emerald-50 text-emerald-800 border-emerald-200 dark:bg-emerald-950/40 dark:text-emerald-300"
                    >
                      <CheckCircle2 className="size-3 mr-1 inline text-emerald-600" />
                      {perm}
                    </Badge>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Tab 2: Direct Permissions */}
          {activeTab === "direct" && (
            <div className="space-y-3">
              <p className="text-xs text-muted-foreground">
                {isKm
                  ? "សិទ្ធិកំណត់ផ្ទាល់ (Direct Overrides) គឺជាសិទ្ធិពិសេសដែលអនុញ្ញាត (GRANT) ឬបដិសេធ (DENY) ដោយមិនគិតពី Roles។"
                  : "Direct permissions override role permissions explicitly (GRANT or DENY)."}
              </p>

              {loading ? (
                <div className="py-8 text-center text-muted-foreground">
                  <RefreshCw className="mx-auto size-5 animate-spin mb-2" />
                  {isKm ? "កំពុងទាញយកទិន្នន័យ..." : "Loading..."}
                </div>
              ) : directPermissions.length === 0 ? (
                <div className="p-6 text-center rounded-lg border border-dashed text-muted-foreground text-xs">
                  <SlidersHorizontal className="mx-auto size-6 mb-2 opacity-40" />
                  {isKm
                    ? "មិនមានសិទ្ធិកំណត់ផ្ទាល់ទេ (អ្នកប្រើប្រាស់ប្រើប្រាស់សិទ្ធិតាម Roles ទាំងស្រុង)"
                    : "No direct overrides. User relies entirely on assigned role permissions."}
                </div>
              ) : (
                <div className="space-y-1.5 max-h-52 overflow-y-auto pr-1">
                  {directPermissions.map((dp) => {
                    const isActing = actionId === dp.uuid
                    return (
                      <div
                        key={dp.uuid || dp.name}
                        className="flex items-center justify-between p-2 rounded-lg border bg-card text-xs"
                      >
                        <div className="flex items-center gap-2">
                          <span className="font-mono font-medium text-foreground">{dp.name}</span>
                          <Badge
                            variant="outline"
                            className={`text-[10px] py-0 px-1.5 ${
                              dp.effect === "DENY"
                                ? "border-rose-200 bg-rose-50 text-rose-700"
                                : "border-emerald-200 bg-emerald-50 text-emerald-700"
                            }`}
                          >
                            {dp.effect}
                          </Badge>
                        </div>

                        <Button
                          variant="ghost"
                          size="icon-xs"
                          disabled={isActing}
                          title={isKm ? "ដកសិទ្ធិចេញ (Revoke)" : "Revoke Permission"}
                          className="text-rose-600 hover:text-rose-700 hover:bg-rose-50"
                          onClick={() => handleRevokePermission(dp.uuid, dp.name)}
                        >
                          {isActing ? (
                            <RefreshCw className="size-3 animate-spin" />
                          ) : (
                            <Trash2 className="size-3.5" />
                          )}
                        </Button>
                      </div>
                    )
                  })}
                </div>
              )}
            </div>
          )}

          {/* Tab 3: Grant Direct Override */}
          {activeTab === "grant" && (
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-xs text-muted-foreground">
                  {isKm ? "ជ្រើសរើសប្រភេទសិទ្ធិផ្ទាល់:" : "Choose permission effect:"}
                </span>
                <select
                  value={effect}
                  onChange={(e) => setEffect(e.target.value)}
                  className="rounded border border-input bg-background px-2 py-0.5 text-xs shadow-xs"
                >
                  <option value="GRANT">GRANT (អនុញ្ញាត)</option>
                  <option value="DENY">DENY (បដិសេធ)</option>
                </select>
              </div>

              <div className="relative">
                <Search className="absolute left-2.5 top-2.5 size-3.5 text-muted-foreground" />
                <Input
                  placeholder={isKm ? "ស្វែងរកសិទ្ធិប្រព័ន្ធ (ឧ. user:create)..." : "Filter system permissions..."}
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  className="pl-8 text-xs h-8"
                />
              </div>

              <div className="space-y-1.5 max-h-48 overflow-y-auto pr-1">
                {filteredSystemPermissions.slice(0, 30).map((perm) => {
                  const alreadyDirect = directPermissions.some(
                    (dp) => dp.uuid === perm.uuid || dp.name === perm.name
                  )
                  const isActing = actionId === perm.uuid

                  return (
                    <div
                      key={perm.uuid}
                      className="flex items-center justify-between p-2 rounded-lg border bg-card hover:bg-muted/20 text-xs transition-colors"
                    >
                      <div>
                        <div className="font-mono font-medium text-foreground">{perm.name}</div>
                        {perm.description && (
                          <div className="text-[11px] text-muted-foreground truncate max-w-xs">
                            {perm.description}
                          </div>
                        )}
                      </div>

                      <div>
                        {alreadyDirect ? (
                          <span className="text-[11px] text-muted-foreground italic">
                            {isKm ? "បានកំណត់រួច" : "Already assigned"}
                          </span>
                        ) : (
                          <Button
                            variant="outline"
                            size="xs"
                            disabled={isActing}
                            className="text-xs h-6"
                            onClick={() => handleGrantPermission(perm.uuid, perm.name)}
                          >
                            {isActing ? (
                              <RefreshCw className="size-3 animate-spin mr-1" />
                            ) : (
                              <Plus className="size-3 mr-1" />
                            )}
                            {isKm ? "ផ្ដល់សិទ្ធិ" : "Grant"}
                          </Button>
                        )}
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
          )}

          <div className="flex justify-end pt-3 border-t">
            <Button variant="outline" onClick={onClose}>
              {t("common.close")}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserPermissionsModal
