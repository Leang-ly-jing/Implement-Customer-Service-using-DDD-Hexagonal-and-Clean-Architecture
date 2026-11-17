import {
  Activity,
  AppWindow,
  FileText,
  History,
  KeyRound,
  LayoutGrid,
  Link2,
  Lock,
  MonitorSmartphone,
  ShieldAlert,
  ShieldCheck,
  User,
  UserX,
  Users,
} from "lucide-react"

export const NAV = [
  {
    label: "My account",
    labelKey: "nav.myAccount",
    items: [
      { title: "Overview", titleKey: "nav.overview", url: "/account", icon: LayoutGrid },
      { title: "Profile", titleKey: "nav.profile", url: "/account/profile", icon: User },
      { title: "Password & security", titleKey: "nav.security", url: "/account/security", icon: Lock },
      { title: "Linked accounts", titleKey: "nav.linkedAccounts", url: "/account/linked", icon: Link2 },
      { title: "Active sessions", titleKey: "nav.sessions", url: "/account/sessions", icon: MonitorSmartphone },
      { title: "Login history", titleKey: "nav.loginHistory", url: "/account/login-history", icon: History },
      { title: "Account activity", titleKey: "nav.activity", url: "/account/activity", icon: Activity },
      { title: "Deactivation", titleKey: "nav.deactivation", url: "/account/deactivate", icon: UserX },
    ],
  },
  {
    label: "Administration",
    labelKey: "nav.administration",
    adminOnly: true,
    items: [
      { title: "Admin overview", titleKey: "nav.adminOverview", url: "/admin", icon: LayoutGrid },
      { title: "Users", titleKey: "nav.users", url: "/admin/users", icon: Users },
      { title: "Login history", titleKey: "nav.loginHistory", url: "/admin/login-history", icon: ShieldAlert },
      { title: "Roles", titleKey: "nav.roles", url: "/admin/roles", icon: ShieldCheck },
      { title: "Permissions", titleKey: "nav.permissions", url: "/admin/permissions", icon: KeyRound },
      { title: "Applications", titleKey: "nav.applications", url: "/admin/applications", icon: AppWindow },
      { title: "Audit log", titleKey: "nav.auditLog", url: "/admin/audit-log", icon: FileText },
    ],
  },
]

export const ALL_ITEMS = NAV.flatMap((s) =>
  s.items.map((i) => ({ ...i, section: s.label, sectionKey: s.labelKey, sectionUrl: s.items[0].url }))
)
