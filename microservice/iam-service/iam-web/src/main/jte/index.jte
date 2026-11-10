@param kh.edu.istad.iam.domain.dto.result.UserResult user
@param kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult authorities
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n
@param kh.edu.istad.iam.domain.entity.User currentUser = null

@template.layout.main(
    title = i18n.t("profile.title"),
    i18n = i18n,
    _csrf = _csrf,
    content = @`
        <div class="space-y-6">

            <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/40 p-6 sm:p-7 flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div class="flex items-center gap-4 sm:gap-5">
                    <div class="relative flex-shrink-0">
                        @if(currentUser != null && currentUser.getPictureUrl() != null && !currentUser.getPictureUrl().isBlank())
                            <img src="${currentUser.getPictureUrl()}" alt="${user.name() != null ? user.name().replaceAll("(?i)\\s+User$", "") : ""}" class="w-16 h-16 sm:w-20 sm:h-20 rounded-2xl object-cover ring-2 ring-white shadow-md shadow-blue-500/10">
                        @else
                            <div class="w-16 h-16 sm:w-20 sm:h-20 rounded-2xl bg-gradient-to-tr from-blue-600 via-indigo-600 to-sky-400 text-white flex items-center justify-center font-extrabold text-2xl sm:text-3xl shadow-lg shadow-blue-500/25 ring-2 ring-white select-none">
                                ${user.name() != null && !user.name().isBlank() ? user.name().substring(0, 1).toUpperCase() : "U"}
                            </div>
                        @endif
                    </div>

                    <div class="space-y-1">
                        <div class="flex flex-wrap items-center gap-2">
                            <h1 class="text-xl sm:text-2xl font-bold tracking-tight text-slate-900">${user.name() != null ? user.name().replaceAll("(?i)\\s+User$", "") : ""}</h1>
                            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200/70">
                                Active
                            </span>
                            @if(user.emailVerified())
                                <span class="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-50 text-blue-700 border border-blue-200/80 shadow-xs">
                                    <svg class="w-3.5 h-3.5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path>
                                    </svg>
                                    Verified
                                </span>
                            @endif
                        </div>

                        @if(user.email() != null && !user.email().isBlank() && !user.email().toLowerCase().contains("@social.istad.co") && !user.email().toLowerCase().contains("@telegram.istad.co"))
                            <div class="flex items-center gap-1.5 text-xs sm:text-sm text-slate-500 font-normal">
                                <svg class="w-4 h-4 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.8" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                </svg>
                                <span>${user.email()}</span>
                            </div>
                        @endif
                    </div>
                </div>

                <div class="flex flex-wrap items-center gap-2.5">
                    <a href="/profile/edit" class="inline-flex items-center gap-1.5 bg-blue-600 hover:bg-blue-700 text-white px-3.5 py-2 rounded-xl text-xs sm:text-sm font-medium shadow-sm shadow-blue-500/20 focus:outline-none focus:ring-2 focus:ring-blue-500/30 transition">
                        <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                        </svg>
                        <span>${i18n.t("profile.edit")}</span>
                    </a>
                    <a href="/profile/password" class="inline-flex items-center gap-1.5 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:border-slate-300 px-3.5 py-2 rounded-xl text-xs sm:text-sm font-medium transition shadow-xs">
                        <svg class="w-3.5 h-3.5 text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                        </svg>
                        <span>${i18n.t("profile.changePassword")}</span>
                    </a>
                    <a href="/profile/accounts" class="inline-flex items-center gap-1.5 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:border-slate-300 px-3.5 py-2 rounded-xl text-xs sm:text-sm font-medium transition shadow-xs">
                        <svg class="w-3.5 h-3.5 text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                        </svg>
                        <span>${i18n.t("profile.linkedAccounts")}</span>
                    </a>
                </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                
                <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/40 p-6 space-y-4">
                    <div class="flex items-center justify-between pb-3 border-b border-slate-100">
                        <div class="flex items-center gap-2">
                            <div class="p-1.5 rounded-lg bg-blue-50 text-blue-600">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                </svg>
                            </div>
                            <h2 class="text-sm font-bold text-slate-900 tracking-tight">${i18n.t("profile.roles")}</h2>
                        </div>
                        <span class="text-xs font-semibold px-2 py-0.5 rounded-md bg-slate-100 text-slate-600">
                            ${authorities != null && authorities.roles() != null ? authorities.roles().size() : 0}
                        </span>
                    </div>

                    <div class="flex flex-wrap gap-2 pt-1">
                        @if(authorities != null && authorities.roles() != null && !authorities.roles().isEmpty())
                            @for(var r : authorities.roles())
                                <span class="inline-flex items-center gap-1.5 bg-blue-50/80 text-blue-700 border border-blue-200/70 text-xs font-medium px-3 py-1.5 rounded-xl transition hover:bg-blue-100/80">
                                    <span class="w-1.5 h-1.5 rounded-full bg-blue-500"></span>
                                    ${r.name()}
                                </span>
                            @endfor
                        @else
                            <p class="text-xs text-slate-400 italic py-2">${i18n.t("profile.noRoles", "No roles currently assigned")}</p>
                        @endif
                    </div>
                </div>

                <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/40 p-6 space-y-4">
                    <div class="flex items-center justify-between pb-3 border-b border-slate-100">
                        <div class="flex items-center gap-2">
                            <div class="p-1.5 rounded-lg bg-emerald-50 text-emerald-600">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                                </svg>
                            </div>
                            <h2 class="text-sm font-bold text-slate-900 tracking-tight">${i18n.t("profile.permissions")}</h2>
                        </div>
                        <span class="text-xs font-semibold px-2 py-0.5 rounded-md bg-slate-100 text-slate-600">
                            ${authorities != null && authorities.effectivePermissions() != null ? authorities.effectivePermissions().size() : 0}
                        </span>
                    </div>

                    <div class="flex flex-wrap gap-2 pt-1 max-h-60 overflow-y-auto pr-1">
                        @if(authorities != null && authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty())
                            @for(var p : authorities.effectivePermissions())
                                <span class="inline-flex items-center gap-1 bg-slate-50 text-slate-700 border border-slate-200/80 text-[11px] font-mono px-2.5 py-1 rounded-lg">
                                    <span class="text-slate-400 font-sans">#</span>
                                    ${p}
                                </span>
                            @endfor
                        @else
                            <p class="text-xs text-slate-400 italic py-2">${i18n.t("profile.noPermissions", "No specific permissions granted")}</p>
                        @endif
                    </div>
                </div>

            </div>
        </div>
    `
)