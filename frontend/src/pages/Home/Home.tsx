import { Card, Row, Col, Statistic } from 'antd';
import { UserOutlined, LikeOutlined, SearchOutlined, TeamOutlined } from '@ant-design/icons';

export function Home() {
  return (
    <div>
      <h2 className="text-xl font-bold mb-6">我的动态</h2>

      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="我的好友"
              value={128}
              prefix={<UserOutlined className="text-blue-500" />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="获赞数"
              value={2560}
              prefix={<LikeOutlined className="text-red-500" />}
              valueStyle={{ color: '#ff4d4f' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="动态数"
              value={86}
              prefix={<TeamOutlined className="text-green-500" />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic
              title="关注数"
              value={512}
              prefix={<UserOutlined className="text-purple-500" />}
              valueStyle={{ color: '#722ed1' }}
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
