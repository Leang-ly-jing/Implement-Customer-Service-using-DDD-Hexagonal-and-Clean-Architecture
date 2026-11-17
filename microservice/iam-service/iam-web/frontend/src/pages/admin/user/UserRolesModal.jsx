import { useState, useEffect } from "react"
import {
  ShieldCheck,
  Shield,
  X,
  RefreshCw,
  UserCheck,
  UserX,
} from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"

export function UserRolesModal({ isOpen, onClose, user, onRolesUpdated }) {
  const { t, i18n } = useTranslation()
  const isKm = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")
  const [systemRoles, setSystemRoles] = useState([])
  const [assignedRoles, setAssignedRoles] = useState([])
  const [loading, setLoading] = useState(false)
  const [actionId, setActionId] = useState(null)

  const loadData = async () => {
    if (!user?.uuid) return
    setLoading(true)
    try {
      const [rolesRes, userAuthRes] = await Promise.all([
        axiosClient.get("/roles?page=0&size=100"),
        axiosClient.get(`/users/${user.uuid}/authorities`),
      ])
      setSystemRoles(rolesRes.data?.content || [])
      setAssignedRoles(userAuthRes.data?.roles || [])
    } catch (err) {
      console.error("Failed to load roles:", err)
      toast.error(isKm ? "កំហុស" : "Error", isKm ? "មិនអាចទាញយកតួនាទីរបស់អ្នកប្រើប្រាស់បានទេ។" : "Could not load roles for this user.")
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

  const refreshUserRoles = async () => {
    try {
      const res = await axiosClient.get(`/users/${user.uuid}/authorities`)
      setAssignedRoles(res.data?.roles || [])
      onRolesUpdated?.()
    } catch (err) {
      console.error("Error refreshing user roles:", err)
    }
  }

  const handleAssignRole = async (roleUuid, roleName) => {
    setActionId(roleUuid)
    try {
      await axiosClient.post(`/users/${user.uuid}/roles`, {
        roleUuids: [roleUuid],
        reason: "ADMIN_ASSIGNED",
      })
      toast.success(
        isKm ? "បានកំណត់តួនាទី" : "Role Assigned",
        isKm ? `បានកំណត់តួនាទី ${roleName} ដោយជោគជ័យ។` : `Role ${roleName} assigned successfully.`
      )
      await refreshUserRoles()
    } catch (err) {
      toast.error(
        isKm ? "បរាជ័យ" : "Assignment Failed",
        err.response?.data?.detail || "Could not assign role."
      )
    } finally {
      setActionId(null)
    }
  }

  const handleRevokeRole = async (roleUuid, roleName) => {
    setActionId(roleUuid)
    try {
      await axiosClient.delete(`/users/${user.uuid}/roles/${roleUuid}?reason=REVOKED`)
      toast.success(
        isKm ? "បានដកតួនាទី" : "Role Revoked",
        isKm ? `បានដកតួនាទី ${roleName} ដោយជោគជ័យ។` : `Role ${roleName} revoked successfully.`
      )
      await refreshUserRoles()
    } catch (err) {
      toast.error(
        isKm ? "បរាជ័យ" : "Revocation Failed",
        err.response?.data?.detail || "Could not revoke role."
      )
    } finally {
      setActionId(null)
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-xs p-4">
      <div className="w-full max-w-lg rounded-xl bg-background border p-6 shadow-xl animate-in fade-in-0 zoom-in-95">
        <div className="flex items-center justify-between pb-3 border-b">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <ShieldCheck className="size-5 text-purple-600" />
            {isKm ? "គ្រប់គ្រងតួនាទី (Role Assignment)" : "Manage User Roles"}
          </h3>
          <Button variant="ghost" size="icon-xs" onClick={onClose}>
            <X className="size-4" />
          </Button>
        </div>

        <div className="space-y-4 pt-4">
          <div className="flex items-center justify-between p-3 rounded-lg bg-muted/40">
            <div className="flex items-center gap-3">
              <div className="flex size-10 items-center justify-center rounded-full bg-purple-100 text-purple-700 font-bold text-sm uppercase">
                {(user.username || "U").slice(0, 2)}
              </div>
              <div>
                <h4 className="font-semibold text-foreground text-sm">{user.name || user.username}</h4>
                <div className="text-xs text-muted-foreground">@{user.username}</div>
              </div>
            </div>
            <Badge variant="outline" className="border-purple-200 bg-purple-50 text-purple-700 text-xs">
              {assignedRoles.length} {isKm ? "តួនាទីសកម្ម" : "Active Roles"}
            </Badge>
          </div>

          <div>
            <h5 className="text-xs font-semibold uppercase text-muted-foreground tracking-wider mb-2">
              {isKm ? "តួនាទីប្រព័ន្ធទាំងអស់ (System Roles)" : "Available Roles"}
            </h5>

            {loading ? (
              <div className="py-8 text-center text-muted-foreground">
                <RefreshCw className="mx-auto size-5 animate-spin mb-2" />
                {isKm ? "កំពុងទាញយកតួនាទី..." : "Loading roles..."}
              </div>
            ) : systemRoles.length === 0 ? (
              <div className="py-6 text-center text-muted-foreground text-sm">
                {isKm ? "មិនមានតួនាទីត្រូវបានរកឃើញទេ" : "No roles available."}
              </div>
            ) : (
              <div className="space-y-2 max-h-72 overflow-y-auto pr-1">
                {systemRoles.map((role) => {
                  const isAssigned = assignedRoles.some(
                    (ar) => ar.uuid === role.uuid || ar.name === role.name
                  )
                  const isActing = actionId === role.uuid

                  return (
                    <div
                      key={role.uuid}
                      className="flex items-center justify-between p-3 rounded-lg border bg-card hover:bg-muted/20 transition-colors"
                    >
                      <div className="flex items-center gap-2.5">
                        <Shield className={`size-4 ${isAssigned ? "text-purple-600" : "text-muted-foreground"}`} />
                        <div>
                          <div className="font-medium text-sm text-foreground flex items-center gap-2">
                            {role.name}
                            {isAssigned && (
                              <Badge variant="outline" className="border-emerald-200 bg-emerald-50 text-emerald-700 text-[10px] py-0 px-1.5">
                                {isKm ? "បានកំណត់" : "Assigned"}
                              </Badge>
                            )}
                          </div>
                        </div>
                      </div>

                      <div>
                        {isAssigned ? (
                          <Button
                            variant="outline"
                            size="xs"
                            disabled={isActing}
                            className="text-rose-600 border-rose-200 hover:bg-rose-50 hover:text-rose-700 text-xs"
                            onClick={() => handleRevokeRole(role.uuid, role.name)}
                          >
                            {isActing ? (
                              <RefreshCw className="size-3 animate-spin mr-1" />
                            ) : (
                              <UserX className="size-3 mr-1" />
                            )}
                            {isKm ? "ដកចេញ (Revoke)" : "Revoke"}
                          </Button>
                        ) : (
                          <Button
                            variant="default"
                            size="xs"
                            disabled={isActing}
                            className="text-xs"
                            onClick={() => handleAssignRole(role.uuid, role.name)}
                          >
                            {isActing ? (
                              <RefreshCw className="size-3 animate-spin mr-1" />
                            ) : (
                              <UserCheck className="size-3 mr-1" />
                            )}
                            {isKm ? "កំណត់ (Assign)" : "Assign"}
                          </Button>
                        )}
                      </div>
                    </div>
                  )
                })}
              </div>
            )}
          </div>

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

export default UserRolesModal
