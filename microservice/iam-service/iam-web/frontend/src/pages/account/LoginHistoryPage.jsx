import { useState, useEffect } from "react"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "./shared"

export function LoginHistoryPage() {
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let isMounted = true

    const fetchHistory = async () => {
      try {
        const res = await axiosClient.get("/me/login-history")
        if (isMounted) {
          setHistory(res.data || [])
        }
      } catch {
        if (isMounted) {
          toast.error("Failed to load history", "Could not retrieve login history.")
        }
      } finally {
        if (isMounted) {
          setLoading(false)
        }
      }
    }

    fetchHistory()

    return () => {
      isMounted = false
    }
  }, [])

  const formatTimestamp = (isoString) => {
    if (!isoString) return "-"
    const date = new Date(isoString)
    return date.toLocaleString(undefined, {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    })
  }

  return (
    <>
      <PageHeader
        title="Login history"
        description="Successful and failed login attempts with device information."
      />

      <Card>
        <CardContent className="pt-6">
          {loading ? (
            <div className="flex h-40 items-center justify-center">
              <p className="text-sm text-muted-foreground">Loading login history...</p>
            </div>
          ) : history.length === 0 ? (
            <div className="flex h-40 items-center justify-center">
              <p className="text-sm text-muted-foreground">No login history recorded yet.</p>
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Status</TableHead>
                  <TableHead>Time</TableHead>
                  <TableHead>Device / Browser</TableHead>
                  <TableHead>IP Address</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {history.map((item) => (
                  <TableRow key={item.id}>
                    <TableCell>
                      <Badge variant={item.success ? "secondary" : "destructive"}>
                        {item.success ? "Success" : "Failed"}
                      </Badge>
                      {!item.success && item.failureReason && (
                        <span className="ml-2 block text-xs text-muted-foreground">
                          {item.failureReason}
                        </span>
                      )}
                    </TableCell>
                    <TableCell>{formatTimestamp(item.loginAt)}</TableCell>
                    <TableCell>{item.device || "Unknown Device"}</TableCell>
                    <TableCell className="font-mono text-xs">{item.ipAddress || "-"}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </>
  )
}

export default LoginHistoryPage
