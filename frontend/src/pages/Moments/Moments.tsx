import { useState, useEffect } from 'react';
import { Card, Avatar, List, Button, Input, Image, Tooltip, Modal, Form, message, Spin } from 'antd';
import { SendOutlined, LikeOutlined, LikeFilled, CommentOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';
import { useUserStore } from '@/stores';
import { getMoments, addMoment } from '@/api';
import type { UserMoment } from '@/types';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

interface ExtendedMoment extends UserMoment {
  nickname?: string;
  avatar?: string;
  content?: string;
  images?: string[];
  likes?: number;
  isLiked?: boolean;
  comments?: Comment[];
  displayContent?: string;
}

interface Comment {
  id: number;
  username: string;
  content: string;
  createTime: string;
}

export function Moments() {
  const { user } = useUserStore();
  const [moments, setMoments] = useState<ExtendedMoment[]>([]);
  const [loading, setLoading] = useState(true);
  const [publishModalVisible, setPublishModalVisible] = useState(false);
  const [commentText, setCommentText] = useState<Record<number, string>>({});
  const [form] = Form.useForm();

  const fetchMoments = async () => {
    setLoading(true);
    try {
      const res = await getMoments();
      if (res.code === '0') {
        // 将后端返回的数据转换为前端需要的格式
        const formattedMoments = res.data.map((m: UserMoment) => ({
          ...m,
          nickname: user?.userInfo?.nick || '用户',
          avatar: user?.userInfo?.avatar || '',
          displayContent: '动态内容',
          likes: 0,
          isLiked: false,
          comments: [],
        }));
        setMoments(formattedMoments);
      }
    } catch {
      message.error('获取动态失败');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMoments();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleLike = async (id: number) => {
    // 点赞功能暂未实现，仅更新 UI 状态
    setMoments(
      moments.map((m) =>
        m.id === id
          ? {
              ...m,
              isLiked: !m.isLiked,
              likes: m.isLiked ? (m.likes || 0) - 1 : (m.likes || 0) + 1,
            }
          : m
      )
    );
    message.info('点赞功能开发中');
  };

  const handleComment = async (momentId: number) => {
    const text = commentText[momentId];
    if (!text?.trim()) return;

    // 评论功能暂未实现，仅更新 UI 状态
    setMoments(
      moments.map((m) =>
        m.id === momentId
          ? {
              ...m,
              comments: [
                ...(m.comments || []),
                {
                  id: Date.now(),
                  username: '我',
                  content: text,
                  createTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
                },
              ],
            }
          : m
      )
    );
    setCommentText({ ...commentText, [momentId]: '' });
    message.success('评论功能开发中');
  };

  const handlePublish = async () => {
    try {
      const values = await form.validateFields();
      const res = await addMoment({
        content: values.content,
        type: 2, // 2-专栏动态
      });
      if (res.code === '0') {
        setPublishModalVisible(false);
        form.resetFields();
        message.success('发布成功');
        fetchMoments();
      }
    } catch {
      message.error('发布失败');
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
        <Button type="primary" icon={<SendOutlined />} onClick={() => setPublishModalVisible(true)}>
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
                <Avatar src={moment.avatar} size={48} />
                <div className="flex-1">
                  <div className="font-medium text-gray-800">{moment.nickname || '用户'}</div>
                  <div className="text-xs text-gray-400">{dayjs(moment.createTime).fromNow()}</div>
                </div>
              </div>

              {moment.displayContent && (
                <div className="mt-3 text-gray-700 leading-relaxed">{moment.displayContent}</div>
              )}

              {moment.images && moment.images.length > 0 && (
                <div className="mt-3 flex flex-wrap gap-2">
                  <Image.PreviewGroup>
                    {moment.images.map((img, idx) => (
                      <Image
                        key={idx}
                        src={img}
                        width={120}
                        height={120}
                        className="object-cover rounded-lg"
                      />
                    ))}
                  </Image.PreviewGroup>
                </div>
              )}

              <div className="mt-4 flex items-center gap-6 text-gray-500">
                <Tooltip title={moment.isLiked ? '取消点赞' : '点赞'}>
                  <span
                    className={`cursor-pointer flex items-center gap-1 ${moment.isLiked ? 'text-red-500' : 'hover:text-red-500'}`}
                    onClick={() => handleLike(moment.id)}
                  >
                    {moment.isLiked ? <LikeFilled /> : <LikeOutlined />}
                    <span>{moment.likes || 0}</span>
                  </span>
                </Tooltip>
                <span className="flex items-center gap-1">
                  <CommentOutlined />
                  <span>{moment.comments?.length || 0}</span>
                </span>
              </div>

              {moment.comments && moment.comments.length > 0 && (
                <div className="mt-4 bg-gray-50 rounded-lg p-3">
                  {moment.comments.map((comment) => (
                    <div key={comment.id} className="mb-2 last:mb-0">
                      <span className="text-blue-600 font-medium">{comment.username}</span>
                      <span className="text-gray-500">：{comment.content}</span>
                      <span className="text-xs text-gray-400 ml-2">
                        {dayjs(comment.createTime).fromNow()}
                      </span>
                    </div>
                  ))}
                </div>
              )}

              <div className="mt-3 flex gap-2">
                <Input
                  placeholder="写评论..."
                  value={commentText[moment.id] || ''}
                  onChange={(e) => setCommentText({ ...commentText, [moment.id]: e.target.value })}
                  onPressEnter={() => handleComment(moment.id)}
                />
                <Button type="primary" onClick={() => handleComment(moment.id)}>
                  发送
                </Button>
              </div>
            </Card>
          )}
        />
      )}

      <Modal
        title="发布动态"
        open={publishModalVisible}
        onOk={handlePublish}
        onCancel={() => setPublishModalVisible(false)}
        okText="发布"
        cancelText="取消"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="content"
            label="内容"
            rules={[{ required: true, message: '请输入动态内容' }]}
          >
            <Input.TextArea rows={4} placeholder="分享你的想法..." maxLength={500} showCount />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
