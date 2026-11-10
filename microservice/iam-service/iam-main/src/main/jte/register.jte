@param kh.edu.istad.iam.web.support.Localizer localizer
@param kh.edu.istad.iam.web.dto.RegisterForm form
@param String errorMessage

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${localizer.get("register.title")}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen py-10">
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-lg">
        <div class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold">${localizer.get("register.title")}</h1>
            <div class="space-x-2">
                <a href="?lang=en" class="text-sm font-semibold text-blue-600">EN</a>
                <span class="text-gray-300">|</span>
                <a href="?lang=km" class="text-sm font-semibold text-blue-600">KM</a>
            </div>
        </div>

        @if(errorMessage != null)
            <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-4 text-sm">
                ${errorMessage}
            </div>
        @endif

        <form action="/register" method="post" class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Given Name</label>
                    <input type="text" name="givenName" value="${form.givenName()}" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Family Name</label>
                    <input type="text" name="familyName" value="${form.familyName()}" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Username</label>
                <input type="text" name="username" value="${form.username()}" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Email</label>
                <input type="email" name="email" value="${form.email()}" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Password</label>
                    <input type="password" name="password" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Confirm Password</label>
                    <input type="password" name="confirmPassword" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Locale</label>
                <select name="locale" class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                    <option value="km" selected>Khmer (km)</option>
                    <option value="en">English (en)</option>
                </select>
            </div>

            <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 font-medium">
                ${localizer.get("register.title")}
            </button>
        </form>

        <p class="mt-4 text-center text-sm text-gray-600">
            Already have an account? <a href="/login" class="text-blue-600 hover:underline">${localizer.get("login.title")}</a>
        </p>
    </div>
</body>
</html>

