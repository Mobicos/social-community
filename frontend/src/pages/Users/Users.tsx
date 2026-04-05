import { useState } from 'react';
import { Card, Avatar, List, Button, Tabs, Tag, Input, Space, message } from 'antd';
import { UserOutlined, SearchOutlined, PlusOutlined, MessageOutlined } from '@ant-design/icons';

interface User {
  id: number;
  username: string;
  nickname: string;
  avatar: string;
  signature: string;
  isFollowing: boolean;
  followers: number;
  following: number;
}

// 模拟好友数据
const mockFollowing: User[] = [
  { id: 1, username: 'zhangsan', nickname: '张三', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1', signature: '热爱生活，享受每一天', isFollowing: true, followers: 128, following: 256 },
  { id: 2, username: 'lisi', nickname: '李四', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2', signature: '代码改变世界', isFollowing: true, followers: 512, following: 128 },
  { id: 3, username: 'wangwu', nickname: '王五', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3', signature: '户外运动爱好者', isFollowing: true, followers: 1024, following: 64 },
];

const mockRecommendations: User[] = [
  { id: 4, username: 'zhaoliu', nickname: '赵六', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=4', signature: '美食探店达人', isFollowing: false, followers: 2048, following: 32 },
  { id: 5, username: 'sunqi', nickname: '孙七', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=5', signature: '摄影爱好者', isFollowing: false, followers: 5120, following: 128 },
  { id: 6, username: 'zhouba', nickname: '周八', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=6', signature: '旅行博主', isFollowing: false, followers: 8192, following: 256 },
];

export function Users() {
  const [following, setFollowing] = useState(mockFollowing);
  const [recommendations, setRecommendations] = useState(mockRecommendations);
  const [searchText, setSearchText] = useState('');

  const handleFollow = (userId: number, isRecommendation: boolean) => {
    if (isRecommendation) {
      const user = recommendations.find((u) => u.id === userId);
      if (user) {
        setRecommendations(recommendations.filter((u) => u.id !== userId));
        setFollowing([...following, { ...user, isFollowing: true }]);
        message.success(`已关注 ${user.nickname}`);
      }
    } else {
      setFollowing(following.filter((u) => u.id !== userId));
      message.success('已取消关注');
    }
  };

  const filteredFollowing = following.filter(
    (u) =>
      u.nickname.toLowerCase().includes(searchText.toLowerCase()) ||
      u.username.toLowerCase().includes(searchText.toLowerCase())
  );

  const UserItem = ({ user, isRecommendation }: { user: User; isRecommendation: boolean }) => (
    <List.Item
      actions={[
        <Button
          key="follow"
          type={user.isFollowing ? 'default' : 'primary'}
          icon={user.isFollowing ? undefined : <PlusOutlined />}
          onClick={() => handleFollow(user.id, isRecommendation)}
        >
          {user.isFollowing ? '已关注' : '关注'}
        </Button>,
        <Button key="message" icon={<MessageOutlined />}>
          私信
        </Button>,
      ]}
    >
      <List.Item.Meta
        avatar={<Avatar src={user.avatar} size={56} />}
        title={
          <Space>
            <span className="font-medium">{user.nickname}</span>
            <Tag color="blue">@{user.username}</Tag>
          </Space>
        }
        description={
          <div>
            <div className="text-gray-500 mb-1">{user.signature}</div>
            <Space className="text-xs text-gray-400">
              <span>粉丝 {user.followers}</span>
              <span>关注 {user.following}</span>
            </Space>
          </div>
        }
      />
    </List.Item>
  );

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
                  <List
                    dataSource={filteredFollowing}
                    renderItem={(user) => <UserItem user={user} isRecommendation={false} />}
                    locale={{ emptyText: '暂无关注的用户' }}
                  />
                </Card>
              </>
            ),
          },
          {
            key: 'recommendations',
            label: '推荐关注',
            children: (
              <Card>
                <List
                  dataSource={recommendations}
                  renderItem={(user) => <UserItem user={user} isRecommendation={true} />}
                  locale={{ emptyText: '暂无推荐' }}
                />
              </Card>
            ),
          },
        ]}
      />
    </div>
  );
}
