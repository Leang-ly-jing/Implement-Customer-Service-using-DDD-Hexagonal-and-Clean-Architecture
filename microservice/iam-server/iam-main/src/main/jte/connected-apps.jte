@param kh.edu.istad.iam.web.support.Localizer localizer

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Connected Applications</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen py-10 px-4">
    <div class="max-w-2xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <div class="flex justify-between items-center border-b pb-4 mb-6">
            <h1 class="text-xl font-bold text-gray-800">Connected Applications</h1>
            <a href="/" class="text-sm text-blue-600 hover:underline">Back to Profile</a>
        </div>

        <div class="space-y-4">
            <div class="flex items-center justify-between p-4 border rounded-lg bg-gray-50">
                <div>
                    <h2 class="text-sm font-semibold text-gray-800">ISTAD E-Commerce Web</h2>
                    <p class="text-xs text-gray-500 font-mono mt-0.5">Scopes: openid, profile, email</p>
                </div>
                <button type="button" class="px-3 py-1.5 border border-red-300 text-red-600 rounded-md hover:bg-red-50 text-xs font-medium">
                    Revoke
                </button>
            </div>
        </div>
    </div>
</body>
</html>

