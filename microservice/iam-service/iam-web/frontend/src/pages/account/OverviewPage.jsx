import { Link } from "react-router-dom"
import { ChevronRight } from "lucide-react"
import { Card, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { PageHeader } from "./shared"

export function OverviewPage() {
  const cards = [
    { title: "Profile", desc: "Name, email, phone and photo", to: "/account/profile" },
    { title: "Password & security", desc: "Change or reset your password", to: "/account/security" },
    { title: "Linked accounts", desc: "2 of 4 providers connected", to: "/account/linked" },
    { title: "Active sessions", desc: "3 devices signed in", to: "/account/sessions" },
  ]
  return (
    <>
      <PageHeader title="My account" description="Manage your identity, security and devices." />
      <div className="grid gap-4 sm:grid-cols-2">
        {cards.map((c) => (
          <Link key={c.to} to={c.to}>
            <Card className="h-full transition-colors hover:bg-muted/50">
              <CardHeader>
                <CardTitle className="flex items-center justify-between text-base">
                  {c.title}
                  <ChevronRight className="size-4 text-muted-foreground" />
                </CardTitle>
                <CardDescription>{c.desc}</CardDescription>
              </CardHeader>
            </Card>
          </Link>
        ))}
      </div>
    </>
  )
}

export default OverviewPage
