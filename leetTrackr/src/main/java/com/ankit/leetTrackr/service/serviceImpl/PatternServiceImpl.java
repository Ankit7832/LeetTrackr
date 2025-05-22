package com.ankit.leetTrackr.service.serviceImpl;

import com.ankit.leetTrackr.entity.Pattern;
import com.ankit.leetTrackr.repository.PatternRepository;
import com.ankit.leetTrackr.service.PatternService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatternServiceImpl implements PatternService {
    private final PatternRepository patternRepository;
    @Override
    @Transactional
    public List<Pattern> resolvePatternsByNames(List<String> patternNames) {
        List<Pattern> resolved = new ArrayList<>();
        for(String name:patternNames){
            Pattern existing =patternRepository.findByName(name).orElse(null);
            if(existing!=null){
                resolved.add(existing);
            }else{
                Pattern newPattern=new Pattern();
                newPattern.setName(name);
                resolved.add(patternRepository.save(newPattern));
            }
        }
        return resolved;
    }
}
