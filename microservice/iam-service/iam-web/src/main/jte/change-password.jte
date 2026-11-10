@param String errorMessage = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n

@template.layout.main(
    title = i18n.t("profile.changePassword"),
    i18n = i18n,
    _csrf = _csrf,
    content = @`
        <div class="max-w-xl mx-auto space-y-6">
            
            <nav class="flex items-center gap-2 text-xs font-medium text-slate-500">
                <a href="/profile" class="hover:text-blue-600 transition">${i18n.t("profile.title")}</a>
                <svg class="w-3.5 h-3.5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
                <span class="text-slate-800 font-semibold">${i18n.t("profile.changePassword")}</span>
            </nav>

            <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/40 p-6 sm:p-8 space-y-6">
                
                <div class="flex items-start gap-4 pb-5 border-b border-slate-100">
                    <div class="w-11 h-11 rounded-xl bg-blue-50 border border-blue-100 flex items-center justify-center text-blue-600 shadow-xs flex-shrink-0">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                        </svg>
                    </div>
                    <div class="space-y-0.5">
                        <h1 class="text-xl sm:text-2xl font-bold tracking-tight text-slate-900">
                            ${i18n.t("profile.changePassword")}
                        </h1>
                        <p class="text-xs sm:text-sm text-slate-500">
                            ${i18n.t("profile.passwordSubtitle", "Ensure your account is using a long, random password to stay secure")}
                        </p>
                    </div>
                </div>

                @if(errorMessage != null)
                    <div class="flex items-start gap-3 p-3.5 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm">
                        <svg class="w-5 h-5 flex-shrink-0 mt-0.5 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                        </svg>
                        <span class="leading-relaxed font-medium">${errorMessage}</span>
                    </div>
                @endif

                <form action="/profile/password" method="post" class="space-y-5">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                    @endif
                    
                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("profile.currentPassword")}
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                                </svg>
                            </div>
                            <input 
                                type="password" 
                                name="currentPassword" 
                                autocomplete="current-password"
                                placeholder="••••••••"
                                class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                            >
                        </div>
                    </div>

                    <div class="h-px bg-slate-100 my-2"></div>

                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("profile.newPassword")}
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                                </svg>
                            </div>
                            <input 
                                type="password" 
                                name="newPassword" 
                                required 
                                autocomplete="new-password"
                                placeholder="••••••••"
                                class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                            >
                        </div>
                    </div>

                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("profile.confirmNewPassword")}
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                </svg>
                            </div>
                            <input 
                                type="password" 
                                name="confirmPassword" 
                                required 
                                autocomplete="new-password"
                                placeholder="••••••••"
                                class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                            >
                        </div>
                    </div>

                    <div class="p-3.5 bg-slate-50 border border-slate-200/70 rounded-xl space-y-1.5 text-xs text-slate-500">
                        <span class="font-medium text-slate-700">${i18n.t("profile.passwordRules.title", "Password requirements:")}</span>
                        <ul class="list-disc list-inside space-y-0.5 text-slate-500">
                            <li>${i18n.t("profile.passwordRules.length", "At least 8 characters long")}</li>
                            <li>${i18n.t("profile.passwordRules.complexity", "Contains uppercase, lowercase, numbers, or symbols")}</li>
                        </ul>
                    </div>

                    <div class="flex items-center justify-end gap-3 pt-4 border-t border-slate-100">
                        <a 
                            href="/profile" 
                            class="inline-flex items-center justify-center px-4 py-2.5 bg-white border border-slate-200 rounded-xl text-xs sm:text-sm font-medium text-slate-700 hover:bg-slate-50 hover:border-slate-300 focus:outline-none focus:ring-2 focus:ring-slate-300 transition shadow-xs"
                        >
                            ${i18n.t("common.cancel")}
                        </a>
                        <button 
                            type="submit" 
                            class="inline-flex items-center justify-center gap-1.5 px-5 py-2.5 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium text-xs sm:text-sm rounded-xl shadow-md shadow-blue-500/20 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition"
                        >
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                            </svg>
                            <span>${i18n.t("common.save")}</span>
                        </button>
                    </div>

                </form>
            </div>
        </div>
    `
)
