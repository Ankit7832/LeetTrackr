package com.ankit.leetTrackr.service;

import com.ankit.leetTrackr.entity.Pattern;

import java.util.List;

public interface PatternService {

    public  List<Pattern> resolvePatternsByNames(List<String> patternNames);
}
