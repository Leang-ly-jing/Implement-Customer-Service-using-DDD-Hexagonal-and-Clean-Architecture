@param String clientName
@param String clientId
@param String state
@param java.util.Set<String> scopes
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n

<!DOCTYPE html>
<html lang="km" class="h-full">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${i18n.t("consent.title")} &mdash; ISTAD IAM</title>
    
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Kantumruy+Pro:wght@400;500;600;700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        sans: ['"Kantumruy Pro"', '"Plus Jakarta Sans"', 'sans-serif'],
                    }
                }
            }
        }
    </script>
    <style>
        .subtle-grid {
            background-image: radial-gradient(rgba(148, 163, 184, 0.15) 1px, transparent 1px);
            background-size: 24px 24px;
        }
    </style>
</head>
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col justify-center items-center p-4 sm:p-6 font-sans relative selection:bg-blue-100 selection:text-blue-700">

    
    <div class="fixed inset-0 pointer-events-none -z-10 subtle-grid"></div>
    <div class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-4xl h-80 bg-gradient-to-b from-blue-100/50 via-blue-50/20 to-transparent blur-3xl pointer-events-none -z-10"></div>

    <div class="w-full max-w-lg my-auto">
        <div class="flex items-center justify-between mb-6 px-1">
            <a href="/" class="flex items-center gap-2.5 group">
                <img src="https://www.istad.co/icon.png" alt="ISTAD Logo" class="w-8 h-8 object-contain rounded-full transition group-hover:scale-105">
                <div class="flex flex-col">
                    <span class="font-bold text-sm text-slate-900 group-hover:text-blue-600 transition">ISTAD IAM</span>
                    <span class="text-[9px] uppercase font-semibold tracking-wider text-slate-400 -mt-0.5">Authorization Server</span>
                </div>
            </a>

            
            <div class="flex items-center bg-white/80 backdrop-blur-sm p-0.5 rounded-lg border border-slate-200/80 shadow-xs text-xs font-medium text-slate-600">
                <a href="?lang=km" class="px-2.5 py-1 rounded-md hover:text-slate-900 hover:bg-slate-100 transition">ខ្មែរ</a>
                <span class="text-slate-300">|</span>
                <a href="?lang=en" class="px-2.5 py-1 rounded-md hover:text-slate-900 hover:bg-slate-100 transition">EN</a>
            </div>
        </div>

        
        <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/50 p-6 sm:p-8 space-y-6">
            
            
            <div class="flex flex-col items-center text-center space-y-3 pb-2 border-b border-slate-100">
                <div class="flex items-center justify-center gap-3">
                    
                    <div class="w-12 h-12 rounded-2xl bg-slate-100 border border-slate-200 flex items-center justify-center text-slate-700 font-bold text-lg shadow-xs">
                        ${clientName != null && !clientName.isEmpty() ? clientName.substring(0, 1).toUpperCase() : "A"}
                    </div>
                    
                    <div class="flex items-center text-slate-400">
                        <svg class="w-5 h-5 text-blue-500 animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
                        </svg>
                    </div>
                    
                    <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-blue-700 to-blue-500 flex items-center justify-center text-white shadow-md shadow-blue-500/25">
                        <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                        </svg>
                    </div>
                </div>

                <div class="space-y-1">
                    <h1 class="text-xl sm:text-2xl font-bold tracking-tight text-slate-900">
                        ${i18n.t("consent.title")}
                    </h1>
                    <p class="text-xs sm:text-sm text-slate-600 max-w-sm">
                        ${i18n.t("consent.clientWantsAccess", clientName)}
                    </p>
                </div>
            </div>

            
            <form action="/oauth2/authorize" method="post" class="space-y-6">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <input type="hidden" name="client_id" value="${clientId}">
                <input type="hidden" name="state" value="${state}">

                
                <div class="space-y-2.5">
                    <div class="flex items-center justify-between">
                        <h2 class="text-xs font-semibold uppercase tracking-wider text-slate-500">
                            ${i18n.t("consent.scopes")}
                        </h2>
                        <span class="text-[11px] text-slate-400 font-mono">client_id: ${clientId}</span>
                    </div>

                    <div class="bg-slate-50/75 border border-slate-200/90 rounded-xl divide-y divide-slate-100 overflow-hidden">
                        @for(String scope : scopes)
                            <label for="${scope}" class="flex items-start gap-3.5 p-3.5 hover:bg-slate-100/60 cursor-pointer transition select-none">
                                <div class="pt-0.5">
                                    <input 
                                        type="checkbox" 
                                        name="scope" 
                                        value="${scope}" 
                                        id="${scope}" 
                                        checked 
                                        class="w-4 h-4 rounded text-blue-600 border-slate-300 focus:ring-blue-500/20 focus:ring-2 focus:ring-offset-0 cursor-pointer"
                                    >
                                </div>
                                <div class="flex-grow">
                                    <div class="flex items-center gap-2">
                                        <span class="text-sm font-semibold text-slate-800 font-mono">${scope}</span>
                                        @if("openid".equalsIgnoreCase(scope) || "profile".equalsIgnoreCase(scope))
                                            <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-medium bg-blue-50 text-blue-700 border border-blue-200/60">
                                                Standard
                                            </span>
                                        @endif
                                    </div>
                                    <p class="text-xs text-slate-500 mt-0.5">
                                        ${i18n.t("scope." + scope + ".description", "Grants authorization permissions for " + scope)}
                                    </p>
                                </div>
                            </label>
                        @endfor
                    </div>
                </div>

                
                <div class="p-3 bg-amber-50/70 border border-amber-200/70 rounded-xl flex items-start gap-2.5 text-amber-800 text-xs">
                    <svg class="w-4 h-4 text-amber-600 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>${i18n.t("consent.notice", "Allowing access grants permissions to read account resources according to selected scopes.")}</span>
                </div>

                
                <div class="grid grid-cols-2 gap-3 pt-1">
                    <button 
                        type="submit" 
                        name="consent_action" 
                        value="cancel" 
                        class="w-full inline-flex items-center justify-center py-2.5 px-4 bg-slate-100 hover:bg-slate-200 active:bg-slate-300 text-slate-700 font-medium text-xs sm:text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-slate-300 transition"
                    >
                        ${i18n.t("consent.cancel")}
                    </button>
                    <button 
                        type="submit" 
                        name="consent_action" 
                        value="approve" 
                        class="w-full inline-flex items-center justify-center py-2.5 px-4 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium text-xs sm:text-sm rounded-xl shadow-md shadow-blue-500/20 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition"
                    >
                        ${i18n.t("consent.allow")}
                    </button>
                </div>
            </form>
        </div>

        
        <p class="text-center text-xs text-slate-400 mt-6">
            &copy; 2026 ISTAD IAM. All rights reserved.
        </p>
    </div>

</body>
</html>
