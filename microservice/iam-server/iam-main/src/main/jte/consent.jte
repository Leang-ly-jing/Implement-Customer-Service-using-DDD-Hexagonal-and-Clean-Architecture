@param kh.edu.istad.iam.web.support.Localizer localizer
@param String clientName
@param String clientId
@param java.util.Set<String> scopes
@param String state

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Consent</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <div class="flex justify-between items-center mb-4">
            <h1 class="text-xl font-bold">${clientName}</h1>
            <div class="space-x-2">
                <a href="?lang=en&client_id=${clientId}&state=${state}" class="text-sm font-semibold text-blue-600">EN</a>
                <span class="text-gray-300">|</span>
                <a href="?lang=km&client_id=${clientId}&state=${state}" class="text-sm font-semibold text-blue-600">KM</a>
            </div>
        </div>
        <p class="text-sm text-gray-600 mb-4">requests access to your account:</p>

        <form action="/oauth2/authorize" method="post" class="space-y-4">
            <input type="hidden" name="client_id" value="${clientId}">
            <input type="hidden" name="state" value="${state}">

            <div class="bg-gray-50 p-4 rounded-md space-y-2 border">
                @for(String scope : scopes)
                    <div class="flex items-center space-x-2">
                        <input type="checkbox" name="scope" value="${scope}" id="${scope}" checked class="rounded text-blue-600">
                        <label for="${scope}" class="text-sm text-gray-700 font-medium">${scope}</label>
                    </div>
                @endfor
            </div>

            <div class="flex space-x-4 pt-2">
                <button type="submit" name="consent_action" value="approve" class="flex-1 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 font-medium">
                    ${localizer.get("consent.allow")}
                </button>
                <button type="submit" name="consent_action" value="cancel" class="flex-1 bg-gray-200 text-gray-700 py-2 rounded-md hover:bg-gray-300 font-medium">
                    ${localizer.get("consent.cancel")}
                </button>
            </div>
        </form>
    </div>
</body>
</html>

