package com.betalpha.fosun.service;


import com.betalpha.fosun.api.BondInfoApi;
import com.betalpha.fosun.model.DataBaseIsIn;
import com.betalpha.fosun.repostory.IsInPoolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class AssetPoolService {

    @Autowired
    private IsInPoolRepository isInPoolRepository;

    @Autowired
    private RatingService ratingService;


    public void saveIsInEndToPool(String isInCode, String grade, String issuer) {
        List<String> sameLevelIsInList = ratingService.getSameLevelIsin(isInCode);
        sameLevelIsInList.forEach(id -> isInPoolRepository.save(new DataBaseIsIn(id, grade, issuer)));
        log.info("save success");
    }

    public Optional<String> checkExistIsIn(String isInCode) {
        DataBaseIsIn dataBaseIsIn = isInPoolRepository.findOne(isInCode);
        if (null != dataBaseIsIn) {
            log.info("isInCode:{} has in database", isInCode);
            return Optional.of(dataBaseIsIn.getIssuer());
        }
        log.info("isInCode:{} has not in database");
        return Optional.empty();
    }

    public List<BondInfoApi> getAssetPool() {
        List<DataBaseIsIn> dataBaseIsInList = isInPoolRepository.findAll();
        return dataBaseIsInList.stream().map(dataBaseIsIn -> new BondInfoApi(dataBaseIsIn.getId(), dataBaseIsIn.getGrade(), dataBaseIsIn.getIssuer())).collect(toList());
    }
}
