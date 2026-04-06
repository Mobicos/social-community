import { useState, useEffect } from 'react';
import { Card, Avatar, List, Tag, Input, Space, message, Spin, Tabs, Button } from 'antd';
import { UserOutlined, SearchOutlined } from '@ant-design/icons';
import { getFollowingList, getFollowersList, followUser, unfollowUser } from '@/api';
import type { UserFollowing } from '@/types';

export function Users() {
  const [following, setFollowing] = useState<UserFollowing[]>([]);
  const [followers, setFollowers] = useState<UserFollowing[]>([]);
  const [searchText, setSearchText] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [followingRes, followersRes] = await Promise.all([
        getFollowingList(),
        getFollowersList(),
      ]);
      if (followingRes.code === '0') {
        setFollowing(followingRes.data || []);
      }
      if (followersRes.code === '0') {
        setFollowers(followersRes.data || []);
      }
    } catch {
      message.error('加载数据失败');
    } finally {
      setLoading(false);
    }
  };

  const handleFollow = async (followingId: number) => {
    try {
      await followUser({ followingId });
      message.success('关注成功');
      fetchData();
    } catch {
      message.error('关注失败');
    }
  };

  const handleUnfollow = async (followingId: number) => {
    try {
      await unfollowUser(followingId);
      message.success('取消关注成功');
      fetchData();
    } catch {
      message.error('取消关注失败');
    }
  };

  const filteredFollowing = following.filter(
    (u) =>
      (u.userInfo?.nick || '').toLowerCase().includes(searchText.toLowerCase()) ||
      String(u.followingId).includes(searchText)
  );

  const filteredFollowers = followers.filter(
    (u) =>
      (u.userInfo?.nick || '').toLowerCase().includes(searchText.toLowerCase()) ||
      String(u.userId).includes(searchText)
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
                      renderItem={(item) => (
                        <List.Item
                          actions={[
                            <Button
                              key="unfollow"
                              danger
                              size="small"
                              onClick={() => handleUnfollow(item.followingId)}
                            >
                              取消关注
                            </Button>,
                          ]}
                        >
                          <List.Item.Meta
                            avatar={<Avatar src={item.userInfo?.avatar} size={56} />}
                            title={
                              <Space>
                                <span className="font-medium">{item.userInfo?.nick || '用户'}</span>
                                <Tag color="blue">@{item.followingId}</Tag>
                              </Space>
                            }
                            description={
                              <div>
                                <div className="text-gray-500 mb-1">
                                  {item.userInfo?.sign || '这个人很懒，什么都没写'}
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
          {
            key: 'followers',
            label: `我的粉丝 (${followers.length})`,
            children: (
              <Card>
                {filteredFollowers.length === 0 ? (
                  <div className="text-center py-12 text-gray-400">
                    <UserOutlined className="text-4xl mb-4" />
                    <p>暂无粉丝</p>
                  </div>
                ) : (
                  <List
                    dataSource={filteredFollowers}
                    renderItem={(item) => (
                      <List.Item
                        actions={[
                          item.userInfo?.followed ? (
                            <Button
                              key="unfollow"
                              danger
                              size="small"
                              onClick={() => handleUnfollow(item.userId)}
                            >
                              取消关注
                            </Button>
                          ) : (
                            <Button
                              key="follow"
                              type="primary"
                              size="small"
                              onClick={() => handleFollow(item.userId)}
                            >
                              关注
                            </Button>
                          ),
                        ]}
                      >
                        <List.Item.Meta
                          avatar={<Avatar src={item.userInfo?.avatar} size={56} />}
                          title={
                            <Space>
                              <span className="font-medium">{item.userInfo?.nick || '用户'}</span>
                              <Tag color="blue">@{item.userId}</Tag>
                            </Space>
                          }
                          description={
                            <div>
                              <div className="text-gray-500 mb-1">
                                {item.userInfo?.sign || '这个人很懒，什么都没写'}
                              </div>
                            </div>
                          }
                        />
                      </List.Item>
                    )}
                  />
                )}
              </Card>
            ),
          },
        ]}
      />
    </div>
  );
}
