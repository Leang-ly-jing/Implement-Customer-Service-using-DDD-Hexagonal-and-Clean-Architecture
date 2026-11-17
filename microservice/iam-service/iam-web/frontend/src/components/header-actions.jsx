import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { Moon, Search, Sun } from "lucide-react"
import { useTranslation } from "react-i18next"

import { Button } from "@/components/ui/button"
import {
  CommandDialog,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { NAV } from "@/config/account-nav"

export function CommandSearch({ isAdmin = false }) {
  const [open, setOpen] = useState(false)
  const navigate = useNavigate()
  const { t } = useTranslation()

  useEffect(() => {
    const down = (e) => {
      if (e.key === "k" && (e.metaKey || e.ctrlKey)) {
        e.preventDefault()
        setOpen((o) => !o)
      }
    }
    document.addEventListener("keydown", down)
    return () => document.removeEventListener("keydown", down)
  }, [])

  return (
    <>
      <Button
        variant="outline"
        className="w-9 justify-start gap-2 px-2.5 text-muted-foreground sm:w-64"
        onClick={() => setOpen(true)}
      >
        <Search className="size-4 shrink-0" />
        <span className="hidden flex-1 text-left font-normal sm:inline">
          {t("common.searchPlaceholder")}
        </span>
        <kbd className="hidden rounded border bg-muted px-1.5 text-[10px] sm:inline">
          ⌘K
        </kbd>
      </Button>

      <CommandDialog open={open} onOpenChange={setOpen}>
        <CommandInput placeholder={t("common.searchPlaceholder")} />
        <CommandList>
          <CommandEmpty>{t("common.noResults")}</CommandEmpty>
          {NAV.filter((s) => !s.adminOnly || Boolean(isAdmin)).map((s) => (
            <CommandGroup key={s.label} heading={s.labelKey ? t(s.labelKey) : s.label}>
              {s.items.map((i) => (
                <CommandItem
                  key={i.url}
                  onSelect={() => {
                    setOpen(false)
                    navigate(i.url)
                  }}
                >
                  <i.icon />
                  {i.titleKey ? t(i.titleKey) : i.title}
                </CommandItem>
              ))}
            </CommandGroup>
          ))}
        </CommandList>
      </CommandDialog>
    </>
  )
}

export function LanguageToggle() {
  const { i18n } = useTranslation()
  const currentLang = (i18n.language || "kh").toUpperCase()
  const isKhmer = currentLang.startsWith("KH") || currentLang.startsWith("KM")

  const pick = (l) => {
    const code = l.toLowerCase()
    i18n.changeLanguage(code)
    localStorage.setItem("lang", code)
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline" size="sm" className="font-semibold">
          {isKhmer ? "ខ្មែរ (KH)" : "EN"}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuItem onClick={() => pick("EN")}>English (EN)</DropdownMenuItem>
        <DropdownMenuItem onClick={() => pick("KH")}>ភាសាខ្មែរ (KH)</DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}

export function ThemeToggle() {
  const [dark, setDark] = useState(false)

  useEffect(() => {
    const stored = localStorage.getItem("theme")
    const isDark =
      stored === "dark" ||
      (!stored && window.matchMedia("(prefers-color-scheme: dark)").matches)
    document.documentElement.classList.toggle("dark", isDark)
    setDark(isDark)
  }, [])

  const toggle = () => {
    const next = !dark
    document.documentElement.classList.toggle("dark", next)
    localStorage.setItem("theme", next ? "dark" : "light")
    setDark(next)
  }

  return (
    <Button variant="outline" size="icon" onClick={toggle} aria-label="Toggle theme">
      {dark ? <Sun className="size-4" /> : <Moon className="size-4" />}
    </Button>
  )
}
