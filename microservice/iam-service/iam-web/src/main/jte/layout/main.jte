@param String title
@param kh.edu.istad.iam.web.util.I18n i18n
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="km" class="h-full">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} &mdash; ISTAD IAM</title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Kantumruy+Pro:ital,wght@0,300..700;1,300..700&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        sans: ['"Kantumruy Pro"', '"Plus Jakarta Sans"', 'sans-serif'],
                    },
                    colors: {
                        brand: {
                            50: '#eef6ff',
                            100: '#d9eaff',
                            500: '#2563eb',
                            600: '#1d4ed8',
                            700: '#1e40af',
                            900: '#1e293b',
                        }
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
<body class="bg-slate-50 text-slate-800 antialiased min-h-screen flex flex-col font-sans relative selection:bg-blue-100 selection:text-blue-700">

    <div class="fixed inset-0 pointer-events-none -z-10 subtle-grid"></div>
    <div class="fixed top-0 left-1/2 -translate-x-1/2 w-full max-w-7xl h-64 bg-gradient-to-b from-blue-100/40 via-blue-50/20 to-transparent blur-3xl pointer-events-none -z-10"></div>

    <header class="sticky top-0 z-50 bg-white/80 backdrop-blur-md border-b border-slate-200/80 transition-all">
        <div class="max-w-6xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
            
            <a href="/" class="flex items-center gap-2.5 group focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 rounded-lg p-1">
                <img src="https://www.istad.co/icon.png" alt="ISTAD Logo" class="w-10 h-10 object-contain rounded-full transition group-hover:scale-105">
                <div class="flex flex-col">
                    <span class="font-bold text-base tracking-tight text-slate-900 group-hover:text-blue-600 transition">ISTAD IAM</span>
                    <span class="text-[10px] uppercase font-semibold tracking-wider text-slate-400 -mt-1">Identity Access</span>
                </div>
            </a>

            <div class="flex items-center gap-3 sm:gap-5">
                <div class="flex items-center bg-slate-100/80 p-0.5 rounded-lg border border-slate-200 text-xs font-medium">
                    <a href="?lang=km" class="px-2.5 py-1 rounded-md text-slate-600 hover:text-slate-900 transition hover:bg-white/70">
                        ខ្មែរ
                    </a>
                    <span class="text-slate-300">|</span>
                    <a href="?lang=en" class="px-2.5 py-1 rounded-md text-slate-600 hover:text-slate-900 transition hover:bg-white/70">
                        EN
                    </a>
                </div>

                <div class="h-4 w-px bg-slate-200"></div>

                <form action="/logout" method="post" class="inline m-0">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                    @endif
                    <button type="submit" class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs sm:text-sm font-medium text-rose-600 hover:bg-rose-50 hover:text-rose-700 transition focus:outline-none focus:ring-2 focus:ring-rose-500/30">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                        </svg>
                        <span>${i18n.t("common.logout")}</span>
                    </button>
                </form>
            </div>
        </div>
    </header>

    <main class="flex-grow flex items-center justify-center px-4 py-8 sm:py-12">
        <div class="w-full max-w-4xl">
            ${content}
        </div>
    </main>
</body>
</html>
