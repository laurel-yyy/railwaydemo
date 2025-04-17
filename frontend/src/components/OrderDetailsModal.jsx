import React from 'react';

// Order Details Modal Component
const OrderDetailsModal = ({ order, onClose }) => {
  if (!order) return null;

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
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 max-w-2xl w-full">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-xl font-bold text-gray-900">Order Details</h3>
          <button 
            onClick={onClose} 
            className="text-gray-400 hover:text-gray-500 focus:outline-none"
          >
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div className="border-t border-gray-200 pt-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="border rounded-lg p-4 bg-gray-50">
              <h4 className="font-medium text-gray-900 mb-3">Order Information</h4>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-gray-500">OrderN:</span>
                  <span className="font-medium text-gray-900">{order.orderSn}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Create at:</span>
                  <span className="font-medium text-gray-900">{formatDateTime(order.createTime)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Status:</span>
                  <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getOrderStatusStyle(order.status)}`}>
                    {getOrderStatusText(order.status)}
                  </span>
                </div>
              </div>
            </div>

            <div className="border rounded-lg p-4 bg-gray-50">
              <h4 className="font-medium text-gray-900 mb-3">Trip Information</h4>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-gray-500">Train Number:</span>
                  <span className="font-medium text-gray-900">{order.trainNumber}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Departure:</span>
                  <span className="font-medium text-gray-900">{order.departure}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Arrival:</span>
                  <span className="font-medium text-gray-900">{order.arrival}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-500">Travel Date:</span>
                  <span className="font-medium text-gray-900">{formatDate(order.ridingDate)}</span>
                </div>
              </div>
            </div>
          </div>


          {/* Price Information */}
          {order.amount && (
            <div className="mt-4 border rounded-lg p-4 bg-gray-50">
              <div className="flex justify-between items-center">
                <span className="text-gray-700 font-medium">Total Order Price:</span>
                <span className="text-xl font-bold text-red-600">¥{order.amount.toFixed(2)}</span>
              </div>
            </div>
          )}
        </div>

        <div className="mt-6 flex justify-end space-x-3">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default OrderDetailsModal;