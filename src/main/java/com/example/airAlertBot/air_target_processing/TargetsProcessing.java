package com.example.airAlertBot.air_target_processing;

import java.util.List;

public interface TargetsProcessing {
    List<TargetInfo> execute(String rawMessageText);
}
