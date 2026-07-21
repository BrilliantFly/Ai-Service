package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanFocusSession;
import com.know.knowboot.mapper.plan.PlanFocusSessionMapper;
import com.know.knowboot.service.plan.IPlanFocusSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 番茄专注记录服务实现
 */
@Service
public class PlanFocusSessionServiceImpl extends ServiceImpl<PlanFocusSessionMapper, PlanFocusSession>
        implements IPlanFocusSessionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addSession(PlanFocusSession session, Long userId) {
        long now = System.currentTimeMillis();
        session.setUserId(userId);
        session.setCreateBy(userId);
        session.setCreateTime(now);
        if (session.getStartTime() == null) {
            session.setStartTime(now);
        }
        if (session.getEndTime() == null && session.getDuration() != null) {
            session.setEndTime(session.getStartTime() + session.getDuration() * 1000L);
        }
        return save(session);
    }

    @Override
    public List<PlanFocusSession> listToday(Long userId) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long dayStart = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long dayEnd = cal.getTimeInMillis() - 1;

        return list(new LambdaQueryWrapper<PlanFocusSession>()
                .eq(PlanFocusSession::getUserId, userId)
                .and(wrapper -> wrapper
                        .and(endWrapper -> endWrapper
                                .isNotNull(PlanFocusSession::getEndTime)
                                .ge(PlanFocusSession::getEndTime, dayStart)
                                .le(PlanFocusSession::getEndTime, dayEnd))
                        .or(startWrapper -> startWrapper
                                .isNull(PlanFocusSession::getEndTime)
                                .ge(PlanFocusSession::getStartTime, dayStart)
                                .le(PlanFocusSession::getStartTime, dayEnd)))
                .orderByDesc(PlanFocusSession::getEndTime)
                .orderByDesc(PlanFocusSession::getStartTime));
    }

    @Override
    public Map<String, Object> getTodayStats(Long userId) {
        List<PlanFocusSession> sessions = listToday(userId);
        long focusCount = sessions.stream().filter(item -> item.getPhase() != null && item.getPhase() == 0).count();
        long breakCount = sessions.stream().filter(item -> item.getPhase() != null && item.getPhase() == 1).count();
        long longBreakCount = sessions.stream().filter(item -> item.getPhase() != null && item.getPhase() == 2).count();
        long focusMinutes = sessions.stream()
                .filter(item -> item.getPhase() != null && item.getPhase() == 0)
                .mapToLong(item -> item.getDuration() == null ? 0 : item.getDuration() / 60)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("focusCount", focusCount);
        stats.put("breakCount", breakCount);
        stats.put("longBreakCount", longBreakCount);
        stats.put("focusMinutes", focusMinutes);
        stats.put("sessions", sessions);
        return stats;
    }
}
