import { useState } from "react"
import { KeyRound, Copy, Check } from "lucide-react"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"

export function ClientSecretModal({
  isOpen,
  onClose,
  data = { clientId: "", rawSecret: "" },
  lang = "km",
}) {
  const [copied, setCopied] = useState(false)

  if (!isOpen || !data?.rawSecret) return null

  const copyToClipboard = (text) => {
    navigator.clipboard.writeText(text)
    setCopied(true)
    toast.success(
      lang === "km" ? "ចម្លងរួចរាល់" : "Copied",
      lang === "km" ? "បានចម្លង Client Secret ទៅក្តារតម្បៀតខ្ទាស់" : "Secret copied to clipboard."
    )
    setTimeout(() => setCopied(false), 2500)
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-amber-600">
            <KeyRound className="size-5" />
            <span>{lang === "km" ? "Client Secret បានបង្កើត" : "OAuth2 Client Credentials"}</span>
          </DialogTitle>
          <DialogDescription>
            {lang === "km"
              ? "សូមចម្លង Client Secret នេះទុកឥឡូវនេះ។ ដើម្បីសុវត្ថិភាព វានឹងមិនត្រូវបានបង្ហាញម្តងទៀតឡើយ!"
              : "Please copy this Client Secret immediately. For security, it cannot be displayed again!"}
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-3 pt-2">
          <div>
            <span className="text-[11px] text-muted-foreground uppercase font-semibold block mb-1">
              Client ID
            </span>
            <div className="p-2.5 rounded-lg border bg-muted/40 font-mono text-xs font-semibold">
              {data.clientId}
            </div>
          </div>

          <div>
            <span className="text-[11px] text-muted-foreground uppercase font-semibold block mb-1">
              Client Secret
            </span>
            <div className="flex items-center gap-2">
              <div className="p-2.5 rounded-lg border bg-amber-50/60 border-amber-300 font-mono text-xs text-amber-900 break-all flex-1 font-semibold">
                {data.rawSecret}
              </div>
              <Button
                size="icon"
                variant="outline"
                onClick={() => copyToClipboard(data.rawSecret)}
                title="Copy secret"
                className="shrink-0 h-9 w-9"
              >
                {copied ? <Check className="size-4 text-emerald-600" /> : <Copy className="size-4" />}
              </Button>
            </div>
          </div>
        </div>

        <DialogFooter className="pt-2">
          <Button onClick={onClose}>
            {lang === "km" ? "ខ្ញុំបានចម្លងរួចរាល់" : "I have saved the secret"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

export default ClientSecretModal
