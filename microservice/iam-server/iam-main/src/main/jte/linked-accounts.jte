@param kh.edu.istad.iam.web.support.Localizer localizer
@param java.util.List<kh.edu.istad.iam.domain.dto.result.IdentityResult> identities

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${localizer.get("profile.linkedAccounts")}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen py-10 px-4">
    <div class="max-w-2xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <div class="flex justify-between items-center border-b pb-4 mb-6">
            <h1 class="text-xl font-bold text-gray-800">${localizer.get("profile.linkedAccounts")}</h1>
            <a href="/" class="text-sm text-blue-600 hover:underline">Back to Profile</a>
        </div>

        <div class="space-y-4 mb-8">
            @if(identities != null && !identities.isEmpty())
                @for(var identity : identities)
                    <div class="flex items-center justify-between p-4 border rounded-lg bg-gray-50">
                        <div>
                            <span class="inline-block px-2.5 py-0.5 rounded text-xs font-semibold bg-blue-100 text-blue-800 uppercase mb-1">
                                ${identity.provider().name()}
                            </span>
                            <p class="text-sm font-medium text-gray-800">${identity.displayName() != null ? identity.displayName() : identity.email()}</p>
                            <p class="text-xs text-gray-500">${identity.email()}</p>
                        </div>
                        <form action="/profile/accounts/${identity.uuid().toString()}/unlink" method="post">
                            <button type="submit" class="px-3 py-1.5 border border-red-300 text-red-600 rounded-md hover:bg-red-50 text-xs font-medium">
                                Unlink
                            </button>
                        </form>
                    </div>
                @endfor
            @else
                <p class="text-sm text-gray-500 text-center py-4">No social accounts linked yet.</p>
            @endif
        </div>

        <div class="border-t pt-6">
            <h2 class="text-sm font-semibold text-gray-700 mb-3">Link more accounts:</h2>
            <div class="flex space-x-3">
                <a href="/oauth2/authorization/google" class="px-4 py-2 border rounded-md hover:bg-gray-50 text-xs font-medium">
                    Google
                </a>
                <a href="/oauth2/authorization/github" class="px-4 py-2 border rounded-md hover:bg-gray-50 text-xs font-medium">
                    GitHub
                </a>
                <a href="/oauth2/authorization/facebook" class="px-4 py-2 border rounded-md hover:bg-gray-50 text-xs font-medium">
                    Facebook
                </a>
            </div>
        </div>
    </div>
</body>
</html>

