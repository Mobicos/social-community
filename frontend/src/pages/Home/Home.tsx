import { useState, useEffect } from 'react';
import { Card, Row, Col, Statistic, Spin } from 'antd';
import { UserOutlined, LikeOutlined, SearchOutlined, TeamOutlined } from '@ant-design/icons';
import { useUserStore } from '@/stores';
import { getFollowingList, getFollowersList, getMoments } from '@/api';

interface Stats {
  following: number;
  followers: number;
  moments: number;
  likes: number;
}

export function Home() {
  const { user } = useUserStore();
  const [stats, setStats] = useState<Stats>({
    following: 0,
    followers: 0,
    moments: 0,
    likes: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [followingRes, followersRes, momentsRes] = await Promise.all([
          getFollowingList(),
          getFollowersList(),
          getMoments(),
        ]);

        if (followingRes.code === '0') {
          setStats(prev => ({
            ...prev,
            following: followingRes.data?.length || 0,
          }));
        }

        if (followersRes.code === '0') {
          setStats(prev => ({
            ...prev,
            followers: followersRes.data?.length || 0,
          }));
        }

        if (momentsRes.code === '0') {
          setStats(prev => ({
            ...prev,
            moments: momentsRes.data?.length || 0,
          }));
        }
      } catch (error) {
        console.error('获取统计数据失败:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div>
      <h2 className="text-xl font-bold mb-6">
        {user?.userInfo?.nick || '欢迎'}，你的数据概览
      </h2>

      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="我的关注"
              value={stats.following}
              prefix={<UserOutlined className="text-blue-500" />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="我的粉丝"
              value={stats.followers}
              prefix={<TeamOutlined className="text-green-500" />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="动态数"
              value={stats.moments}
              prefix={<SearchOutlined className="text-orange-500" />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="获赞数"
              value={stats.likes}
              prefix={<LikeOutlined className="text-red-500" />}
              valueStyle={{ color: '#ff4d4f' }}
            />
          </Card>
        </Col>
      </Row>

      <Card className="mt-6" title="欢迎使用朋友圈社区">
        <div className="text-gray-500">
          <p>在这里，你可以：</p>
          <ul className="list-disc list-inside mt-2 space-y-1">
            <li>发布动态 - 分享你的生活点滴</li>
            <li>添加好友 - 认识更多有趣的人</li>
            <li>浏览动态 - 看看朋友们在做什么</li>
            <li>搜索内容 - 找到你感兴趣的话题</li>
          </ul>
        </div>
      </Card>
    </div>
  );
}
