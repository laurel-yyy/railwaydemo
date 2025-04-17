import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/axios';

const TicketSearch = () => {
  const navigate = useNavigate();
  // Search parameters state
  const [searchParams, setSearchParams] = useState({
    fromStation: 'New York',
    toStation: 'San Francisco',
    departureDate: '2025-06-01',
  });

  // Search result state
  const [searchResult, setSearchResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Handle input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams({
      ...searchParams,
      [name]: value,
    });
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    // Ensure both stations are entered
    if (!searchParams.fromStation || !searchParams.toStation) {
      setError('Please enter departure and arrival stations');
      setLoading(false);
      return;
    }

    try {
      // Build query parameters - use structure matching TicketPageQueryReqDTO
      const queryParams = {
        fromStation: searchParams.fromStation,
        toStation: searchParams.toStation,
        departureDate: searchParams.departureDate,
        departure: searchParams.fromStation,  // Ensure these fields are also included in the request
        arrival: searchParams.toStation
      };

      console.log('Sending query request, parameters:', queryParams);

      // Use GET request, parameters passed as URL parameters
      const response = await api.get('/ticket/query', { params: queryParams });
      console.log('Query response:', response);

      // Check response
      if (response.code !== 1 || !response.data) {
        throw new Error(response.msg || 'Query failed');
      }

      // Set query result
      setSearchResult(response.data);
    } catch (err) {
      console.error('Query failed:', err);
      
      // Extract more detailed error information
      let errorMsg = 'Query failed, please try again later';
      
      if (err.response) {
        // Server returned error response
        console.error('Error status code:', err.response.status);
        console.error('Error data:', err.response.data);
        
        if (err.response.status === 500) {
          errorMsg = 'Internal server error, please contact administrator';
        } else {
          errorMsg = err.response.data?.message || errorMsg;
        }
      } else if (err.request) {
        // Request was made but no response received
        console.error('No response received:', err.request);
        errorMsg = 'No server response received, please check network connection';
      } else {
        // Request setup error
        console.error('Request error:', err.message);
        errorMsg = err.message || errorMsg;
      }
      
      setError(errorMsg);
    } finally {
      setLoading(false);
    }
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
      13: 'Moving Sleeper',
    };
    return seatClassNames[type] || `Type ${type}`;
  };

  // Get sale status text
  const getSaleStatusText = (status) => {
    const statusMap = {
      0: 'Not On Sale',
      1: 'Available',
      2: 'Sold Out',
      3: 'Sale Suspended',
    };
    return statusMap[status] || 'Unknown Status';
  };

  // Get sale status style
  const getSaleStatusStyle = (status) => {
    switch (status) {
      case 0: // Not On Sale
        return 'text-gray-500';
      case 1: // Available
        return 'text-green-600 font-semibold';
      case 2: // Sold Out
        return 'text-red-600';
      case 3: // Sale Suspended
        return 'text-gray-500';
      default:
        return 'text-gray-500';
    }
  };

  const handleReturnHome = () => {
    navigate('/');
  };

  // Handle ticket purchase
  const handlePurchase = (train, seatType) => {
    // Ensure user is logged in
    const token = localStorage.getItem('accessToken');
    if (!token) {
      alert('Please login first');
      navigate('/login');
      return;
    }

    // Build query parameters for the purchase page
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

    // Navigate to purchase page
    navigate(`/ticket-purchase?${queryParams.toString()}`);
  };

  return (
    <div className="min-h-screen bg-gray-100 p-4 sm:p-6 lg:p-8">
      <div className="max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold text-gray-800">Ticket Search</h1>
          <button
            onClick={handleReturnHome}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Return to Home
          </button>
        </div>
        
        {/* Search Form */}
        <div className="bg-white p-6 rounded-lg shadow-md mb-6">
          <form onSubmit={handleSubmit} className="flex flex-wrap gap-4">
            <div className="flex-1 min-w-[200px]">
              <label htmlFor="fromStation" className="block text-sm font-medium text-gray-700 mb-1">
                Departure Station
              </label>
              <input
                type="text"
                id="fromStation"
                name="fromStation"
                value={searchParams.fromStation}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="e.g.: New York"
              />
            </div>
            
            <div className="flex-1 min-w-[200px]">
              <label htmlFor="toStation" className="block text-sm font-medium text-gray-700 mb-1">
                Arrival Station
              </label>
              <input
                type="text"
                id="toStation"
                name="toStation"
                value={searchParams.toStation}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                placeholder="e.g.: San Francisco"
              />
            </div>
            
            <div className="flex-1 min-w-[200px]">
              <label htmlFor="departureDate" className="block text-sm font-medium text-gray-700 mb-1">
                Departure Date
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
                {loading ? 'Searching...' : 'Search'}
              </button>
            </div>
          </form>
        </div>
        
        {/* Error Message */}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md mb-6">
            <div className="font-medium">Search Error</div>
            <div className="mt-1">{error}</div>
          </div>
        )}
        
        {/* Search Results */}
        {searchResult && (
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-gray-800">
                {searchResult.trainList.length > 0 
                  ? `Trains from ${searchParams.fromStation} to ${searchParams.toStation}` 
                  : 'No matching trains found'}
              </h2>
              <p className="text-sm text-gray-600 mt-1">
                {searchParams.departureDate} · Found {searchResult.trainList.length} trains
              </p>
            </div>
            
            {/* Train List */}
            <div className="overflow-y-auto max-h-[600px]">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50 sticky top-0">
                  <tr>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Train Number
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Departure/Arrival
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Duration
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Seat Information
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
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
                                +{train.daysArrived} days
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
                                  {seat.quantity > 0 ? `${seat.quantity} tickets` : 'No tickets'}
                                </span>
                                <span className="text-sm font-medium text-orange-600 mr-3">¥{seat.price}</span>
                                {train.saleStatus === 1 && seat.quantity > 0 && (
                                  <button
                                    onClick={() => handlePurchase(train, seat)}
                                    className="px-2 py-1 text-xs bg-blue-600 text-white rounded hover:bg-blue-700 focus:outline-none"
                                  >
                                    Purchase
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
                          On sale: {train.saleTime}
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