import { Navigate, useLocation } from 'react-router-dom';
import { useUserStore } from '@/stores';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

export function ProtectedRoute({ children }: ProtectedRouteProps) {
  const { token } = useUserStore();
  const location = useLocation();

  if (!token) {
    // 重定向到登录页，并保存当前路径
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
}
