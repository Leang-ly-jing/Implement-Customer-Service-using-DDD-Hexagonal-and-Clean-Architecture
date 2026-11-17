import { LogOut } from "lucide-react"
import { useNavigate } from "react-router-dom"
import { useTranslation } from "react-i18next"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import {
  SidebarMenu,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar"

const initials = (name) =>
  name ? name.split(" ").map((w) => w[0]).slice(0, 2).join("").toUpperCase() : "U"

export function NavUser({ user, onLogout }) {
  const { isMobile, state } = useSidebar()
  const navigate = useNavigate()
  const {  i18n } = useTranslation()
  const isKhmer = (i18n.language || "kh").startsWith("kh") || (i18n.language || "").startsWith("km")

  const isCollapsed = state === "collapsed" && !isMobile

  if (isCollapsed) {
    return (
      <SidebarMenu>
        <SidebarMenuItem className="flex justify-center">
          <Button
            variant="ghost"
            size="icon"
            onClick={onLogout}
            title={isKhmer ? "ចាកចេញ (Log out)" : "Log out"}
            className="size-8 text-rose-600 hover:text-rose-700 hover:bg-rose-50 rounded-lg"
          >
            <LogOut className="size-4" />
          </Button>
        </SidebarMenuItem>
      </SidebarMenu>
    )
  }

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <div className="flex w-full items-center justify-between gap-2 rounded-lg p-2 hover:bg-sidebar-accent transition-colors border border-border/40 bg-sidebar/50">
          <div
            onClick={() => navigate("/account/profile")}
            className="flex flex-1 items-center gap-2 overflow-hidden cursor-pointer group"
            title={isKhmer ? "មើលប្រវត្តិរូប" : "View Profile"}
          >
            <Avatar className="h-8 w-8 rounded-lg shrink-0">
              <AvatarImage src={user?.avatar} alt={user?.name} />
              <AvatarFallback className="rounded-lg bg-primary/10 text-primary font-semibold text-xs">
                {initials(user?.name)}
              </AvatarFallback>
            </Avatar>
            <div className="grid flex-1 text-left text-sm leading-tight min-w-0">
              <span className="truncate font-medium text-foreground group-hover:text-primary transition-colors text-xs">
                {user?.name || "User"}
              </span>
              <span className="truncate text-[11px] text-muted-foreground">
                {user?.email || "-"}
              </span>
            </div>
          </div>

          <Button
            variant="ghost"
            size="icon"
            onClick={onLogout}
            title={isKhmer ? "ចាកចេញ (Log out)" : "Log out"}
            className="size-8 shrink-0 text-rose-600 hover:text-rose-700 hover:bg-rose-100/80 rounded-md transition-colors"
          >
            <LogOut className="size-4" />
          </Button>
        </div>
      </SidebarMenuItem>
    </SidebarMenu>
  )
}

export default NavUser
