// package com.ourgroup.railway.service.impl;

// import com.alibaba.fastjson2.JSON;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// import org.springframework.stereotype.Service;
// import org.springframework.util.CollectionUtils;
// import org.springframework.util.StringUtils;

// import com.ourgroup.railway.framework.cache.DistributedCache;
// import com.ourgroup.railway.framework.constant.RedisKeyConstant;
// import com.ourgroup.railway.framework.exception.ClientException;
// import com.ourgroup.railway.framework.exception.ServiceException;
// import com.ourgroup.railway.framework.util.BeanUtil;
// import com.ourgroup.railway.framework.util.IdcardUtil;
// import com.ourgroup.railway.framework.util.PhoneUtil;
// import com.ourgroup.railway.framework.user.UserContext;
// import com.ourgroup.railway.mapper.PassengerMapper;
// import com.ourgroup.railway.model.dao.PassengerDO;
// import com.ourgroup.railway.model.dto.req.PassengerRemoveReqDTO;
// import com.ourgroup.railway.model.dto.req.PassengerReqDTO;
// import com.ourgroup.railway.model.dto.resp.PassengerActualRespDTO;
// import com.ourgroup.railway.model.dto.resp.PassengerRespDTO;
// import com.ourgroup.railway.model.enums.VerifyStatusEnum;
// import com.ourgroup.railway.service.PassengerService;

// import java.util.Date;
// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;

// /**
//  * Passenger service implementation
//  */
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class PassengerServiceImpl implements PassengerService {

//     private final PassengerMapper passengerMapper;
//     private final DistributedCache distributedCache;

//     @Override
//     public List<PassengerRespDTO> listPassengerQueryByUsername(String username) {
//         String actualUserPassengerListStr = getActualUserPassengerListStr(username);
//         return Optional.ofNullable(actualUserPassengerListStr)
//                 .map(each -> JSON.parseArray(each, PassengerDO.class))
//                 .map(each -> BeanUtil.convert(each, PassengerRespDTO.class))
//                 .orElse(null);
//     }

//     /**
//      * Get passenger list string from cache or database
//      * 
//      * @param username Username
//      * @return JSON string of passenger list
//      */
//     private String getActualUserPassengerListStr(String username) {
//         return distributedCache.safeGet(
//                 RedisKeyConstant.USER_PASSENGER_LIST + username,
//                 String.class,
//                 () -> {
//                     LambdaQueryWrapper<PassengerDO> queryWrapper = Wrappers.lambdaQuery(PassengerDO.class)
//                             .eq(PassengerDO::getUsername, username);
//                     List<PassengerDO> passengerDOList = passengerMapper.selectList(queryWrapper);
//                     return !CollectionUtils.isEmpty(passengerDOList) ? JSON.toJSONString(passengerDOList) : null;
//                 },
//                 1,
//                 TimeUnit.DAYS
//         );
//     }

//     @Override
//     public List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids) {
//         String actualUserPassengerListStr = getActualUserPassengerListStr(username);
//         if (!StringUtils.hasText(actualUserPassengerListStr)) {
//             return null;
//         }
//         return JSON.parseArray(actualUserPassengerListStr, PassengerDO.class)
//                 .stream().filter(passengerDO -> ids.contains(passengerDO.getId()))
//                 .map(each -> BeanUtil.convert(each, PassengerActualRespDTO.class))
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public PassengerRespDTO getPassengerById(String passengerId) {
//         String username = UserContext.getUsername();
//         PassengerDO passengerDO = selectPassenger(username, passengerId);
//         return BeanUtil.convert(passengerDO, PassengerRespDTO.class);
//     }

//     @Override
//     public void savePassenger(PassengerReqDTO requestParam) {
//         verifyPassenger(requestParam);
//         String username = UserContext.getUsername();
//         try {
//             PassengerDO passengerDO = BeanUtil.convert(requestParam, PassengerDO.class);
//             passengerDO.setUsername(username);
//             passengerDO.setCreateTime(new Date());
//             passengerDO.setVerifyStatus(VerifyStatusEnum.REVIEWED.getCode());
//             int inserted = passengerMapper.insert(passengerDO);
//             if (inserted <= 0) {
//                 throw new ServiceException(String.format("[%s] Failed to add passenger", username));
//             }
//         } catch (Exception ex) {
//             if (ex instanceof ServiceException) {
//                 log.error("{}，Request params：{}", ex.getMessage(), JSON.toJSONString(requestParam));
//             } else {
//                 log.error("[{}] Failed to add passenger, request params：{}", username, JSON.toJSONString(requestParam), ex);
//             }
//             throw ex;
//         }
//         delUserPassengerCache(username);
//     }

//     @Override
//     public void updatePassenger(PassengerReqDTO requestParam) {
//         verifyPassenger(requestParam);
//         String username = UserContext.getUsername();
//         try {
//             PassengerDO passengerDO = BeanUtil.convert(requestParam, PassengerDO.class);
//             passengerDO.setUsername(username);
//             LambdaUpdateWrapper<PassengerDO> updateWrapper = Wrappers.lambdaUpdate(PassengerDO.class)
//                     .eq(PassengerDO::getUsername, username)
//                     .eq(PassengerDO::getId, requestParam.getId());
//             int updated = passengerMapper.update(passengerDO, updateWrapper);
//             if (updated <= 0) {
//                 throw new ServiceException(String.format("[%s] Failed to update passenger", username));
//             }
//         } catch (Exception ex) {
//             if (ex instanceof ServiceException) {
//                 log.error("{}，Request params：{}", ex.getMessage(), JSON.toJSONString(requestParam));
//             } else {
//                 log.error("[{}] Failed to update passenger, request params：{}", username, JSON.toJSONString(requestParam), ex);
//             }
//             throw ex;
//         }
//         delUserPassengerCache(username);
//     }

//     @Override
//     public void removePassenger(PassengerRemoveReqDTO requestParam) {
//         String username = UserContext.getUsername();
//         PassengerDO passengerDO = selectPassenger(username, requestParam.getId());
//         if (Objects.isNull(passengerDO)) {
//             throw new ClientException("Passenger data does not exist");
//         }
//         try {
//             LambdaUpdateWrapper<PassengerDO> deleteWrapper = Wrappers.lambdaUpdate(PassengerDO.class)
//                     .eq(PassengerDO::getUsername, username)
//                     .eq(PassengerDO::getId, requestParam.getId());
//             // Logical delete, modifies del_flag in the database
//             int deleted = passengerMapper.delete(deleteWrapper);
//             if (deleted <= 0) {
//                 throw new ServiceException(String.format("[%s] Failed to delete passenger", username));
//             }
//         } catch (Exception ex) {
//             if (ex instanceof ServiceException) {
//                 log.error("{}，Request params：{}", ex.getMessage(), JSON.toJSONString(requestParam));
//             } else {
//                 log.error("[{}] Failed to delete passenger, request params：{}", username, JSON.toJSONString(requestParam), ex);
//             }
//             throw ex;
//         }
//         delUserPassengerCache(username);
//     }

//     /**
//      * Select passenger by username and passenger ID
//      * 
//      * @param username Username
//      * @param passengerId Passenger ID
//      * @return Passenger data object
//      */
//     private PassengerDO selectPassenger(String username, String passengerId) {
//         LambdaQueryWrapper<PassengerDO> queryWrapper = Wrappers.lambdaQuery(PassengerDO.class)
//                 .eq(PassengerDO::getUsername, username)
//                 .eq(PassengerDO::getId, passengerId);
//         return passengerMapper.selectOne(queryWrapper);
//     }

//     /**
//      * Delete user passenger cache
//      * 
//      * @param username Username
//      */
//     private void delUserPassengerCache(String username) {
//         distributedCache.delete(RedisKeyConstant.USER_PASSENGER_LIST + username);
//     }

//     /**
//      * Verify passenger information
//      * 
//      * @param requestParam Passenger request parameters
//      */
//     private void verifyPassenger(PassengerReqDTO requestParam) {
//         int length = requestParam.getRealName().length();
//         if (!(length >= 2 && length <= 16)) {
//             throw new ClientException("Passenger name must be 2-16 characters in length");
//         }
//         if (!IdcardUtil.isValidCard(requestParam.getIdCard())) {
//             throw new ClientException("Invalid passenger ID card");
//         }
//         if (!PhoneUtil.isMobile(requestParam.getPhone())) {
//             throw new ClientException("Invalid passenger phone number");
//         }
//     }
// }