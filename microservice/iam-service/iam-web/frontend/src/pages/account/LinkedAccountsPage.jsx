import { useState, useEffect } from "react"
import { CircleCheck, ExternalLink } from "lucide-react"
import axiosClient from "@/api/axiosClient"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import { PageHeader, Confirm } from "./shared"

const SUPPORTED_PROVIDERS = [
  { id: "google", name: "Google" },
  { id: "github", name: "GitHub" },
  { id: "facebook", name: "Facebook" },
  { id: "telegram", name: "Telegram" },
]

export function LinkedAccountsPage() {
  const [identities, setIdentities] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let isMounted = true

    const fetchIdentities = async () => {
      try {
        const res = await axiosClient.get("/me/identities")
        if (isMounted) {
          setIdentities(res.data || [])
        }
      } catch {
        if (isMounted) {
          toast.error("Failed to load accounts", "Could not retrieve linked accounts.")
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    fetchIdentities()

    return () => {
      isMounted = false
    }
  }, [])

  const handleUnlink = async (providerName, identityUuid) => {
    try {
      await axiosClient.delete(`/me/identities/${identityUuid}`)
      setIdentities((prev) => prev.filter((i) => i.uuid !== identityUuid))
      toast.success(
        "Account unlinked",
        `Disconnected from ${providerName} successfully.`
      )
    } catch (err) {
      toast.error(
        "Action failed",
        err.response?.data?.message || "Unable to unlink account."
      )
    }
  }

  const handleConnect = (providerId) => {
    window.location.assign(`/oauth2/authorization/${providerId}`)
  }

  if (loading) {
    return (
      <div className="flex h-48 items-center justify-center">
        <p className="text-sm text-muted-foreground">Loading linked accounts...</p>
      </div>
    )
  }

  const providerList = SUPPORTED_PROVIDERS.map((p) => {
    const linkedIdentity = identities.find(
      (i) => i.provider?.toLowerCase() === p.id.toLowerCase()
    )

    return {
      ...p,
      linked: !!linkedIdentity,
      identityUuid: linkedIdentity?.uuid,
      account: linkedIdentity?.email || linkedIdentity?.displayName || "",
    }
  })

  return (
    <>
      <PageHeader
        title="Linked accounts"
        description="Connect social profiles to sign in to ISTAD with one click and synchronize profile info."
      />

      <Card>
        <CardContent className="divide-y p-0">
          {providerList.map((p) => (
            <div key={p.id} className="flex items-center gap-4 p-5">
              <div className="flex size-10 shrink-0 items-center justify-center rounded-lg bg-muted font-semibold">
                {p.name[0]}
              </div>

              <div className="min-w-0 flex-1">
                <div className="flex flex-wrap items-center gap-2 font-medium">
                  {p.name}
                  {p.linked ? (
                    <Badge
                      variant="outline"
                      className="gap-1 border-green-200 bg-green-50 text-green-700 dark:border-green-900 dark:bg-green-950 dark:text-green-400"
                    >
                      <CircleCheck className="size-3" /> Connected
                    </Badge>
                  ) : (
                    <Badge variant="secondary">Not linked</Badge>
                  )}
                </div>
                <p className="truncate text-sm text-muted-foreground">
                  {p.linked
                    ? p.account
                    : `Link your ${p.name} account to enable SSO sign-in.`}
                </p>
              </div>

              {p.linked ? (
                <Confirm
                  trigger={
                    <Button
                      variant="outline"
                      size="sm"
                      className="border-red-200 text-red-600 hover:bg-red-50 hover:text-red-700 dark:border-red-900 dark:hover:bg-red-950"
                    >
                      Unlink
                    </Button>
                  }
                  title={`Unlink ${p.name}?`}
                  description={`You will no longer be able to sign in with your ${p.name} account.`}
                  action="Unlink"
                  destructive
                  onConfirm={() => handleUnlink(p.name, p.identityUuid)}
                />
              ) : (
                <Button
                  size="sm"
                  onClick={() => handleConnect(p.id)}
                  className="gap-1.5"
                >
                  Connect <ExternalLink className="size-3.5" />
                </Button>
              )}
            </div>
          ))}
        </CardContent>
      </Card>
    </>
  )
}

export default LinkedAccountsPage