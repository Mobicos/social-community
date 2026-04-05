import { useState } from 'react';
import { Card, Avatar, List, Button, Input, Space, Image, Tooltip, Modal, Form, message } from 'antd';
import { LikeOutlined, LikeFilled, CommentOutlined, SendOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/zh-cn';

dayjs.extend(relativeTime);
dayjs.locale('zh-cn');

interface Moment {
  id: number;
  userId: number;
  username: string;
  avatar: string;
  content: string;
  images: string[];
  likes: number;
  isLiked: boolean;
  comments: Comment[];
  createTime: string;
}

interface Comment {
  id: number;
  userId: number;
  username: string;
  content: string;
  createTime: string;
}

// 模拟动态数据
const mockMoments: Moment[] = [
  {
    id: 1,
    userId: 1,
    username: '张三',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=1',
    content: '今天天气真好，出去走走 🌞',
    images: ['https://picsum.photos/400/300?random=1', 'https://picsum.photos/400/300?random=2'],
    likes: 42,
    isLiked: false,
    comments: [
      { id: 1, userId: 2, username: '李四', content: '确实很棒！', createTime: '2024-01-15 14:30:00' },
    ],
    createTime: '2024-01-15 12:00:00',
  },
  {
    id: 2,
    userId: 2,
    username: '李四',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
    content: '刚刚完成了一个新项目，分享一下成果 💪',
    images: ['https://picsum.photos/400/300?random=3'],
    likes: 88,
    isLiked: true,
    comments: [],
    createTime: '2024-01-15 10:30:00',
  },
  {
    id: 3,
    userId: 3,
    username: '王五',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
    content: '周末去爬山了，风景太美了！推荐大家也去看看 🏔️',
    images: ['https://picsum.photos/400/300?random=4', 'https://picsum.photos/400/300?random=5', 'https://picsum.photos/400/300?random=6'],
    likes: 156,
    isLiked: false,
    comments: [
      { id: 2, userId: 1, username: '张三', content: '在哪里呀？', createTime: '2024-01-14 18:00:00' },
      { id: 3, userId: 3, username: '王五', content: '就在市郊的青云山', createTime: '2024-01-14 18:05:00' },
    ],
    createTime: '2024-01-14 17:30:00',
  },
];

export function Moments() {
  const [moments, setMoments] = useState(mockMoments);
  const [publishModalVisible, setPublishModalVisible] = useState(false);
  const [commentText, setCommentText] = useState<Record<number, string>>({});
  const [form] = Form.useForm();

  const handleLike = (id: number) => {
    setMoments(
      moments.map((m) =>
        m.id === id
          ? {
              ...m,
              isLiked: !m.isLiked,
              likes: m.isLiked ? m.likes - 1 : m.likes + 1,
            }
          : m
      )
    );
  };

  const handleComment = (momentId: number) => {
    const text = commentText[momentId];
    if (!text?.trim()) return;

    setMoments(
      moments.map((m) =>
        m.id === momentId
          ? {
              ...m,
              comments: [
                ...m.comments,
                {
                  id: Date.now(),
                  userId: 999,
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
    message.success('评论成功');
  };

  const handlePublish = async () => {
    const values = await form.validateFields();
    const newMoment: Moment = {
      id: Date.now(),
      userId: 999,
      username: '我',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=me',
      content: values.content,
      images: [],
      likes: 0,
      isLiked: false,
      comments: [],
      createTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
    };
    setMoments([newMoment, ...moments]);
    setPublishModalVisible(false);
    form.resetFields();
    message.success('发布成功');
  };

  return (
    <div>
      <div className="mb-4 flex justify-between items-center">
        <h2 className="text-xl font-bold">动态</h2>
        <Button type="primary" icon={<SendOutlined />} onClick={() => setPublishModalVisible(true)}>
          发布动态
        </Button>
      </div>

      <List
        dataSource={moments}
        renderItem={(moment) => (
          <Card className="mb-4" key={moment.id}>
            <div className="flex gap-3">
              <Avatar src={moment.avatar} size={48} />
              <div className="flex-1">
                <div className="font-medium text-gray-800">{moment.username}</div>
                <div className="text-xs text-gray-400">{dayjs(moment.createTime).fromNow()}</div>
              </div>
            </div>

            <div className="mt-3 text-gray-700 leading-relaxed">{moment.content}</div>

            {moment.images.length > 0 && (
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
                  <span>{moment.likes}</span>
                </span>
              </Tooltip>
              <span className="flex items-center gap-1">
                <CommentOutlined />
                <span>{moment.comments.length}</span>
              </span>
            </div>

            {moment.comments.length > 0 && (
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
