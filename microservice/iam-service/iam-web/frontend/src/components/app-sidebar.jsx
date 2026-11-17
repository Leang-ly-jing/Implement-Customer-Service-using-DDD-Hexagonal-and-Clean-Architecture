
import { Link, useLocation } from "react-router-dom"
import { ShieldCheck } from "lucide-react"
import { useTranslation } from "react-i18next"

import { NavUser } from "@/components/nav-user"
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarRail,
} from "@/components/ui/sidebar"
import { NAV } from "@/config/account-nav"

export function AppSidebar({ user, isAdmin = false, onLogout, ...props }) {
  const { pathname } = useLocation()
  const { t } = useTranslation()

  return (
    <Sidebar collapsible="icon" {...props}>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton size="lg" render={<Link to="/account" />}>
              <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground">
                <ShieldCheck className="size-4" />
              </div>
              <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate">
                  <span className="font-semibold">ISTAD</span> | IAM
                </span>
              </div>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>

      <SidebarContent>
        {NAV.filter((s) => !s.adminOnly || Boolean(isAdmin)).map((section) => (
          <SidebarGroup key={section.label}>
            <SidebarGroupLabel className="uppercase tracking-wider">
              {section.labelKey ? t(section.labelKey) : section.label}
            </SidebarGroupLabel>
            <SidebarMenu>
              {section.items.map((item) => (
                <SidebarMenuItem key={item.url}>
                  <SidebarMenuButton
                    tooltip={item.titleKey ? t(item.titleKey) : item.title}
                    isActive={pathname === item.url}
                    render={<Link to={item.url} />}
                  >
                    <item.icon />
                    <span>{item.titleKey ? t(item.titleKey) : item.title}</span>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroup>
        ))}
      </SidebarContent>

      <SidebarFooter>
        <NavUser user={user} onLogout={onLogout} />
      </SidebarFooter>
      <SidebarRail />
    </Sidebar>
  )
}
