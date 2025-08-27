import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:9090', // 后端地址
    withCredentials: false
});

// 自动在每次请求时加上 JWT
api.interceptors.request.use(cfg => {
    const token = localStorage.getItem('token');
    if (token) {
        cfg.headers.Authorization = `Bearer ${token}`;
    }
    return cfg;
});

export const login = async (user) => {
    const res = await api.post('/auth/login', user);
    return res.data;
};

export const register = async (user) => {
    await api.post('/auth/register', user);
};