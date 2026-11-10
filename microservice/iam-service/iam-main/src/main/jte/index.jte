@param kh.edu.istad.iam.web.support.Localizer localizer
@param kh.edu.istad.iam.domain.dto.result.UserResult user
@param kh.edu.istad.iam.domain.dto.result.UserAuthoritiesResult authorities

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${localizer.get("profile.title")}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen py-10 px-4">
    <div class="max-w-3xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <div class="flex justify-between items-center border-b pb-4 mb-6">
            <div>
                <h1 class="text-2xl font-bold text-gray-800">${localizer.get("profile.title")}</h1>
                <p class="text-sm text-gray-500">${user.email()}</p>
            </div>
            <div class="flex items-center space-x-4">
                <div class="space-x-1">
                    <a href="?lang=en" class="text-xs font-semibold text-blue-600">EN</a>
                    <span class="text-gray-300">|</span>
                    <a href="?lang=km" class="text-xs font-semibold text-blue-600">KM</a>
                </div>
                <form action="/logout" method="post" class="inline">
                    <button type="submit" class="bg-red-50 text-red-600 hover:bg-red-100 px-3 py-1.5 rounded-md text-sm font-medium">
                        ${localizer.get("common.logout")}
                    </button>
                </form>
            </div>
        </div>

        <div class="flex space-x-4 mb-6 border-b pb-3 text-sm font-medium text-gray-600">
            <a href="/" class="text-blue-600 border-b-2 border-blue-600 pb-2">Overview</a>
            <a href="/profile/edit" class="hover:text-blue-600 pb-2">Edit Profile</a>
            <a href="/profile/password" class="hover:text-blue-600 pb-2">Change Password</a>
            <a href="/profile/accounts" class="hover:text-blue-600 pb-2">${localizer.get("profile.linkedAccounts")}</a>
            <a href="/profile/apps" class="hover:text-blue-600 pb-2">Connected Apps</a>
        </div>

        <div class="grid grid-cols-2 gap-6 mb-8">
            <div class="space-y-3">
                <h2 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">Claims</h2>
                <div>
                    <span class="text-xs text-gray-500">Subject (UUID)</span>
                    <p class="text-sm font-mono text-gray-800">${user.uuid().toString()}</p>
                </div>
                <div>
                    <span class="text-xs text-gray-500">Username</span>
                    <p class="text-sm font-semibold text-gray-800">${user.username()}</p>
                </div>
                <div>
                    <span class="text-xs text-gray-500">Full Name</span>
                    <p class="text-sm text-gray-800">${user.name()}</p>
                </div>
                <div>
                    <span class="text-xs text-gray-500">Locale</span>
                    <p class="text-sm text-gray-800">${user.locale()}</p>
                </div>
            </div>

            <div class="space-y-4">
                <div>
                    <h2 class="text-sm font-semibold text-gray-400 uppercase tracking-wider mb-2">Assigned Roles</h2>
                    <div class="flex flex-wrap gap-2">
                        @if(authorities.roles() != null && !authorities.roles().isEmpty())
                            @for(var role : authorities.roles())
                                <span class="bg-blue-100 text-blue-800 text-xs px-2.5 py-1 rounded-full font-medium">
                                    ${role.name()}
                                </span>
                            @endfor
                        @else
                            <span class="text-sm text-gray-400">None</span>
                        @endif
                    </div>
                </div>

                <div>
                    <h2 class="text-sm font-semibold text-gray-400 uppercase tracking-wider mb-2">Effective Permissions</h2>
                    <div class="flex flex-wrap gap-1.5 max-h-48 overflow-y-auto">
                        @if(authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty())
                            @for(var perm : authorities.effectivePermissions())
                                <span class="bg-gray-100 text-gray-700 text-xs px-2 py-0.5 rounded font-mono">
                                    ${perm}
                                </span>
                            @endfor
                        @else
                            <span class="text-sm text-gray-400">None</span>
                        @endif
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

