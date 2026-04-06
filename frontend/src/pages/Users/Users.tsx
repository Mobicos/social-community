import { useState, useEffect } from 'react';
import { Card, Avatar, List, Tag, Input, Space, message, Spin, Tabs } from 'antd';
import { UserOutlined, SearchOutlined } from '@ant-design/icons';
import { getFollowingList } from '@/api';
import type { User } from '@/types';

interface ExtendedUser extends User {
  followed?: boolean;
}

export function Users() {
  const [following, setFollowing] = useState<ExtendedUser[]>([]);
  const [searchText, setSearchText] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFollowing();
  }, []);

  const fetchFollowing = async () => {
    setLoading(true);
    try {
      const res = await getFollowingList();
      if (res.code === '0') {
        setFollowing(res.data || []);
      }
    } catch {
      message.error('加载关注列表失败');
    } finally {
      setLoading(false);
    }
  };

  const filteredFollowing = following.filter(
    (u) =>
      (u.userInfo?.nick || '').toLowerCase().includes(searchText.toLowerCase()) ||
      (u.email || '').toLowerCase().includes(searchText.toLowerCase())
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div>
      <h2 className="text-xl font-bold mb-6">好友</h2>

      <Tabs
        items={[
          {
            key: 'following',
            label: `我的关注 (${following.length})`,
            children: (
              <>
                <Input
                  placeholder="搜索好友..."
                  prefix={<SearchOutlined />}
                  value={searchText}
                  onChange={(e) => setSearchText(e.target.value)}
                  className="mb-4"
                  allowClear
                />
                <Card>
                  {filteredFollowing.length === 0 ? (
                    <div className="text-center py-12 text-gray-400">
                      <UserOutlined className="text-4xl mb-4" />
                      <p>暂无关注的用户</p>
                    </div>
                  ) : (
                    <List
                      dataSource={filteredFollowing}
                      renderItem={(user) => (
                        <List.Item>
                          <List.Item.Meta
                            avatar={<Avatar src={user.userInfo?.avatar} size={56} />}
                            title={
                              <Space>
                                <span className="font-medium">{user.userInfo?.nick || '用户'}</span>
                                <Tag color="blue">@{user.email || user.id}</Tag>
                              </Space>
                            }
                            description={
                              <div>
                                <div className="text-gray-500 mb-1">
                                  {user.userInfo?.sign || '这个人很懒，什么都没写'}
                                </div>
                              </div>
                            }
                          />
                        </List.Item>
                      )}
                    />
                  )}
                </Card>
              </>
            ),
          },
        ]}
      />
    </div>
  );
}
