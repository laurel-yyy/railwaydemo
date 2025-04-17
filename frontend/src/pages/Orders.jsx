import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/axios';
import OrderDetailsModal from '../components/OrderDetailsModal'; 

const Orders = () => {
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showPaymentConfirm, setShowPaymentConfirm] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [paymentLoading, setPaymentLoading] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false); 

  useEffect(() => {
    const fetchOrders = async () => {
      setLoading(true);
      setError('');
      
      try {
        const response = await api.get('/order-service/order/ticket/page', {
          params: {
            current: 1,
            size: 50
          }
        });
        
        console.log('API Response:', response);
        
        if (response && response.code === 1) {
          setOrders(response.data.records || []);
          console.log('Setting order data:', response.data.records);
        } else {
          throw new Error((response && response.msg) || 'Failed to get orders');
        }
      } catch (err) {
        console.error('Failed to get orders:', err);
        setError('Failed to get order data, please try again later');
        setOrders([]);
      } finally {
        setLoading(false);
      }
    };
    
    fetchOrders();
  }, []);

  // Return to home page
  const handleReturnHome = () => {
    navigate('/');
  };

  // Show payment confirmation dialog
  const handlePayOrder = (order) => {
    setSelectedOrder(order);
    setShowPaymentConfirm(true);
  };

  // Close payment confirmation dialog
  const handleClosePaymentConfirm = () => {
    setShowPaymentConfirm(false);
    setSelectedOrder(null);
  };

  // Confirm payment for order
  const handleConfirmPayment = async () => {
    if (!selectedOrder) return;
    
    setPaymentLoading(true);
    console.log('Preparing to send payment request, order number:', selectedOrder.orderSn);
    try {
      // Send payment request
      const response = await api.post(`/order-service/order/ticket/pay?orderSn=${selectedOrder.orderSn}`);
      
      console.log('Payment response:', response);
      
      if (response && response.code === 1) {
        // Payment successful, update order status
        alert('Payment successful!');
        
        // Update order status in the order list
        setOrders(prevOrders => 
          prevOrders.map(order => 
            order.id === selectedOrder.id 
              ? { ...order, status: 2 } // Update status to paid
              : order
          )
        );
      } else {
        throw new Error((response && response.msg) || 'Payment failed');
      }
    } catch (err) {
      console.error('Payment failed:', err);
      alert(`Payment failed: ${err.message || 'Please try again later'}`);
    } finally {
      setPaymentLoading(false);
      setShowPaymentConfirm(false);
      setSelectedOrder(null);
    }
  };

  // View order details - modified to open modal
  const handleViewOrderDetails = (order) => {
    setSelectedOrder(order);
    setShowDetailsModal(true);
  };


  const handleCloseDetailsModal = () => {
    setShowDetailsModal(false);
    setSelectedOrder(null);
  };


  const formatDateTime = (dateTimeStr) => {
    if (!dateTimeStr) return '-';
    const date = new Date(dateTimeStr);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };


  const formatDate = (dateStr) => {
    if (!dateStr) return '-';
    return dateStr;
  };

  // Get order status text
  const getOrderStatusText = (statusType) => {
    switch (statusType) {
      case 0: return 'Unpaid';
      case 2: return 'Paid';
      default: return 'Unknown Status';
    }
  };

  // Get order status style
  const getOrderStatusStyle = (statusType) => {
    switch (statusType) {
      case 0: return 'bg-yellow-100 text-yellow-800';
      case 2: return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold text-gray-900">My Orders</h1>
          <button
            onClick={handleReturnHome}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Return to Home
          </button>
        </div>

        {/* Loading state */}
        {loading && (
          <div className="flex justify-center py-10">
            <div className="animate-spin rounded-full h-10 w-10 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        )}

        {/* Error message */}
        {error && !loading && (
          <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md mb-6">
            <div className="font-medium">Error</div>
            <div className="mt-1">{error}</div>
          </div>
        )}

        {/* Order list */}
        {!loading && !error && (
          <div className="bg-white shadow overflow-hidden sm:rounded-lg">
            {orders.length === 0 ? (
              <div className="text-center py-10">
                <p className="text-gray-500">No orders yet</p>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Order Number
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Train Info
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Departure/Arrival
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Travel Date
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Order Time
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Status
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {orders.map((order) => (
                      <tr key={order.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {order.orderSn}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{order.trainNumber}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex flex-col">
                            <div className="text-sm text-gray-900">{order.departure}</div>
                            <div className="text-sm text-gray-500 mt-1">{order.arrival}</div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {formatDate(order.ridingDate)}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {formatDateTime(order.createTime)}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getOrderStatusStyle(order.status)}`}>
                            {getOrderStatusText(order.status)}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <button
                            onClick={() => handleViewOrderDetails(order)}
                            className="text-indigo-600 hover:text-indigo-900 mr-3"
                          >
                            View
                          </button>
                          {order.status === 0 && (
                            <button
                              onClick={() => handlePayOrder(order)}
                              className="text-green-600 hover:text-green-900"
                            >
                              Pay
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}

        {/* Payment confirmation dialog */}
        {showPaymentConfirm && selectedOrder && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-6 max-w-md w-full">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Confirm Payment</h3>
              <p className="text-gray-600 mb-6">Are you sure you want to pay for order <span className="font-semibold">{selectedOrder.orderSn}</span>?</p>
              
              <div className="mt-2 border border-gray-200 rounded-md p-4 bg-gray-50 mb-6">
                <div className="grid grid-cols-2 gap-2 text-sm">
                  <div className="text-gray-500">Train:</div>
                  <div className="font-medium">{selectedOrder.trainNumber}</div>
                  <div className="text-gray-500">Departure/Arrival:</div>
                  <div className="font-medium">{selectedOrder.departure} - {selectedOrder.arrival}</div>
                  <div className="text-gray-500">Travel Date:</div>
                  <div className="font-medium">{formatDate(selectedOrder.ridingDate)}</div>
                </div>
              </div>

              <div className="flex justify-end space-x-3">
                <button
                  onClick={handleClosePaymentConfirm}
                  disabled={paymentLoading}
                  className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none"
                >
                  Cancel
                </button>
                <button
                  onClick={handleConfirmPayment}
                  disabled={paymentLoading}
                  className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none"
                >
                  {paymentLoading ? 'Processing...' : 'Confirm Payment'}
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Order details modal */}
        {showDetailsModal && selectedOrder && (
          <OrderDetailsModal 
            order={selectedOrder} 
            onClose={handleCloseDetailsModal} 
          />
        )}
      </div>
    </div>
  );
};

export default Orders;