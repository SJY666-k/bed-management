package com.neusoft.yiyang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.yiyang.common.BedDetailsStatus;
import com.neusoft.yiyang.common.BedStatus;
import com.neusoft.yiyang.common.BusinessException;
import com.neusoft.yiyang.dto.BedDetailsQueryDTO;
import com.neusoft.yiyang.dto.BedSwapDTO;
import com.neusoft.yiyang.dto.UpdateEndTimeDTO;
import com.neusoft.yiyang.entity.Bed;
import com.neusoft.yiyang.entity.BedDetails;
import com.neusoft.yiyang.entity.Customer;
import com.neusoft.yiyang.entity.Room;
import com.neusoft.yiyang.mapper.BedDetailsMapper;
import com.neusoft.yiyang.mapper.BedMapper;
import com.neusoft.yiyang.mapper.CustomerMapper;
import com.neusoft.yiyang.mapper.RoomMapper;
import com.neusoft.yiyang.service.BedManageService;
import com.neusoft.yiyang.util.DateUtils;
import com.neusoft.yiyang.vo.BedDetailsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 床位管理业务实现。
 *
 * <p>对应需求：床位使用详情多条件查询、修改床位使用结束时间、床位调换。</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BedManageServiceImpl implements BedManageService {

    private final BedDetailsMapper bedDetailsMapper;
    private final BedMapper bedMapper;
    private final RoomMapper roomMapper;
    private final CustomerMapper customerMapper;

    /** 所属楼房编号，配置在 application.yml 的 yiyang.building-no */
    @Value("${yiyang.building-no:606}")
    private String buildingNo;

    @Override
    public List<BedDetailsVO> listDetails(BedDetailsQueryDTO query) {
        BedDetailsQueryDTO condition = query == null ? new BedDetailsQueryDTO() : query;

        // 页签取值：1 = 正在使用、0 = 使用历史（与 is_deleted 的 0/1 相反）；
        // 不传或非法值时默认查「正在使用」。
        int tabStatus = condition.getStatus() == null ? BedDetailsStatus.TAB_IN_USE : condition.getStatus();
        int isDeleted = (tabStatus == BedDetailsStatus.TAB_HISTORY)
                ? BedDetailsStatus.HISTORY
                : BedDetailsStatus.IN_USE;

        String customerName = StringUtils.hasText(condition.getCustomerName())
                ? condition.getCustomerName().trim()
                : null;
        // 统一成 yyyy-MM-dd（前端可能传 ISO 字符串）
        String startDate = DateUtils.normalize(condition.getStartDate());

        return bedDetailsMapper.selectDetailsList(isDeleted, customerName, startDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEndTime(UpdateEndTimeDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new BusinessException("床位使用详情ID不能为空");
        }

        BedDetails record = bedDetailsMapper.selectById(dto.getId());
        if (record == null) {
            throw new BusinessException("床位使用详情不存在");
        }

        LocalDate endDate = DateUtils.parseRequired(dto.getEndDate(), "结束时间");
        if (record.getStartDate() != null && endDate.isBefore(record.getStartDate())) {
            throw new BusinessException("结束时间不能早于入住时间：" + record.getStartDate());
        }

        // 只更新结束时间，其它字段不动（需求：只能修改结束时间）
        BedDetails update = new BedDetails();
        update.setId(record.getId());
        update.setEndDate(endDate);
        bedDetailsMapper.updateById(update);

        log.info("修改床位使用结束时间：beddetailsId={}, endDate={}", record.getId(), endDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void swapBed(BedSwapDTO dto) {
        if (dto == null || dto.getCustomerId() == null) {
            throw new BusinessException("客户ID不能为空");
        }
        if (dto.getNewBedId() == null) {
            throw new BusinessException("请选择要调换的空闲床位");
        }

        // 需求要求：只能当天调换当天办理
        LocalDate today = LocalDate.now();
        LocalDate swapDate = DateUtils.parse(dto.getSwapDate());
        if (swapDate != null && !swapDate.isEqual(today)) {
            throw new BusinessException("床位调换只能当天调换当天办理");
        }

        Customer customer = customerMapper.selectById(dto.getCustomerId());
        if (customer == null) {
            throw new BusinessException("客户不存在，客户ID：" + dto.getCustomerId());
        }

        // 新床位必须存在且空闲
        Bed newBed = bedMapper.selectById(dto.getNewBedId());
        if (newBed == null) {
            throw new BusinessException("目标床位不存在，床位ID：" + dto.getNewBedId());
        }
        if (!Integer.valueOf(BedStatus.FREE).equals(newBed.getBedStatus())) {
            throw new BusinessException("只能调换到空闲的床位");
        }

        // 校验新床位是否属于所选楼层与房间
        Room newRoom = roomMapper.selectOne(new LambdaQueryWrapper<Room>()
                .eq(Room::getRoomNo, newBed.getRoomNo())
                .last("LIMIT 1"));
        if (newRoom == null) {
            throw new BusinessException("目标床位所属房间不存在，房间号：" + newBed.getRoomNo());
        }
        if (dto.getNewRoomNo() != null && !dto.getNewRoomNo().equals(newRoom.getRoomNo())) {
            throw new BusinessException("所选床位不属于所选房间");
        }
        if (StringUtils.hasText(dto.getNewFloor()) && !dto.getNewFloor().trim().equals(newRoom.getRoomFloor())) {
            throw new BusinessException("所选床位不属于所选楼层");
        }

        // 客户当前正在使用的床位记录
        BedDetails current = bedDetailsMapper.selectOne(new LambdaQueryWrapper<BedDetails>()
                .eq(BedDetails::getCustomerId, dto.getCustomerId())
                .eq(BedDetails::getIsDeleted, BedDetailsStatus.IN_USE)
                .orderByDesc(BedDetails::getId)
                .last("LIMIT 1"));
        if (current == null) {
            throw new BusinessException("该客户当前没有正在使用的床位，无法调换");
        }
        if (current.getBedId() != null && current.getBedId().equals(dto.getNewBedId())) {
            throw new BusinessException("新床位与当前床位相同，无需调换");
        }
        // 旧使用记录失效：is_deleted = 1，结束时间改为当天
        BedDetails closeOld = new BedDetails();
        closeOld.setId(current.getId());
        closeOld.setIsDeleted(BedDetailsStatus.HISTORY);
        closeOld.setEndDate(today);
        bedDetailsMapper.updateById(closeOld);

        // 旧床位恢复空闲
        if (current.getBedId() != null) {
            Bed oldBed = new Bed();
            oldBed.setId(current.getBedId());
            oldBed.setBedStatus(BedStatus.FREE);
            bedMapper.updateById(oldBed);
        }

        // 新床位标记为有人
        Bed occupyNewBed = new Bed();
        occupyNewBed.setId(newBed.getId());
        occupyNewBed.setBedStatus(BedStatus.OCCUPIED);
        bedMapper.updateById(occupyNewBed);

        // 新增使用记录：入住时间为当天，结束时间为空
        BedDetails newRecord = new BedDetails();
        newRecord.setCustomerId(customer.getId());
        newRecord.setBedId(newBed.getId());
        newRecord.setStartDate(today);
        newRecord.setBedDetails(buildBedDetails(newRoom.getRoomNo(), newBed.getBedNo()));
        newRecord.setIsDeleted(BedDetailsStatus.IN_USE);
        bedDetailsMapper.insert(newRecord);

        // 同步客户信息：房间号、床号、楼房
        Customer updateCustomer = new Customer();
        updateCustomer.setId(customer.getId());
        updateCustomer.setRoomNo(String.valueOf(newRoom.getRoomNo()));
        updateCustomer.setBedId(newBed.getId());
        updateCustomer.setBuildingNo(buildingNo);
        customerMapper.updateById(updateCustomer);

        log.info("床位调换成功：客户[{}]{} {} -> {}",
                customer.getId(), customer.getCustomerName(), current.getBedDetails(), newRecord.getBedDetails());
    }

    /**
     * 拼接床位详情描述，例如：606栋-1层-101房间-2号床位。
     *
     * @param roomNo 房间号
     * @param bedNo  床位编号
     * @return 床位详情
     */
    private String buildBedDetails(Integer roomNo, String bedNo) {
        Room room = roomMapper.selectOne(new LambdaQueryWrapper<Room>()
                .eq(Room::getRoomNo, roomNo)
                .last("LIMIT 1"));
        String floor = room == null ? "" : room.getRoomFloor();
        return String.format("%s栋-%s层-%s房间-%s号床位", buildingNo, floor, roomNo, bedNo);
    }
}
