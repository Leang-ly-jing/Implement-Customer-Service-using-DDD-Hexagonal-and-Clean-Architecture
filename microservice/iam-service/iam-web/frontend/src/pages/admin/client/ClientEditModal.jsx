import { useState, useEffect } from "react"
import { Edit2, RefreshCw } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"

const STANDARD_SCOPES = ["openid", "profile", "email", "offline_access"]

export function ClientEditModal({
  isOpen,
  onClose,
  client,
  loading = false,
  onSubmit,
  submitting = false,
  lang = "km",
}) {
  const [form, setForm] = useState({
    clientName: "",
    clientAuthenticationMethods: [],
    authorizationGrantTypes: [],
    redirectUris: [],
    scopes: [],
    requireProofKey: false,
    requireAuthorizationConsent: false,
    accessTokenTimeToLiveSeconds: 3600,
    refreshTokenTimeToLiveSeconds: 2592000,
  })
  const [redirectInput, setRedirectInput] = useState("")

  useEffect(() => {
    if (client) {
      setForm({
        clientName: client.clientName || "",
        clientAuthenticationMethods: Array.from(client.clientAuthenticationMethods || []),
        authorizationGrantTypes: Array.from(client.authorizationGrantTypes || []),
        redirectUris: Array.from(client.redirectUris || []),
        scopes: Array.from(client.scopes || []),
        requireProofKey: client.requireProofKey ?? false,
        requireAuthorizationConsent: client.requireAuthorizationConsent ?? false,
        accessTokenTimeToLiveSeconds: client.accessTokenTimeToLiveSeconds ?? 3600,
        refreshTokenTimeToLiveSeconds: client.refreshTokenTimeToLiveSeconds ?? 2592000,
      })
    }
  }, [client])

  if (!isOpen || !client) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[85vh] flex flex-col">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Edit2 className="size-5 text-primary" />
            <span>{lang === "km" ? "កែប្រែការកំណត់ Client" : "Edit Client Configuration"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km"
              ? "កែប្រែ Grant types, redirect URIs, scopes និង security settings"
              : "Update client credentials, grant types, redirect URIs, and scopes."}
          </DialogDescription>
        </DialogHeader>

        {loading ? (
          <div className="py-16 text-center text-muted-foreground">
            <RefreshCw className="size-6 animate-spin mx-auto mb-2 opacity-60" />
            <p className="text-xs">Loading configuration...</p>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto space-y-4 pr-1 py-1">
            <div>
              <label className="text-xs font-medium text-foreground block mb-1">
                {lang === "km" ? "Client Name *" : "Client Name *"}
              </label>
              <Input
                value={form.clientName}
                onChange={(e) => setForm({ ...form, clientName: e.target.value })}
                required
                className="text-xs"
              />
            </div>

            <div>
              <label className="text-xs font-medium text-foreground block mb-1.5">
                {lang === "km" ? "Authorization Grant Types" : "Authorization Grant Types"}
              </label>
              <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 border rounded-lg p-2.5 bg-muted/20 text-xs">
                {[
                  { id: "authorization_code", label: "Authorization Code" },
                  { id: "refresh_token", label: "Refresh Token" },
                  { id: "client_credentials", label: "Client Credentials" },
                ].map((g) => {
                  const checked = form.authorizationGrantTypes.includes(g.id)
                  return (
                    <label key={g.id} className="flex items-center gap-2 cursor-pointer">
                      <input
                        type="checkbox"
                        checked={checked}
                        onChange={(e) => {
                          if (e.target.checked) {
                            setForm({ ...form, authorizationGrantTypes: [...form.authorizationGrantTypes, g.id] })
                          } else {
                            setForm({
                              ...form,
                              authorizationGrantTypes: form.authorizationGrantTypes.filter((x) => x !== g.id),
                            })
                          }
                        }}
                        className="size-3.5 rounded border-border text-primary"
                      />
                      <span className="font-mono text-[11px]">{g.label}</span>
                    </label>
                  )
                })}
              </div>
            </div>

            <div>
              <label className="text-xs font-medium text-foreground block mb-1.5">
                {lang === "km" ? "Client Authentication Methods" : "Client Authentication Methods"}
              </label>
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-2 border rounded-lg p-2.5 bg-muted/20 text-xs">
                {[
                  { id: "client_secret_basic", label: "client_secret_basic" },
                  { id: "client_secret_post", label: "client_secret_post" },
                  { id: "none", label: "none (Public/PKCE)" },
                ].map((m) => {
                  const checked = form.clientAuthenticationMethods.includes(m.id)
                  return (
                    <label key={m.id} className="flex items-center gap-2 cursor-pointer">
                      <input
                        type="checkbox"
                        checked={checked}
                        onChange={(e) => {
                          if (e.target.checked) {
                            setForm({ ...form, clientAuthenticationMethods: [...form.clientAuthenticationMethods, m.id] })
                          } else {
                            setForm({
                              ...form,
                              clientAuthenticationMethods: form.clientAuthenticationMethods.filter((x) => x !== m.id),
                            })
                          }
                        }}
                        className="size-3.5 rounded border-border text-primary"
                      />
                      <span className="font-mono text-[11px]">{m.label}</span>
                    </label>
                  )
                })}
              </div>
            </div>

            <div>
              <label className="text-xs font-medium text-foreground block mb-1">
                {lang === "km" ? "Redirect URIs" : "Redirect URIs"}
              </label>
              <div className="flex gap-2 mb-2">
                <Input
                  value={redirectInput}
                  onChange={(e) => setRedirectInput(e.target.value)}
                  placeholder="https://app.example.com/oauth/callback"
                  className="font-mono text-xs flex-1"
                />
                <Button
                  type="button"
                  size="sm"
                  variant="outline"
                  onClick={() => {
                    if (redirectInput.trim()) {
                      setForm({
                        ...form,
                        redirectUris: [...form.redirectUris, redirectInput.trim()],
                      })
                      setRedirectInput("")
                    }
                  }}
                  className="text-xs"
                >
                  {lang === "km" ? "បន្ថែម" : "Add"}
                </Button>
              </div>
              <div className="flex flex-wrap gap-1.5 max-h-24 overflow-y-auto">
                {form.redirectUris.map((uri) => (
                  <Badge key={uri} variant="outline" className="font-mono text-[11px] gap-1 pr-1 border-blue-300 bg-blue-50 text-blue-700">
                    <span>{uri}</span>
                    <button
                      type="button"
                      onClick={() =>
                        setForm({ ...form, redirectUris: form.redirectUris.filter((u) => u !== uri) })
                      }
                      className="hover:text-rose-600 ml-1 text-xs"
                    >
                      ×
                    </button>
                  </Badge>
                ))}
              </div>
            </div>

            <div>
              <label className="text-xs font-medium text-foreground block mb-1.5">
                {lang === "km" ? "Allowed Scopes" : "Allowed Scopes"}
              </label>
              <div className="flex flex-wrap gap-2 mb-2">
                {STANDARD_SCOPES.map((s) => {
                  const checked = form.scopes.includes(s)
                  return (
                    <label key={s} className="flex items-center gap-1.5 text-xs bg-muted/40 border px-2 py-1 rounded cursor-pointer">
                      <input
                        type="checkbox"
                        checked={checked}
                        onChange={(e) => {
                          if (e.target.checked) {
                            setForm({ ...form, scopes: [...form.scopes, s] })
                          } else {
                            setForm({ ...form, scopes: form.scopes.filter((x) => x !== s) })
                          }
                        }}
                        className="size-3.5 rounded border-border text-primary"
                      />
                      <span className="font-mono text-[11px]">{s}</span>
                    </label>
                  )
                })}
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 border-t pt-3">
              <label className="flex items-center gap-2 cursor-pointer text-xs">
                <input
                  type="checkbox"
                  checked={form.requireProofKey}
                  onChange={(e) => setForm({ ...form, requireProofKey: e.target.checked })}
                  className="size-4 rounded border-border text-primary"
                />
                <div>
                  <span className="font-medium block">{lang === "km" ? "តម្រូវឱ្យប្រើ PKCE" : "Require PKCE"}</span>
                </div>
              </label>

              <label className="flex items-center gap-2 cursor-pointer text-xs">
                <input
                  type="checkbox"
                  checked={form.requireAuthorizationConsent}
                  onChange={(e) => setForm({ ...form, requireAuthorizationConsent: e.target.checked })}
                  className="size-4 rounded border-border text-primary"
                />
                <div>
                  <span className="font-medium block">{lang === "km" ? "តម្រូវផ្ទាំង Consent" : "Require Consent"}</span>
                </div>
              </label>
            </div>

            <DialogFooter className="border-t pt-3">
              <Button type="button" variant="outline" onClick={onClose} disabled={submitting}>
                {lang === "km" ? "បោះបង់" : "Cancel"}
              </Button>
              <Button type="submit" disabled={submitting} className="gap-1.5">
                {submitting && <RefreshCw className="size-3.5 animate-spin" />}
                <span>{lang === "km" ? "រក្សាទុកការកែប្រែ" : "Save Changes"}</span>
              </Button>
            </DialogFooter>
          </form>
        )}
      </DialogContent>
    </Dialog>
  )
}

export default ClientEditModal
