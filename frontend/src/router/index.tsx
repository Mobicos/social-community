import { createBrowserRouter, Navigate } from 'react-router-dom';
import { MainLayout } from '@/layouts';
import { ProtectedRoute } from '@/components';
import { Login, Home, Users, Moments, Search, NotFound } from '@/pages';

export const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login />,
  },
  {
    path: '/',
    element: (
      <ProtectedRoute>
        <MainLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <Navigate to="/home" replace />,
      },
      {
        path: 'home',
        element: <Home />,
      },
      {
        path: 'users',
        element: <Users />,
      },
      {
        path: 'moments',
        element: <Moments />,
      },
      {
        path: 'search',
        element: <Search />,
      },
    ],
  },
  {
    path: '*',
    element: <NotFound />,
  },
]);
