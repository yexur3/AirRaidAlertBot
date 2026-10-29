package com.example.airAlertBot.air_target_processing.shared;

import com.example.airAlertBot.air_target_processing.TargetInfo;
import com.example.airAlertBot.air_target_processing.TargetsProcessing;

import java.util.List;

public class NotImplementedTagretProcessing implements TargetsProcessing {
    @Override
    public List<TargetInfo> execute(String rawMessageText) {
        return List.of();
    }
}
