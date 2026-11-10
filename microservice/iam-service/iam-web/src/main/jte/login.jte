@param boolean error = false
@param String telegramBotUsername = ""
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n

<!DOCTYPE html>
<html lang="km" class="h-full">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${i18n.t("login.title")} &mdash; ISTAD IAM</title>
    
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

    <div class="w-full max-w-md">
        <div class="flex items-center justify-between mb-6 px-1">
            <a href="/" class="flex items-center gap-2 group">
                <img src="https://www.istad.co/icon.png" alt="ISTAD Logo" class="w-8 h-8 object-contain rounded-full transition group-hover:scale-105">
                <span class="font-bold text-slate-900 group-hover:text-blue-600 transition text-sm">ISTAD IAM</span>
            </a>

            <div class="flex items-center bg-white/80 backdrop-blur-sm p-0.5 rounded-lg border border-slate-200/80 shadow-xs text-xs font-medium text-slate-600">
                <a href="?lang=km" class="px-2.5 py-1 rounded-md hover:text-slate-900 hover:bg-slate-100 transition">ខ្មែរ</a>
                <span class="text-slate-300">|</span>
                <a href="?lang=en" class="px-2.5 py-1 rounded-md hover:text-slate-900 hover:bg-slate-100 transition">EN</a>
            </div>
        </div>

        <div class="bg-white/95 backdrop-blur-md border border-slate-200/80 rounded-2xl shadow-xl shadow-slate-200/50 p-6 sm:p-8 space-y-6">
            
            <div class="space-y-1">
                <h1 class="text-xl sm:text-2xl font-bold tracking-tight text-slate-900">
                    ${i18n.t("login.title")}
                </h1>
                <p class="text-xs sm:text-sm text-slate-500">
                    ${i18n.t("login.subtitle", "Enter your credentials to access your account")}
                </p>
            </div>

            @if(error)
                <div class="flex items-start gap-3 p-3.5 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm">
                    <svg class="w-5 h-5 flex-shrink-0 mt-0.5 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <span class="leading-relaxed font-medium">${i18n.t("login.error")}</span>
                </div>
            @endif

            <form action="/login" method="post" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <div class="space-y-1.5">
                    <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                        ${i18n.t("login.username")}
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                            </svg>
                        </div>
                        <input 
                            type="text" 
                            name="username" 
                            required 
                            autocomplete="username"
                            placeholder="username"
                            class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                        >
                    </div>
                </div>

                <div class="space-y-1.5">
                    <div class="flex items-center justify-between">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("login.password")}
                        </label>
                        <a href="/forgot-password" class="text-xs font-medium text-blue-600 hover:text-blue-700 hover:underline">
                            ${i18n.t("login.forgotPassword", "Forgot?")}
                        </a>
                    </div>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                        </div>
                        <input 
                            type="password" 
                            name="password" 
                            required 
                            autocomplete="current-password"
                            placeholder="••••••••"
                            class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                        >
                    </div>
                </div>

                <button 
                    type="submit" 
                    class="w-full mt-2 inline-flex items-center justify-center py-2.5 px-4 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium text-sm rounded-xl shadow-md shadow-blue-500/20 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition"
                >
                    ${i18n.t("login.submit")}
                </button>
            </form>

            <div class="relative flex items-center justify-center">
                <div class="w-full border-t border-slate-200"></div>
                <span class="absolute bg-white px-3 text-xs uppercase tracking-wider text-slate-400 font-medium">
                    ${i18n.t("login.or")}
                </span>
            </div>

            <div class="space-y-2.5">
                <a href="/oauth2/authorization/google" class="w-full inline-flex items-center justify-center gap-3 py-2.5 px-4 bg-white border border-slate-200 rounded-xl text-xs sm:text-sm font-medium text-slate-700 hover:bg-slate-50 hover:border-slate-300 focus:outline-none focus:ring-2 focus:ring-slate-400/20 transition shadow-xs">
                    <svg class="w-4 h-4" viewBox="0 0 24 24">
                        <path fill="#4285F4" d="M23.745 12.27c0-.7-.06-1.4-.19-2.07H12v4.51h6.6c-.29 1.52-1.14 2.8-2.4 3.66v3.05h3.88c2.27-2.09 3.66-5.17 3.66-9.15z"/>
                        <path fill="#34A853" d="M12 24c3.24 0 5.95-1.08 7.93-2.91l-3.88-3.05c-1.08.72-2.45 1.16-4.05 1.16-3.12 0-5.77-2.1-6.72-4.93H1.24v3.15C3.26 21.4 7.34 24 12 24z"/>
                        <path fill="#FBBC05" d="M5.28 14.27a7.2 7.2 0 0 1 0-4.54V6.58H1.24a12.01 12.01 0 0 0 0 10.84l4.04-3.15z"/>
                        <path fill="#EA4335" d="M12 4.75c1.77 0 3.35.61 4.6 1.8l3.42-3.42C17.95 1.19 15.24 0 12 0 7.34 0 3.26 2.6 1.24 6.58l4.04 3.15c.95-2.83 3.6-4.98 6.72-4.98z"/>
                    </svg>
                    <span>${i18n.t("login.continueWith", "Google")}</span>
                </a>

                <a href="/oauth2/authorization/github" class="w-full inline-flex items-center justify-center gap-3 py-2.5 px-4 bg-slate-900 border border-slate-900 rounded-xl text-xs sm:text-sm font-medium text-white hover:bg-slate-800 focus:outline-none focus:ring-2 focus:ring-slate-900/30 transition shadow-xs">
                    <svg class="w-4 h-4 fill-current" viewBox="0 0 24 24">
                        <path fill-rule="evenodd" clip-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"/>
                    </svg>
                    <span>${i18n.t("login.continueWith", "GitHub")}</span>
                </a>

                <a href="/oauth2/authorization/facebook" class="w-full inline-flex items-center justify-center gap-3 py-2.5 px-4 bg-[#1877F2] hover:bg-[#166fe5] border border-[#1877F2] rounded-xl text-xs sm:text-sm font-medium text-white focus:outline-none focus:ring-2 focus:ring-[#1877F2]/30 transition shadow-xs">
                    <svg class="w-4 h-4 fill-current" viewBox="0 0 24 24">
                        <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                    </svg>
                    <span>${i18n.t("login.continueWith", "Facebook")}</span>
                </a>

                <a href="/oauth2/authorization/telegram" class="w-full inline-flex items-center justify-center gap-3 py-2.5 px-4 bg-[#24A1DE] hover:bg-[#1e8bc0] border border-[#24A1DE] rounded-xl text-xs sm:text-sm font-medium text-white focus:outline-none focus:ring-2 focus:ring-[#24A1DE]/30 transition shadow-xs">
                    <svg class="w-4 h-4 fill-current" viewBox="0 0 24 24">
                        <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm5.894 8.221l-1.97 9.28c-.145.658-.537.818-1.084.508l-3-2.21-1.446 1.394c-.14.18-.357.295-.6.295-.002 0-.003 0-.005 0l.213-3.054 5.56-5.022c.24-.213-.054-.334-.373-.121l-6.869 4.326-2.96-.924c-.643-.204-.657-.643.136-.953l11.57-4.461c.536-.195 1.006.128.832.922z"/>
                    </svg>
                    <span>${i18n.t("login.continueWith", "Telegram")}</span>
                </a>
            </div>

            <p class="text-center text-xs sm:text-sm text-slate-500 pt-2 border-t border-slate-100">
                ${i18n.t("login.noAccount")} 
                <a href="/register" class="text-blue-600 font-semibold hover:text-blue-700 hover:underline ml-1">
                    ${i18n.t("login.register")}
                </a>
            </p>
        </div>
        
        <p class="text-center text-xs text-slate-400 mt-6">
            &copy; 2026 ISTAD IAM. All rights reserved.
        </p>
    </div>
</body>
</html>
