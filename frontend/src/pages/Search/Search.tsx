import { useState } from 'react';
import { Card, Input, Button, List, Avatar, Tag, Empty, Spin, Tabs, message } from 'antd';
import { SearchOutlined, PictureOutlined, FileTextOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';
import { searchBooksByAuthor, searchVideos } from '@/api';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

interface SearchResult {
  id: number;
  type: 'book' | 'video';
  title: string;
  content: string;
  author?: string;
  rating?: number;
  createTime: string;
}

export function Search() {
  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState<SearchResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [activeTab, setActiveTab] = useState('all');

  const handleSearch = async () => {
    if (!keyword.trim()) {
      message.warning('请输入搜索关键词');
      return;
    }

    setLoading(true);
    try {
      // 搜索书籍（按作者搜索）
      const booksRes = await searchBooksByAuthor(keyword);
      const searchResults: SearchResult[] = [];

      if (booksRes.code === '0' && booksRes.data) {
        booksRes.data.forEach((book: unknown) => {
          const b = book as Record<string, unknown>;
          searchResults.push({
            id: b.id as number,
            type: 'book',
            title: (b.title as string) || '未知书名',
            content: (b.author as string) || '未知作者',
            author: b.author as string,
            rating: b.rating as number,
            createTime: (b.createTime as string) || dayjs().format('YYYY-MM-DD'),
          });
        });
      }

      // 搜索视频
      const videosRes = await searchVideos({
        keyword: keyword,
        pubtime_begin_s: 0,
        pubtime_end_s: Date.now() / 1000,
        order: 'desc',
        duration: 0,
        limit: 10,
        offset: 0,
      });

      if (videosRes.code === '0' && videosRes.data) {
        videosRes.data.forEach((video: unknown) => {
          const v = video as Record<string, unknown>;
          searchResults.push({
            id: v.id as number,
            type: 'video',
            title: (v.title as string) || '未知视频',
            content: (v.description as string) || '暂无描述',
            createTime: (v.createTime as string) || dayjs().format('YYYY-MM-DD'),
          });
        });
      }

      setResults(searchResults);
      setLoading(false);
      setSearched(true);
    } catch {
      message.error('搜索失败');
      setLoading(false);
    }
  };

  const filteredResults =
    activeTab === 'all' ? results : results.filter((r) => r.type === activeTab);

  const renderIcon = (type: string) => {
    switch (type) {
      case 'book':
        return <FileTextOutlined className="text-blue-500" />;
      case 'video':
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
            placeholder="搜索书籍（作者）、视频..."
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
            { key: 'book', label: `书籍 (${results.filter((r) => r.type === 'book').length})` },
            { key: 'video', label: `视频 (${results.filter((r) => r.type === 'video').length})` },
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
                      <Avatar icon={renderIcon(item.type)} size={48} className="bg-gray-100" />
                    }
                    title={
                      <span>
                        {item.title}
                        <Tag color={item.type === 'book' ? 'blue' : 'purple'} className="ml-2">
                          {item.type === 'book' ? '书籍' : '视频'}
                        </Tag>
                      </span>
                    }
                    description={
                      <div>
                        <div>
                          {item.type === 'book' && item.author && (
                            <span className="text-gray-500 mr-2">作者：{item.author}</span>
                          )}
                          {item.type === 'book' && item.rating !== undefined && (
                            <span className="text-yellow-500">★ {item.rating}</span>
                          )}
                        </div>
                        <div>{item.content}</div>
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
