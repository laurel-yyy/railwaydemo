import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);

  // 获取用户信息
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        // 从localStorage获取用户信息
        const user = JSON.parse(localStorage.getItem('user'));
        setUserData(user);
      } catch (error) {
        console.error('获取用户信息失败:', error);
        // 如果获取信息失败，可能是token已失效
        if (error.response && error.response.status === 401) {
          handleLogout();
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);

  // 退出登录
  const handleLogout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    navigate('/login');
  };

  // 导航到票务查询页面
  const goToTicketSearch = () => {
    navigate('/ticket-search');
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      {/* 头部导航 */}
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <span className="text-xl font-semibold text-gray-800">火车票预订系统</span>
            </div>
            <div className="flex items-center">
              {userData && (
                <div className="flex items-center space-x-4">
                  <span className="text-gray-700">
                    欢迎，{userData.realName || userData.username}
                  </span>
                  <button
                    onClick={handleLogout}
                    className="px-3 py-1 bg-red-500 text-white text-sm rounded-md hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
                  >
                    退出登录
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* 内容区域 */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        {/* 功能卡片区域 */}
        <div className="px-4 py-6 sm:px-0">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {/* 车票查询卡片 */}
            <div className="bg-white overflow-hidden shadow rounded-lg">
              <div className="px-4 py-5 sm:p-6">
                <h3 className="text-lg font-medium text-gray-900">车票查询</h3>
                <div className="mt-2 max-w-xl text-sm text-gray-500">
                  <p>查询车票信息、价格和余票数量。</p>
                </div>
                <div className="mt-5">
                  <button
                    onClick={goToTicketSearch}
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                  >
                    前往查询
                  </button>
                </div>
              </div>
            </div>

            {/* 其他功能卡片可以在这里添加 */}
          </div>
        </div>

        {/* 用户信息区域 */}
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white rounded-lg shadow-md p-6 mt-6">
            <h2 className="text-lg font-medium text-gray-800 mb-4">您的账户信息</h2>
            {userData && (
              <div className="space-y-2">
                <p><span className="font-medium">用户名:</span> {userData.username}</p>
                {userData.realName && (
                  <p><span className="font-medium">真实姓名:</span> {userData.realName}</p>
                )}
                {/* 可以显示更多用户信息 */}
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default Home;