@param java.util.List<kh.edu.istad.iam.web.dto.ConnectedAppDto> apps
@param String errorMessage = null
@param String successMessage = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n

@template.layout.main(
    title = i18n.t("profile.connectedApps"),
    i18n = i18n,
    _csrf = _csrf,
    content = @`
        <div class="max-w-2xl mx-auto space-y-6">
            
            <nav class="flex items-center gap-2 text-xs font-medium text-slate-500">
                <a href="/profile" class="hover:text-blue-600 transition">${i18n.t("profile.title")}</a>
                <svg class="w-3.5 h-3.5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
                <span class="text-slate-800 font-semibold">${i18n.t("profile.connectedApps")}</span>
            </nav>

            <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/40 p-6 sm:p-8 space-y-6">
                
                <div class="flex items-start gap-4 pb-5 border-b border-slate-100">
                    <div class="w-11 h-11 rounded-xl bg-blue-50 border border-blue-100 flex items-center justify-center text-blue-600 shadow-xs flex-shrink-0">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                        </svg>
                    </div>
                    <div class="space-y-0.5">
                        <h1 class="text-xl sm:text-2xl font-bold tracking-tight text-slate-900">
                            ${i18n.t("profile.connectedApps")}
                        </h1>
                        <p class="text-xs sm:text-sm text-slate-500">
                            Applications you have granted access to your account data
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
                    @if(apps != null && !apps.isEmpty())
                        @for(var app : apps)
                            <div class="flex flex-col sm:flex-row sm:items-center justify-between p-4 bg-slate-50/70 border border-slate-200/80 rounded-xl gap-4">
                                <div>
                                    <div class="flex items-center gap-2">
                                        <span class="text-sm font-semibold text-slate-900">${app.clientName()}</span>
                                        <span class="px-2 py-0.5 rounded-md bg-slate-100 text-[11px] font-mono text-slate-600">${app.clientId()}</span>
                                    </div>
                                    <div class="flex flex-wrap gap-1.5 mt-2">
                                        @for(var sc : app.scopes())
                                            <span class="px-2 py-0.5 rounded bg-blue-50 text-blue-700 border border-blue-200/60 text-[10px] font-mono">${sc}</span>
                                        @endfor
                                    </div>
                                </div>

                                <form action="/profile/apps/${app.clientId()}/revoke" method="post" class="m-0 flex-shrink-0">
                                    @if(_csrf != null)
                                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                                    @endif
                                    <button type="submit" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium text-rose-600 hover:bg-rose-50 hover:text-rose-700 border border-rose-200 transition">
                                        <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                        </svg>
                                        <span>${i18n.t("profile.revoke", "Revoke")}</span>
                                    </button>
                                </form>
                            </div>
                        @endfor
                    @else
                        <div class="text-center py-8 text-slate-400 text-sm">
                            No authorized applications currently linked.
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

