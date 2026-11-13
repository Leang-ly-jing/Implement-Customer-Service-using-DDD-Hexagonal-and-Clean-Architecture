@param kh.edu.istad.iam.domain.dto.result.UserResult user
@param kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult authorities
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n
@param kh.edu.istad.iam.domain.entity.User currentUser = null

!{String displayName = user.name() != null ? user.name().replaceAll("(?i)\\s+User$", "") : "";}
!{String initial = !displayName.isBlank() ? displayName.substring(0, 1).toUpperCase() : "U";}
!{boolean hasPicture = currentUser != null && currentUser.getPictureUrl() != null && !currentUser.getPictureUrl().isBlank();}
!{boolean showEmail = user.email() != null && !user.email().isBlank() && !user.email().toLowerCase().contains("@social.istad.co") && !user.email().toLowerCase().contains("@telegram.istad.co");}
!{int roleCount = authorities != null && authorities.roles() != null ? authorities.roles().size() : 0;}
!{int permCount = authorities != null && authorities.effectivePermissions() != null ? authorities.effectivePermissions().size() : 0;}

@template.layout.main(
    title = i18n.t("profile.title"),
    i18n = i18n,
    _csrf = _csrf,
    content = @`
        <div class="max-w-5xl mx-auto space-y-6">

            <section class="bg-white border border-slate-200/80 rounded-3xl shadow-xl shadow-slate-200/50 p-6 sm:p-8">
                <div class="flex flex-col sm:flex-row sm:items-center gap-4 sm:gap-6">
                    <div class="relative flex-shrink-0">
                        @if(hasPicture)
                            <img src="${currentUser.getPictureUrl()}" alt="${displayName}" class="w-20 h-20 sm:w-24 sm:h-24 rounded-2xl object-cover ring-2 ring-slate-100 shadow-md bg-white">
                        @else
                            <div class="w-20 h-20 sm:w-24 sm:h-24 rounded-2xl bg-gradient-to-tr from-blue-600 via-indigo-600 to-sky-400 text-white flex items-center justify-center font-extrabold text-3xl sm:text-4xl shadow-md shadow-blue-500/25 select-none">
                                ${initial}
                            </div>
                        @endif
                    </div>

                    <div class="flex-1 min-w-0">
                        <div class="flex flex-wrap items-center gap-2">
                            <h1 class="text-2xl sm:text-3xl font-bold tracking-tight text-slate-900 truncate">${displayName}</h1>
                            <span class="inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200/70">
                                <span class="w-1.5 h-1.5 rounded-full bg-emerald-500"></span>
                                Active
                            </span>
                            @if(user.emailVerified())
                                <span class="inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-50 text-blue-700 border border-blue-200/80">
                                    <svg class="w-3.5 h-3.5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                                        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path>
                                    </svg>
                                    Verified
                                </span>
                            @endif
                        </div>

                        @if(showEmail)
                            <div class="mt-1.5 flex items-center gap-1.5 text-sm text-slate-500">
                                <svg class="w-4 h-4 text-slate-400 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.8" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                </svg>
                                <span class="truncate">${user.email()}</span>
                            </div>
                        @endif
                    </div>
                </div>

                <div class="mt-6 pt-5 border-t border-slate-100 flex flex-wrap items-center gap-2.5">
                        <a href="/profile/edit" class="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-700 active:scale-[0.98] text-white px-4 py-2.5 rounded-xl text-sm font-medium shadow-md shadow-blue-500/25 focus:outline-none focus-visible:ring-4 focus-visible:ring-blue-500/30 transition">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                            </svg>
                            <span>${i18n.t("profile.edit")}</span>
                        </a>
                        <a href="/profile/password" class="inline-flex items-center gap-2 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:border-slate-300 active:scale-[0.98] px-4 py-2.5 rounded-xl text-sm font-medium focus:outline-none focus-visible:ring-4 focus-visible:ring-slate-400/20 transition">
                            <svg class="w-4 h-4 text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                            <span>${i18n.t("profile.changePassword")}</span>
                        </a>
                        <a href="/profile/accounts" class="inline-flex items-center gap-2 bg-white border border-slate-200 text-slate-700 hover:bg-slate-50 hover:border-slate-300 active:scale-[0.98] px-4 py-2.5 rounded-xl text-sm font-medium focus:outline-none focus-visible:ring-4 focus-visible:ring-slate-400/20 transition">
                            <svg class="w-4 h-4 text-slate-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                            </svg>
                            <span>${i18n.t("profile.linkedAccounts")}</span>
                        </a>
                    </div>
            </section>

            <section class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div class="bg-white border border-slate-200/80 rounded-2xl shadow-sm p-5 flex items-center gap-4">
                    <div class="w-11 h-11 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                        </svg>
                    </div>
                    <div>
                        <p class="text-2xl font-bold text-slate-900 leading-none">${roleCount}</p>
                        <p class="mt-1 text-xs font-medium text-slate-500">${i18n.t("profile.roles")}</p>
                    </div>
                </div>

                <div class="bg-white border border-slate-200/80 rounded-2xl shadow-sm p-5 flex items-center gap-4">
                    <div class="w-11 h-11 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                        </svg>
                    </div>
                    <div>
                        <p class="text-2xl font-bold text-slate-900 leading-none">${permCount}</p>
                        <p class="mt-1 text-xs font-medium text-slate-500">${i18n.t("profile.permissions")}</p>
                    </div>
                </div>

                <div class="bg-white border border-slate-200/80 rounded-2xl shadow-sm p-5 flex items-center gap-4">
                    @if(user.emailVerified())
                        <div class="w-11 h-11 rounded-xl bg-violet-50 text-violet-600 flex items-center justify-center">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                            </svg>
                        </div>
                        <div>
                            <p class="text-sm font-bold text-slate-900 leading-none">Verified</p>
                            <p class="mt-1 text-xs font-medium text-slate-500">Email</p>
                        </div>
                    @else
                        <div class="w-11 h-11 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
                            </svg>
                        </div>
                        <div>
                            <p class="text-sm font-bold text-slate-900 leading-none">Not verified</p>
                            <p class="mt-1 text-xs font-medium text-slate-500">Email</p>
                        </div>
                    @endif
                </div>
            </section>

            <section class="grid grid-cols-1 lg:grid-cols-5 gap-6">

                <div class="lg:col-span-2 bg-white border border-slate-200/80 rounded-2xl shadow-lg shadow-slate-200/40 p-6">
                    <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                        <h2 class="text-sm font-bold text-slate-900 tracking-tight">${i18n.t("profile.roles")}</h2>
                        <span class="text-xs font-semibold px-2 py-0.5 rounded-md bg-blue-50 text-blue-700">${roleCount}</span>
                    </div>

                    <div class="mt-4 flex flex-wrap gap-2">
                        @if(authorities != null && authorities.roles() != null && !authorities.roles().isEmpty())
                            @for(var r : authorities.roles())
                                <span class="inline-flex items-center gap-2 bg-gradient-to-b from-blue-50 to-blue-50/60 text-blue-700 border border-blue-200/70 text-xs font-semibold px-3 py-1.5 rounded-xl">
                                    <span class="w-1.5 h-1.5 rounded-full bg-blue-500"></span>
                                    ${r.name()}
                                </span>
                            @endfor
                        @else
                            <div class="w-full flex flex-col items-center text-center py-8 text-slate-400">
                                <svg class="w-8 h-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                </svg>
                                <p class="text-xs italic">${i18n.t("profile.noRoles", "No roles currently assigned")}</p>
                            </div>
                        @endif
                    </div>
                </div>

                <div class="lg:col-span-3 bg-white border border-slate-200/80 rounded-2xl shadow-lg shadow-slate-200/40 p-6">
                    <div class="flex items-center justify-between pb-4 border-b border-slate-100">
                        <h2 class="text-sm font-bold text-slate-900 tracking-tight">${i18n.t("profile.permissions")}</h2>
                        <span class="text-xs font-semibold px-2 py-0.5 rounded-md bg-emerald-50 text-emerald-700">${permCount}</span>
                    </div>

                    @if(authorities != null && authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty())
                        <div class="relative mt-4">
                            <svg class="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35M17 10a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                            <input id="perm-search" type="search" autocomplete="off" placeholder="${i18n.t("profile.searchPermissions", "Search permissions...")}"
                                   class="w-full pl-9 pr-3 py-2 text-sm bg-slate-50 border border-slate-200 rounded-xl placeholder:text-slate-400 focus:outline-none focus:bg-white focus:border-blue-400 focus:ring-4 focus:ring-blue-500/10 transition">
                        </div>

                        <div class="mt-4 flex flex-wrap gap-2 max-h-72 overflow-y-auto pr-1">
                            @for(var p : authorities.effectivePermissions())
                                <button type="button"
                                        data-perm="${String.valueOf(p).toLowerCase()}"
                                        data-perm-raw="${String.valueOf(p)}"
                                        title="Click to copy"
                                        class="inline-flex items-center gap-1 bg-slate-50 hover:bg-emerald-50 text-slate-700 hover:text-emerald-700 border border-slate-200/80 hover:border-emerald-200 text-[11px] font-mono px-2.5 py-1 rounded-lg cursor-pointer transition">
                                    <span class="text-slate-400 font-sans">#</span>${p}
                                </button>
                            @endfor
                        </div>

                        <p id="perm-empty" style="display:none" class="py-8 text-center text-xs italic text-slate-400">
                            ${i18n.t("profile.noPermissionMatch", "No matching permissions")}
                        </p>
                    @else
                        <div class="flex flex-col items-center text-center py-8 text-slate-400">
                            <svg class="w-8 h-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                            </svg>
                            <p class="text-xs italic">${i18n.t("profile.noPermissions", "No specific permissions granted")}</p>
                        </div>
                    @endif
                </div>
            </section>
        </div>

        <script>
            (function () {
                var input = document.getElementById('perm-search');
                if (!input) return;
                var chips = document.querySelectorAll('[data-perm]');
                var empty = document.getElementById('perm-empty');

                input.addEventListener('input', function () {
                    var q = input.value.trim().toLowerCase();
                    var shown = 0;
                    chips.forEach(function (c) {
                        var match = c.getAttribute('data-perm').indexOf(q) !== -1;
                        c.style.display = match ? '' : 'none';
                        if (match) shown++;
                    });
                    empty.style.display = shown === 0 ? '' : 'none';
                });

                chips.forEach(function (c) {
                    c.addEventListener('click', function () {
                        if (!navigator.clipboard) return;
                        navigator.clipboard.writeText(c.getAttribute('data-perm-raw'));
                        c.classList.add('ring-2', 'ring-emerald-300');
                        setTimeout(function () { c.classList.remove('ring-2', 'ring-emerald-300'); }, 600);
                    });
                });
            })();
        </script>
    `
)
