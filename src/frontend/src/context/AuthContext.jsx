import { createContext, useContext, useState, useEffect } from 'react';
import { login as apiLogin, register as apiRegister } from '../api/auth';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    // 初始加载时检查本地是否有 token
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) setUser({ token }); // 简化：这里只存 token
    }, []);

    const login = async (username, password) => {
        const { token } = await apiLogin({ username, password });
        localStorage.setItem('token', token);
        setUser({ token });
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);