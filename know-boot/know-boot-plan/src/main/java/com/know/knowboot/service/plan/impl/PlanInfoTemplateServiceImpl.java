package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.know.knowboot.dto.plan.UseInPlanRequest;
import com.know.knowboot.dto.plan.UseInPlanResult;
import com.know.knowboot.dto.plan.UseTemplateRequest;
import com.know.knowboot.dto.plan.UseTemplateResult;
import com.know.knowboot.entity.plan.*;
import com.know.knowboot.mapper.plan.PlanHabitMapper;
import com.know.knowboot.mapper.plan.PlanInfoMapper;
import com.know.knowboot.mapper.plan.PlanInfoTemplateMapper;
import com.know.knowboot.service.plan.IPlanHabitService;
import com.know.knowboot.service.plan.IPlanHabitTemplateService;
import com.know.knowboot.service.plan.IPlanInfoTemplateService;
import com.know.knowboot.service.plan.IPlanScheduleEventService;
import com.know.knowboot.service.plan.IPlanScheduleEventTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 计划信息模板服务实现
 */
@Service
public class PlanInfoTemplateServiceImpl extends ServiceImpl<PlanInfoTemplateMapper, PlanInfoTemplate> implements IPlanInfoTemplateService {

    @Autowired
    private PlanInfoTemplateMapper planInfoTemplateMapper;

    @Autowired
    private IPlanHabitTemplateService planHabitTemplateService;

    @Autowired
    private IPlanScheduleEventTemplateService planScheduleEventTemplateService;

    @Autowired
    private IPlanHabitService planHabitService;

    @Autowired
    private IPlanScheduleEventService planScheduleEventService;

    @Autowired
    private PlanInfoMapper planInfoMapper;

    @Autowired
    private PlanHabitMapper planHabitMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public IPage<PlanInfoTemplate> page(PlanInfoTemplate query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PlanInfoTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getTemplateName() != null, PlanInfoTemplate::getTemplateName, query.getTemplateName())
                .eq(query.getPlanType() != null, PlanInfoTemplate::getPlanType, query.getPlanType())
                .ne(query.getVisibility() == null, PlanInfoTemplate::getVisibility, 0)
                .orderByDesc(PlanInfoTemplate::getSort)
                .orderByDesc(PlanInfoTemplate::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<PlanInfoTemplate> hotList(int limit) {
        return list(new LambdaQueryWrapper<PlanInfoTemplate>()
                .ne(PlanInfoTemplate::getVisibility, 0)
                .orderByDesc(PlanInfoTemplate::getUseCount)
                .last("LIMIT " + limit));
    }

    @Override
    public PlanInfoTemplate getDetail(Long id) {
        return planInfoTemplateMapper.selectById(id);
    }

    @Override
    public List<PlanInfoTemplate> getChildren(Long parentId) {
        return list(new LambdaQueryWrapper<PlanInfoTemplate>()
                .eq(PlanInfoTemplate::getParentId, parentId)
                .orderByAsc(PlanInfoTemplate::getSort));
    }

    @Override
    public List<Map<String, Object>> getTree(Long rootId) {
        PlanInfoTemplate root = getById(rootId);
        if (root == null) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(buildTreeNode(root));
        return result;
    }

    private Map<String, Object> buildTreeNode(PlanInfoTemplate template) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("key", template.getId());
        node.put("id", template.getId());
        node.put("title", template.getTemplateName());
        node.put("templateName", template.getTemplateName());
        node.put("description", template.getDescription());
        node.put("icon", template.getIcon());
        node.put("color", template.getColor());
        node.put("planType", template.getPlanType());
        node.put("defaultPriority", template.getDefaultPriority());
        node.put("defaultDurationDays", template.getDefaultDurationDays());
        node.put("visibility", template.getVisibility());
        node.put("useCount", template.getUseCount());

        List<PlanInfoTemplate> children = list(new LambdaQueryWrapper<PlanInfoTemplate>()
                .eq(PlanInfoTemplate::getParentId, template.getId())
                .eq(PlanInfoTemplate::getDelFlag, 0)
                .orderByAsc(PlanInfoTemplate::getSort));
        if (!children.isEmpty()) {
            List<Map<String, Object>> childNodes = new ArrayList<>();
            for (PlanInfoTemplate child : children) {
                childNodes.add(buildTreeNode(child));
            }
            node.put("children", childNodes);
        }
        return node;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(PlanInfoTemplate t, Long userId) {
        t.setCreateBy(userId);
        t.setCreateTime(System.currentTimeMillis());
        t.setDelFlag(0);
        t.setUseCount(0);
        t.setDeleteTime(0L);
        save(t);
        return t.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PlanInfoTemplate t, Long userId) {
        t.setUpdateBy(userId);
        t.setUpdateTime(System.currentTimeMillis());
        return updateById(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        List<Long> ids = new ArrayList<>();
        collectDescendantIds(id, ids);
        ids.add(id);
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UseTemplateResult useTemplate(Long templateId, Long userId, UseTemplateRequest request) {
        PlanInfoTemplate template = planInfoTemplateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("模板不存在: " + templateId);
        }
        if (request == null) {
            request = new UseTemplateRequest();
        }
        if (request.getCustomizations() == null) {
            request.setCustomizations(new HashMap<>());
        }

        List<Map<String, Object>> habitDefs = resolveHabitDefs(template);
        List<Map<String, Object>> eventDefs = resolveEventDefs(template);

        applyOverrides(habitDefs, getOverrideList(request.getCustomizations(), "habitOverrides"));
        applyOverrides(eventDefs, getOverrideList(request.getCustomizations(), "eventOverrides"));

        long startMs = request.getStartDate() != null ? request.getStartDate() : System.currentTimeMillis();
        Integer execStatus = request.getExecStatus() != null ? request.getExecStatus() : 1;

        PlanInfo rootPlan = new PlanInfo();
        rootPlan.setPlanName(request.getPlanName() != null ? request.getPlanName() : template.getTemplateName());
        rootPlan.setPlanType(template.getPlanType());
        rootPlan.setQuadrantId(template.getQuadrantId());
        rootPlan.setPriority(template.getDefaultPriority());
        rootPlan.setStatus(0);
        rootPlan.setProgress(0);
        rootPlan.setPlanStartTime(startMs);
        if (template.getDefaultDurationDays() != null && template.getDefaultDurationDays() > 0) {
            rootPlan.setPlanEndTime(startMs + template.getDefaultDurationDays() * 86400000L);
        }
        rootPlan.setCreateBy(userId);
        rootPlan.setCreateTime(System.currentTimeMillis());
        rootPlan.setDeleteTime(0L);
        rootPlan.setTemplateId(templateId);
        planInfoMapper.insert(rootPlan);

        int habitsCreated = createHabitsForPlan(habitDefs, rootPlan.getId(), startMs, userId, execStatus, templateId);
        int eventsCreated = createEventsForPlan(eventDefs, rootPlan.getId(), startMs, userId, execStatus, templateId);

        int plansCreated = 1;
        List<Integer> skipSubPlans = getSkipSubPlans(request.getCustomizations());
        List<Map<String, Object>> subPlanDefs = parseJsonList(template.getDefaultSubPlans());
        if (subPlanDefs != null) {
            long cursor = startMs;
            for (int i = 0; i < subPlanDefs.size(); i++) {
                if (skipSubPlans.contains(i)) continue;
                Map<String, Object> def = subPlanDefs.get(i);
                Object[] counts = createSubPlan(def, rootPlan.getId(), userId, cursor, execStatus, templateId);
                plansCreated += ((Number) counts[0]).intValue();
                habitsCreated += ((Number) counts[1]).intValue();
                eventsCreated += ((Number) counts[2]).intValue();
                cursor += defDurationDays(def) * 86400000L;
            }
        }

        template.setUseCount(template.getUseCount() != null ? template.getUseCount() + 1 : 1);
        template.setUpdateTime(System.currentTimeMillis());
        planInfoTemplateMapper.updateById(template);

        UseTemplateResult result = new UseTemplateResult();
        result.setPlanId(rootPlan.getId());
        result.setPlansCreated(plansCreated);
        result.setHabitsCreated(habitsCreated);
        result.setEventsCreated(eventsCreated);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UseInPlanResult useInPlan(UseInPlanRequest request, Long userId) {
        PlanInfoTemplate template = planInfoTemplateMapper.selectById(request.getTemplateId());
        if (template == null) {
            throw new RuntimeException("模板不存在: " + request.getTemplateId());
        }
        PlanInfo targetPlan = planInfoMapper.selectById(request.getPlanId());
        if (targetPlan == null) {
            throw new RuntimeException("目标计划不存在: " + request.getPlanId());
        }
        if (request.getCustomizations() == null) {
            request.setCustomizations(new HashMap<>());
        }

        List<Map<String, Object>> habitDefs = resolveHabitDefs(template);
        List<Map<String, Object>> eventDefs = resolveEventDefs(template);

        applyOverrides(habitDefs, getOverrideList(request.getCustomizations(), "habitOverrides"));
        applyOverrides(eventDefs, getOverrideList(request.getCustomizations(), "eventOverrides"));

        Integer execStatus = request.getExecStatus() != null ? request.getExecStatus() : 1;

        if (request.getSelectedHabitIds() != null && !request.getSelectedHabitIds().isEmpty()) {
            List<Map<String, Object>> filtered = new ArrayList<>();
            List<Long> templateHabitIds = parseLongList(template.getDefaultHabitIds());
            for (int i = 0; i < habitDefs.size(); i++) {
                Long defId = (i < templateHabitIds.size()) ? templateHabitIds.get(i) : null;
                if (defId != null && request.getSelectedHabitIds().contains(defId)) {
                    filtered.add(habitDefs.get(i));
                }
            }
            habitDefs = filtered;
        }

        if (request.getSelectedEventIds() != null && !request.getSelectedEventIds().isEmpty()) {
            List<Map<String, Object>> filtered = new ArrayList<>();
            List<Long> templateEventIds = parseLongList(template.getDefaultEventIds());
            for (int i = 0; i < eventDefs.size(); i++) {
                Long defId = (i < templateEventIds.size()) ? templateEventIds.get(i) : null;
                if (defId != null && request.getSelectedEventIds().contains(defId)) {
                    filtered.add(eventDefs.get(i));
                }
            }
            eventDefs = filtered;
        }

        int habitCount = createHabitsForPlan(habitDefs, request.getPlanId(), targetPlan.getPlanStartTime(), userId, execStatus, request.getTemplateId());
        int eventCount = createEventsForPlan(eventDefs, request.getPlanId(), targetPlan.getPlanStartTime(), userId, execStatus, request.getTemplateId());

        template.setUseCount(template.getUseCount() != null ? template.getUseCount() + 1 : 1);
        template.setUpdateTime(System.currentTimeMillis());
        planInfoTemplateMapper.updateById(template);

        UseInPlanResult result = new UseInPlanResult();
        result.setPlanId(request.getPlanId());
        result.setHabitCount(habitCount);
        result.setEventCount(eventCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateFromPlan(Long planId, String templateName, String description, Integer visibility, Long userId) {
        PlanInfo plan = planInfoMapper.selectById(planId);
        if (plan == null) {
            throw new RuntimeException("计划不存在: " + planId);
        }

        PlanInfoTemplate template = new PlanInfoTemplate();
        template.setTemplateName(templateName);
        template.setDescription(description);
        template.setPlanType(plan.getPlanType());
        template.setCategoryId(plan.getCategoryId());
        template.setQuadrantId(plan.getQuadrantId());
        template.setDefaultPriority(plan.getPriority());
        template.setVisibility(visibility != null ? visibility : 1);
        template.setCreateBy(userId);
        template.setCreateTime(System.currentTimeMillis());
        template.setDelFlag(0);
        template.setUseCount(0);
        template.setDeleteTime(0L);

        List<Map<String, Object>> subPlans = buildSubPlanTree(planId, userId);
        if (!subPlans.isEmpty()) {
            try {
                template.setDefaultSubPlans(objectMapper.writeValueAsString(subPlans));
            } catch (Exception e) {
                throw new RuntimeException("序列化子计划失败", e);
            }
        }

        List<PlanHabit> rootHabits = planHabitMapper.selectList(
                new LambdaQueryWrapper<PlanHabit>().eq(PlanHabit::getPlanId, planId));
        if (rootHabits != null && !rootHabits.isEmpty()) {
            List<Map<String, Object>> habitMaps = new ArrayList<>();
            for (PlanHabit h : rootHabits) {
                habitMaps.add(habitToMap(h));
            }
            try {
                template.setDefaultHabits(objectMapper.writeValueAsString(habitMaps));
            } catch (Exception e) {
                throw new RuntimeException("序列化习惯失败", e);
            }
        }

        List<PlanScheduleEvent> rootEvents = planScheduleEventService.listByPlanId(planId);
        if (rootEvents != null && !rootEvents.isEmpty()) {
            List<Map<String, Object>> eventMaps = new ArrayList<>();
            for (PlanScheduleEvent e : rootEvents) {
                eventMaps.add(eventToMap(e));
            }
            try {
                template.setDefaultEvents(objectMapper.writeValueAsString(eventMaps));
            } catch (Exception e) {
                throw new RuntimeException("序列化事件失败", e);
            }
        }

        planInfoTemplateMapper.insert(template);
        return template.getId();
    }

    // ============================
    // 私有方法: 模板解析与创建
    // ============================

    private List<Map<String, Object>> resolveHabitDefs(PlanInfoTemplate template) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> inlineHabits = parseJsonList(template.getDefaultHabits());
        List<Long> habitIds = parseLongList(template.getDefaultHabitIds());

        if (habitIds != null) {
            for (Long hid : habitIds) {
                PlanHabitTemplate ht = planHabitTemplateService.getDetail(hid);
                if (ht != null) {
                    Map<String, Object> map = habitTemplateToMap(ht);
                    result.add(map);
                }
            }
        }

        if (inlineHabits != null) {
            for (Map<String, Object> inline : inlineHabits) {
                result.add(inline);
            }
        }

        return result;
    }

    private List<Map<String, Object>> resolveEventDefs(PlanInfoTemplate template) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> inlineEvents = parseJsonList(template.getDefaultEvents());
        List<Long> eventIds = parseLongList(template.getDefaultEventIds());

        if (eventIds != null) {
            for (Long eid : eventIds) {
                PlanScheduleEventTemplate et = planScheduleEventTemplateService.getDetail(eid);
                if (et != null) {
                    Map<String, Object> map = eventTemplateToMap(et);
                    result.add(map);
                }
            }
        }

        if (inlineEvents != null) {
            for (Map<String, Object> inline : inlineEvents) {
                result.add(inline);
            }
        }

        return result;
    }

    private void applyOverrides(List<Map<String, Object>> defs, List<Map<String, Object>> overrides) {
        if (overrides == null || overrides.isEmpty()) return;
        for (int i = 0; i < overrides.size() && i < defs.size(); i++) {
            if (overrides.get(i) != null) {
                defs.get(i).putAll(overrides.get(i));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getOverrideList(Map<String, Object> customizations, String key) {
        if (customizations == null) return null;
        Object val = customizations.get(key);
        if (val instanceof List) {
            return (List<Map<String, Object>>) val;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getSkipSubPlans(Map<String, Object> customizations) {
        if (customizations == null) return Collections.emptyList();
        Object val = customizations.get("skipSubPlans");
        if (val instanceof List) {
            List<Integer> result = new ArrayList<>();
            for (Object item : (List<?>) val) {
                if (item instanceof Number) {
                    result.add(((Number) item).intValue());
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    /**
     * 递归创建子计划
     * @return [plansCreated, habitsCreated, eventsCreated]
     */
    private Object[] createSubPlan(Map<String, Object> def, Long parentPlanId, Long userId,
                                   long parentStartMs, Integer execStatus, Long templateId) {
        int plansCount = 0;
        int habitsCount = 0;
        int eventsCount = 0;

        String planName = getStringValue(def, "plan_name");
        Integer durationDays = getIntegerValue(def, "duration_days");
        Integer priority = getIntegerValue(def, "priority");

        long planStartMs = parentStartMs;
        long planEndMs = planStartMs;
        if (durationDays != null && durationDays > 0) {
            planEndMs = planStartMs + durationDays * 86400000L;
        }

        PlanInfo subPlan = new PlanInfo();
        subPlan.setParentId(parentPlanId);
        subPlan.setPlanName(planName);
        subPlan.setPlanType(getStringValue(def, "plan_type"));
        subPlan.setPriority(priority);
        subPlan.setStatus(0);
        subPlan.setProgress(0);
        subPlan.setPlanStartTime(planStartMs);
        subPlan.setPlanEndTime(planEndMs);
        subPlan.setCreateBy(userId);
        subPlan.setCreateTime(System.currentTimeMillis());
        subPlan.setDeleteTime(0L);
        subPlan.setTemplateId(templateId);
        planInfoMapper.insert(subPlan);
        plansCount++;

        List<Map<String, Object>> nodeHabits = getHabitsForNode(def);
        habitsCount += createHabitsForPlan(nodeHabits, subPlan.getId(), planStartMs, userId, execStatus, templateId);

        List<Map<String, Object>> nodeEvents = getEventsForNode(def);
        eventsCount += createEventsForPlan(nodeEvents, subPlan.getId(), planStartMs, userId, execStatus, templateId);

        List<Map<String, Object>> childSubPlans = getSubPlansForNode(def);
        if (childSubPlans != null && !childSubPlans.isEmpty()) {
            long childCursor = planStartMs;
            for (Map<String, Object> childDef : childSubPlans) {
                Object[] childCounts = createSubPlan(childDef, subPlan.getId(), userId,
                        childCursor, execStatus, templateId);
                plansCount += ((Number) childCounts[0]).intValue();
                habitsCount += ((Number) childCounts[1]).intValue();
                eventsCount += ((Number) childCounts[2]).intValue();
                childCursor += defDurationDays(childDef) * 86400000L;
            }
        }

        return new Object[]{plansCount, habitsCount, eventsCount};
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getHabitsForNode(Map<String, Object> node) {
        List<Map<String, Object>> result = new ArrayList<>();
        Object habitsVal = node.get("habits");
        if (habitsVal instanceof List) {
            result.addAll((List<Map<String, Object>>) habitsVal);
        }
        Object habitIdsVal = node.get("habit_ids");
        if (habitIdsVal instanceof List) {
            for (Object idObj : (List<?>) habitIdsVal) {
                if (idObj instanceof Number) {
                    Long hid = ((Number) idObj).longValue();
                    PlanHabitTemplate ht = planHabitTemplateService.getDetail(hid);
                    if (ht != null) {
                        result.add(habitTemplateToMap(ht));
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getEventsForNode(Map<String, Object> node) {
        List<Map<String, Object>> result = new ArrayList<>();
        Object eventsVal = node.get("events");
        if (eventsVal instanceof List) {
            result.addAll((List<Map<String, Object>>) eventsVal);
        }
        Object eventIdsVal = node.get("event_ids");
        if (eventIdsVal instanceof List) {
            for (Object idObj : (List<?>) eventIdsVal) {
                if (idObj instanceof Number) {
                    Long eid = ((Number) idObj).longValue();
                    PlanScheduleEventTemplate et = planScheduleEventTemplateService.getDetail(eid);
                    if (et != null) {
                        result.add(eventTemplateToMap(et));
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getSubPlansForNode(Map<String, Object> node) {
        Object val = node.get("sub_plans");
        if (val instanceof List) {
            return (List<Map<String, Object>>) val;
        }
        return null;
    }

    /**
     * 从定义Map创建习惯记录
     */
    private int createHabitsForPlan(List<Map<String, Object>> habitDefs, Long planId, long planStartMs, Long userId, Integer execStatus, Long templateId) {
        if (habitDefs == null || habitDefs.isEmpty()) return 0;
        int count = 0;
        for (Map<String, Object> def : habitDefs) {
            PlanHabit habit = new PlanHabit();
            habit.setPlanId(planId);
            habit.setUserId(userId);
            habit.setTemplateId(templateId);
            habit.setHabitTemplateId(getLongValue(def, "id"));
            habit.setName(getStringValue(def, "name"));
            habit.setDescription(getStringValue(def, "description"));
            habit.setIcon(getStringValue(def, "icon"));
            habit.setColor(getStringValue(def, "color"));
            habit.setTargetDays(getIntegerValue(def, "target_days"));
            habit.setTargetValue(getIntegerValue(def, "target_value"));
            habit.setTargetUnit(getStringValue(def, "target_unit"));
            habit.setTrackingType(getStringValue(def, "tracking_type"));
            habit.setFrequencyType(getIntegerValue(def, "frequency_type"));
            habit.setFrequencyRule(getStringValue(def, "frequency_rule"));
            habit.setReminderTime(getStringValue(def, "reminder_time"));
            habit.setRestDays(getStringValue(def, "rest_days"));
            habit.setExecStatus(execStatus);
            habit.setStatus(0);
            habit.setCurrentDays(0);
            habit.setTotalDays(0);
            habit.setCreateBy(userId);
            habit.setCreateTime(System.currentTimeMillis());
            habit.setDeleteTime(0L);
            habit.setStartDate(planStartMs);
            Integer targetDays = habit.getTargetDays();
            if (targetDays != null && targetDays > 0) {
                habit.setEndDate(planStartMs + targetDays * 86400000L);
            }
            planHabitMapper.insert(habit);
            count++;
        }
        return count;
    }

    /**
     * 从定义Map创建事件记录
     */
    private int createEventsForPlan(List<Map<String, Object>> eventDefs, Long planId, long planStartMs, Long userId, Integer execStatus, Long templateId) {
        if (eventDefs == null || eventDefs.isEmpty()) return 0;
        int count = 0;
        for (Map<String, Object> def : eventDefs) {
            PlanScheduleEvent event = new PlanScheduleEvent();
            event.setPlanId(planId);
            event.setUserId(userId);
            event.setTemplateId(templateId);
            event.setEventTemplateId(getLongValue(def, "id"));
            event.setTitle(getStringValue(def, "title"));
            event.setContent(getStringValue(def, "content"));
            event.setEventType(getIntegerValue(def, "event_type"));
            event.setQuadrant(getIntegerValue(def, "quadrant"));
            event.setPriority(getIntegerValue(def, "priority"));
            event.setIsRepeat(getIntegerValue(def, "is_repeat"));
            event.setRepeatType(getIntegerValue(def, "repeat_type"));
            event.setRepeatRule(getStringValue(def, "repeat_rule"));
            event.setIsAllDay(getIntegerValue(def, "is_all_day"));
            event.setStartTime(getLongValue(def, "start_time"));
            event.setEndTime(getLongValue(def, "end_time"));
            event.setRemindMinutes(getIntegerValue(def, "remind_minutes"));
            event.setRemindTime(getStringValue(def, "remind_time"));
            event.setLocation(getStringValue(def, "location"));
            event.setExecStatus(execStatus);
            event.setStatus(0);
            event.setCreateBy(userId);
            event.setCreateTime(System.currentTimeMillis());
            event.setDeleteTime(0L);
            if (event.getStartTime() == null) event.setStartTime(planStartMs);
            if (event.getEndTime() == null) event.setEndTime(planStartMs + 3600000L);
            planScheduleEventService.add(event, userId);
            count++;
        }
        return count;
    }

    // ============================
    // 私有方法: JSON解析与辅助
    // ============================

    private void collectDescendantIds(Long parentId, List<Long> ids) {
        List<PlanInfoTemplate> children = list(new LambdaQueryWrapper<PlanInfoTemplate>()
                .eq(PlanInfoTemplate::getParentId, parentId)
                .select(PlanInfoTemplate::getId));
        for (PlanInfoTemplate child : children) {
            ids.add(child.getId());
            collectDescendantIds(child.getId(), ids);
        }
    }

    private List<Map<String, Object>> parseJsonList(String json) {
        if (json == null || json.trim().isEmpty()) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    private List<Long> parseLongList(String json) {
        if (json == null || json.trim().isEmpty()) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    private Object getValue(Map<String, Object> map, String key) {
        if (map == null) return null;
        Object v = map.get(key);
        if (v != null) return v;
        String camel = toCamel(key);
        if (camel != null) { v = map.get(camel); if (v != null) return v; }
        String snake = toSnake(key);
        if (snake != null) { v = map.get(snake); if (v != null) return v; }
        return null;
    }

    private static String toCamel(String key) {
        if (key == null || !key.contains("_")) return null;
        StringBuilder sb = new StringBuilder();
        boolean upper = false;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '_') {
                upper = true;
            } else {
                sb.append(upper ? Character.toUpperCase(c) : c);
                upper = false;
            }
        }
        return sb.toString();
    }

    private static String toSnake(String key) {
        if (key == null || key.indexOf('_') >= 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object val = getValue(map, key);
        return val != null ? String.valueOf(val) : null;
    }

    private Integer getIntegerValue(Map<String, Object> map, String key) {
        Object val = getValue(map, key);
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return null;
    }

    private Long getLongValue(Map<String, Object> m, String key) {
        try {
            Object v = getValue(m, key);
            if (v == null) return null;
            if (v instanceof Number) return ((Number) v).longValue();
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private int defDurationDays(Map<String, Object> def) {
        Integer d = getIntegerValue(def, "duration_days");
        return d == null ? 0 : d;
    }

    private Map<String, Object> habitTemplateToMap(PlanHabitTemplate ht) {
        Map<String, Object> map = new LinkedHashMap<>();
        putIfNotNull(map, "name", ht.getName());
        putIfNotNull(map, "description", ht.getDescription());
        putIfNotNull(map, "icon", ht.getIcon());
        putIfNotNull(map, "color", ht.getColor());
        putIfNotNull(map, "frequency_type", ht.getFrequencyType());
        putIfNotNull(map, "frequency_rule", ht.getFrequencyRule());
        putIfNotNull(map, "reminder_time", ht.getReminderTime());
        putIfNotNull(map, "rest_days", ht.getRestDays());
        putIfNotNull(map, "target_days", ht.getTargetDays());
        putIfNotNull(map, "target_value", ht.getTargetValue());
        putIfNotNull(map, "target_unit", ht.getTargetUnit());
        putIfNotNull(map, "tracking_type", ht.getTrackingType());
        return map;
    }

    private Map<String, Object> eventTemplateToMap(PlanScheduleEventTemplate et) {
        Map<String, Object> map = new LinkedHashMap<>();
        putIfNotNull(map, "title", et.getTitle());
        putIfNotNull(map, "content", et.getContent());
        putIfNotNull(map, "event_type", et.getEventType());
        putIfNotNull(map, "quadrant", et.getQuadrant());
        putIfNotNull(map, "priority", et.getPriority());
        putIfNotNull(map, "is_repeat", et.getIsRepeat());
        putIfNotNull(map, "repeat_type", et.getRepeatType());
        putIfNotNull(map, "repeat_rule", et.getRepeatRule());
        putIfNotNull(map, "is_all_day", et.getIsAllDay());
        putIfNotNull(map, "start_time", et.getStartTime());
        putIfNotNull(map, "end_time", et.getEndTime());
        putIfNotNull(map, "remind_minutes", et.getRemindMinutes());
        putIfNotNull(map, "location", et.getLocation());
        return map;
    }

    private Map<String, Object> habitToMap(PlanHabit h) {
        Map<String, Object> map = new LinkedHashMap<>();
        putIfNotNull(map, "name", h.getName());
        putIfNotNull(map, "description", h.getDescription());
        putIfNotNull(map, "icon", h.getIcon());
        putIfNotNull(map, "color", h.getColor());
        putIfNotNull(map, "frequency_type", h.getFrequencyType());
        putIfNotNull(map, "frequency_rule", h.getFrequencyRule());
        putIfNotNull(map, "reminder_time", h.getReminderTime());
        putIfNotNull(map, "rest_days", h.getRestDays());
        putIfNotNull(map, "target_days", h.getTargetDays());
        putIfNotNull(map, "target_value", h.getTargetValue());
        putIfNotNull(map, "target_unit", h.getTargetUnit());
        putIfNotNull(map, "tracking_type", h.getTrackingType());
        return map;
    }

    private Map<String, Object> eventToMap(PlanScheduleEvent e) {
        Map<String, Object> map = new LinkedHashMap<>();
        putIfNotNull(map, "title", e.getTitle());
        putIfNotNull(map, "content", e.getContent());
        putIfNotNull(map, "event_type", e.getEventType());
        putIfNotNull(map, "quadrant", e.getQuadrant());
        putIfNotNull(map, "priority", e.getPriority());
        putIfNotNull(map, "is_repeat", e.getIsRepeat());
        putIfNotNull(map, "repeat_type", e.getRepeatType());
        putIfNotNull(map, "repeat_rule", e.getRepeatRule());
        putIfNotNull(map, "is_all_day", e.getIsAllDay());
        putIfNotNull(map, "start_time", e.getStartTime());
        putIfNotNull(map, "end_time", e.getEndTime());
        putIfNotNull(map, "remind_minutes", e.getRemindMinutes());
        putIfNotNull(map, "remind_time", e.getRemindTime());
        putIfNotNull(map, "location", e.getLocation());
        return map;
    }

    private void putIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    // ============================
    // 私有方法: 递归构建计划树(用于generateFromPlan)
    // ============================

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> buildSubPlanTree(Long parentId, Long userId) {
        List<PlanInfo> children = planInfoMapper.selectList(
                new LambdaQueryWrapper<PlanInfo>()
                        .eq(PlanInfo::getParentId, parentId)
                        .orderByAsc(PlanInfo::getCreateTime));
        List<Map<String, Object>> result = new ArrayList<>();
        for (PlanInfo child : children) {
            Map<String, Object> node = new LinkedHashMap<>();
            node.put("plan_name", child.getPlanName());
            node.put("plan_type", child.getPlanType());
            node.put("priority", child.getPriority());

            if (child.getPlanStartTime() != null && child.getPlanEndTime() != null) {
                long durationMs = child.getPlanEndTime() - child.getPlanStartTime();
                node.put("duration_days", (int) (durationMs / 86400000L));
            }

            List<PlanHabit> habits = planHabitMapper.selectList(
                    new LambdaQueryWrapper<PlanHabit>().eq(PlanHabit::getPlanId, child.getId()));
            if (habits != null && !habits.isEmpty()) {
                List<Map<String, Object>> habitMaps = new ArrayList<>();
                for (PlanHabit h : habits) {
                    habitMaps.add(habitToMap(h));
                }
                node.put("habits", habitMaps);
            }

            List<PlanScheduleEvent> events = planScheduleEventService.listByPlanId(child.getId());
            if (events != null && !events.isEmpty()) {
                List<Map<String, Object>> eventMaps = new ArrayList<>();
                for (PlanScheduleEvent e : events) {
                    eventMaps.add(eventToMap(e));
                }
                node.put("events", eventMaps);
            }

            List<Map<String, Object>> subPlans = buildSubPlanTree(child.getId(), userId);
            if (!subPlans.isEmpty()) {
                node.put("sub_plans", subPlans);
            }

            result.add(node);
        }
        return result;
    }
}
