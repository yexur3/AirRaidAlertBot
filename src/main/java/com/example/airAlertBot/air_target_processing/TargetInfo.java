package com.example.airAlertBot.air_target_processing;

import com.example.airAlertBot.enums.ObjectType;

public record TargetInfo(ObjectType objectType, String origin,
                         String passingLocation, String destination,
                         String directionDetail) {}