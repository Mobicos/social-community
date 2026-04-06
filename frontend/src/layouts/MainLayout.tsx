import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { Layout, Menu, Dropdown, Avatar, Space } from 'antd';
import {
  UserOutlined,
  HomeOutlined,
  SettingOutlined,
  LogoutOutlined,
  SearchOutlined,
  FileImageOutlined,
} from '@ant-design/icons';
import { useUserStore } from '@/stores';

const { Header, Content, Sider } = Layout;

const menuItems = [
  {
    key: '/home',
    icon: <HomeOutlined />,
    label: '首页',
  },
  {
    key: '/users',
    icon: <UserOutlined />,
    label: '好友',
  },
  {
    key: '/moments',
    icon: <FileImageOutlined />,
    label: '动态',
  },
  {
    key: '/search',
    icon: <SearchOutlined />,
    label: '搜索',
  },
];

export function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useUserStore();

  const handleMenuClick = (key: string) => {
    navigate(key);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      onClick: () => navigate('/profile'),
    },
    {
      key: 'settings',
      icon: <SettingOutlined />,
      label: '设置',
      onClick: () => navigate('/settings'),
    },
    {
      type: 'divider' as const,
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  return (
    <Layout className="min-h-screen">
      <Sider width={220} className="bg-white shadow-md">
        <div className="h-16 flex items-center justify-center border-b border-gray-100">
          <h1 className="text-xl font-bold text-blue-600">朋友圈社区</h1>
        </div>
        <Menu
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={({ key }) => handleMenuClick(key)}
          className="border-none"
        />
      </Sider>
      <Layout>
        <Header className="bg-white px-6 flex items-center justify-between shadow-sm">
          <div className="text-lg font-medium text-gray-800">
            {menuItems.find((item) => item.key === location.pathname)?.label || '首页'}
          </div>
          <Space>
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
              <div className="flex items-center cursor-pointer hover:bg-gray-50 px-3 py-2 rounded-lg">
                <Avatar src={user?.userInfo?.avatar} icon={<UserOutlined />} className="bg-blue-500" />
                <span className="ml-2 text-gray-700">{user?.userInfo?.nick || '用户'}</span>
              </div>
            </Dropdown>
          </Space>
        </Header>
        <Content className="m-6 p-6 bg-white rounded-lg shadow-sm min-h-[calc(100vh-112px)]">
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
