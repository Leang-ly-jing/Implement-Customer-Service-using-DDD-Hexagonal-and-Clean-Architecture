@param java.util.List<kh.edu.istad.iam.domain.dto.result.IdentityResult> identities
@param String errorMessage = null
@param String successMessage = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n

@template.layout.main(
    title = i18n.t("profile.linkedAccounts"),
    i18n = i18n,
    _csrf = _csrf,
    content = @`
        <div class="max-w-2xl mx-auto space-y-6">
            
            <nav class="flex items-center gap-2 text-xs font-medium text-slate-500">
                <a href="/profile" class="hover:text-blue-600 transition">${i18n.t("profile.title")}</a>
                <svg class="w-3.5 h-3.5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
                <span class="text-slate-800 font-semibold">${i18n.t("profile.linkedAccounts")}</span>
            </nav>

            <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/40 p-6 sm:p-8 space-y-6">
                
                <div class="flex items-start gap-4 pb-5 border-b border-slate-100">
                    <div class="w-11 h-11 rounded-xl bg-blue-50 border border-blue-100 flex items-center justify-center text-blue-600 shadow-xs flex-shrink-0">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                        </svg>
                    </div>
                    <div class="space-y-0.5">
                        <h1 class="text-xl sm:text-2xl font-bold tracking-tight text-slate-900">
                            ${i18n.t("profile.linkedAccounts")}
                        </h1>
                        <p class="text-xs sm:text-sm text-slate-500">
                            Manage third-party social logins connected to your unified identity
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

                @if(successMessage != null)
                    <div class="flex items-start gap-3 p-3.5 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm">
                        <svg class="w-5 h-5 flex-shrink-0 mt-0.5 text-emerald-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"/>
                        </svg>
                        <span class="leading-relaxed font-medium">${successMessage}</span>
                    </div>
                @endif

                <div class="space-y-3">
                    @if(identities != null && !identities.isEmpty())
                        @for(var idn : identities)
                            <div class="flex items-center justify-between p-4 bg-slate-50/70 border border-slate-200/80 rounded-xl">
                                <div class="flex items-center gap-3.5">
                                    <div class="w-10 h-10 rounded-xl bg-white border border-slate-200 flex items-center justify-center font-bold text-slate-700 shadow-xs">
                                        ${idn.provider() != null && !idn.provider().isEmpty() ? idn.provider().substring(0, 1).toUpperCase() : "S"}
                                    </div>
                                    <div>
                                        <div class="flex items-center gap-2">
                                            <span class="text-sm font-semibold text-slate-900">${idn.provider() != null ? idn.provider() : "Identity"}</span>
                                            <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200/70">Connected</span>
                                        </div>
                                        <p class="text-xs text-slate-500 font-mono mt-0.5">${idn.displayName() != null ? idn.displayName() : (idn.email() != null ? idn.email() : "")}</p>
                                    </div>
                                </div>

                                <form action="/profile/accounts/${idn.uuid().toString()}/unlink" method="post" class="m-0">
                                    @if(_csrf != null)
                                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                                    @endif
                                    <button type="submit" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium text-rose-600 hover:bg-rose-50 hover:text-rose-700 border border-rose-200 transition">
                                        <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                        </svg>
                                        <span>${i18n.t("profile.unlink", "Unlink")}</span>
                                    </button>
                                </form>
                            </div>
                        @endfor
                    @else
                        <div class="text-center py-8 text-slate-400 text-sm">
                            No third-party accounts currently linked.
                        </div>
                    @endif
                </div>

                <div class="pt-4 border-t border-slate-100 flex justify-end">
                    <a href="/profile" class="inline-flex items-center justify-center px-4 py-2.5 bg-white border border-slate-200 rounded-xl text-xs sm:text-sm font-medium text-slate-700 hover:bg-slate-50 hover:border-slate-300 transition shadow-xs">
                        Back to Profile
                    </a>
                </div>

            </div>
        </div>
    `
)
