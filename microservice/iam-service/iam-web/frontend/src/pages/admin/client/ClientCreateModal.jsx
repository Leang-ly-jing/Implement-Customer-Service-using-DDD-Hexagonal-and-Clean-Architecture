import { useState } from "react"
import { AppWindow, RefreshCw } from "lucide-react"
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

const INITIAL_FORM = {
  clientId: "",
  clientName: "",
  clientSecret: "",
  clientAuthenticationMethods: ["client_secret_basic"],
  authorizationGrantTypes: ["authorization_code", "refresh_token"],
  redirectUris: [],
  scopes: ["openid", "profile", "email"],
  requireProofKey: true,
  requireAuthorizationConsent: true,
  accessTokenTimeToLiveSeconds: 3600,
  refreshTokenTimeToLiveSeconds: 2592000,
}

export function ClientCreateModal({
  isOpen,
  onClose,
  onSubmit,
  submitting = false,
  lang = "km",
}) {
  const [form, setForm] = useState(INITIAL_FORM)
  const [newRedirectInput, setNewRedirectInput] = useState("")
  const [customScopeInput, setCustomScopeInput] = useState("")

  if (!isOpen) return null

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit(form)
    setForm(INITIAL_FORM)
    setNewRedirectInput("")
    setCustomScopeInput("")
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[85vh] flex flex-col">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <AppWindow className="size-5 text-primary" />
            <span>{lang === "km" ? "ចុះឈ្មោះ OAuth2 / OIDC Client ថ្មី" : "Register OAuth2 / OIDC Client"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km"
              ? "កំណត់ Client ID, Secret, Grant Types, Redirect URIs និង Scopes សម្រាប់កម្មវិធី"
              : "Configure credentials, allowed grant types, authentication methods, redirect URIs, and scopes."}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="flex-1 overflow-y-auto space-y-4 pr-1 py-1">

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="text-xs font-medium text-foreground block mb-1">
                {lang === "km" ? "Client ID *" : "Client ID *"}
              </label>
              <Input
                value={form.clientId}
                onChange={(e) =>
                  setForm({ ...form, clientId: e.target.value.toLowerCase().replace(/\s+/g, "-") })
                }
                placeholder="e.g. mobile-app, web-spa"
                className="font-mono text-xs"
                required
              />
            </div>

            <div>
              <label className="text-xs font-medium text-foreground block mb-1">
                {lang === "km" ? "Client Name *" : "Client Name *"}
              </label>
              <Input
                value={form.clientName}
                onChange={(e) => setForm({ ...form, clientName: e.target.value })}
                placeholder="e.g. Mobile Customer App"
                className="text-xs"
                required
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1">
              {lang === "km" ? "Client Secret (ទុកទំនេរដើម្បី Auto-Generate)" : "Client Secret (Leave blank to auto-generate)"}
            </label>
            <Input
              type="password"
              value={form.clientSecret}
              onChange={(e) => setForm({ ...form, clientSecret: e.target.value })}
              placeholder="Optional custom secret"
              className="font-mono text-xs"
            />
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1.5">
              {lang === "km" ? "Authorization Grant Types *" : "Authorization Grant Types *"}
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
                          setForm({ ...form, authorizationGrantTypes: form.authorizationGrantTypes.filter((x) => x !== g.id) })
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
              {lang === "km" ? "Client Authentication Methods *" : "Client Authentication Methods *"}
            </label>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-2 border rounded-lg p-2.5 bg-muted/20 text-xs">
              {[
                { id: "client_secret_basic", label: "client_secret_basic (Basic Auth)" },
                { id: "client_secret_post", label: "client_secret_post (POST body)" },
                { id: "none", label: "none (Public Client / PKCE)" },
              ].map((m) => {
                const checked = form.clientAuthenticationMethods.includes(m.id)
                return (
                  <label key={m.id} className="flex items-center gap-2 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={checked}
                      onChange={(e) => {
                        if (e.target.checked) {
                          const updated = [...form.clientAuthenticationMethods, m.id]
                          setForm({
                            ...form,
                            clientAuthenticationMethods: updated,
                            requireProofKey: m.id === "none" ? true : form.requireProofKey,
                          })
                        } else {
                          setForm({ ...form, clientAuthenticationMethods: form.clientAuthenticationMethods.filter((x) => x !== m.id) })
                        }
                      }}
                      className="size-3.5 rounded border-border text-primary"
                    />
                    <span className="font-mono text-[11px] truncate">{m.label}</span>
                  </label>
                )
              })}
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-foreground block mb-1">
              {lang === "km" ? "Redirect URIs (Callback URLs)" : "Redirect URIs (Callback URLs)"}
            </label>
            <div className="flex gap-2 mb-2">
              <Input
                value={newRedirectInput}
                onChange={(e) => setNewRedirectInput(e.target.value)}
                placeholder="https://app.example.com/oauth/callback"
                className="font-mono text-xs flex-1"
              />
              <Button
                type="button"
                size="sm"
                variant="outline"
                onClick={() => {
                  if (newRedirectInput.trim()) {
                    setForm({
                      ...form,
                      redirectUris: [...form.redirectUris, newRedirectInput.trim()],
                    })
                    setNewRedirectInput("")
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
                    onClick={() => setForm({ ...form, redirectUris: form.redirectUris.filter((u) => u !== uri) })}
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
            <div className="flex gap-2">
              <Input
                value={customScopeInput}
                onChange={(e) => setCustomScopeInput(e.target.value.toLowerCase())}
                placeholder="Custom scope e.g. read:orders"
                className="font-mono text-xs flex-1"
              />
              <Button
                type="button"
                size="sm"
                variant="outline"
                onClick={() => {
                  if (customScopeInput.trim() && !form.scopes.includes(customScopeInput.trim())) {
                    setForm({ ...form, scopes: [...form.scopes, customScopeInput.trim()] })
                    setCustomScopeInput("")
                  }
                }}
                className="text-xs"
              >
                {lang === "km" ? "បន្ថែម Scope" : "Add Scope"}
              </Button>
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
                <span className="font-medium block">{lang === "km" ? "តម្រូវឱ្យប្រើ PKCE" : "Require PKCE (Proof Key)"}</span>
                {form.clientAuthenticationMethods.includes("none") ? (
                  <span className="text-[11px] text-amber-600 font-medium block">
                    {lang === "km" ? "តម្រូវជាចាំបាច់សម្រាប់ Public Client (none)" : "Mandatory for Public Client (none)"}
                  </span>
                ) : (
                  <span className="text-[11px] text-muted-foreground block">
                    {lang === "km" ? "ណែនាំសម្រាប់ SPA និង Mobile App" : "Recommended for SPAs and mobile apps"}
                  </span>
                )}
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
                <span className="font-medium block">{lang === "km" ? "តម្រូវផ្ទាំង Consent" : "Require Authorization Consent"}</span>
                <span className="text-[11px] text-muted-foreground block">
                  {lang === "km" ? "សួរអ្នកប្រើប្រាស់មុនពេលផ្ដល់សិទ្ធិ" : "Prompts user to consent scopes"}
                </span>
              </div>
            </label>
          </div>

          <DialogFooter className="border-t pt-3">
            <Button type="button" variant="outline" onClick={onClose} disabled={submitting}>
              {lang === "km" ? "បោះបង់" : "Cancel"}
            </Button>
            <Button type="submit" disabled={submitting} className="gap-1.5">
              {submitting && <RefreshCw className="size-3.5 animate-spin" />}
              <span>{lang === "km" ? "ចុះឈ្មោះ Client" : "Register Client"}</span>
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

export default ClientCreateModal
