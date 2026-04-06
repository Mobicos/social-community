import { useState, useEffect } from 'react';
import { Card, Avatar, List, Button, message, Spin } from 'antd';
import { SendOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';
import { useUserStore } from '@/stores';
import { getMoments, addMoment } from '@/api';
import type { UserMoment } from '@/types';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

export function Moments() {
  const { user } = useUserStore();
  const [moments, setMoments] = useState<UserMoment[]>([]);
  const [loading, setLoading] = useState(true);
  const [publishing, setPublishing] = useState(false);

  const fetchMoments = async () => {
    setLoading(true);
    try {
      const res = await getMoments();
      if (res.code === '0') {
        setMoments(res.data || []);
      }
    } catch {
      message.error('获取动态失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMoments();
  }, []);

  const handlePublish = async () => {
    setPublishing(true);
    try {
      const res = await addMoment({
        type: 2,
        contentId: Date.now(),
      });

      if (res.code === '0') {
        message.success('发布成功');
        fetchMoments();
      }
    } catch {
      message.error('发布失败');
    } finally {
      setPublishing(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div>
      <div className="mb-4 flex justify-between items-center">
        <h2 className="text-xl font-bold">动态</h2>
        <Button
          type="primary"
          icon={<SendOutlined />}
          onClick={handlePublish}
          loading={publishing}
        >
          发布动态
        </Button>
      </div>

      {moments.length === 0 ? (
        <div className="text-center py-12 text-gray-400">
          <p>暂无动态</p>
        </div>
      ) : (
        <List
          dataSource={moments}
          renderItem={(moment) => (
            <Card className="mb-4" key={moment.id}>
              <div className="flex gap-3">
                <Avatar src={user?.userInfo?.avatar} size={48} />
                <div className="flex-1">
                  <div className="font-medium text-gray-800">
                    {user?.userInfo?.nick || '用户'}
                  </div>
                  <div className="text-xs text-gray-400">
                    {dayjs(moment.createTime).fromNow()}
                  </div>
                </div>
              </div>

              <div className="mt-3 text-gray-700">
                动态 #{moment.contentId}
              </div>
            </Card>
          )}
        />
      )}
    </div>
  );
}
