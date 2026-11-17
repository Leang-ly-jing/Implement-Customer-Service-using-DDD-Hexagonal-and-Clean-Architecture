import { useState, useEffect } from "react"
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom"
import { LogOut } from "lucide-react"
import { useTranslation } from "react-i18next"
import axiosClient from "@/api/axiosClient"
import { AppSidebar } from "@/components/app-sidebar"
import { CommandSearch, LanguageToggle, ThemeToggle } from "@/components/header-actions"
import { Button } from "@/components/ui/button"
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb"
import { Separator } from "@/components/ui/separator"
import { SidebarInset, SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar"
import { ALL_ITEMS } from "@/config/account-nav"
import { hasAdminRole } from "@/lib/auth-utils"

export default function AccountLayout() {
  const { pathname } = useLocation()
  const navigate = useNavigate()
  const { t } = useTranslation()
  const current = ALL_ITEMS.find((i) => i.url === pathname)

  const [user, setUser] = useState({
    name: "",
    email: "",
    avatar: localStorage.getItem("user_avatar") || "",
  })

  const [isAdmin, setIsAdmin] = useState(() => hasAdminRole())

  useEffect(() => {
    axiosClient
      .get("/me")
      .then((res) => {
        const u = res.data
        const adminStatus = hasAdminRole(u.roles)
        setIsAdmin(adminStatus)

        try {
          const stored = JSON.parse(localStorage.getItem("user") || "{}")
          const roleNames = (u.roles || []).map((r) => (typeof r === "string" ? r : r.name))
          localStorage.setItem(
            "user",
            JSON.stringify({
              ...stored,
              username: u.username,
              roles: roleNames,
            })
          )
        } catch {
           console.error("Failed to update user in localStorage")
        }

        setUser({
          name: u.name || u.username,
          email: u.email,
          avatar: u.pictureUrl || localStorage.getItem("user_avatar") || "",
        })
      })
      .catch(() => {
        localStorage.removeItem("access_token")
        localStorage.removeItem("user")
        navigate("/login")
      })
  }, [navigate])

  useEffect(() => {
    if (!isAdmin && pathname.startsWith("/admin")) {
      navigate("/account/profile", { replace: true })
    }
  }, [isAdmin, pathname, navigate])

  // const onLogout = () => {
  //   localStorage.removeItem("access_token")
  //   localStorage.removeItem("user")
  //   localStorage.removeItem("user_avatar")
  //   window.location.assign("/login")
  // }

  const onLogout = () => {
  localStorage.removeItem("access_token")
  localStorage.removeItem("user")
  localStorage.removeItem("user_avatar")
  window.location.href = "/logout"
}

  return (
    <SidebarProvider>
      <AppSidebar user={user} isAdmin={isAdmin} onLogout={onLogout} />
      <SidebarInset>
        <header className="sticky top-0 z-10 flex h-16 shrink-0 items-center gap-2 border-b bg-background px-4">
          <SidebarTrigger className="-ml-1" />
          <Separator orientation="vertical" className="mr-2 data-[orientation=vertical]:h-4" />
          <Breadcrumb>
            <BreadcrumbList>
              <BreadcrumbItem className="hidden md:block">
                <BreadcrumbLink render={<Link to={current?.sectionUrl ?? "/account"} />}>
                  {current?.sectionKey ? t(current.sectionKey) : (current?.section ?? t("nav.myAccount"))}
                </BreadcrumbLink>
              </BreadcrumbItem>
              <BreadcrumbSeparator className="hidden md:block" />
              <BreadcrumbItem>
                <BreadcrumbPage>
                  {current?.titleKey ? t(current.titleKey) : (current?.title ?? t("nav.overview"))}
                </BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>

          <div className="ml-auto flex items-center gap-2">
            <CommandSearch isAdmin={isAdmin} />
            <LanguageToggle />
            <ThemeToggle />
            <Button
              variant="outline"
              size="sm"
              onClick={onLogout}
              className="gap-1.5 text-rose-600 hover:text-rose-700 hover:bg-rose-50 border-rose-200 shadow-2xs"
              title={t("common.logout")}
            >
              <LogOut className="size-4" />
              <span className="hidden sm:inline font-medium">{t("common.logout")}</span>
            </Button>
          </div>
        </header>

        <main className={`mx-auto w-full p-6 ${pathname.startsWith("/admin") ? "max-w-6xl" : "max-w-3xl"}`}>
          <Outlet />
        </main>
      </SidebarInset>
    </SidebarProvider>
  )
}
