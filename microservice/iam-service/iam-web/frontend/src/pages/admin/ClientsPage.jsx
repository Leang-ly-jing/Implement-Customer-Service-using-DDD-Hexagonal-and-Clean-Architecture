import { useState, useEffect } from "react"
import { Plus, AppWindow } from "lucide-react"
import { useTranslation } from "react-i18next"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/toast"
import axiosClient from "@/api/axiosClient"
import { PageHeader } from "../account/shared"
import { ClientsTable } from "./client/ClientsTable"
import { ClientCreateModal } from "./client/ClientCreateModal"
import { ClientEditModal } from "./client/ClientEditModal"
import { ClientSecretModal } from "./client/ClientSecretModal"
import { ClientRegenSecretDialog } from "./client/ClientRegenSecretDialog"
import { ClientDeleteDialog } from "./client/ClientDeleteDialog"

export default function ClientsPage() {
  const [clients, setClients] = useState([])
  const [pageMeta, setPageMeta] = useState({ number: 0, size: 10, totalElements: 0, totalPages: 0 })
  const [loading, setLoading] = useState(true)
  const [currentPage, setCurrentPage] = useState(0)

  const [createOpen, setCreateOpen] = useState(false)
  const [createSubmitting, setCreateSubmitting] = useState(false)

  const [editOpen, setEditOpen] = useState(false)
  const [editingClient, setEditingClient] = useState(null)
  const [editLoading, setEditLoading] = useState(false)
  const [editSubmitting, setEditSubmitting] = useState(false)

  const [deleteOpen, setDeleteOpen] = useState(false)
  const [deletingClient, setDeletingClient] = useState(null)
  const [deleteSubmitting, setDeleteSubmitting] = useState(false)

  const [regenOpen, setRegenOpen] = useState(false)
  const [regenTarget, setRegenTarget] = useState(null)
  const [regenSubmitting, setRegenSubmitting] = useState(false)

  const [secretModalOpen, setSecretModalOpen] = useState(false)
  const [revealedSecretData, setRevealedSecretData] = useState({ clientId: "", rawSecret: "" })

  const { i18n } = useTranslation()
  const rawLang = i18n?.language || localStorage.getItem("lang") || "kh"
  const isKhmer = rawLang.startsWith("kh") || rawLang.startsWith("km")
  const lang = isKhmer ? "km" : "en"

  const fetchClients = async (page = 0) => {
    setLoading(true)
    try {
      const url = `/admin/clients?page=${page}&size=10`
      const res = await axiosClient.get(url)
      setClients(res.data?.content || [])
      setPageMeta(res.data?.page || { number: 0, size: 10, totalElements: 0, totalPages: 0 })
      setCurrentPage(page)
    } catch (err) {
      console.error("Failed to fetch clients:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Could not retrieve OAuth2 clients."
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchClients(0)
  }, [])

  const handleCreateSubmit = async (form) => {
    if (!form.clientId.trim() || !form.clientName.trim()) {
      toast.error(
        lang === "km" ? "កំហុស" : "Validation",
        lang === "km" ? "Client ID និង Client Name ត្រូវតែបំពេញ" : "Client ID and Client Name are required."
      )
      return
    }
    setCreateSubmitting(true)
    try {
      const res = await axiosClient.post("/admin/clients", {
        clientId: form.clientId.trim(),
        clientName: form.clientName.trim(),
        clientSecret: form.clientSecret.trim() || undefined,
        clientAuthenticationMethods: form.clientAuthenticationMethods,
        authorizationGrantTypes: form.authorizationGrantTypes,
        redirectUris: form.redirectUris,
        scopes: form.scopes,
        requireProofKey: form.requireProofKey,
        requireAuthorizationConsent: form.requireAuthorizationConsent,
        accessTokenTimeToLiveSeconds: form.accessTokenTimeToLiveSeconds,
        refreshTokenTimeToLiveSeconds: form.refreshTokenTimeToLiveSeconds,
      })

      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Created",
        lang === "km" ? "បានចុះឈ្មោះ OAuth2 Client ដោយជោគជ័យ" : "Client registered successfully."
      )
      setCreateOpen(false)
      fetchClients(0)

      if (res.data?.rawSecret) {
        setRevealedSecretData({
          clientId: res.data.clientId,
          rawSecret: res.data.rawSecret,
        })
        setSecretModalOpen(true)
      }
    } catch (err) {
      console.error("Create client error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to register client."
      )
    } finally {
      setCreateSubmitting(false)
    }
  }

  const openEditModal = async (client) => {
    setEditingClient(client)
    setEditOpen(true)
    setEditLoading(true)
    try {
      const res = await axiosClient.get(`/admin/clients/${client.id}`)
      setEditingClient(res.data)
    } catch (err) {
      console.error("Failed to load client details:", err)
      toast.error(lang === "km" ? "បរាជ័យ" : "Error", "Could not load client details.")
    } finally {
      setEditLoading(false)
    }
  }

  const handleEditSubmit = async (form) => {
    if (!editingClient) return
    setEditSubmitting(true)
    try {
      await axiosClient.put(`/admin/clients/${editingClient.id}`, form)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Updated",
        lang === "km" ? "បានកែប្រែព័ត៌មាន Client រួចរាល់" : "Client configuration updated."
      )
      setEditOpen(false)
      fetchClients(currentPage)
    } catch (err) {
      console.error("Update client error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to update client."
      )
    } finally {
      setEditSubmitting(false)
    }
  }

  const handleRegenConfirm = async () => {
    if (!regenTarget) return
    setRegenSubmitting(true)
    try {
      const res = await axiosClient.post(`/admin/clients/${regenTarget.id}/regenerate-secret`)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Generated",
        lang === "km" ? "បានបង្កើត Client Secret ថ្មីរួចរាល់" : "Client secret regenerated."
      )
      setRegenOpen(false)
      setRevealedSecretData({
        clientId: res.data.clientId,
        rawSecret: res.data.rawSecret,
      })
      setSecretModalOpen(true)
    } catch (err) {
      console.error("Regenerate secret error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to regenerate secret."
      )
    } finally {
      setRegenSubmitting(false)
    }
  }

  const handleDeleteConfirm = async () => {
    if (!deletingClient) return
    setDeleteSubmitting(true)
    try {
      await axiosClient.delete(`/admin/clients/${deletingClient.id}`)
      toast.success(
        lang === "km" ? "ជោគជ័យ" : "Deleted",
        lang === "km" ? "បានលុប Client ដោយជោគជ័យ" : "Client deleted successfully."
      )
      setDeleteOpen(false)
      fetchClients(currentPage)
    } catch (err) {
      console.error("Delete client error:", err)
      toast.error(
        lang === "km" ? "បរាជ័យ" : "Error",
        err.response?.data?.detail || "Failed to delete client."
      )
    } finally {
      setDeleteSubmitting(false)
    }
  }

  return (
    <div className="space-y-6">

      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <PageHeader
          title={lang === "km" ? "OAuth2 / OIDC Clients" : "Client Management"}
          description={
            lang === "km"
              ? "គ្រប់គ្រង OAuth2/OIDC Clients ចុះឈ្មោះ កែប្រែ credentials, grant types, redirect URIs និង scopes សម្រាប់ការភ្ជាប់ជាមួយ Authorization Server"
              : "Register, view, update, and delete OAuth2/OIDC clients, configure grant types, redirect URIs, and scopes."
          }
        />
        <div className="flex items-center gap-2">
          <Button size="sm" onClick={() => setCreateOpen(true)} className="gap-1.5">
            <Plus className="size-4" />
            <span>{lang === "km" ? "ចុះឈ្មោះ Client" : "Register Client"}</span>
          </Button>
        </div>
      </div>

      <ClientsTable
        clients={clients}
        pageMeta={pageMeta}
        loading={loading}
        currentPage={currentPage}
        onPageChange={fetchClients}
        onOpenEdit={openEditModal}
        onOpenRegenSecret={(c) => {
          setRegenTarget(c)
          setRegenOpen(true)
        }}
        onOpenDelete={(c) => {
          setDeletingClient(c)
          setDeleteOpen(true)
        }}
        lang={lang}
      />

      <ClientCreateModal
        isOpen={createOpen}
        onClose={() => setCreateOpen(false)}
        onSubmit={handleCreateSubmit}
        submitting={createSubmitting}
        lang={lang}
      />

      <ClientEditModal
        isOpen={editOpen}
        onClose={() => setEditOpen(false)}
        client={editingClient}
        loading={editLoading}
        onSubmit={handleEditSubmit}
        submitting={editSubmitting}
        lang={lang}
      />

      <ClientSecretModal
        isOpen={secretModalOpen}
        onClose={() => setSecretModalOpen(false)}
        data={revealedSecretData}
        lang={lang}
      />

      <ClientRegenSecretDialog
        isOpen={regenOpen}
        onClose={() => setRegenOpen(false)}
        client={regenTarget}
        onConfirm={handleRegenConfirm}
        submitting={regenSubmitting}
        lang={lang}
      />

      <ClientDeleteDialog
        isOpen={deleteOpen}
        onClose={() => setDeleteOpen(false)}
        client={deletingClient}
        onConfirm={handleDeleteConfirm}
        submitting={deleteSubmitting}
        lang={lang}
      />
    </div>
  )
}
