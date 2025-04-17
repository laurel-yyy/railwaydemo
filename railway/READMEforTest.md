User:
register:
    Post:
        http://localhost:8080/api/user/register



Ticket
Get:
    http://localhost:8080/api/ticket/query
    {
    "fromStation": "New York",
    "toStation": "Philadelphia", 
    "departureDate": "2025-06-01",
    "departure": "New York",
    "arrival": "Philadelphia" 
    }   

    return:
        {"code":1,"msg":null,"data":{"trainList":[{"trainId":"1","trainNumber":"US101","departureTime":"02:56","arrivalTime":"08:14","duration":"5h18min","daysArrived":0,"departure":"New York","arrival":"Philadelphia","departureFlag":true,"arrivalFlag":true,"saleTime":"05-15 07:30","saleStatus":1,"seatClassList":[{"type":0,"quantity":0,"price":2537.5},{"type":1,"quantity":140,"price":1197.0},{"type":2,"quantity":810,"price":745.0}]}],"departureStationList":["New York"],"arrivalStationList":["Philadelphia"],"seatClassTypeList":[0,1,2]}}


Order:
    Post:
        http://localhost:8080/api/ticket/purchase

    return:
    {"code":1,"msg":null,"data":{"orderSn":"RW1744871175239102e8279","ticketOrderDetails":[{"id":null,"orderSn":"RW1744871175239102e8279","userId":1744859198560,"username":"john_doe","trainId":1,"departure":"New York","arrival":"Chicago","ridingDate":"2025-04-17","orderTime":"2025-04-17","trainNumber":"US101","departureTime":"17:56","arrivalTime":"23:14","source":null,"status":0,"payType":null,"payTime":null,"createTime":"2025-04-17T06:26:15.000+00:00","updateTime":"2025-04-17T06:26:15.000+00:00","totalAmount":null,"passengerDetails":null}]}}

    pageTicketOrder:
    GET:
        http://localhost:8080/api/order-service/order/ticket/page?userId=1744859198560&statusType=0&current=1&size=10

    RETURN:
        {"code":1,"msg":null,"data":{"current":1,"size":10,"total":2,"records":[{"id":2,"orderSn":"RW1744871175239102e8279","userId":1744859198560,"username":"john_doe","trainId":1,"departure":"New York","arrival":"Chicago","ridingDate":"2025-04-17","orderTime":"2025-04-17","trainNumber":"US101","departureTime":"17:56","arrivalTime":"23:14","source":null,"status":0,"payType":null,"payTime":null,"createTime":"2025-04-17T06:26:15.000+00:00","updateTime":"2025-04-17T06:26:15.000+00:00","totalAmount":null,"passengerDetails":null},{"id":1,"orderSn":"RW1744868916819a29ecf5f","userId":1744859198560,"username":"john_doe","trainId":1,"departure":"New York","arrival":"Chicago","ridingDate":"2025-04-17","orderTime":"2025-04-17","trainNumber":"US101","departureTime":"17:56","arrivalTime":"23:14","source":null,"status":0,"payType":null,"payTime":null,"createTime":"2025-04-17T05:48:37.000+00:00","updateTime":"2025-04-17T05:48:37.000+00:00","totalAmount":null,"passengerDetails":null}],"totalPages":1}}

    cancelTicketOrder:
    Post:
        http://localhost:8080/api/order-service/order/ticket/cancel?orderSn=RW1744868916819a29ecf5f
    
    Return:
        {
            "code": 1,
            "msg": null,
            "data": true
        }

    After cancel, pageTicket order again:
    GET:
        http://localhost:8080/api/order-service/order/ticket/page?userId=1744859198560&statusType=0&current=1&size=10
    Return:
        {"code":1,"msg":null,"data":{"current":1,"size":10,"total":2,"records":[{"id":2,"orderSn":"RW1744871175239102e8279","userId":1744859198560,"username":"john_doe","trainId":1,"departure":"New York","arrival":"Chicago","ridingDate":"2025-04-17","orderTime":"2025-04-17","trainNumber":"US101","departureTime":"17:56","arrivalTime":"23:14","source":null,"status":0,"payType":null,"payTime":null,"createTime":"2025-04-17T06:26:15.000+00:00","updateTime":"2025-04-17T06:26:15.000+00:00","totalAmount":null,"passengerDetails":null},{"id":1,"orderSn":"RW1744868916819a29ecf5f","userId":1744859198560,"username":"john_doe","trainId":1,"departure":"New York","arrival":"Chicago","ridingDate":"2025-04-17","orderTime":"2025-04-17","trainNumber":"US101","departureTime":"17:56","arrivalTime":"23:14","source":null,"status":2,"payType":null,"payTime":null,"createTime":"2025-04-17T05:48:37.000+00:00","updateTime":"2025-04-17T11:55:44.000+00:00","totalAmount":null,"passengerDetails":null}],"totalPages":1}}
    