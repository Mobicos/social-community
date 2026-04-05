import { useState } from 'react';
import { Card, Input, Button, List, Avatar, Tag, Empty, Spin, Tabs, Image } from 'antd';
import { SearchOutlined, UserOutlined, PictureOutlined, FileTextOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

interface SearchResult {
  id: number;
  type: 'user' | 'moment' | 'image';
  title: string;
  content: string;
  avatar?: string;
  images?: string[];
  createTime: string;
}

// 模拟搜索结果
const mockResults: SearchResult[] = [
  { id: 1, type: 'user', title: '张三', content: '热爱生活，享受每一天', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1', createTime: '2024-01-15' },
  { id: 2, type: 'moment', title: '张三', content: '今天天气真好，出去走走 🌞', images: ['https://picsum.photos/200/150?random=10'], createTime: '2024-01-15 12:00:00' },
  { id: 3, type: 'user', title: '李四', content: '代码改变世界', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2', createTime: '2024-01-14' },
  { id: 4, type: 'image', title: '风景照', content: '周末爬山拍的', images: ['https://picsum.photos/200/150?random=11', 'https://picsum.photos/200/150?random=12'], createTime: '2024-01-14 17:30:00' },
];

export function Search() {
  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState<SearchResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [activeTab, setActiveTab] = useState('all');

  const handleSearch = async () => {
    if (!keyword.trim()) return;

    setLoading(true);
    // 模拟 API 调用
    await new Promise((resolve) => setTimeout(resolve, 500));
    setResults(mockResults.filter((r) => r.title.includes(keyword) || r.content.includes(keyword)));
    setLoading(false);
    setSearched(true);
  };

  const filteredResults =
    activeTab === 'all' ? results : results.filter((r) => r.type === activeTab);

  const renderIcon = (type: string) => {
    switch (type) {
      case 'user':
        return <UserOutlined className="text-blue-500" />;
      case 'moment':
        return <FileTextOutlined className="text-green-500" />;
      case 'image':
        return <PictureOutlined className="text-purple-500" />;
      default:
        return null;
    }
  };

  return (
    <div>
      <h2 className="text-xl font-bold mb-6">搜索</h2>

      <Card className="mb-6">
        <div className="flex gap-4">
          <Input
            size="large"
            placeholder="搜索用户、动态、图片..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onPressEnter={handleSearch}
            prefix={<SearchOutlined className="text-gray-400" />}
            className="flex-1"
          />
          <Button type="primary" size="large" onClick={handleSearch} loading={loading}>
            搜索
          </Button>
        </div>
      </Card>

      {searched && (
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          items={[
            { key: 'all', label: `全部 (${results.length})` },
            { key: 'user', label: `用户 (${results.filter((r) => r.type === 'user').length})` },
            { key: 'moment', label: `动态 (${results.filter((r) => r.type === 'moment').length})` },
            { key: 'image', label: `图片 (${results.filter((r) => r.type === 'image').length})` },
          ]}
        />
      )}

      <Card>
        {loading ? (
          <div className="text-center py-12">
            <Spin size="large" />
          </div>
        ) : searched ? (
          filteredResults.length > 0 ? (
            <List
              dataSource={filteredResults}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={
                      item.avatar ? (
                        <Avatar src={item.avatar} size={48} />
                      ) : (
                        <Avatar icon={renderIcon(item.type)} size={48} className="bg-gray-100" />
                      )
                    }
                    title={
                      <span>
                        {item.title}
                        <Tag color={item.type === 'user' ? 'blue' : item.type === 'moment' ? 'green' : 'purple'} className="ml-2">
                          {item.type === 'user' ? '用户' : item.type === 'moment' ? '动态' : '图片'}
                        </Tag>
                      </span>
                    }
                    description={
                      <div>
                        <div>{item.content}</div>
                        {item.images && item.images.length > 0 && (
                          <div className="mt-2 flex gap-2">
                            {item.images.map((img, idx) => (
                              <Image key={idx} src={img} width={60} height={60} className="object-cover rounded" />
                            ))}
                          </div>
                        )}
                        <div className="text-xs text-gray-400 mt-1">{dayjs(item.createTime).fromNow()}</div>
                      </div>
                    }
                  />
                </List.Item>
              )}
            />
          ) : (
            <Empty description="未找到相关结果" />
          )
        ) : (
          <div className="text-center text-gray-400 py-12">
            <SearchOutlined className="text-4xl mb-4" />
            <p>搜索你感兴趣的内容</p>
          </div>
        )}
      </Card>
    </div>
  );
}
