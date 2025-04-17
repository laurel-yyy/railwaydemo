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