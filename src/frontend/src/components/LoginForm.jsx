import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const LoginForm = () => {
    const [form, setForm] = useState({ username: '', password: '' });
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(form.username, form.password);
            navigate('/dashboard');
        } catch (err) {
            alert('Login failed');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                placeholder="用户名"
                value={form.username}
                onChange={e => setForm({...form, username: e.target.value})}
            />
            <input
                placeholder="密码"
                type="password"
                value={form.password}
                onChange={e => setForm({...form, password: e.target.value})}
            />
            <button type="submit">登录</button>
        </form>
    );
};

export default LoginForm;