import { Card, Avatar, Descriptions } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { useUserStore } from '@/stores';

export function Profile() {
  const { user } = useUserStore();

  return (
    <div>
      <h2 className="text-xl font-bold mb-6">个人中心</h2>

      <Card>
        <div className="flex items-start gap-8">
          <div className="text-center">
            <Avatar
              size={120}
              src={user?.userInfo?.avatar}
              icon={<UserOutlined />}
              className="bg-blue-500"
            />
          </div>

          <div className="flex-1">
            <Descriptions column={1} labelStyle={{ width: 100 }}>
              <Descriptions.Item label="昵称">{user?.userInfo?.nick || '未设置'}</Descriptions.Item>
              <Descriptions.Item label="手机号">{user?.phone || '未绑定'}</Descriptions.Item>
              <Descriptions.Item label="邮箱">{user?.email || '未绑定'}</Descriptions.Item>
              <Descriptions.Item label="签名">{user?.userInfo?.sign || '这个人很懒，什么都没写'}</Descriptions.Item>
              <Descriptions.Item label="性别">
                {user?.userInfo?.gender === 0 ? '男' : user?.userInfo?.gender === 1 ? '女' : '未知'}
              </Descriptions.Item>
              <Descriptions.Item label="注册时间">{user?.createTime?.split('T')[0] || '-'}</Descriptions.Item>
            </Descriptions>
          </div>
        </div>
      </Card>
    </div>
  );
}
