package com.khc.service;

import com.khc.dto.StateSaveReq;
import com.khc.model.State;
import com.khc.repository.StateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StateService {
    @Autowired
    private StateRepo stateRepo;

    public String stateSave(StateSaveReq stateSaveReq) {
        if (stateSaveReq.getStateName() == null) return "state name is mandatory";
        State state = new State();
        state.setStateName(stateSaveReq.getStateName());
        state.setCountry(stateSaveReq.getCountry());
        state.setIsActive(stateSaveReq.getIsActive());
        state.setCreatedId(stateSaveReq.getCreatedId());
        state.setModifyId(stateSaveReq.getModifyId());
        state.setCreatedDate(LocalDateTime.now());
        state.setModifyDate(LocalDateTime.now());
        stateRepo.save(state);
        return "State Saved Successfully";
    }
}
