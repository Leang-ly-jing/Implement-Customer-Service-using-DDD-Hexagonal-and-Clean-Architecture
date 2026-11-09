@param kh.edu.istad.iam.web.support.Localizer localizer
@param kh.edu.istad.iam.domain.dto.result.UserResult user

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen py-10 px-4">
    <div class="max-w-2xl mx-auto bg-white p-8 rounded-lg shadow-md">
        <div class="flex justify-between items-center border-b pb-4 mb-6">
            <h1 class="text-2xl font-bold text-gray-800">Edit Profile</h1>
            <a href="/" class="text-sm text-blue-600 hover:underline">Back to Profile</a>
        </div>

        <form action="/profile/edit" method="post" class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Given Name</label>
                    <input type="text" name="givenName" value="${user.name()}" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Family Name</label>
                    <input type="text" name="familyName" value="${user.name()}" required class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Nickname</label>
                <input type="text" name="nickname" class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Phone Number</label>
                <input type="text" name="phoneNumber" class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Locale</label>
                <select name="locale" class="mt-1 block w-full px-3 py-2 border rounded-md border-gray-300 shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                    <option value="km" selected>Khmer (km)</option>
                    <option value="en">English (en)</option>
                </select>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
                <a href="/" class="px-4 py-2 border rounded-md text-gray-700 hover:bg-gray-50 text-sm font-medium">Cancel</a>
                <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 text-sm font-medium">Save Changes</button>
            </div>
        </form>
    </div>
</body>
</html>

