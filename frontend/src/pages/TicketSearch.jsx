import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/axios';

const TicketSearch = () => {
  const navigate = useNavigate();
  // 查询参数状态
  const [searchParams, setSearchParams] = useState({
    fromStation: 'New York',
    toStation: 'San Francisco',
    departureDate: '2025-06-01',
  });

  // 查询结果状态
  const [searchResult, setSearchResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // 处理输入变化
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams({
      ...searchParams,
      [name]: value,
    });
  };

  // 处理表单提交
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    // 确保两个站点都已输入
    if (!searchParams.fromStation || !searchParams.toStation) {
      setError('请输入出发站和到达站');
      setLoading(false);
      return;
    }

    try {
      // 构建查询参数 - 使用与TicketPageQueryReqDTO匹配的结构
      const queryParams = {
        fromStation: searchParams.fromStation,
        toStation: searchParams.toStation,
        departureDate: searchParams.departureDate,
        departure: searchParams.fromStation,  // 确保这些字段也包含在请求中
        arrival: searchParams.toStation
      };

      console.log('发送查询请求，参数:', queryParams);

      // 使用GET请求，参数作为URL参数传递
      const response = await api.get('/ticket/query', { params: queryParams });
      console.log('查询响应:', response);

      // 检查响应
      if (response.code !== 1 || !response.data) {
        throw new Error(response.msg || '查询失败');
      }

      // 设置查询结果
      setSearchResult(response.data);
    } catch (err) {
      console.error('查询失败:', err);
      
      // 提取更详细的错误信息
      let errorMsg = '查询失败，请稍后重试';
      
      if (err.response) {
        // 服务器返回错误响应
        console.error('错误状态码:', err.response.status);
        console.error('错误数据:', err.response.data);
        
        if (err.response.status === 500) {
          errorMsg = '服务器内部错误，请联系管理员';
        } else {
          errorMsg = err.response.data?.message || errorMsg;
        }
      } else if (err.request) {
        // 请求已发送但没有收到响应
        console.error('未收到响应:', err.request);
        errorMsg = '未收到服务器响应，请检查网络连接';
      } else {
        // 请求设置错误
        console.error('请求错误:', err.message);
        errorMsg = err.message || errorMsg;
      }
      
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
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

  // 获取销售状态
  const getSaleStatusText = (status) => {
    const statusMap = {
      0: '未开售',
      1: '可预订',
      2: '已售完',
      3: '已停售',
    };
    return statusMap[status] || '未知状态';
  };

  // 获取销售状态的样式
  const getSaleStatusStyle = (status) => {
    switch (status) {
      case 0: // 未开售
        return 'text-gray-500';
      case 1: // 可预订
        return 'text-green-600 font-semibold';
      case 2: // 已售完
        return 'text-red-600';
      case 3: // 已停售
        return 'text-gray-500';
      default:
        return 'text-gray-500';
    }
  };

  // 处理购票
  const handlePurchase = (train, seatType) => {
    // 确保用户已登录
    const token = localStorage.getItem('accessToken');
    if (!token) {
      alert('请先登录');
      navigate('/login');
      return;
    }

    // 构建购票页面的查询参数
    const queryParams = new URLSearchParams({
      trainId: train.trainId,
      trainNumber: train.trainNumber,
      departureTime: train.departureTime,
      arrivalTime: train.arrivalTime,
      departure: train.departure,
      arrival: train.arrival,
      seatType: seatType.type,
      price: seatType.price
    });

    // 跳转到购票页面
    navigate(`/ticket-purchase?${queryParams.toString()}`);
  };

  return (
    <div className="min-h-screen bg-gray-100 p-4 sm:p-6 lg:p-8">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">车票查询</h1>
        
        {/* 搜索表单 */}
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <form onSubmit={handleSubmit} className="flex flex-wrap gap-4">
            <div className="flex-1 min-w-[200px]">
              <label htmlFor="fromStation" className="block text-sm font-medium text-gray-700 mb-1">
                出发站
              </label>
              <input
                type="text"
                id="fromStation"
                name="fromStation"
                value={searchParams.fromStation}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="如：New York"
              />
            </div>
            
            <div className="flex-1 min-w-[200px]">
              <label htmlFor="toStation" className="block text-sm font-medium text-gray-700 mb-1">
                到达站
              </label>
              <input
                type="text"
                id="toStation"
                name="toStation"
                value={searchParams.toStation}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="如：San Francisco"
              />
            </div>
            
            <div className="flex-1 min-w-[200px]">
              <label htmlFor="departureDate" className="block text-sm font-medium text-gray-700 mb-1">
                出发日期
              </label>
              <input
                type="date"
                id="departureDate"
                name="departureDate"
                value={searchParams.departureDate}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            
            <div className="flex items-end w-full sm:w-auto">
              <button
                type="submit"
                disabled={loading}
                className="w-full sm:w-auto px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? '查询中...' : '查询'}
              </button>
            </div>
          </form>
        </div>
        
        {/* 错误信息 */}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md mb-6">
            <div className="font-medium">查询错误</div>
            <div className="mt-1">{error}</div>
          </div>
        )}
        
        {/* 查询结果 */}
        {searchResult && (
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">
                {searchResult.trainList.length > 0 
                  ? `${searchParams.fromStation} → ${searchParams.toStation} 的列车` 
                  : '没有找到匹配的列车'}
              </h2>
              <p className="text-sm text-gray-600 mt-1">
                {searchParams.departureDate} · 共找到 {searchResult.trainList.length} 趟列车
              </p>
            </div>
            
            {/* 列车列表 */}
            <div className="overflow-y-auto max-h-[600px]">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50 sticky top-0">
                  <tr>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      车次
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      出发/到达
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      历时
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      座位信息
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      状态
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {searchResult.trainList.map((train) => (
                    <tr key={train.trainId} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-lg font-semibold text-gray-900">{train.trainNumber}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex flex-col">
                          <div className="flex items-center">
                            <span className="text-base font-medium text-gray-900">{train.departureTime}</span>
                            <span className="ml-2 text-sm text-gray-500">{train.departure}</span>
                          </div>
                          <div className="flex items-center mt-1">
                            <span className="text-base font-medium text-gray-900">{train.arrivalTime}</span>
                            <span className="ml-2 text-sm text-gray-500">{train.arrival}</span>
                            {train.daysArrived > 0 && (
                              <span className="ml-2 text-xs px-1 bg-gray-200 rounded-sm">
                                +{train.daysArrived}天
                              </span>
                            )}
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{train.duration}</div>
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex flex-col space-y-2">
                          {train.seatClassList.map((seat) => (
                            <div key={seat.type} className="flex items-center justify-between">
                              <span className="text-sm font-medium text-gray-900">{getSeatClassName(seat.type)}</span>
                              <div className="flex items-center">
                                <span className="text-sm text-gray-600 mr-3">
                                  {seat.quantity > 0 ? `${seat.quantity}张` : '无票'}
                                </span>
                                <span className="text-sm font-medium text-orange-600 mr-3">¥{seat.price}</span>
                                {train.saleStatus === 1 && seat.quantity > 0 && (
                                  <button
                                    onClick={() => handlePurchase(train, seat)}
                                    className="px-2 py-1 text-xs bg-blue-600 text-white rounded hover:bg-blue-700 focus:outline-none"
                                  >
                                    购票
                                  </button>
                                )}
                              </div>
                            </div>
                          ))}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className={`text-sm ${getSaleStatusStyle(train.saleStatus)}`}>
                          {getSaleStatusText(train.saleStatus)}
                        </div>
                        <div className="text-xs text-gray-500 mt-1">
                          开售: {train.saleTime}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TicketSearch;