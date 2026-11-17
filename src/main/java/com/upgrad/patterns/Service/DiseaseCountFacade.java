package com.upgrad.patterns.Service;

import com.upgrad.patterns.Constants.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiseaseCountFacade {

    private IndiaDiseaseStatFactory indiaDiseaseStat;

    @Autowired
    public DiseaseCountFacade(IndiaDiseaseStatFactory indiaDiseaseStat) {
        this.indiaDiseaseStat = indiaDiseaseStat;
    }

    public Object getDiseaseShCount() {
        return indiaDiseaseStat.GetInstance(SourceType.DiseaseSh).GetActiveCount();
    }

    public Object getJohnHopkinCount() {
        return indiaDiseaseStat.GetInstance(SourceType.JohnHopkins).GetActiveCount();
    }

    public Object getInfectedRatio(String sourceType) throws IllegalArgumentException {
        SourceType sourceEnum;
        try {
            sourceEnum = SourceType.valueOf(sourceType);
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid source type specified. Available source type (%s, %s)",
                    SourceType.DiseaseSh, SourceType.JohnHopkins);
            throw new IllegalArgumentException(message, e);
        }

        Float population = 900000000F;
        Float active = Float.valueOf(indiaDiseaseStat.GetInstance(sourceEnum).GetActiveCount());
        Float percent = Float.valueOf(active / population);
        return String.format("%.3f", percent * 100);
    }
}
