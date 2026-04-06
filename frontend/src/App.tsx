import { RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import { router } from '@/router';
import { useUserStore } from '@/stores';
import { useEffect } from 'react';
import { getCurrentUser } from '@/api';

// 创建 React Query 客户端
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

// 初始化用户状态
function initUser() {
  const token = localStorage.getItem('token');
  if (token) {
    useUserStore.getState().setToken(token);
    // 异步获取用户信息
    getCurrentUser()
      .then((res) => {
        if (res.code === '0' && res.data) {
          useUserStore.getState().setUser(res.data);
        }
      })
      .catch(() => {
        // 获取用户信息失败，清除 token
        localStorage.removeItem('token');
        useUserStore.getState().logout();
      });
  }
}

function App() {
  // 应用启动时初始化用户状态
  useEffect(() => {
    initUser();
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      <ConfigProvider
        locale={zhCN}
        theme={{
          token: {
            colorPrimary: '#1890ff',
            borderRadius: 6,
          },
        }}
      >
        <RouterProvider router={router} />
      </ConfigProvider>
    </QueryClientProvider>
  );
}

export default App;
