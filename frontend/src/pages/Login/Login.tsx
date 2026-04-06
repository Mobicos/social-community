import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Form, Input, Button, Card, message, Tabs } from 'antd';
import { LockOutlined, MailOutlined, PhoneOutlined, UserOutlined } from '@ant-design/icons';
import { loginDts, register, getCurrentUser, getRsaPublicKey } from '@/api';
import { useUserStore } from '@/stores';
import type { LoginRequest } from '@/types';
import forge from 'node-forge';

interface LocationState {
  from?: { pathname: string };
}

// 后端硬编码的 RSA 公钥
const BACKEND_PUBLIC_KEY = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPIRFhtJorDVqC4LyrITnjWZABfKrZzauisnp3BNGzCWWFZ5hqtJSt39D/QSV5Wx/BWcnz+YnOzQ4xHWY6VE7Zmw9yVc4H8Y1G2lBJ4lAia7EZVlkgP3N1LHlgERIpgHnasnJoYpubM/g1Ne28EiZUHRyfO+qsuCr710vvdFL5HwIDAQAB';

export function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const { setUser, setToken, setRefreshToken } = useUserStore();
  const [loading, setLoading] = useState(false);
  const [activeTab, setActiveTab] = useState('login');
  const [rsaPublicKey, setRsaPublicKey] = useState<string>('');

  // 获取 RSA 公钥
  useEffect(() => {
    const fetchPublicKey = async () => {
      try {
        const res = await getRsaPublicKey();
        if (res.code === '0') {
          setRsaPublicKey(res.data);
        }
      } catch {
        console.warn('获取公钥失败，将使用后端硬编码公钥');
      }
    };
    fetchPublicKey();
  }, []);

  // 使用 node-forge 进行 RSA 加密
  const encryptPassword = (password: string): string => {
    try {
      // 优先使用后端返回的公钥，否则使用硬编码的公钥
      const publicKeyBase64 = rsaPublicKey || BACKEND_PUBLIC_KEY;

      // 构造 PEM 格式的公钥
      const pemFormattedKey = `-----BEGIN PUBLIC KEY-----\n${publicKeyBase64}\n-----END PUBLIC KEY-----`;

      // 使用 node-forge 解析公钥
      const pubKey = forge.pki.publicKeyFromPem(pemFormattedKey);

      // 加密密码（使用 PKCS#1 v1.5 填充，与 Java 默认一致）
      const encryptedBytes = pubKey.encrypt(password, 'RSAES-PKCS1-V1_5');

      // 转换为 Base64
      const encryptedBase64 = forge.util.encode64(encryptedBytes);
      return encryptedBase64;
    } catch (error) {
      console.error('RSA 加密失败:', error);
      return password; // 加密失败返回明文
    }
  };

  const handleLogin = async (values: LoginRequest) => {
    setLoading(true);
    try {
      // 加密密码
      const encryptedPassword = encryptPassword(values.password);
      const loginData = {
        ...values,
        password: encryptedPassword,
      };

      const res = await loginDts(loginData);
      if (res.code === '0' && res.data) {
        const { accessToken, refreshToken } = res.data;
        setToken(accessToken);
        setRefreshToken(refreshToken);
        localStorage.setItem('token', accessToken);
        localStorage.setItem('refreshToken', refreshToken);

        // 获取用户信息
        try {
          const userRes = await getCurrentUser();
          if (userRes.code === '0' && userRes.data) {
            setUser(userRes.data);
          }
        } catch {
          console.warn('获取用户信息失败');
        }

        message.success('登录成功');
        const from = (location.state as LocationState)?.from?.pathname || '/home';
        navigate(from, { replace: true });
      }
    } catch {
      // 错误已在拦截器中处理
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (values: { phone?: string; email?: string; password: string }) => {
    setLoading(true);
    try {
      // 加密密码
      const encryptedPassword = encryptPassword(values.password);
      const registerData = {
        phone: values.phone,
        email: values.email,
        password: encryptedPassword,
      };

      const res = await register(registerData);
      if (res.code === '0') {
        message.success('注册成功，请登录');
        setActiveTab('login');
      }
    } catch {
      // 错误已在拦截器中处理
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-500 to-purple-600">
      <Card className="w-[400px] shadow-2xl">
        <div className="text-center mb-6">
          <h1 className="text-2xl font-bold text-gray-800">朋友圈社区</h1>
          <p className="text-gray-500 mt-2">分享生活，连接你我</p>
        </div>

        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          centered
          items={[
            {
              key: 'login',
              label: '登录',
              children: (
                <Form onFinish={handleLogin} size="large">
                  <Form.Item
                    name="phone"
                    rules={[{ required: true, message: '请输入手机号或邮箱' }]}
                  >
                    <Input prefix={<UserOutlined />} placeholder="手机号/邮箱" />
                  </Form.Item>
                  <Form.Item
                    name="password"
                    rules={[{ required: true, message: '请输入密码' }]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="密码" />
                  </Form.Item>
                  <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading} block>
                      登录
                    </Button>
                  </Form.Item>
                </Form>
              ),
            },
            {
              key: 'register',
              label: '注册',
              children: (
                <Form onFinish={handleRegister} size="large">
                  <Form.Item
                    name="phone"
                    rules={[{ required: true, message: '请输入手机号' }]}
                  >
                    <Input prefix={<PhoneOutlined />} placeholder="手机号" />
                  </Form.Item>
                  <Form.Item
                    name="email"
                    rules={[{ required: true, message: '请输入邮箱' }]}
                  >
                    <Input prefix={<MailOutlined />} placeholder="邮箱" />
                  </Form.Item>
                  <Form.Item
                    name="password"
                    rules={[{ required: true, message: '请输入密码' }]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="密码" />
                  </Form.Item>
                  <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading} block>
                      注册
                    </Button>
                  </Form.Item>
                </Form>
              ),
            },
          ]}
        />
      </Card>
    </div>
  );
}
