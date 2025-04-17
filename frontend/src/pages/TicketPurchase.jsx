import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../utils/axios';

const TicketPurchase = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [purchaseData, setPurchaseData] = useState({
    trainId: '',
    passengerId: '',
    seatType: 1,
    departure: '',
    arrival: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [trainInfo, setTrainInfo] = useState(null);
  const [userData, setUserData] = useState(null);

  // 从URL参数获取购票信息
  useEffect(() => {
    try {
      // 从本地存储获取用户信息
      const user = JSON.parse(localStorage.getItem('user'));
      setUserData(user);
      
      // 从URL参数获取数据
      const searchParams = new URLSearchParams(location.search);
      const trainId = searchParams.get('trainId');
      const departure = searchParams.get('departure');
      const arrival = searchParams.get('arrival');
      const trainNumber = searchParams.get('trainNumber');
      const departureTime = searchParams.get('departureTime');
      const arrivalTime = searchParams.get('arrivalTime');
      const seatType = parseInt(searchParams.get('seatType') || '1');
      const price = parseFloat(searchParams.get('price') || '0');
      
      // 验证必要参数
      if (!trainId || !departure || !arrival) {
        throw new Error('缺少必要的购票信息');
      }
      
      // 设置列车信息
      setTrainInfo({
        trainNumber,
        departureTime,
        arrivalTime,
        departure,
        arrival,
        seatType,
        price
      });
      
      // 设置购票数据
      setPurchaseData({
        trainId,
        passengerId: user?.id || '', // 用户ID作为乘客ID
        seatType,
        departure,
        arrival
      });
    } catch (err) {
      console.error('获取购票信息失败:', err);
      setError(err.message || '获取购票信息失败');
    }
  }, [location]);

  // 处理输入变化
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPurchaseData({
      ...purchaseData,
      [name]: value
    });
  };

  // 处理座位类型选择
  const handleSeatTypeChange = (seatType) => {
    setPurchaseData({
      ...purchaseData,
      seatType
    });
  };

  // 获取座位类型的名称
  const getSeatClassName = (type) => {
    const seatClassNames = {
      0: '商务座',
      1: '一等座',
      2: '二等座',
      3: '硬卧',
      4: '软卧',
      5: '硬座',
      13: '动卧',
    };
    return seatClassNames[type] || `类型${type}`;
  };

  // 处理表单提交
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // 检查必要字段
      if (!purchaseData.trainId || !purchaseData.passengerId || !purchaseData.departure || !purchaseData.arrival) {
        throw new Error('请完善购票信息');
      }

      console.log('发送购票请求:', purchaseData);
      
      // 发送购票请求
      const response = await api.post('/ticket/purchase', purchaseData);
      console.log('购票响应:', response);

      // 检查响应
      if (response.code !== 1) {
        throw new Error(response.msg || '购票失败');
      }

      // 显示成功信息
      setSuccess('购票成功！');
      
      // 3秒后返回查询页面
      setTimeout(() => {
        navigate('/ticket-search');
      }, 3000);
    } catch (err) {
      console.error('购票失败:', err);
      setError(err.message || '购票失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  // 返回查询页面
  const handleGoBack = () => {
    navigate('/ticket-search');
  };

  if (!trainInfo) {
    return (
      <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
        <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">加载中...</h2>
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md">
              {error}
            </div>
          )}
          <button
            onClick={handleGoBack}
            className="mt-4 w-full px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2"
          >
            返回查询页面
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100 p-4 sm:p-6 lg:p-8">
      <div className="max-w-3xl mx-auto">
        <div className="bg-white rounded-lg shadow-md overflow-hidden">
          <div className="px-6 py-4 bg-blue-600 text-white">
            <h1 className="text-2xl font-bold">购买车票</h1>
          </div>
          
          <div className="p-6">
            {/* 列车信息 */}
            <div className="mb-6 border-b border-gray-200 pb-6">
              <h2 className="text-xl font-semibold text-gray-800 mb-4">列车信息</h2>
              
              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <div>
                    <div className="text-2xl font-bold">{trainInfo.trainNumber}</div>
                    <div className="text-sm text-gray-500">
                      {trainInfo.departure} → {trainInfo.arrival}
                    </div>
                  </div>
                  <div className="text-right">
                    <div className="text-lg font-semibold">
                      {trainInfo.departureTime} - {trainInfo.arrivalTime}
                    </div>
                  </div>
                </div>
                
                <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4 sm:gap-0 mt-4">
                  <div>
                    <div className="text-sm text-gray-500">当前选择</div>
                    <div className="font-medium">{getSeatClassName(purchaseData.seatType)}</div>
                  </div>
                  <div>
                    <div className="text-sm text-gray-500">票价</div>
                    <div className="text-lg font-bold text-orange-600">¥{trainInfo.price}</div>
                  </div>
                </div>
              </div>
            </div>
            
            {/* 座位类型选择 */}
            <div className="mb-6 border-b border-gray-200 pb-6">
              <h2 className="text-lg font-semibold text-gray-800 mb-4">座位类型</h2>
              
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
                {[0, 1, 2, 3].map((type) => (
                  <button
                    key={type}
                    type="button"
                    disabled={true}
                    className={`px-4 py-2 rounded-md text-sm font-medium focus:outline-none ${
                      purchaseData.seatType === type
                        ? 'bg-blue-600 text-white'
                        : 'bg-gray-100 text-gray-800 hover:bg-gray-200'
                    }`}
                    onClick={() => handleSeatTypeChange(type)}
                  >
                    {getSeatClassName(type)}
                  </button>
                ))}
              </div>
            </div>
            
            {/* 乘客信息 */}
            <div className="mb-6">
              <h2 className="text-lg font-semibold text-gray-800 mb-4">乘客信息</h2>
              
              <div className="mb-4">
                <label htmlFor="passengerId" className="block text-sm font-medium text-gray-700 mb-1">
                  乘客ID
                </label>
                <input
                  type="text"
                  id="passengerId"
                  name="passengerId"
                  value={purchaseData.passengerId}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  placeholder="请输入乘客ID"
                  required
                />
                <p className="mt-1 text-sm text-gray-500">当前用户ID作为乘客ID</p>
              </div>
            </div>
            
            {/* 操作按钮 */}
            <div className="flex flex-col sm:flex-row gap-4">
              <button
                type="button"
                onClick={handleGoBack}
                className="px-6 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2"
              >
                返回
              </button>
              <button
                type="button"
                onClick={handleSubmit}
                disabled={loading}
                className="flex-1 px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? '处理中...' : '确认购票'}
              </button>
            </div>
            
            {/* 错误信息 */}
            {error && (
              <div className="mt-6 bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md">
                {error}
              </div>
            )}
            
            {/* 成功信息 */}
            {success && (
              <div className="mt-6 bg-green-50 border border-green-200 text-green-600 px-4 py-3 rounded-md">
                {success}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TicketPurchase;