@param kh.edu.istad.iam.web.dto.RegisterForm request = null
@param String errorMessage = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.iam.web.util.I18n i18n

<!DOCTYPE html>
<html lang="km" class="h-full">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${i18n.t("register.title")} &mdash; ISTAD IAM</title>
    
    
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
                    <span class="text-[9px] uppercase font-semibold tracking-wider text-slate-400 -mt-0.5">Identity Access</span>
                </div>
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
                    ${i18n.t("register.title")}
                </h1>
                <p class="text-xs sm:text-sm text-slate-500">
                    ${i18n.t("register.subtitle", "Create your unified identity account")}
                </p>
            </div>

            
            @if(errorMessage != null)
                <div class="flex items-start gap-3 p-3.5 rounded-xl bg-rose-50 border border-rose-200 text-rose-700 text-sm">
                    <svg class="w-5 h-5 flex-shrink-0 mt-0.5 text-rose-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <span class="leading-relaxed font-medium">${errorMessage}</span>
                </div>
            @endif

            
            <form action="/register" method="post" class="space-y-4">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                
                
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("register.givenName")}
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                </svg>
                            </div>
                            <input 
                                type="text" 
                                name="givenName" 
                                value="${request != null ? request.givenName() : ""}"
                                required 
                                placeholder="Sokha"
                                class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                            >
                        </div>
                    </div>
                    
                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("register.familyName")}
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                </svg>
                            </div>
                            <input 
                                type="text" 
                                name="familyName" 
                                value="${request != null ? request.familyName() : ""}"
                                required 
                                placeholder="Chan"
                                class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                            >
                        </div>
                    </div>
                </div>

                
                <div class="space-y-1.5">
                    <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                        ${i18n.t("register.username")}
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </div>
                        <input 
                            type="text" 
                            name="userName" 
                            value="${request != null ? request.userName() : ""}"
                            required 
                            autocomplete="username"
                            placeholder="sokhachan"
                            class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                        >
                    </div>
                </div>

                
                <div class="space-y-1.5">
                    <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                        ${i18n.t("register.email")}
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207" />
                            </svg>
                        </div>
                        <input 
                            type="email" 
                            name="email" 
                            value="${request != null ? request.email() : ""}"
                            required 
                            autocomplete="email"
                            placeholder="sokha.chan@example.com"
                            class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                        >
                    </div>
                </div>

                
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("register.password")}
                        </label>
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
                                autocomplete="new-password"
                                placeholder="••••••••"
                                class="w-full pl-10 pr-3.5 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-900 text-sm placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-600 focus:bg-white transition"
                            >
                        </div>
                    </div>

                    <div class="space-y-1.5">
                        <label class="block text-xs font-semibold uppercase tracking-wider text-slate-600">
                            ${i18n.t("register.confirmPassword")}
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
                </div>

                
                <button 
                    type="submit" 
                    class="w-full mt-2 inline-flex items-center justify-center py-2.5 px-4 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium text-sm rounded-xl shadow-md shadow-blue-500/20 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition"
                >
                    ${i18n.t("register.submit")}
                </button>
            </form>

            
            <p class="text-center text-xs sm:text-sm text-slate-500 pt-3 border-t border-slate-100">
                ${i18n.t("register.hasAccount")} 
                <a href="/login" class="text-blue-600 font-semibold hover:text-blue-700 hover:underline ml-1">
                    ${i18n.t("register.login")}
                </a>
            </p>
        </div>

        
        <p class="text-center text-xs text-slate-400 mt-6">
            &copy; 2026 ISTAD IAM. All rights reserved.
        </p>
    </div>

</body>
</html>
