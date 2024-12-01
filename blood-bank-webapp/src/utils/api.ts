import axios from 'axios';

const BASE_URL = 'http://localhost:8080';

const api = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (error: any = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve();
        }
    });
    failedQueue = [];
};

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // If error is not 401 or request already retried, reject
        if (error.response?.status !== 401 || originalRequest._retry) {
            return Promise.reject(error);
        }

        // If request is for refresh token and it failed, clear auth and reject
        if (originalRequest.url === '/api/auth/refresh-token') {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/authentication';
            return Promise.reject(error);
        }

        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject });
            })
            .then(() => {
                return api(originalRequest);
            })
            .catch(err => {
                return Promise.reject(err);
            });
        }

        originalRequest._retry = true;
        isRefreshing = true;

        return new Promise((resolve, reject) => {
            const token = localStorage.getItem('token');
            api.post('/api/auth/refresh-token', null, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            .then(response => {
                const newToken = response.data.token;
                localStorage.setItem('token', newToken);
                api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
                processQueue(null);
                resolve(api(originalRequest));
            })
            .catch((err) => {
                processQueue(err);
                reject(err);
                // Clear auth and redirect on refresh failure
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                window.location.href = '/authentication';
            })
            .finally(() => {
                isRefreshing = false;
            });
        });
    }
);

export const setAuthToken = (token: string) => {
    if (token) {
        localStorage.setItem('token', token);
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        localStorage.removeItem('token');
        delete api.defaults.headers.common['Authorization'];
    }
};

export default api;
