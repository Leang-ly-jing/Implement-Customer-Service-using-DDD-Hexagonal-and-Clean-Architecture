import axiosClient from './axiosClient';

export const authService = {
    login: async (username, password) => {
        const response = await axiosClient.post('/auth/login', { username, password });
        if (response.data?.accessToken) {
            localStorage.setItem('access_token', response.data.accessToken);
            localStorage.setItem('user', JSON.stringify({
                username: response.data.username,
                roles: response.data.roles
            }));
        }
        return response.data;
    },

    register: async (payload) => {
        const response = await axiosClient.post('/auth/register', payload);
        return response.data;
    },

    logout: () => {
        localStorage.removeItem('access_token');
        localStorage.removeItem('user');
        window.location.href = '/login';
    }
};