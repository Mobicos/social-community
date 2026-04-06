import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { User } from '@/types';
import { logout as apiLogout } from '@/api';

interface UserState {
  user: User | null;
  token: string | null;
  refreshToken: string | null;
  setUser: (user: User | null) => void;
  setToken: (token: string | null) => void;
  setRefreshToken: (refreshToken: string | null) => void;
  logout: () => Promise<void>;
  isAuthenticated: () => boolean;
}

export const useUserStore = create<UserState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      refreshToken: null,
      setUser: (user) => set({ user }),
      setToken: (token) => set({ token }),
      setRefreshToken: (refreshToken) => set({ refreshToken }),
      logout: async () => {
        try {
          await apiLogout();
        } catch {
          // 退出失败也清除本地状态
        } finally {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          set({ user: null, token: null, refreshToken: null });
        }
      },
      isAuthenticated: () => !!get().token,
    }),
    {
      name: 'user-storage',
      partialize: (state) => ({ user: state.user, token: state.token, refreshToken: state.refreshToken }),
    }
  )
);
