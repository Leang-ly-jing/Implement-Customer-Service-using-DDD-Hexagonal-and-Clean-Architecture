import { useState, useEffect } from "react"
import axiosClient from "@/api/axiosClient"
import { Card, CardContent, CardFooter } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import { PageHeader, Field } from "./shared"

export function ProfilePage() {
  const [form, setForm] = useState({
    givenName: "",
    familyName: "",
    username: "",
    email: "",
    phone: "",
  })
  const [avatar, setAvatar] = useState(() => localStorage.getItem("user_avatar") || "")
  const [avatarFile, setAvatarFile] = useState(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [loadError, setLoadError] = useState(false)

  useEffect(() => {
    const controller = new AbortController()

    const fetchProfile = async () => {
      try {
        const res = await axiosClient.get("/me", { signal: controller.signal })
        const user = res.data

        let fam = ""
        let giv = ""
        if (user.name) {
          const parts = user.name.trim().split(" ")
          fam = parts[0] || ""
          giv = parts.slice(1).join(" ") || ""
        }

        setForm({
          givenName: giv,
          familyName: fam,
          username: user.username || "",
          email: user.email || "",
          phone: user.phoneNumber || "",
        })

        if (user.pictureUrl) {
          setAvatar(user.pictureUrl)
        }
        setLoadError(false)
      } catch (err) {
        if (err.name !== "CanceledError" && err.name !== "AbortError") {
          setLoadError(true)
          toast.error(
            "Failed to load profile",
            err.response?.data?.message ?? "Could not retrieve user details"
          )
        }
      } finally {
        setLoading(false)
      }
    }

    fetchProfile()

    return () => {
      controller.abort()
    }
  }, [])

  useEffect(() => {
    return () => {
      if (avatar && avatar.startsWith("blob:")) {
        URL.revokeObjectURL(avatar)
      }
    }
  }, [avatar])

  const handleChange = (key) => (e) => {
    setForm({ ...form, [key]: e.target.value })
  }

  const handleImageChange = (e) => {
    const file = e.target.files?.[0]
    if (!file) return

    if (avatar && avatar.startsWith("blob:")) {
      URL.revokeObjectURL(avatar)
    }

    const objectUrl = URL.createObjectURL(file)
    setAvatarFile(file)
    setAvatar(objectUrl)

    const reader = new FileReader()
    reader.onload = () => {
      if (reader.result) {
        localStorage.setItem("user_avatar", reader.result)
      }
    }
    reader.readAsDataURL(file)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)

    try {
      if (avatarFile) {
        try {
          const fd = new FormData()
          fd.append("file", avatarFile)
          await axiosClient.post("/me/picture", fd)
        } catch {
        }
      }

      await axiosClient.patch("/me", {
        givenName: form.givenName,
        familyName: form.familyName,
        phoneNumber: form.phone,
      })

      toast.success("Profile updated", "Your personal information has been saved successfully.")
    } catch (err) {
      toast.error(
        "Update failed",
        err.response?.data?.message ?? "Failed to update profile information."
      )
    } finally {
      setSaving(false)
    }
  }

  const initials = [form.familyName, form.givenName]
    .filter(Boolean)
    .map((n) => n[0])
    .join("")
    .toUpperCase() || "US"

  if (loading) {
    return (
      <div className="flex h-48 items-center justify-center">
        <p className="text-sm text-muted-foreground">Loading profile...</p>
      </div>
    )
  }

  return (
    <form onSubmit={handleSave} className="space-y-6">
      <PageHeader title="Profile" description="View and update your personal information." />

      <Card>
        <CardContent className="space-y-6 pt-6">
          <div className="flex items-center gap-4">
            <Avatar className="size-20">
              <AvatarImage src={avatar} />
              <AvatarFallback>{initials}</AvatarFallback>
            </Avatar>
            <div className="space-y-2">
              <Label htmlFor="avatar">Profile picture</Label>
              <Input
                id="avatar"
                type="file"
                accept="image/*"
                onChange={handleImageChange}
              />
            </div>
          </div>

          <div className="grid gap-4 sm:grid-cols-2">
            <Field
              label="Family name"
              id="familyName"
              value={form.familyName}
              onChange={handleChange("familyName")}
            />
            <Field
              label="Given name"
              id="givenName"
              value={form.givenName}
              onChange={handleChange("givenName")}
            />
            <Field
              label="Username"
              id="username"
              value={form.username}
              disabled
            />
            <Field
              label="Email"
              id="email"
              type="email"
              value={form.email}
              disabled
            />
            <Field
              label="Phone number"
              id="phone"
              type="tel"
              value={form.phone}
              onChange={handleChange("phone")}
            />
          </div>
        </CardContent>

        <CardFooter className="justify-end">
          <Button type="submit" disabled={saving || loadError}>
            {saving ? "Saving..." : "Save changes"}
          </Button>
        </CardFooter>
      </Card>
    </form>
  )
}

export default ProfilePage
