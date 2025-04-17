// package com.ourgroup.railway.service;

// import com.ourgroup.railway.model.dto.req.PassengerRemoveReqDTO;
// import com.ourgroup.railway.model.dto.req.PassengerReqDTO;
// import com.ourgroup.railway.model.dto.resp.PassengerActualRespDTO;
// import com.ourgroup.railway.model.dto.resp.PassengerRespDTO;

// import java.util.List;

// /**
//  * Passenger service interface
//  */
// public interface PassengerService {

//     /**
//      * Query passenger list by username
//      *
//      * @param username Username
//      * @return List of passenger response DTOs
//      */
//     List<PassengerRespDTO> listPassengerQueryByUsername(String username);

//     /**
//      * Query passenger list by passenger IDs
//      *
//      * @param username Username
//      * @param ids Passenger ID collection
//      * @return List of actual passenger response DTOs
//      */
//     List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids);

//     /**
//      * Get passenger by ID
//      * 
//      * @param passengerId Passenger ID
//      * @return Passenger information
//      */
//     PassengerRespDTO getPassengerById(String passengerId);

//     /**
//      * Save new passenger
//      *
//      * @param requestParam Passenger information
//      */
//     void savePassenger(PassengerReqDTO requestParam);

//     /**
//      * Update passenger information
//      *
//      * @param requestParam Passenger information
//      */
//     void updatePassenger(PassengerReqDTO requestParam);

//     /**
//      * Remove passenger
//      *
//      * @param requestParam Remove passenger information
//      */
//     void removePassenger(PassengerRemoveReqDTO requestParam);
// }