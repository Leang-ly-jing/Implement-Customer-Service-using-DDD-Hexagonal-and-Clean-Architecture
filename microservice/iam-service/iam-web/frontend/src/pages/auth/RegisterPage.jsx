import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import { useTranslation } from "react-i18next"
import { authService } from "../../api/authService"
import { toast } from "@/components/ui/toast"

export default function RegisterPage() {
  const { t, i18n } = useTranslation()
  const currentLang = (i18n.language || "kh").toLowerCase()
  const isKh = currentLang.startsWith("kh") || currentLang.startsWith("km")

  const [formData, setFormData] = useState({
    familyName: "",
    givenName: "",
    userName: "",
    email: "",
    password: "",
    confirmPassword: "",
    locate: isKh ? "kh" : "en",
  })
  const [error, setError] = useState("")
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleLanguageChange = (newLang) => {
    const normalized = (newLang || "kh").toLowerCase()
    i18n.changeLanguage(normalized)
    localStorage.setItem("lang", normalized)
    setFormData((prev) => ({ ...prev, locate: normalized }))
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    if (formData.password !== formData.confirmPassword) {
      setError(t("auth.passwordMismatch"))
      return
    }

    setLoading(true)

    try {
      await authService.register(formData)
      const successTitle = isKh ? "ការចុះឈ្មោះបានជោគជ័យ" : "Registration Successful"
      const successMessage = t("auth.registerSuccess")
      toast.success(successTitle, successMessage)
      navigate("/login", {
        replace: true,
        state: {
          registered: true,
          username: formData.userName,
          message: successMessage,
        },
      })
    } catch (err) {
      setError(
        err.response?.data?.message ||
        err.response?.data?.detail ||
        (isKh ? "ការចុះឈ្មោះបានបរាជ័យ" : "Registration failed.")
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col justify-center items-center p-4 sm:p-6 font-sans relative selection:bg-blue-100 selection:text-blue-700">
      <div className="fixed inset-0 pointer-events-none -z-10 subtle-grid"></div>
      <div className="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-4xl h-80 bg-gradient-to-b from-blue-100/50 via-blue-50/20 to-transparent blur-3xl pointer-events-none -z-10"></div>

      <div className="w-full max-w-lg">
        <div className="flex items-center justify-between mb-6 px-1">
          <Link to="/" className="flex items-center gap-2 group">
            <img
              src="https://www.istad.co/icon.png"
              alt="ISTAD Logo"
              className="w-8 h-8 object-contain rounded-full transition group-hover:scale-105"
            />
            <span className="font-bold text-slate-900 group-hover:text-blue-600 transition text-sm">
              ISTAD IAM
            </span>
          </Link>

          <div className="flex items-center bg-white/80 backdrop-blur-sm p-0.5 rounded-lg border border-slate-200/80 shadow-xs text-xs font-medium text-slate-600">
            <button
              type="button"
              onClick={() => handleLanguageChange("kh")}
              className={`px-2.5 py-1 rounded-md transition ${isKh ? "bg-slate-100 text-slate-900 font-semibold" : "hover:text-slate-900 hover:bg-slate-100"}`}
            >
              ខ្មែរ (KH)
            </button>
            <span className="text-slate-300">|</span>
            <button
              type="button"
              onClick={() => handleLanguageChange("en")}
              className={`px-2.5 py-1 rounded-md transition ${!isKh ? "bg-slate-100 text-slate-900 font-semibold" : "hover:text-slate-900 hover:bg-slate-100"}`}
            >
              EN
            </button>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-xl shadow-slate-200/50 border border-slate-200/80 p-6 sm:p-8 backdrop-blur-xl">
          <div className="mb-6">
            <h1 className="text-xl sm:text-2xl font-bold text-slate-900 tracking-tight">
              {t("auth.registerTitle")}
            </h1>
            <p className="text-xs sm:text-sm text-slate-500 mt-1">{t("auth.registerSubtitle")}</p>
          </div>

          {error && (
            <div className="mb-5 p-3 rounded-xl bg-red-50 border border-red-200 text-red-600 text-xs sm:text-sm flex items-start gap-2.5 animate-shake">
              <svg
                className="w-4 h-4 shrink-0 mt-0.5"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                />
              </svg>
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <label className="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                  {t("auth.familyName")}
                </label>
                <input
                  type="text"
                  name="familyName"
                  value={formData.familyName}
                  onChange={handleChange}
                  required
                  placeholder="Sok"
                  className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                />
              </div>

              <div className="space-y-1.5">
                <label className="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                  {t("auth.givenName")}
                </label>
                <input
                  type="text"
                  name="givenName"
                  value={formData.givenName}
                  onChange={handleChange}
                  required
                  placeholder="Dara"
                  className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                />
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                {t("auth.username")}
              </label>
              <input
                type="text"
                name="userName"
                value={formData.userName}
                onChange={handleChange}
                required
                minLength={3}
                maxLength={50}
                placeholder="sokdara"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
              />
            </div>

            <div className="space-y-1.5">
              <label className="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                {t("auth.email")}
              </label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                placeholder="dara@example.com"
                className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
              />
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <label className="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                  {t("auth.password")}
                </label>
                <input
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  minLength={8}
                  placeholder="••••••••"
                  className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                />
              </div>

              <div className="space-y-1.5">
                <label className="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                  {t("auth.confirmPassword")}
                </label>
                <input
                  type="password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  required
                  placeholder="••••••••"
                  className="w-full px-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full mt-3 py-2.5 px-4 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium text-sm rounded-xl shadow-md shadow-blue-500/20 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition disabled:opacity-60 cursor-pointer"
            >
              {loading ? t("auth.signingUp") : t("auth.createAccount")}
            </button>
          </form>

          <div className="mt-6 text-center text-xs text-slate-500">
            {t("auth.haveAccount")}{" "}
            <Link to="/login" className="font-semibold text-blue-600 hover:text-blue-700 hover:underline">
              {t("auth.signIn")}
            </Link>
          </div>
        </div>

        <div className="mt-8 text-center text-xs text-slate-400">
          &copy; {new Date().getFullYear()} ISTAD IAM. {t("auth.copyright")}
        </div>
      </div>
    </div>
  )
}
