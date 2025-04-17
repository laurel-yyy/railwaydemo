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

  // Get ticket purchase information from URL parameters
  useEffect(() => {
    try {
      // Get user information from local storage
      const user = JSON.parse(localStorage.getItem('user'));
      setUserData(user);
      
      // Get data from URL parameters
      const searchParams = new URLSearchParams(location.search);
      const trainId = searchParams.get('trainId');
      const departure = searchParams.get('departure');
      const arrival = searchParams.get('arrival');
      const trainNumber = searchParams.get('trainNumber');
      const departureTime = searchParams.get('departureTime');
      const arrivalTime = searchParams.get('arrivalTime');
      const seatType = parseInt(searchParams.get('seatType') || '1');
      const price = parseFloat(searchParams.get('price') || '0');
      
      // Validate required parameters
      if (!trainId || !departure || !arrival) {
        throw new Error('Missing required ticket purchase information');
      }
      
      // Set train information
      setTrainInfo({
        trainNumber,
        departureTime,
        arrivalTime,
        departure,
        arrival,
        seatType,
        price
      });
      
      // Set purchase data
      setPurchaseData({
        trainId,
        passengerId: '', 
        seatType,
        departure,
        arrival
      });
    } catch (err) {
      console.error('Failed to get ticket purchase information:', err);
      setError(err.message || 'Failed to get ticket purchase information');
    }
  }, [location]);

  // Handle input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPurchaseData({
      ...purchaseData,
      [name]: value
    });
  };

  // Handle seat type selection
  const handleSeatTypeChange = (seatType) => {
    setPurchaseData({
      ...purchaseData,
      seatType
    });
  };

  // Get seat class name
  const getSeatClassName = (type) => {
    const seatClassNames = {
      0: 'Business Class',
      1: 'First Class',
      2: 'Second Class',
      3: 'Hard Sleeper',
      4: 'Soft Sleeper',
      5: 'Hard Seat',
      6: 'Moving Sleeper',
    };
    return seatClassNames[type] || `Type ${type}`;
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // Check required fields
      if (!purchaseData.trainId || !purchaseData.departure || !purchaseData.arrival) {
        throw new Error('Please complete the ticket purchase information');
      }

      console.log('Sending ticket purchase request:', purchaseData);
      
      // Send purchase request
      const response = await api.post('/ticket/purchase', purchaseData);
      console.log('Purchase response:', response);

      // Check response
      if (response.code !== 1) {
        throw new Error(response.msg || 'Purchase failed');
      }

      // Show success message
      setSuccess('Purchase successful!');
      
      // Return to search page after 3 seconds
      setTimeout(() => {
        navigate('/ticket-search');
      }, 3000);
    } catch (err) {
      console.error('Purchase failed:', err);
      setError(err.message || 'Purchase failed, please try again later');
    } finally {
      setLoading(false);
    }
  };

  // Return to search page
  const handleGoBack = () => {
    navigate('/ticket-search');
  };

  if (!trainInfo) {
    return (
      <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
        <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
          <h2 className="text-xl font-semibold text-gray-800 mb-4">Loading...</h2>
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md">
              {error}
            </div>
          )}
          <button
            onClick={handleGoBack}
            className="mt-4 w-full px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2"
          >
            Return to Search Page
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
            <h1 className="text-2xl font-bold">Purchase Ticket</h1>
          </div>
          
          <div className="p-6">
            {/* Train Information */}
            <div className="mb-6 border-b border-gray-200 pb-6">
              <h2 className="text-xl font-semibold text-gray-800 mb-4">Train Information</h2>
              
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
                    <div className="text-sm text-gray-500">Current Selection</div>
                    <div className="font-medium">{getSeatClassName(purchaseData.seatType)}</div>
                  </div>
                  <div>
                    <div className="text-sm text-gray-500">Ticket Price</div>
                    <div className="text-lg font-bold text-orange-600">¥{trainInfo.price}</div>
                  </div>
                </div>
              </div>
            </div>
            
            {/* Seat Type Selection */}
            <div className="mb-6 border-b border-gray-200 pb-6">
              <h2 className="text-lg font-semibold text-gray-800 mb-4">Seat Type</h2>
              
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
            
            
            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4">
              <button
                type="button"
                onClick={handleGoBack}
                className="px-6 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2"
              >
                Back
              </button>
              <button
                type="button"
                onClick={handleSubmit}
                disabled={loading}
                className="flex-1 px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Processing...' : 'Confirm Purchase'}
              </button>
            </div>
            
            {/* Error Message */}
            {error && (
              <div className="mt-6 bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md">
                {error}
              </div>
            )}
            
            {/* Success Message */}
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