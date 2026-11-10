@param kh.edu.istad.iam.web.support.Localizer localizer
@param boolean error

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${localizer.get("login.title")}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold">${localizer.get("login.title")}</h1>
            <div class="space-x-2">
                <a href="?lang=en" class="text-sm font-semibold text-blue-600">EN</a>
                <span class="text-gray-300">|</span>
                <a href="?lang=km" class="text-sm font-semibold text-blue-600">KM</a>
            </div>
        </div>

        @if(error)
            <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-4 text-sm">
                Invalid username or password.
            </div>
        @endif

        <form action="/login" method="post" class="space-y-4">
            <div>
                <label class="block text-sm font-medium text-gray-700">Username</label>
                <input type="text" name="username" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Password</label>
                <input type="password" name="password" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>
            <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 font-medium">
                ${localizer.get("login.title")}
            </button>
        </form>

        <div class="mt-6 border-t pt-4 space-y-2">
            <a href="/oauth2/authorization/google" class="w-full flex items-center justify-center border border-gray-300 py-2 rounded-md hover:bg-gray-50 text-sm font-medium">
                ${localizer.get("login.continueWith", "Google")}
            </a>
            <a href="/oauth2/authorization/github" class="w-full flex items-center justify-center border border-gray-300 py-2 rounded-md hover:bg-gray-50 text-sm font-medium">
                ${localizer.get("login.continueWith", "GitHub")}
            </a>
            <a href="/oauth2/authorization/facebook" class="w-full flex items-center justify-center border border-gray-300 py-2 rounded-md hover:bg-gray-50 text-sm font-medium">
                ${localizer.get("login.continueWith", "Facebook")}
            </a>
        </div>

        <p class="mt-4 text-center text-sm text-gray-600">
            <a href="/register" class="text-blue-600 hover:underline">${localizer.get("register.title")}</a>
        </p>
    </div>
</body>
</html>

